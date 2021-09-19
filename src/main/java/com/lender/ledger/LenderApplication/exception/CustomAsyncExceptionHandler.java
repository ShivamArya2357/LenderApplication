package com.lender.ledger.LenderApplication.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

  @Override
  public void handleUncaughtException(Throwable throwable, Method method, Object... params) {

    logger.error("Exception message - {}" , throwable.getMessage());
    logger.error("Method name - {}" , method.getName());
    for (Object param : params) {
      logger.error("Parameter value -{} " , param);
    }
  }
}
