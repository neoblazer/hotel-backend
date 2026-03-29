package com.example.demo.logging;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.demo.service..*(..)) || execution(* com.example.demo.controller..*(..))")
    public void applicationLayer() {}

    @Around("applicationLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = maskSensitiveArgs(joinPoint.getArgs());

        logger.info("➡️ Entering: {} | Args: {}", methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long timeTaken = System.currentTimeMillis() - start;

            String resultString = maskSensitiveResult(result);

            logger.info("✅ Exiting: {} | Result: {} | ⏱️ {} ms",
                    methodName, resultString, timeTaken);

            return result;

        } catch (Exception ex) {
            long timeTaken = System.currentTimeMillis() - start;

            logger.error("❌ Exception in: {} | ⏱️ {} ms | Message: {}",
                    methodName, timeTaken, ex.getMessage(), ex);

            throw ex;
        }
    }

    // Mask sensitive fields in arguments
    private Object[] maskSensitiveArgs(Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return null;
                    String str = arg.toString();
                    // Basic password/token masking
                    if (str.toLowerCase().contains("password") || str.toLowerCase().contains("token")) {
                        return "***masked***";
                    }
                    return str;
                })
                .toArray();
    }

    // Mask sensitive info in result
    private String maskSensitiveResult(Object result) {
        if (result == null) return "null";
        String str = result.toString();
        // Truncate long outputs
        if (str.length() > 500) {
            str = str.substring(0, 500) + "...(truncated)";
        }
        // Mask any passwords/tokens
        if (str.toLowerCase().contains("password") || str.toLowerCase().contains("token")) {
            str = str.replaceAll("(?i)password=[^,}]*", "password=***masked***");
            str = str.replaceAll("(?i)token=[^,}]*", "token=***masked***");
        }
        return str;
    }
}