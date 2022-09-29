package com.vrp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vrp.pojo.Needs;

import java.util.List;

/**
 * @Author xiaobai
 * @Date Created in 2022/4/6 9:45
 * @Description
 * @Since version-1.0
 */
public interface INeedsService extends IService<Needs> {
    /**
     * 条件+分页查询
     * @param currPage 当前页码
     * @param pageSize 总页码数
     * @param needs 查询条件
     * @return 分页的订单数据
     */
    IPage<Needs> getPage(int currPage, int pageSize, Needs needs);

    /**
     * 根据路径编号查询
     * @param id 路径编号
     * @return 订单数据
     */
    List<Needs> getNeedsByPathId(Integer id);
}
