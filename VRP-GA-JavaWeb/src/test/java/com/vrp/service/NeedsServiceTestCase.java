package com.vrp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vrp.controller.NeedsController;
import com.vrp.pojo.Needs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author xiaobai
 * @Date Created in 2022/4/5 22:15
 * @Description
 * @Since version-1.0
 */
@SpringBootTest
public class NeedsServiceTestCase {
    @Autowired
    private INeedsService needsService;
    @Autowired
    private NeedsController needsController;
    @Test
    void batch(){

    }
    @Test
    void getById(){
        System.out.println(needsService.getById(1));
    }
    @Test
    void  testSave(){
        Needs needs = new Needs();
        needs.setCliNeeds(0.4);
        needs.setCliX(18.4);
        needs.setCliY(3.4);
        needs.setCliStart(1.5);
        needs.setCliEnd(6.0);
        needsService.save(needs);
    }
    @Test
    void testUpDate(){
        Needs needs = new Needs();
        needs.setId(3);
        needs.setCliNeeds(1.2);
        needs.setCliX(15.4);
        needs.setCliY(16.6);
        needs.setCliStart(4.7);
        needs.setCliEnd(10.2);
        needsService.updateById(needs);
    }
    @Test
    void testDelete(){
        needsService.removeById(3);
    }
    @Test
    void testGetAll(){
        System.out.println(needsService.list());
    }
    @Test
    void testGetPage(){
        IPage<Needs> page = new Page<Needs>(1, 5);
        needsService.page(page);
        System.out.println(page.getRecords());
    }

}


