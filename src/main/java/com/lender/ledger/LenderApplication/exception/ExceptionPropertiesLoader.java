package com.lender.ledger.LenderApplication.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class ExceptionPropertiesLoader {

  @Value("${error.message.properties.location}")
  private String errorPropertiesPath;

  @Bean
  public MessageSource errorMessageSource() {

    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename(errorPropertiesPath);
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
