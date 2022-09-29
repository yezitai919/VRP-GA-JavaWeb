package com.vrp.pojo;

import lombok.Data;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/12 15:04
 * @Description 注册信息对象,注册信息有时候比用户对象多一条注册码数据,
 * 不能直接用用户对象接收前端传过来的数据,设个对象方便接收. 接受以后再封装用户对象
 * @Since version-1.0
 */
@Data
public class Register {
    /**
     * 用户编号
     */
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用户姓名,(网名,昵称都行,打电话称呼用的)
     */
    private String netName;
    /**
     * 用户电话
     */
    private String phoneNumb;
    /**
     * 管理员注册码
     */
    private String registrationCode;
}
