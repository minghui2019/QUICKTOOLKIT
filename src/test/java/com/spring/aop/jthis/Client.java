package com.spring.aop.jthis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan(basePackageClasses = {Client.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public class Client {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Client.class);
        IService service = annotationConfigApplicationContext.getBean(IService.class);
        service.m1();
        log.info("{}", service instanceof ServiceImpl);
    }
}