package com.vrp.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/5 19:39
 * @Description MybatisPlus配置
 * @Since version-1.0
 */
@Configuration
public class MPConfig {
    @Bean
    public MybatisPlusInterceptor mpInterceptor(){
        /*创建MybatisPlus拦截器*/
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        /*添加分页相关的拦截器，没有这个分页效果无法显示，就是给SQL语句加上limit关键字*/
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mpInterceptor;
    }
}
