package com.vrp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vrp.dao.GeneticDao;
import com.vrp.pojo.Genetic;
import com.vrp.service.IGeneticService;
import org.springframework.stereotype.Service;

/**
 * @Author xiaobai
 * @Date Created in 2022/4/7 22:54
 * @Description 服务层, 把一个或多个对遗传参数表的增伤改查功能组成符合业务需求的一组事务,
 * 再获取数据库连接对象执行这组事务,整体封装成一个对遗传参数表的服务方法;这里ServiceImpl
 * 把大部分工作都封装了,直接创建本类对象就可以调用很多封装好的标准方法对遗传参数表进行简单的增
 * 删改查操作,如果有复杂的需求可以在这里进一步封装,否则可以啥都不写.
 * @Since version-1.0
 */
@Service
public class GeneticServiceImpl extends ServiceImpl<GeneticDao, Genetic> implements IGeneticService {
}
