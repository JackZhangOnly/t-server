package com.tstartup.tserver.config;

import com.tstartup.tserver.config.interceptor.CrmInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private CrmInterceptor crmInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String api = "/api/**/*";
        String crm = "/crm/**/*";

        // crm登录接口
        String crmLoginUrl = "/crm/login";

        registry.addInterceptor(crmInterceptor).addPathPatterns(crm).excludePathPatterns(crmLoginUrl);


    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}