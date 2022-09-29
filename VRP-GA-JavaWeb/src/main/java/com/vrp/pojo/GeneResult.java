package com.vrp.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/8 12:46
 * @Description 遗传算法计算结果对象,计算结果以这个格式发送给前端
 * @Since version-1.0
 */
@Data
public class GeneResult {
    /**
     * 最小成本
     */
    private Double minCost;
    /**
     * 计算时间
     */
    private Long calculatingTime;
    /**
     * 最优路径
     */
    private List<String> optimalPath = new ArrayList<>();
}
