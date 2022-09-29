package com.vrp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author xiaobai
 * @Date Created in 2022/4/7 23:42
 * @Description
 * @Since version-1.0
 */
@SpringBootTest
public class GeneticServiceTest {
    @Autowired
    private INeedsService genericService;
    @Test
    void getAll(){
        System.out.println(genericService.list());
    }
}
