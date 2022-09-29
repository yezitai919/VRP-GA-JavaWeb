package com.vrp.pojo;

import lombok.Data;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/12 16:12
 * @Description 修改信息对象, 修改信息比用户对象多一条新密码的数据,
 * 不能直接用用户对象接收前端传过来的数据,设个对象方便接收. 接受以后再封装用户对象
 * @Since version-1.0
 */
@Data
public class Modify {
    /**
     * 用户编号
     */
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户旧密码
     */
    private String oldPassword;
    /**
     * 用户新密码
     */
    private String newPassword;
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
