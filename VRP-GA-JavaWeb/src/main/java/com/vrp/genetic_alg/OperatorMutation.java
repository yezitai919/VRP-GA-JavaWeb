package com.vrp.genetic_alg;

/**
 * @Author jinjun99
 * @Date Created in 2022/3/20 15:10
 * @Description 对种群中的染色体进行变异
 * @Since version-1.0
 */
public class OperatorMutation {
    /**
     * 子代染色体变异的方法
     * @param gAr01 算法需要的资源
     */
    public static void mutation(GenAlgResources gAr01){
        //遍历子代每个染色体的每个基因
        for (int i = 0; i < gAr01.populationNum; i++) {
            for (int j = 0; j < gAr01.gene; j++) {
                //如果随机数小于变异率就进行变异操作
                if (Math.random()<gAr01.mutationRate){
                    while (true){
                        /*在当前染色体上随机选两个基因，有三种情况可直接交换这两个基因的位置*/
                        int temp1 = (int)(Math.random()*(gAr01.gene-2))+1;
                        int temp2 = (int)(Math.random()*(gAr01.gene-2))+1;
                        /*两个基因都不为0*/
                        boolean b1 = gAr01.childChromosome[i][temp1]!=0&&gAr01.childChromosome[i][temp2]!=0;
                        /*其中一个为0，另一个不为0且左右都不为0*/
                        boolean b2 = gAr01.childChromosome[i][temp1]==0&&gAr01.childChromosome[i][temp2]!=0&&
                                gAr01.childChromosome[i][temp2+1]!=0&&gAr01.childChromosome[i][temp2-1]!=0;
                        boolean b3 = gAr01.childChromosome[i][temp2]==0&&gAr01.childChromosome[i][temp1]!=0&&
                                gAr01.childChromosome[i][temp1+1]!=0&&gAr01.childChromosome[i][temp1-1]!=0;
                        /*符合条件直接交换基因*/
                        if (b1||b2||b3){
                            int temp3 = gAr01.childChromosome[i][temp1];
                            gAr01.childChromosome[i][temp1]=gAr01.childChromosome[i][temp2];
                            gAr01.childChromosome[i][temp2]=temp3;
                            break;
                        }
                    }
                }
            }
        }
        //变异完后子代染色体就完成了，子代染色体替换父代，准备下一次迭代
        for (int i = 0; i < gAr01.populationNum; i++) {
            gAr01.chromosome[i]=gAr01.childChromosome[i].clone();
        }
    }

}
