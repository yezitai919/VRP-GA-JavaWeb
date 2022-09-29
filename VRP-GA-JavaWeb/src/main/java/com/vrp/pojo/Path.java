package com.vrp.pojo;

import lombok.Data;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/10 12:59
 * @Description 路径数据表对象,对应数据库中的路径数据表,一个对象可以存储查出来的一行数据.
 * @Since version-1.0
 */
@Data
public class Path {
    /**
     * 路径编号
     */
    private Integer id;
    /**
     * 路径序列(字符串表示一条路径的顺序)
     */
    private String route;
    /**
     * 车牌号
     */
    private String plateNum;
    /**
     * 货车名字
     */
    private String truckName;
    /**
     * 货车类型
     */
    private String truckType;
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
     * 路径总路程
     */
    private Double totalDist;
    /**
     * 当前已行驶的路程
     */
    private Double currDist;
}
