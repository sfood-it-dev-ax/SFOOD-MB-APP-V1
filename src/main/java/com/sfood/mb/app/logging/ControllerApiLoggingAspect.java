package com.sfood.mb.app.logging;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class ControllerApiLoggingAspect {

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllerApi(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestInfo = resolveRequestInfo();
        String signature = joinPoint.getSignature().toShortString();
        String argsInfo = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> !(arg instanceof HttpSession))
                .filter(arg -> !(arg instanceof ServletRequest))
                .filter(arg -> !(arg instanceof ServletResponse))
                .map(arg -> arg == null ? "null" : arg.toString())
                .reduce((left, right) -> left + ", " + right)
                .orElse("");

        long start = System.currentTimeMillis();
        log.info("controller api start request={} handler={} args=[{}]", requestInfo, signature, argsInfo);
        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            String resultType = (result == null) ? "null" : result.getClass().getSimpleName();
            log.info("controller api end request={} handler={} elapsedMs={} resultType={}",
                    requestInfo, signature, elapsed, resultType);
            return result;
        } catch (Exception ex) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("controller api error request={} handler={} elapsedMs={} message={}",
                    requestInfo, signature, elapsed, ex.getMessage(), ex);
            throw ex;
        }
    }

    private String resolveRequestInfo() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes servletAttributes)) {
            return "N/A";
        }
        return servletAttributes.getRequest().getMethod() + " " + servletAttributes.getRequest().getRequestURI();
    }
}
