package com.lender.ledger.LenderApplication.exception;

import com.lender.ledger.LenderApplication.enums.ErrorCode;
import com.lender.ledger.LenderApplication.enums.ServiceStatus;
import com.lender.ledger.LenderApplication.model.Error;
import com.lender.ledger.LenderApplication.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExceptionHelper {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);

  @Autowired
  @Qualifier("errorMessageSource")
  private MessageSource errorMessageSource;

  /**
   * @param ex
   * @return
   */
  public ServiceResponse<Object> prepareErrorResponse(LenderServiceException ex) {

    ServiceResponse<Object> response = new ServiceResponse<>();
    response.setStatus(ServiceStatus.FAIL);
    List<Error> errors = new ArrayList<>();
    Error error = new Error();
    if (null == ex.getErrorCode()) ex.setErrorCode(ErrorCode.INTERNAL_EXCEPTION);
    error.setErrorCode(ex.getErrorCode().getCode());
    error.setErrorDescription(getErrorDesc(ex));
    errors.add(error);
    response.setErrors(errors);
    return response;
  }

  /**
   * @param ex
   * @return
   */
  public ServiceResponse<Object> prepareErrorResponse(Exception ex) {

    ServiceResponse<Object> response = new ServiceResponse<>();
    response.setStatus(ServiceStatus.FAIL);
    List<Error> errors = new ArrayList<>();
    Error error = new Error();
    error.setErrorCode("500");
    error.setErrorDescription(ex.getMessage() == null ? "Unknown Error" : ex.getMessage());
    errors.add(error);
    response.setErrors(errors);
    return response;
  }

  /**
   * @param exception
   * @return
   */
  public ServiceResponse<Object> prepareHTTPErrorResponse(Exception exception) {

    ServiceResponse<Object> response = new ServiceResponse<>();
    response.setStatus(ServiceStatus.FAIL);
    List<Error> errors = new ArrayList<>();
    Error error = new Error();
    error.setErrorCode("400");
    error.setErrorDescription(
        exception.getMessage() == null ? "Unknown Error" : exception.getMessage());
    errors.add(error);
    response.setErrors(errors);
    return response;
  }

  /**
   * @param ex
   * @return
   */
  private String getErrorDesc(LenderServiceException ex) {

    String format = "";
    String errorDesc = "";
    try {
      format = errorMessageSource.getMessage(ex.getErrorCode().getCode(), null, null);
      if ( !format.isEmpty()) {
        errorDesc = MessageFormat.format(format, (Object[]) ex.getArguments());
      }
    } catch (Exception e) {
      logger.error("Exception while getting Error description", e);
    }
    return errorDesc;
  }
}
