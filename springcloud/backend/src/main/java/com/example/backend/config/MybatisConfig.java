package com.example.backend.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor paginationInnerInterceptor =
                new PaginationInnerInterceptor(DbType.MYSQL);

        // 可选：超过总页数后是否进行处理，默认 false
        // paginationInnerInterceptor.setOverflow(false);

        // 可选：单页最大数量限制
        // paginationInnerInterceptor.setMaxLimit(500L);

        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}