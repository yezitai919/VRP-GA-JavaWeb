package com.vrp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vrp.controller.utils.Res;
import com.vrp.pojo.Needs;
import com.vrp.service.INeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/6 11:02
 * @Description 处理前端对订单表的相关请求
 * @Since version-1.0
 */
@RestController
@RequestMapping("/needs")
public class NeedsController {
    /**
     * 注入订单表服务对象
     */
    @Autowired
    private INeedsService needsService;

    /**
     * 处理前端分页查询的请求
     * @param currPage 当前页码数
     * @param pageSize 所有页码数
     * @param needs 查询条件(可以为空，即查询所有)
     * @return
     */
    @GetMapping("{currPage}/{pageSize}")
    public Res getPage(@PathVariable int currPage, @PathVariable int pageSize, Needs needs) {
        /*直接调用订单表服务对象自定义的条件查询+分页查询方法，把页码信息和条件信息传进去，返回一个分页的订单对象集合*/
        IPage<Needs> page = needsService.getPage(currPage, pageSize, needs);
        /*分页优化：最后一页只有一条数据时，删除这条数据会重新更改分页数查询*/
        if (currPage > page.getPages()) {
            page = needsService.getPage((int) page.getPages(), pageSize, needs);
        }
        /*包装成标准格式返回给前端*/
        return new Res(true, page);
    }
    /**
     * 处理前端添加一条订单信息的请求
     * @param needs 封装一条订单信息的对象
     * @return
     */
    @PostMapping
    private Res save(@RequestBody Needs needs) {
         /*直接调用订单表服务对象的添加方法修改一条订单信息，
        返回一个布尔值表示是否成功，包装成标准格式返回给前端*/
        boolean flag = needsService.save(needs);
        return new Res(flag, flag ? "添加成功٩(๑^o^๑)۶" : "添加失败(இωஇ )");
    }

    /**
     * 处理前端修改一条订单信息的请求
     * @param needs 封装一条订单信息的对象
     * @return
     */
    @PutMapping
    private Res upDate(@RequestBody Needs needs) {
         /*直接调用订单表服务对象的修改方法修改一条订单信息，
        返回一个布尔值表示是否成功，包装成标准格式返回给前端*/
        boolean flag = needsService.updateById(needs);
        return new Res(flag, flag ? "修改成功(*^ω^*)" : "修改失败(´•̥̥̥ω•̥̥̥`)");
    }

    /**
     * 处理前端删除一条订单信息的请求
     * @param id 封装一条订单信息的对象
     * @return
     */
    @DeleteMapping("{id}")
    private Res delete(@PathVariable Integer id) {
        /*直接调用订单表服务对象的删除方法删除一条订单信息，
        返回一个布尔值表示是否成功，包装成标准格式返回给前端*/
        boolean flag = needsService.removeById(id);
        return new Res(flag, flag ? "删除成功(●'◡'●)" : "删除失败(ᇂ_ᇂ|||)");
    }


    /**
     * 处理前端查询一条订单信息的请求
     * @param id 查询的订单编号
     * @return
     */
    @GetMapping("{id}")
    private Res getById(@PathVariable Integer id) {
         /*直接调用订单表服务对象的根据编号查询方法查询一条订单信息，
        返回一个订单信息对象，包装成标准格式返回给前端*/
        return new Res(true, needsService.getById(id));

    }


}
