package com.vrp.path;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/10 15:46
 * @Description
 * @Since version-1.0
 */
@SpringBootTest
public class GetClient {
    @Test
    void stringTrim(){
        String str = " 0 → 20 → 2 → 17 → 22 → 7 → 33 → 0 ";
        String[] strs =str.split("→");
        int len = strs.length-2;
        int[] ids = new int[len];
        for (int i = 0; i < len; i++) {
            ids[i] = Integer.parseInt(strs[i+1].trim());
        }
        System.out.println(Arrays.toString(ids));


    }
}
