package com.jia.demo.user.entity;/**
 * @author ChenJia
 * @create 2023-12-25 15:00
 */

import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

/**
 *@ClassName CoreDept
 *@Description TODO
 *@Author jia
 *@Date 2023/12/25 15:00
 *@Version 1.0
 **/
@Table(name="core_dept")
@Data
public class CoreDept  {

    /**
     * @AutoID
     * @AssignID("simple")
     * @SeqID(name = "core_dept_seq")
     */

    private Long deptId;


    private Integer sort;


    private Integer version;


    private Long createUser;


    private String description;


    private String fullName;


    private Long pid;


    private String pids;

    private String simpleName;


    private Long updateUser;


    private Date createTime;


    private Date updateTime;


    public CoreDept() {
    }


}
