package com.vrp.pojo;

import lombok.Data;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/5 17:54
 * @Description 订单数据表对象,对应数据库中的订单数据表,一个对象可以存储查出来的一行数据.
 * @Since version-1.0
 */
@Data
public class Needs {
    /**
     * 订单编号
     */
    private Integer id;
    /**
     * 订单需求量
     */
    private Double cliNeeds;
    /**
     * 客户横坐标
     */
    private Double cliX;
    /**
     * 客户纵坐标
     */
    private Double cliY;
    /**
     * 最早收货时间
     */
    private Double cliStart;
    /**
     * 最晚收货时间
     */
    private Double cliEnd;
    /**
     * 下单的用户编号
     */
    private Integer userId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户电话
     */
    private String phoneNum;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 配送员编号
     */
    private Integer driverId;
    /**
     * 配送员姓名
     */
    private String driverName;
    /**
     * 配送员电话
     */
    private String driverPhone;
    /**
     * 订单价格
     */
    private Double price;
    /**
     * 到达时间
     */
    private Double arrivalTime;
    /**
     * 运输状态
     */
    private String state;
    /**
     * 评价信息
     */
    private String evaluation;
    /**
     * 超时赔付金
     */
    private Double compensation;
    /**
     * 配送速度评分(打星)
     */
    private Integer rate1;
    /**
     *配送质量评分(打星)
     */
    private Integer rate2;
    /**
     * 服务态度评分(打星)
     */
    private Integer rate3;
    /**
     * 所属路径的编号
     */
    private Integer pathId;
    /**
     * 该路径从仓库出发到该订单位置的总路程
     */
    private Double totalDist;
    /**
     * 配送员当前距离该客户的路程
     */
    private Double currDist;
}
