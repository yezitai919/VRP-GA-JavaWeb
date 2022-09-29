package com.vrp.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vrp.pojo.Needs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/5 18:03
 * @Description
 * @Since version-1.0
 */
@SpringBootTest
public class NeedsDaoTest {
    @Autowired
    private NeedsDao needsDao;
    @Test
    void  testGetById(){
        System.out.println(needsDao.selectById(1));
    }
    @Test
    void  testSave(){
        Needs needs = new Needs();
        needs.setCliNeeds(0.4);
        needs.setCliX(18.4);
        needs.setCliY(3.4);
        needs.setCliStart(1.5);
        needs.setCliEnd(6.0);
        needsDao.insert(needs);
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
        needsDao.updateById(needs);
    }
    @Test
    void testDelete(){
        needsDao.deleteById(3);
    }
    @Test
    void testGetAll(){
        System.out.println(needsDao.selectList(null));;
    }
    @Test
    void testGetPage(){
        IPage page = new Page(1,5);
        needsDao.selectPage(page,null);
    }
    @Test
    void testGetBy(){

        QueryWrapper<Needs> qw = new QueryWrapper<>();
        qw.eq("id",1);
        needsDao.selectList(qw);
    }
    @Test
    void testGetBy2(){

        double needs = 0.4;
        LambdaQueryWrapper<Needs> lqw = new LambdaQueryWrapper<Needs>();
        lqw.eq(Needs::getCliNeeds,needs);
        needsDao.selectList(lqw);
    }
}
