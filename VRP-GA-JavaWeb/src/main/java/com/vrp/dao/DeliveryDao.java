package com.vrp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vrp.pojo.Delivery;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/5 18:02
 * @Description 数据访问层,对配送表的数据进行基础的增删改查, 把SQL语句封装成对应功能的方法,
 * 因为继承的BaseMapper封装了所有常用的SQL语句,传入配送表对象就能自动修改直接使用,所以啥都不用写
 * @Since version-1.0
 */
@Mapper
public interface DeliveryDao extends BaseMapper<Delivery> {

}
