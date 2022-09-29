package com.vrp.genetic_alg;

/**
 * @Author jinjun99
 * @Date Created in 2022/3/20 11:24
 * @Description 计算所有染色体适应度
 * @Since version-1.0
 */
public class GenAlgFitness {
    /**
     * 计算适应度方法
     * @param gAr01 算法需要的资源
     */
    public static void calculateFitness(GenAlgResources gAr01) {
        //适应度总和是直接用+=计算的，所以要先初始化。
        gAr01.fitnessSum = 0;
        //零时变量，记录当前种群的最优适应度及其编号
        double minCost = Double.MAX_VALUE;
        int bestChroId = 0;
        /*记录最优染色体中每个订单的超时赔付金*/
        double[] minCompensation = new double[gAr01.k];
        /*记录最优染色体中每个订单的到达时间*/
        double[] earArrivalTime = new double[gAr01.k];
        /*记录最优染色体中每个子路径的总路程*/
        double[] minSubPathDist = new double[gAr01.m];
        /*记录最优染色体中每个订单的总路程*/
        double[] minNeedsDist = new double[gAr01.gene];
        for (int i = 0; i < gAr01.populationNum; i++) {
            /*当前染色体安排路径的总路程*/
            double distance = 0;
            /*当前子路径累计装载量*/
            int cLoad = 0;
            /*当前子路径累计路程*/
            double cDist = 0;
            /*当前染色体每个基因代表的订单的超时赔付金*/
            double[] compensation = new double[gAr01.k];
            double[] arrivalTime = new double[gAr01.k];
            /*子路径路程和每个订单路程*/
            int t1 = 0;
            double[] subPathDist = new double[gAr01.m];
            double[] needsDist = new double[gAr01.gene];
            /*当前要计算距离的两点坐标*/
            double x1 = gAr01.coordinate[0][0];
            double y1 = gAr01.coordinate[0][1];
            double x2 = gAr01.coordinate[gAr01.chromosome[i][1]][0];
            double y2 = gAr01.coordinate[gAr01.chromosome[i][1]][1];
            for (int j = 1; j < gAr01.gene; j++) {
                /*获取当前遍历的基因对应的订单编号*/
                int cliId = gAr01.chromosome[i][j];
                double dist1 = Math.sqrt(Math.abs(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
                /*回到仓库归零*/
                if (cliId == 0) {
                    cLoad = 0;
                    /*记录子路程路程*/
                    if (cDist!=0){
                        subPathDist[t1]=cDist+dist1;
                        t1++;
                    }
                    cDist = 0;
                } else {
                    /*累加子路径的装载量*/
                    cLoad += gAr01.needs[cliId];
                    /*累加子路径的路程*/
                    cDist += dist1;
                    /*记录当前遍历的基因表示的订单的路程*/
                    needsDist[j]=cDist;
                    /*记录当前遍历的基因表示的订单的累计用时*/
                    double cTime = cDist / gAr01.speed;
                    /*记录当前遍历的基因表示的订单的到达时间*/
                    arrivalTime[cliId-1]=cTime;
                    /*记录当前遍历的基因表示的订单的超时赔付金*/
                    if (cTime < gAr01.timeLimit[cliId - 1][0]) {
                        compensation[cliId-1]=(gAr01.timeLimit[cliId - 1][0] - cTime)*gAr01.punish;
                    }
                    if (cTime > gAr01.timeLimit[cliId - 1][1]) {
                        compensation[cliId-1]=(cTime - gAr01.timeLimit[cliId - 1][1])*gAr01.punish;
                    }
                }

                /*如果该路程累计的订单需求量超过货车载量，说明该路程不可行，
                该染色体适应度变为无穷大直接淘汰，虽然初始化时约束过了，
                但交叉变异也可能一些染色体导致不符合约束，要在这里筛选掉*/
                if (cLoad > gAr01.loading) {
                    distance = Double.MAX_VALUE;
                    break;
                }
                /*累加该染色体的总路程*/
                distance += dist1;

                /*更新两点坐标*/
                if (j + 1 < gAr01.gene) {
                    x1 = x2;
                    y1 = y2;
                    x2 = gAr01.coordinate[gAr01.chromosome[i][j + 1]][0];
                    y2 = gAr01.coordinate[gAr01.chromosome[i][j + 1]][1];
                }
            }

            if (distance < Double.MAX_VALUE) {
                /*计算当前染色体的路程总成本*/
                double currCost = distance * gAr01.drivingCost;
                for (int j = 0; j < gAr01.k; j++) {
                    currCost+=compensation[j];
                }

                /*如果当前染色体路程总成本小于这个种群的最小路程总成本，
                则更新当前最优染色体的成本和编号等信息*/
                if (currCost < minCost) {
                    bestChroId = i;
                    minCost = currCost;
                    minCompensation = compensation.clone();
                    earArrivalTime = arrivalTime.clone();
                    minSubPathDist = subPathDist.clone();
                    minNeedsDist = needsDist.clone();
                }
                gAr01.fitness[i] = (1.0 / currCost);
                gAr01.fitnessSum += gAr01.fitness[i];
            }
        }
        //如果当前种群的最优适应度大于当前之前每一代种群的最优适应度就更新当前的最优值和最优解
        if (minCost < gAr01.optimalValue) {
            gAr01.optimalValue = minCost;
            gAr01.compensation = minCompensation.clone();
            gAr01.arrivalTime = earArrivalTime.clone();
            gAr01.subPathDist = minSubPathDist.clone();
            gAr01.needsDist = minNeedsDist.clone();
            gAr01.optimalSolution = gAr01.chromosome[bestChroId].clone();

        }
    }
}
