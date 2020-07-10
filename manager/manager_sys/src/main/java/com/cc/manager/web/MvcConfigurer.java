package com.cc.manager.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 跨域访问配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-02 23:17
 */
@Configuration
public class MvcConfigurer extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/templates/");
        super.addResourceHandlers(registry);
    }

//    /**
//     * 解决中文乱码的问题
//     *
//     * @param converters HttpMessageConverter
//     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//        converters.add(responseBodyConverter());
//    }
//
//    @Bean
//    public HttpMessageConverter<String> responseBodyConverter() {
//        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders("*")
                // 允许使用的请求方法，以逗号隔开
                .allowedMethods("PUT", "GET", "POST", "DELETE", "OPTIONS")
                // 表示接受任意域名请求
                .allowedOrigins("*")
                // 表示是否允许发送Cookie。默认情况下Cookie不包括在CORS请求中。当设为true时表示服务器明确许可，Cookie可以包含在请求中一起发送给服务器。
                .allowCredentials(true)
                // 缓存此次请求的秒数。在这个时间范围内，所有同类型的请求都将不再发送预检请求而是直接使用此次返回的头作为判断依据，非常有用，大幅优化请求次数
                .maxAge(3600);
    }

}
