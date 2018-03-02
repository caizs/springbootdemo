package org.caizs.project.configs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Component;

/**
 * 监控相关aop
 */
@Aspect
@Component
public class MetricsAspectConfig {

  @Autowired
  private CounterService counterService;
  @Autowired
  private GaugeService gaugeService;

  /**
   * 统计controller调用次数
   */
  @Before("execution(* com.lianfan.project.*..controller..*.*(..))")
  public void countControllerInvoke(JoinPoint joinPoint) {
    counterService.increment(joinPoint.getSignature().toString());
  }

  /**
   * 统计controller调用时间
   */
  @Around("execution(* com.lianfan.project.*..controller..*.*(..))")
  public Object latencyController(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.currentTimeMillis();
    Object proceed = pjp.proceed();
    long end = System.currentTimeMillis();
    gaugeService.submit(pjp.getSignature().toString(), end - start);

    return proceed;
  }

}
