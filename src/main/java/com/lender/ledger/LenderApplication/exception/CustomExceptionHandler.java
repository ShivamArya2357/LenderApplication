package com.lender.ledger.LenderApplication.exception;

import com.lender.ledger.LenderApplication.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

  @Autowired
  private ExceptionHelper exceptionHelper;

  @ExceptionHandler(LenderServiceException.class)
  public final ResponseEntity<Object> handleRideSharingException(LenderServiceException ex, WebRequest request) {

    logger.error("Exception in handlePackServiceException for {}", request, ex);
    ServiceResponse<Object> errorResponse = exceptionHelper.prepareErrorResponse(ex);
    return ResponseEntity.status(getHTTPStatusCode(ex)).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {

    logger.error("Exception in handleGenericException for {}", request, ex);
    ServiceResponse<Object> errorResponse = exceptionHelper.prepareErrorResponse(ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                           HttpStatus status, WebRequest request
  ) {

    logger.error("Exception in handleExceptionInternal for {}", request, ex);
    ServiceResponse<Object> errorResponse = exceptionHelper.prepareHTTPErrorResponse(ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * @param ex
   * @return
   */
	private HttpStatus getHTTPStatusCode(LenderServiceException ex) {

		if (ex.getErrorCode().getCode().contains("403")) {
			return HttpStatus.FORBIDDEN;
		} else if (ex.getErrorCode().getCode().contains("400")) {
			return HttpStatus.BAD_REQUEST;
		} else {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
