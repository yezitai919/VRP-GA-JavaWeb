package com.vrp.genetic_alg;

/**
 * @Author jinjun99
 * @Date Created in 2022/3/20 15:11
 * @Description 对种群中的染色体进行选择
 * @Since version-1.0
 */
public class OperatorSelection {
    /**
     * 从当前种群中选择较优的染色作为下一代的父母
     * @param gAr01 算法需要的资源
     */
    public static void rouletteWheelSelect(GenAlgResources gAr01) {
        int n = gAr01.populationNum;
        //计算每个个体的生存区间(用一维数组的区间对应轮盘的扇形面积)
        double[] survivalRate = new double[n];
        for (int i = 0; i < n; i++) {
            double temp = gAr01.fitness[i] / gAr01.fitnessSum;
            survivalRate[i] = i == 0 ? temp : temp + survivalRate[i - 1];
        }
        //转动次数=种群个体数
        for (int i = 0; i < n; i++) {
            //用随机数代表轮盘上的指针
            double temp = Math.random();
            //寻找被指针选中的个体编号
            for (int j = 0; j < n; j++) {
                if (temp < survivalRate[j]) {
                    //被选中个体拥有繁衍后代的权力，放入父母数组，
                    gAr01.parent[i] = j;
                    break;
                }
            }
        }
    }
}
