package com.spring.aop.jthis;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
@Aspect
@Component
@Slf4j
public class Interceptor1 {

    @Pointcut("this(com.spring.aop.jthis.ServiceImpl)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        log.info("方法执行之前");
        Object result = invocation.proceed();
        log.info("方法执行完毕");
        return result;
    }
}