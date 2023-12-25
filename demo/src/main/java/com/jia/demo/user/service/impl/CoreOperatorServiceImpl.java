package com.jia.demo.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jia.demo.exception.ServiceException;
import com.jia.demo.param.LoginParam;
import com.jia.demo.param.LoginParamMobile;
import com.jia.demo.state.CodeConstant;
import com.jia.demo.state.ManagerStatus;
import com.jia.demo.state.YesOrNotEnum;
import com.jia.demo.user.entity.*;
import com.jia.demo.user.mapper.CoreDeptMapper;
import com.jia.demo.user.mapper.CoreOperatorMapper;
import com.jia.demo.user.mapper.CoreRoleMapper;
import com.jia.demo.user.mapper.CoreRoleOperMapper;
import com.jia.demo.user.service.CoreMenuService;
import com.jia.demo.user.service.CoreOperatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.demo.util.RSAUtil;
import com.jia.demo.vo.LoginResult;
import com.jia.demo.vo.RoleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 操作员 服务实现类
 * </p>
 *
 * @author jia
 * @since 2023-11-07
 */
@Service
public class CoreOperatorServiceImpl extends ServiceImpl<CoreOperatorMapper, CoreOperator> implements CoreOperatorService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CoreRoleOperMapper coreRoleOperMapper;

    @Autowired
    private CoreMenuService coreMenuService;

    @Autowired
    private CoreRoleMapper coreRoleMapper;

    //定义一个记录错误次数的字符串前缀
    private final String ERRORSTR = "login_error_time_";

    @Value("${debug.verifyCode:}")
    private String debugVerifyCode;

    @Autowired
    private CoreOperatorMapper coreOperatorMapper;

    @Autowired
    private CoreDeptMapper coreDeptMapper;



    //定义一个默认错误次数
    private Integer errorNum = 0;
    @Override
    public LoginResult login(LoginParam param, String currCode) {
        //判断参数是否为空
        Assert.isTrue(!ObjectUtil.hasEmpty(param,param.getUsername(),param.getPassword(),param.getVerifyCode()),"缺少参数");
        //这里可以设置将账号和密码进行前端公钥加密后端私钥解密
        try {
            param.setPassword(RSAUtil.privateDecode(Base64.decode(param.getPassword())));
            param.setPassword(RSAUtil.privateDecode(Base64.decode(param.getUsername())));
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException(CodeConstant.ERROR,"用户名或密码有错误");
        }
        //判断用户输入的验证码和后端生成的验证码是否一致
        boolean b = param.getVerifyCode().equalsIgnoreCase(currCode);
        Assert.isTrue(b,"验证码不正确");
        //查看是否存在该用户
        CoreOperator operator = getOperatorByAccount(param.getUsername());
        Assert.isTrue(operator!=null,"用户名或密码名不正确");
        String password = param.getPassword();
        String salt = operator.getSalt();
        String md5Password = SecureUtil.md5(password + salt);
        //输入超过一定次数就要30分钟后重试
        String keyStr = ERRORSTR + operator.getAccount();
        if(redisTemplate.hasKey(keyStr)){
           errorNum = Integer.valueOf(redisTemplate.opsForValue().get(keyStr).toString());
           Assert.isTrue(errorNum<5,"登录次数超过5次，请30分钟后重试");
        }
        if(!md5Password.equalsIgnoreCase(operator.getPassword())){
            if(redisTemplate.hasKey(keyStr)){
                errorNum = Integer.valueOf(redisTemplate.opsForValue().get(keyStr).toString());
            }else {
                errorNum=0;
            }
            redisTemplate.opsForValue().set(keyStr,errorNum+1,1800, TimeUnit.SECONDS);
        }
        Assert.isTrue(md5Password.equalsIgnoreCase(operator.getPassword()),"用户名或密码不正确");
        redisTemplate.delete(keyStr);
        Assert.isTrue(ManagerStatus.ENABLE.getCode().equalsIgnoreCase(operator.getStatus()),"用户被冻结");
        LoginResult result = new LoginResult();
        BeanUtil.copyProperties(operator,result);
        //判断用户是什么角色
        LambdaQueryWrapper<CoreRoleOper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoreRoleOper::getOperId, operator.getOperId()).select(CoreRoleOper::getRoleId);
        List<CoreRoleOper> roleOpers = coreRoleOperMapper.selectList(queryWrapper);
        cn.hutool.core.lang.Assert.isTrue(CollUtil.isNotEmpty(roleOpers),"该用户无角色[{}]",operator.getOperId());
        List<Long> roleIds = roleOpers.stream().map(CoreRoleOper::getRoleId).collect(Collectors.toList());
        if(!roleIds.isEmpty()){
            LambdaQueryWrapper<CoreRole> roleQuery = new LambdaQueryWrapper<>();
            roleQuery.in(CoreRole::getRoleId, roleIds);
            List<CoreRole> coreRoles = coreRoleMapper.selectList(roleQuery);
            List<RoleResult> roList = coreRoles.stream().map(r->{
                RoleResult ro = new RoleResult();
                ro.setRoleName(r.getName());
                ro.setRoleId(r.getRoleId());
                return ro;
            }).collect(Collectors.toList());
            result.setRoles(roList);
        }
        List<CoreMenu> list = coreMenuService.listOperMenu(operator.getOperId());
        List<CoreMenu> menuList = list.stream().filter(coreMenu -> YesOrNotEnum.Y.getDesc().equalsIgnoreCase(coreMenu.getMenuFlag())).collect(Collectors.toList());
        List<Tree<Long>> menuTree = coreMenuService.menuListToTree(menuList);
        result.setMenus(menuTree);
        return result;
    }

    @Override
    public CoreOperator getOperatorByAccount(String account) {
        cn.hutool.core.lang.Assert.isTrue(StrUtil.isNotBlank(account), "account must be not blank");
        LambdaQueryWrapper<CoreOperator> wrapper = new LambdaQueryWrapper<>();
        CoreOperator operator = wrapper.eq(CoreOperator::getAccount, account).getEntity();
        return operator;
    }

    @Override
    public LoginResult login(LoginParamMobile param, String currCode) {
        cn.hutool.core.lang.Assert.isTrue(!ObjectUtil.hasEmpty(param, param.getPhone(), param.getSmsVerifyCode(), param.getVerifyCode()), "缺少参数");
        try {
            param.setPhone(RSAUtil.privateDecode(Base64.decode(param.getPhone())));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CodeConstant.ERROR,"用户手机号有误");
        }
        boolean b = param.getVerifyCode().equalsIgnoreCase(currCode);
        if (StrUtil.isNotBlank(debugVerifyCode)) {
            boolean a = param.getVerifyCode().equalsIgnoreCase(debugVerifyCode);
            cn.hutool.core.lang.Assert.isTrue(a || b, "验证码不正确");
        } else {
            cn.hutool.core.lang.Assert.isTrue(b, "验证码不正确");
        }

        LambdaQueryWrapper<CoreOperator> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoreOperator::getPhone,param.getPhone()).select();
        List<CoreOperator> operators = coreOperatorMapper.selectList(queryWrapper);
        cn.hutool.core.lang.Assert.isTrue(operators != null && operators.size()>0, "未找到用户");
        cn.hutool.core.lang.Assert.isTrue(operators != null && operators.size()==1, "该手机号绑定了多个工号，请用工号登录！");

        CoreOperator operator = getOperatorByPhone(param.getPhone());
        String keyStr = ERRORSTR+operator.getAccount();
        if(redisTemplate.hasKey(keyStr)){
            errorNum = Integer.valueOf(redisTemplate.opsForValue().get(keyStr).toString());
            cn.hutool.core.lang.Assert.isTrue(errorNum<5,"登录次数超过5次，请30分钟后再试");
        }
        String keysms = "sms"+operator.getPhone();
        cn.hutool.core.lang.Assert.isTrue(redisTemplate.hasKey(keysms) , "验证码已失效！");
        cn.hutool.core.lang.Assert.isTrue(redisTemplate.opsForValue().get(keysms).toString().equalsIgnoreCase(param.getSmsVerifyCode()), "验证码不正确");
        cn.hutool.core.lang.Assert.isTrue(ManagerStatus.ENABLE.getCode().equalsIgnoreCase(operator.getStatus()), "用户被锁定");
        LoginResult result = new LoginResult();
        BeanUtil.copyProperties(operator, result);

        Long deptId = operator.getDeptId();
        if (deptId != null) {
            CoreDept dept = coreDeptMapper.selectById(deptId);
            if (dept != null) {
                result.setDeptPid(dept.getPid());
                result.setDeptName(dept.getSimpleName());
            }
        }

        Long operId = operator.getOperId();
        LambdaQueryWrapper<CoreRoleOper> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(CoreRoleOper::getOperId, operId).select(CoreRoleOper::getRoleId);
        List<CoreRoleOper> roleOpers = coreRoleOperMapper.selectList(queryWrapper1);
        cn.hutool.core.lang.Assert.isTrue(CollUtil.isNotEmpty(roleOpers), "用户[{}]无角色", operId);
        List<Long> roleIds = roleOpers.stream().map(CoreRoleOper::getRoleId).collect(Collectors.toList());
        if (!roleIds.isEmpty()) {
            result.setRoleIds(roleIds);
            LambdaQueryWrapper<CoreRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(CoreRole::getRoleId, roleIds);
            List<CoreRole> roles = coreRoleMapper.selectList(wrapper);
            List<RoleResult> roList = roles.stream().map(r -> {
                RoleResult ro = new RoleResult();
                ro.setRoleName(r.getName());
                ro.setRoleId(r.getRoleId());
                return ro;
            }).collect(Collectors.toList());
            result.setRoles(roList);

        }

        List<CoreMenu> list = coreMenuService.listOperMenu(operator.getOperId());
        List<CoreMenu> menuList = list.stream().filter(coreMenu -> YesOrNotEnum.Y.getDesc().equalsIgnoreCase(coreMenu.getMenuFlag())).collect(Collectors.toList());
        List<Tree<Long>> menuTree = coreMenuService.menuListToTree(menuList);
        result.setMenus(menuTree);
        return result;
    }

    @Override
    public CoreOperator getOperatorByPhone(String phone) {
        cn.hutool.core.lang.Assert.isTrue(StrUtil.isNotBlank(phone), "phone must be not blank");
        LambdaQueryWrapper<CoreOperator> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoreOperator::getPhone, phone).eq(CoreOperator::getStatus,ManagerStatus.ENABLE.getCode());
        CoreOperator operator = coreOperatorMapper.selectOne(queryWrapper);
        return operator;
    }


}
