package com.blaze.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlus 配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 21:48
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.blaze.data.mapper")
public class MybatisPlusConfig {
}
