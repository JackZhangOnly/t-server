package com.tstartup.tserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class TServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TServerApplication.class, args);
    }

    /*@Bean
    public ServletRegistrationBean<DispatcherServlet> druidServlet(ApplicationContext applicationContext) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setNamespace("ChatServiceServlet");
        dispatcherServlet.setApplicationContext(applicationContext);

        ServletRegistrationBean<DispatcherServlet> servletRegistrationBean = new ServletRegistrationBean<>(
                dispatcherServlet, "/api/t/*", "/*");
        servletRegistrationBean.setLoadOnStartup(1);

        return servletRegistrationBean;
    }*/

}
