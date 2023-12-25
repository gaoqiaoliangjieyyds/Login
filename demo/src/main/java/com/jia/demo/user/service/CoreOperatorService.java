package com.jia.demo.user.service;

import com.jia.demo.param.LoginParam;
import com.jia.demo.param.LoginParamMobile;
import com.jia.demo.user.entity.CoreOperator;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.demo.vo.LoginResult;

/**
 * <p>
 * 操作员 服务类
 * </p>
 *
 * @author jia
 * @since 2023-11-07
 */
public interface CoreOperatorService extends IService<CoreOperator> {
    LoginResult login(LoginParam param, String currCode);

    public CoreOperator getOperatorByAccount(String account);

    LoginResult login(LoginParamMobile param, String currCode);

    CoreOperator getOperatorByPhone(String phone);
}
