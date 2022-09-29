package com.vrp.pojo;

import lombok.Data;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/7 22:46
 * @Description 遗传算法参数表对象,对应数据库中的遗传算法参数表,一个对象可以存储查出来的一行数据.
 * @Since version-1.0
 */
@Data
public class Genetic {
    /**
     * 数据编号
     */
    private Integer id;
    /**
     * 种群数量
     */
    private Integer popuNum ;
    /**
     * 遗传代数
     */
    private Integer geneNum ;
    /**
     * 交叉率
     */
    private Double crosRate;
    /**
     * 变异率
     */
    private Double mutaRate;
}
