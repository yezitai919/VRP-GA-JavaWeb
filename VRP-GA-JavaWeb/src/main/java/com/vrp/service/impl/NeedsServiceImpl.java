package com.vrp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vrp.dao.NeedsDao;
import com.vrp.pojo.Needs;
import com.vrp.service.INeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author xiaobai
 * @Date Created in 2022/4/6 10:46
 * @Description 服务层, 把一个或多个对订单表的增伤改查功能组成符合业务需求的一组事务,
 * 再获取数据库连接对象执行这组事务,整体封装成一个对订单表的服务方法;这里ServiceImpl
 * 把大部分工作都封装了,直接创建本类对象就可以调用很多封装好的标准方法对订单表进行简单的增
 * 删改查操作,如果有复杂的需求可以在这里进一步封装,否则可以啥都不写.
 * @Since version-1.0
 */
@Service
public class NeedsServiceImpl extends ServiceImpl<NeedsDao, Needs> implements INeedsService {
    /**
     * 注入封装订单表SQL语句的对象
     */
    @Autowired
    private NeedsDao needsDao;

    /**
     * 分页+条件查询
     * @param currPage 当前页数
     * @param pageSize 总页数
     * @param needs 查询条件
     * @return 分页的订单数据
     */
    @Override
    public IPage<Needs> getPage(int currPage, int pageSize, Needs needs) {
        /*条件查询,挨个比较前端查询输入框输入的值，为空就不比较*/
        LambdaQueryWrapper<Needs> lqw = new LambdaQueryWrapper<Needs>();
        lqw.eq(needs.getId()!=null,Needs::getId,needs.getId());
        lqw.eq(needs.getUserId()!=null,Needs::getUserId,needs.getUserId());
        lqw.like(needs.getUserName()!=null&&needs.getUserName()!="",Needs::getUserName,needs.getUserName());
        lqw.eq(needs.getPhoneNum()!=null&&needs.getPhoneNum()!="",Needs::getPhoneNum,needs.getPhoneNum());
        lqw.eq(needs.getCliNeeds()!=null,Needs::getCliNeeds,needs.getCliNeeds());
        lqw.eq(needs.getCliX()!=null,Needs::getCliX,needs.getCliX());
        lqw.eq(needs.getCliY()!=null,Needs::getCliY,needs.getCliY());
        lqw.eq(needs.getCliStart()!=null,Needs::getCliStart,needs.getCliStart());
        lqw.eq(needs.getCliEnd()!=null,Needs::getCliEnd,needs.getCliEnd());
        lqw.eq(needs.getPrice()!=null,Needs::getPrice,needs.getPrice());
        lqw.eq(needs.getArrivalTime()!=null,Needs::getArrivalTime,needs.getArrivalTime());
        lqw.eq(needs.getState()!=null& !Objects.equals(needs.getState(), ""),Needs::getState,needs.getState());
        lqw.eq(needs.getDriverId()!=null,Needs::getDriverId,needs.getDriverId());
        lqw.like(needs.getDriverName()!=null&& !Objects.equals(needs.getDriverName(), ""),Needs::getDriverName,needs.getDriverName());
        lqw.eq(needs.getDriverPhone()!=null&& !Objects.equals(needs.getDriverPhone(), ""),Needs::getDriverPhone,needs.getDriverPhone());
        lqw.like(needs.getRemark()!=null&& !Objects.equals(needs.getRemark(), ""),Needs::getRemark,needs.getRemark());
        /*分页查询*/
        IPage<Needs> page = new Page<Needs>(currPage,pageSize);
        needsDao.selectPage(page,lqw);
        return page;
    }


    /**
     * 根据路径编号查询订单
     * @param id 路径编号
     * @return 订单数据
     */
    @Override
    public List<Needs> getNeedsByPathId(Integer id){
        LambdaQueryWrapper<Needs> lqw = new LambdaQueryWrapper<Needs>();
        lqw.eq(id!=null,Needs::getPathId,id);
        return needsDao.selectList(lqw);
    }
}
