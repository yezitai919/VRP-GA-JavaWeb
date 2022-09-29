package com.vrp.genetic_alg;

/**
 * @Author jinjun99
 * @Date Created in 2022/3/20 14:58
 * @Description 对种群中的染色体进行交叉
 * @Since version-1.0
 */
public class OperatorCrossover {
    /**
     * 交叉染色体的方法
     * @param gAr01 算法需要的资源
     */
    public static void crossover(GenAlgResources gAr01){
        /*遍历种群中的染色体*/
        for (int i = 0; i < gAr01.populationNum; i++) {
            /*生成一个随机数，如果小于交叉率就进行交叉*/
            if (i+2<gAr01.populationNum && Math.random()<gAr01.crossoverRate) {
                //双点交叉
                int t1 = (int) (Math.random() * (gAr01.gene - 3)) + 1;
                int t2 = (int) (Math.random() * (gAr01.gene - 3)) + 2;
                /*保证t2>t1*/
                if (t2 < t1) {
                    int t3 = t1;
                    t1 = t2;
                    t2 = t3;
                }
                /*染色体交叉生成子代*/
                for (int j = 0; j < gAr01.gene; j++) {
                    /*i和i+1两条染色体中处于两个交叉点之间的部分交换基因，0的点不交换*/
                    if (j >= t1 && j <= t2&&gAr01.chromosome[gAr01.parent[i]][j]!=0&&gAr01.chromosome[gAr01.parent[i+1]][j]!=0) {
                        gAr01.childChromosome[i][j] = gAr01.chromosome[gAr01.parent[i+1]][j];
                        gAr01.childChromosome[i+1][j] = gAr01.chromosome[gAr01.parent[i]][j];
                    } else {
                        /*不换的部分直接复制*/
                        gAr01.childChromosome[i][j] = gAr01.chromosome[gAr01.parent[i]][j];
                        gAr01.childChromosome[i+1][j] = gAr01.chromosome[gAr01.parent[i+1]][j];
                    }
                }
                /*冲突互换，放进死循环，直到没有冲突为止*/
                int count = 0;
                while (true){
                    for (int j = 0; j < gAr01.gene; j++) {
                        /*遍历交换部分*/
                        if (j>=t1&&j<=t2&&gAr01.childChromosome[i][j]!=0&&gAr01.childChromosome[i+1][j]!=0){
                            for (int k = 0; k < gAr01.gene; k++) {
                                /*遍历未交换部分*/
                                if (k<t1||k>t2||gAr01.childChromosome[i][k]==0||gAr01.childChromosome[i+1][k]==0){
                                    /*对i号染色体，如果交换段之外的k号基因与交换段内的j号基因相同且不是0，
                                    则把i+1号染色体中与j同位置的基因和i号染色体的k基因互换*/
                                    if (gAr01.childChromosome[i][k]!=0&&gAr01.childChromosome[i][k]==gAr01.childChromosome[i][j]){
                                        gAr01.childChromosome[i][k]=gAr01.childChromosome[i+1][j];
                                        count++;
                                    }
                                     /*对i+1号染色体，如果交换段之外的k号基因与交换段内的j号基因相同且不是0，
                                    则把i号染色体中与j同位置的基因和i+1号染色体的k基因互换*/
                                    if (gAr01.childChromosome[i+1][k]!=0&&gAr01.childChromosome[i+1][k]==gAr01.childChromosome[i+1][j]){
                                        gAr01.childChromosome[i+1][k]=gAr01.childChromosome[i][j];
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                    if (count==0){
                        break;
                    }else {
                        count=0;
                    }
                }
                i++;
            }else {
                /*大于交叉率的部分直接复制到子代*/
                gAr01.childChromosome[i]=gAr01.chromosome[gAr01.parent[i]].clone();
            }
        }
    }
}
