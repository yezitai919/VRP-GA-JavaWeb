package com.vrp.genetic_alg;

/**
 * @Author jinjun99
 * @Date Created in 2022/3/19 17:24
 * @Description 种群染色体初始化，包括编码
 * @Since version-1.0
 */
public class GenAlgInitialize {
    /**
     * 初始化方法
     * @param gAr01 算法需要的资源
     */
    public static void initialization(GenAlgResources gAr01) {
        /*基因数=卡车数+订单数+1；示例：01230450表示1号车从仓库出发依次给123号订单
        送货后回到仓库，同时2号车从仓库出发依次给45号订单送货后回到仓库*/
        gAr01.gene = gAr01.k + gAr01.m + 1;
        /*存储一个种群的染色体的数组*/
        gAr01.chromosome = new int[gAr01.populationNum][gAr01.gene];
        /*记录一个种群每条染色体的适应度*/
        gAr01.fitness = new double[gAr01.populationNum];
        /*生存者编号，从当前种群中筛选出适应度较高的个体，作为下一代的父母，其他的个体淘汰。
         * 此数组只记录编号，染色体仍记录在chromosome数组*/
        gAr01.parent = new int[gAr01.populationNum];
        /*由上面数组中标记的染色体交换后产生的子代染色体记录在此数组*/
        gAr01.childChromosome = new int[gAr01.populationNum][gAr01.gene];
        /*记录最优的染色体*/
        gAr01.optimalValue = Double.MAX_VALUE;
        gAr01.optimalSolution = new int[gAr01.gene];

        //种群初始化
        /*遍历每条染色体*/
        for (int i = 0; i < gAr01.populationNum; i++) {

            /*生成基因的规则：头尾是0，共m+1个0，每个0不能相邻，k个订单的编号
            填充在m+1个0之间，两个0之间的订单需求之和不能大于货车载重。*/
            /*该while循环主要是防止少数不合法的情况(车安排完了订单没安排完)而重新生成该染色体*/
            while (true) {
                /*当前路线的订单需求和*/
                int cNeeds = 0;
                /*当前确定的货车数*/
                int truck = 0;
                /*标记已选过的订单编号*/
                boolean[] defK = new boolean[gAr01.k + 1];
                defK[0] = true;
                /*要标记一种情况：车没安排完订单安排完了*/
                int temp = 0;
                /*头尾固定，遍历1~gene-1的下标*/
                for (int j = 1; j < gAr01.gene - 1; j++) {
                    /*随机选个订单编号，如果被选过，就向左右遍历找到一个没被选过的
                     * 因为确定的订单越多，随机数越难找到未选的*/
                    int kGene = (int) (Math.random() * (gAr01.k) + 1);
                    int kL = kGene;
                    int kR = kGene;
                    while (defK[kGene]) {
                        /*订单安排完了车没安排完，跳出循环执行后面的逻辑*/
                        if (kR == gAr01.k && kL == 1) {
                            /*记录当前遍历到的基因号*/
                            temp = j;
                            break;
                        }
                        /*往右移动一个下标*/
                        if (kR < gAr01.k) {
                            kR++;
                        }
                        /*往左移动一个下标*/
                        if (kL > 1) {
                            kL--;
                        }
                        /*判断左右移动后的下标代表的订单是否被选中*/
                        if (!defK[kR]) {
                            kGene = kR;
                        } else if (!defK[kL]) {
                            kGene = kL;
                        }
                    }
                    /*订单安排完了车没安排完，跳出循环执行后面的逻辑*/
                    if (temp != 0) {
                        break;
                    }
                    /*找到未选过的订单后，先判断当前货车能否装下*/
                    cNeeds += gAr01.needs[kGene];
                    if (cNeeds > gAr01.loading) {
                        /*不能装下则当前位置设为0，当前货车的路程结束，
                        当前子路程的所有订单需求量归零准备安排下一辆车的路程*/
                        gAr01.chromosome[i][j] = 0;
                        cNeeds = 0;
                        truck++;
                    } else {
                        /*能装下则标记订单并写入染色体*/
                        defK[kGene] = true;
                        gAr01.chromosome[i][j] = kGene;
                    }
                }

                /*车没安排完订单安排完了，后面剩下一堆连起来的0*/
                if (temp != 0) {
                    /*遍历这些0*/
                    for (int j = temp; j < gAr01.gene - 1; j++) {
                        while (true) {
                            /*随机找一个符合约束的位置和当前的0交换*/
                            int g1 = (int) (Math.random() * (temp - 2) + 2);
                            /*判断是否符合约束*/
                            boolean b1 = gAr01.chromosome[i][g1] != 0 &&
                                    gAr01.chromosome[i][g1 - 1] != 0
                                    && gAr01.chromosome[i][g1 + 1] != 0;
                            if (b1){
                                int t1 = gAr01.chromosome[i][g1];
                                gAr01.chromosome[i][g1] = 0;
                                gAr01.chromosome[i][j] = t1;
                                truck++;
                                break;
                            }
                        }
                    }
                }
                /*货车数符合则跳出循环，否则重新生成该染色体*/
                if (truck == gAr01.m - 1) {
                    break;
                }
            }
        }
    }
}