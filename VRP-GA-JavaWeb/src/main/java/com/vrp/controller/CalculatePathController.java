package com.vrp.controller;

import com.vrp.controller.utils.Res;
import com.vrp.genetic_alg.*;
import com.vrp.pojo.*;
import com.vrp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/8 11:03
 * @Description 处理前端调用遗传算法计算最优路径的请求，并更新相关数据到数据库
 * @Since version-1.0
 */
@RestController
@RequestMapping("/calculate")
public class CalculatePathController {
    /**
     * 注入订单数据表服务对象
     */
    @Autowired
    private INeedsService needsService;
    /**
     * 注入配送数据表服务对象
     */
    @Autowired
    private IDeliveryService deliveryService;
    /**
     * 注入遗传参数表服务对象
     */
    @Autowired
    private IGeneticService geneticService;
    /**
     * 注入路径数据表服务对象
     */
    @Autowired
    private IPathService pathService;

    @GetMapping
    public Res getPath(){
        /*调用订单数据表服务对象的查询所有方法，
        返回一个订单数据对象的集合*/
        List<Needs> needsList = needsService.list();
          /*调用配送数据表服务对象的查询所有方法，
        返回一个配送数据对象的集合(其实只有一个，但是id在修改中可能会变，咋也不确定就不好根据id查一条)*/
        List<Delivery> deliveryList = deliveryService.list();
          /*调用遗传参数表服务对象的查询所有方法，
        返回一个遗传参数对象的集合(其实只有一个，同上)*/
        List<Genetic> geneticList = geneticService.list();

        /*把数据库取出的计算必要数据记录到一些变量中传给遗传算法，计算出结果再更新到数据库，一部分传给前端*/

        /*获取订单数量*/
        int k = needsList.size();
        /*创建记录所有订单编号的数组*/
        int[]ids = new int[k+1];
        /*创建记录所有订单需求量的数组*/
        double[] needs = new double[k+1];
        /*创建记录所有订单位置的数组*/
        double[][]coordinate= new double[k+1][2];
        /*创建记录所有订单收货时限的数组*/
        double[][] timeLimit = new double[k][2];
        /*把订单对象集合中的数据记录到前面创建的数组中*/
        for (int i = 0; i < k; i++) {
            Needs needs1 = needsList.get(i);
            ids[i+1]=needs1.getId();
            needs[i+1]= needs1.getCliNeeds();
            coordinate[i+1][0] = needs1.getCliX();
            coordinate[i+1][1] = needs1.getCliY();
            timeLimit[i][0] = needs1.getCliStart();
            timeLimit[i][1] = needs1.getCliEnd();
        }
        /*记录货车数量*/
        int m = deliveryList.get(0).getTruck();
        /*记录货车载重量*/
        double loading = deliveryList.get(0).getLoading();
        /*记录货车速度*/
        double speed = deliveryList.get(0).getSpeed();
        /*记录每公里行驶成本*/
        double drivingCost = deliveryList.get(0).getCost();
        /*记录每小时超时罚金*/
        double punish = deliveryList.get(0).getPunish();
        /*记录仓库横纵坐标*/
        coordinate[0][0] = deliveryList.get(0).getDeportX();
        coordinate[0][1] = deliveryList.get(0).getDeportY();
        /*记录种群数量*/
        int populationNum = geneticList.get(0).getPopuNum();
        /*记录遗传代数*/
        int generationNum = geneticList.get(0).getGeneNum();
        /*记录交叉率*/
        double crossoverRate = geneticList.get(0).getCrosRate();
        /*记录变异率*/
        double mutationRate = geneticList.get(0).getMutaRate();

        /*创建遗传算法资源对象，把上面记录的参数传进去*/
        GenAlgResources gAr01 = new GenAlgResources(k,m,needs,timeLimit,
                coordinate,loading,speed,punish,drivingCost,populationNum,
                generationNum,crossoverRate,mutationRate);
        //计算开始时间
        long s = System.currentTimeMillis();
        double allNeed = 0;
        /*如果订单总重量超过货车总载量，返回计算失败信息*/
        for (int i = 1; i <= k; i++) {
            allNeed+=needs[i];
        }
        if (allNeed>=m*loading){
            return new Res(false,"客户总需求量大于货车总装载量");
        }
        //初始化种群数据
        GenAlgInitialize.initialization(gAr01);
        /*循环计算遗传代数*/
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


        /*封装计算结果传给前端*/
        GeneResult result = new GeneResult();
        result.setMinCost(gAr01.optimalValue);
        result.setCalculatingTime(e-s);
        splitPath(result,ids,gAr01.getOptimalSolution());

        /*更新数据库中的订单表和路径表*/
        updatePath(result,needsList,gAr01);
        updateNeeds(gAr01,needsList);
        return new Res(true,result);
    }

    /**
     * 把最优染色体裁剪修改成适合展示的格式：比如把记录为
     * 0,1,2,0,5,4,3,0的染色体int数组改成String数组" 0 → 1 → 2 → 0 "," 0 → 5 → 4 → 3 → 0 "
     * @param result 遗传算法计算结果
     * @param ids 所有订单编号
     * @param optimalSolution 最优路径染色体
     */
    private void splitPath(GeneResult result,int[] ids ,int[] optimalSolution){
        int t1 = 0;
        String path = "  ";
        for (int i = 0; i < optimalSolution.length; i++) {
            int g = optimalSolution[i];
            if (g==0){
                t1++;
            }
            if (t1<2){
                path+=ids[g]+" → ";
            }else {
                path+=ids[g]+"  ";
                result.getOptimalPath().add(path);
                t1=0;
                path="  ";
                i--;
            }
        }
    }

    /**
     * 更新路径表
     * @param result 遗传算法计算结果
     * @param needsList 所有订单对象集合，先修改，后面再更新到数据库
     * @param gAr01 遗传算法资源对象
     */
    private void updatePath(GeneResult result,List<Needs> needsList,GenAlgResources gAr01){
        /*获取遗传算法计算结果中的所有路径序列*/
        List<String> path1 = result.getOptimalPath();
        /*记录路径数*/
        int len1 = path1.size();
        /*调用路径表服务对象的查询所有方法获取所有路径*/
        List<Path> pathList = pathService.list();
        /*记录路径数*/
        int len2 = pathList.size();

        /*记录遗传算法计算结果中每个订单对应的路径编号*/
        gAr01.needsPath = new int[gAr01.k][2];
        /*工具变量*/
        int t1 = 0;
        /*遍历计算结果的路径数，一条条写入数据库中的路径表，
        如果前端修改了配送数据的货车数量，计算出的路径数量也会变，
        如果计算结果比数据库中的路径少，就删除数据库表中多余的路径数据，
        如果计算结果比数据库中的路径多，就新增路径数据，实现动态的修改路径表*/
        for (int i = 0; i < len1; i++) {
            if (i+1<=len2){
                /*更新路径表的路径数据中的路径序列和总路程，这两条是算法决定的，
                其他是前端管理员页面自由改的*/
                pathList.get(i).setRoute(path1.get(i));
                pathList.get(i).setTotalDist(gAr01.getSubPathDist()[i]);
                /*把修改结果更新到数据库*/
                pathService.updateById(pathList.get(i));
                /*获取这条路径上的所有订单编号*/
                int[] pathIds = strToArr(path1.get(i));
                for (int pathId : pathIds) {
                    /*记录订单编号对应的路径编号*/
                    gAr01.needsPath[t1][0]= pathList.get(i).getId();
                    gAr01.needsPath[t1][1]= pathId;
                    t1++;
                    /*路径修改以后要顺便修改路径上所有订单的路径编号、配送员编号、姓名、电话*/
                    for (Needs needs1 : needsList) {
                        if (pathId == needs1.getId()) {
                            needs1.setPathId(pathList.get(i).getId());
                            needs1.setDriverId(pathList.get(i).getDriverId());
                            needs1.setDriverName(pathList.get(i).getDriverName());
                            needs1.setDriverPhone(pathList.get(i).getDriverPhone());
                            break;
                        }
                    }
                }
            }else {
                /*如果计算结果比数据库中的路径少，就删除数据库表中多余的路径数据*/
                Path path2 = new Path();
                path2.setRoute(path1.get(i));
                path2.setTotalDist(gAr01.getSubPathDist()[i]);
                pathService.save(path2);
            }
        }
        /*如果计算结果比数据库中的路径多，就新增路径数据，实现动态的修改路径表*/
        if (len2>len1){
            for (int i = len1; i < len2; i++) {
                pathService.removeById(pathList.get(i).getId());
            }
        }
    }

    /**
     * 前面splitPath()方法的逆运算，把String数组" 0 → 1 → 2 → 0 ",
     * " 0 → 5 → 4 → 3 → 0 "改成int数组0,1,2,0,5,4,3,0
     * @param str 字符串形式的路径序列，路径表记录的数据和前端计算结果展示都是这个格式
     * @return
     */
    public static int[] strToArr(String str){
        String[] str1 =str.split("→");
        int len = str1.length-2;
        int[] pathIds = new int[len];
        for (int j = 0; j < len; j++) {
            pathIds[j] = Integer.parseInt(str1[j+1].trim());
        }
        return pathIds;
    }

    /**
     * 更新订单表
     * @param gAr01 遗传算法资源对象
     * @param needsList 所有订单对象集合
     */
    private void updateNeeds(GenAlgResources gAr01,List<Needs> needsList){
        /*把计算好的每个订单的预计到达时间、赔付金额和总路程(记录在遗传资源对象里)更新到所有订单对象集合*/
        for (int i = 0; i < needsList.size(); i++) {
            /*小数精度限制为1位*/
            double d1 = (double) Math.round(gAr01.arrivalTime[i] * 10) / 10;
            double d2 = (double) Math.round(gAr01.compensation[i] * 10) / 10;
            needsList.get(i).setArrivalTime(d1);
            needsList.get(i).setCompensation(d2);
            for (int j = 0; j < gAr01.gene; j++) {
                if (gAr01.optimalSolution[j]==i+1){
                    needsList.get(i).setTotalDist(gAr01.needsDist[j]);
                }
            }
            /*所有订单的运输状态设为默认值*/
            needsList.get(i).setState("等待发货");
        }
        /*把修改后的所有订单对象集合更新到数据库中的订单表*/
        needsService.updateBatchById(needsList);
    }

}
