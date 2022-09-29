package com.vrp.pojo;

import lombok.Data;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/8 17:17
 * @Description 用户信息表对象,对应数据库的用户信息表,一个对象可以存储查出来的一行数据.
 * @Since version-1.0
 */
@Data
public class User {
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
}
