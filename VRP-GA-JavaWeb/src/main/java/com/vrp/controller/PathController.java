package com.vrp.controller;

import com.vrp.controller.utils.Res;
import com.vrp.pojo.Needs;
import com.vrp.pojo.Path;
import com.vrp.service.INeedsService;
import com.vrp.service.IPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/7 22:58
 * @Description 处理前端对路径表的相关请求
 * @Since version-1.0
 */
@RestController
@RequestMapping("/path")
public class PathController {
    /**
     * 注入路径表服务对象
     */
    @Autowired
    private IPathService pathService;
    /**
     * 注入订单表服务对象
     */
    @Autowired
    private INeedsService needsService;

    /**
     * 处理前端查询所有路径请求
     * @return
     */
    @GetMapping
    public Res getAll() {
        /*直接调用路径表服务对象的查询所有方法，
        返回一个路径对象的集合，包装成标准格式返回给前端*/
        return new Res(true, pathService.list());
    }

    /**
     * 处理前端修改一条路径的请求，路径的顺序是算法安排的，
     * 管理员只能修改对这条路径安排的配送员和货车
     * @param path 路径对象，包含修改内容和路径编号
     * @return 标准返回格式，返回一些信息
     */
    @PutMapping
    private Res upDate(@RequestBody Path path) {
        /*调用路径表服务对象的修改方法修改这条路径的信息*/
        boolean flag = pathService.updateById(path);
        if (flag) {
            /*如果修改成功，再调用订单表服务对象的查询所有方法获取所有订单信息对象，
            找到被修改的路径中的所有订单，修改这些订单的配送员编号、姓名、电话，使其与修改后的路径信息保持一致*/
            List<Needs> needsList = needsService.getNeedsByPathId(path.getId());
            for (Needs needs : needsList) {
                needs.setDriverId(path.getDriverId());
                needs.setDriverName(path.getDriverName());
                needs.setDriverPhone(path.getDriverPhone());
            }
            /*再把修改后的订单信息更新到数据库*/
            needsService.updateBatchById(needsList);
        }

        return new Res(flag, flag ? "修改成功(*^ω^*)" : "修改失败(´•̥̥̥ω•̥̥̥`)");
    }

    /**
     * 处理前端根据编号查找路径的请求
     * @param id
     * @return
     */
    @GetMapping("{id}")
    private Res getById(@PathVariable Integer id) {
         /*直接调用路径表服务对象的根据id查询方法，
        返回一个路径对象，包装成标准格式返回给前端*/
        return new Res(true, pathService.getById(id));

    }

    /**
     * 处理前端根据配送员编号查找路径的请求
     * @param id
     * @return
     */
    @GetMapping("/driverId={id}")
    private Res getByDriverId(@PathVariable Integer id) {
         /*直接调用路径表服务对象自定义的根据配送员id查询方法，
        返回一个路径对象，包装成标准格式返回给前端*/
        return new Res(true, pathService.getPathByDriverId(id));
    }

    /**
     * 处理前端修改当前行程请求，前端滑动行程记录条更改当前行程后都会发送这个请求
     * @param path
     * @return
     */
    @PutMapping("/position")
    private Res upDatePosition(@RequestBody Path path) {
        /*调用路径表服务对象的根据编号修改方法更新当前行驶路程*/
        boolean flag = pathService.updateById(path);
        if (flag) {
             /*如果修改成功，再调用订单表服务对象的查询所有方法获取所有订单信息对象，
            找到被修改的路径中的所有订单，修改这些订单的当前距离客户路程和运输状态，
            根据路径当前行程和订单的行程计算可得结果*/
            List<Needs> needsList = needsService.getNeedsByPathId(path.getId());
            double currDist = path.getCurrDist();
            for (Needs needs:needsList){
                if (currDist==0){
                    needs.setState("等待发货");
                    needs.setCurrDist(needs.getTotalDist());
                }
                if (currDist>0&&currDist<needs.getTotalDist()){
                    needs.setState("运输中");
                    needs.setCurrDist(needs.getTotalDist()-currDist);
                }
                if (currDist>=needs.getTotalDist()){
                    needs.setState("已送达");
                    needs.setCurrDist(0.0);
                }
            }
            /*再把修改后的订单信息更新到数据库*/
            needsService.updateBatchById(needsList);
        }
        return new Res(flag);
    }


}
