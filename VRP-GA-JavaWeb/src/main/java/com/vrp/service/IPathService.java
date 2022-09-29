package com.vrp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vrp.pojo.Path;

import java.util.List;

/**
 * @Author xiaobai
 * @Date Created in 2022/4/7 22:52
 * @Description
 * @Since version-1.0
 */
public interface IPathService extends IService<Path> {
    /**
     * 条件查询,根据配送员编号查询一条路径数据
     * @param id 配送员编号
     * @return 路径数据
     */
    List<Path> getPathByDriverId(Integer id);
}
