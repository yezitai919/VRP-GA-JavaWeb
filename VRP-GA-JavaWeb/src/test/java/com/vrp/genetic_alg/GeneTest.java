package com.vrp.genetic_alg;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/8 10:57
 * @Description
 * @Since version-1.0
 */
@SpringBootTest
public class GeneTest {
    @Test
    void geneTest(){
        int k = 10;
        int m = 3;
        double loading = 5;

        double[] needs = {0,0.1,0.4,1.2,1.5,0.8,1.3,1.7,0.6,1.2,0.4};
        double[][]coordinate={{14.5,13.0},{12.8,8.5},{18.4,3.4},{15.4,16.6},{18.9,15.2},
                {15.5,11.6},{3.9,10.6},{10.6,7.6},{8.6,8.4},{12.5,2.1},{13.8,5.2}};

        double[][] timeLimit = {{4.7,10.5},{1.5,6.0},{4.7,10.2},{5.1,9.5},{3.7,8.9},
                {6.7,12.3},{7.9,12.9},{0.6,5.7},{2.6,6.8},{2.5,8.1}};
        double speed = 5;
        double punish = 2;
        double drivingCost = 2;
        int populationNum = 800;
        int generationNum = 80;
        double crossoverRate = 0.6;
        double mutationRate = 0.01;
        GenAlgResources gAr01 = new GenAlgResources(k,m,needs,timeLimit,
                coordinate,loading,speed,punish,drivingCost,populationNum,
                generationNum,crossoverRate,mutationRate);
        //计算开始时间
        long s = System.currentTimeMillis();
        double allNeed = 0;
        for (int i = 1; i <= k; i++) {
            allNeed+=needs[i];
        }
        if (allNeed>=m*loading){
            System.out.println("无法装下");
            return;
        }
        //初始化种群数据
        GenAlgInitialize.initialization(gAr01);

        //繁衍迭代
        for (int i = 0; i < gAr01.generationNum; i++) {
            //计算i代的适应度
            GenAlgFitness.calculateFitness(gAr01);
            //从i代中选择第i+1代的父母染色体
            OperatorSelection.rouletteWheelSelect(gAr01);
            //父母染色体交叉或复制繁衍子代
            OperatorCrossover.crossover(gAr01);
            //子代染色体变异并替换父代染色体
            OperatorMutation.mutation(gAr01);
        }
        //计算结束时间
        long e = System.currentTimeMillis();
        System.out.println("最优值为：" + gAr01.optimalValue);
        System.out.println("最优解为：" + Arrays.toString(gAr01.optimalSolution));
        System.out.println("计算时间为："+(e-s)+"毫秒。");
    }
}
