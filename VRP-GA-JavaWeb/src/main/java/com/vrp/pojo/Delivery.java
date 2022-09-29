package com.vrp.pojo;

import lombok.Data;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/7 22:43
 * @Description 配送数据表对象,对应数据库中的配送数据表,一个对象可以存储查出来的一行数据.
 * @Since version-1.0
 */
@Data
public class Delivery {
    /**
     * 数据编号,暂时只有一条,数据库表就算只有一条数据也要设一列编号才方便查询修改.
     */
    private Integer id;

    /**
     * 货车数量
     */
    private Integer truck;
    /**
     * 货车装载量
     */
    private Double loading;
    /**
     * 货车速度
     */
    private Double speed;
    /**
     * 每公里行驶成本
     */
    private Double cost;
    /**
     * 每超1小时赔付金额
     */
    private Double punish;
    /**
     * 仓库横坐标
     */
    private Double deportX;
    /**
     * 仓库纵坐标
     */
    private Double deportY;
    /**
     * 每吨收费
     */
    private Double priceT;
    /**
     * 每公里收费
     */
    private Double priceKm;
    /**
     * 值班管理员姓名
     */
    private String adminName;
    /**
     * 值班管理员电话
     */
    private String adminPhone;
    /**
     * 管理员注册码
     */
    private String registrationCode;
}
