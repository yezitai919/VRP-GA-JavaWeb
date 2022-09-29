package com.vrp.controller;

import com.vrp.controller.utils.Res;
import com.vrp.pojo.Delivery;
import com.vrp.service.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/7 22:58
 * @Description 处理前端对遗传参数表的相关请求
 * @Since version-1.0
 */
@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    /**
     * 注入配送数据表服务对象
     */
    @Autowired
    private IDeliveryService deliveryService;

    /**
     * 处理前端查询所有配送数据的请求(其实只有一个，但是id在修改中可能会变，咱也不确定就不好根据id查一条)
     * @return
     */
    @GetMapping
    public Res getAll(){
         /*直接调用配送数据表服务对象的查询所有方法，
        返回一个配送数据对象的集合，包装成标准格式返回给前端*/
        return new Res(true,deliveryService.list());
    }

    /**
     * 处理前端修改一条配送数据的请求
     * @param delivery 修改后的一条配送信息
     * @return
     */
    @PutMapping
    private Res upDate(@RequestBody Delivery delivery){
        /*直接调用配送数据表服务对象的修改方法修改一条订单信息，
        返回一个布尔值表示是否成功，包装成标准格式返回给前端*/
        boolean flag = deliveryService.updateById(delivery);
        return new Res(flag,flag?"修改成功(*^ω^*)":"修改失败(´•̥̥̥ω•̥̥̥`)");
    }
}
