package com.netty.aspect.logaspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author qll
 * @create 2019-02-28 22:11
 * @desc 日志打印
 **/
@Component
@Slf4j
@Aspect
public class LogAspect {

    // 定义切入点
    @Pointcut("@annotation(com.netty.annotion.LogAspect)")
    public void point(){

    }

    @Before("point()")
    public void before(){

    }

    @Around("point()")
    public void around(ProceedingJoinPoint proceedingJoinPoint){
        String method = proceedingJoinPoint.getSignature().getName();
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String s = proceedingJoinPoint.getSignature().toString();
        Object[] args = proceedingJoinPoint.getArgs();
        log.info("执行了{}|{}|{}",className,method,args);
    }
}
