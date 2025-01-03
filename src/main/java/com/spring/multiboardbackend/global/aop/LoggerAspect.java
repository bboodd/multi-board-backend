package com.spring.multiboardbackend.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LoggerAspect {

    @Around("execution(* com.spring.multiboardbackend.domain..*Controller.*(..)) || execution(* com.spring.multiboardbackend.domain..*Service.*(..)) || execution(* com.spring.multiboardbackend.domain..*Repository.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable{

        String name = joinPoint.getSignature().getDeclaringTypeName();
        String type;
        if (StringUtils.contains(name, "Controller")) {
            type = "Controller ===> ";
        } else if (StringUtils.contains(name, "Service")) {
            type = "Service ===> ";
        } else if (StringUtils.contains(name, "Mapper")) {
            type = "Mapper ===> ";
        } else {
            type = "";
        }

        log.debug("{}{}.{}()", type, name, joinPoint.getSignature().getName());
        return joinPoint.proceed();
    }
}
