package com.lender.ledger.LenderApplication.exception;

import com.lender.ledger.LenderApplication.enums.ErrorCode;
import com.lender.ledger.LenderApplication.enums.ServiceStatus;
import com.lender.ledger.LenderApplication.model.Error;
import com.lender.ledger.LenderApplication.model.ServiceResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExceptionHandlerFilter extends GenericFilterBean implements ApplicationContextAware {

	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		try {
			if (((HttpServletResponse) response).getStatus() == HttpStatus.FORBIDDEN.value()) {
				throw new LenderServiceException(ErrorCode.REQUEST_FORBIDDEN,
						"Access to the requested resource has been denied"
				);
			}
			filterChain.doFilter(request, response);
		} catch (RuntimeException | IOException ex) {
			ServiceResponse<Object> serviceResponse = new ServiceResponse<>();
			serviceResponse.setStatus(ServiceStatus.FAIL);
			List<Error> errors = new ArrayList<>();
			Error error = new Error();
			int httpErrorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
			if (ex instanceof LenderServiceException) {
				LenderServiceException lex = (LenderServiceException) ex;

				if (lex.getErrorCode().getCode().startsWith("400")) {
					httpErrorCode = HttpStatus.BAD_REQUEST.value();
				} else if (lex.getErrorCode().getCode().startsWith("401")) {
					httpErrorCode = HttpStatus.UNAUTHORIZED.value();
				} else if (lex.getErrorCode().getCode().startsWith("500")) {
					httpErrorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
				}else if (lex.getErrorCode().getCode().startsWith("403")) {
					httpErrorCode = HttpStatus.FORBIDDEN.value();
				}
				error.setErrorCode(lex.getErrorCode().getCode());
				error.setErrorDescription(lex.getErrorCode().name() + " " + Arrays.toString(lex.getArguments()));
				errors.add(error);
				serviceResponse.setErrors(errors);
			} else {
				error.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
				error.setErrorDescription(ex.getMessage());
				errors.add(error);
				serviceResponse.setErrors(errors);
			}
			((HttpServletResponse) response).setStatus(httpErrorCode);
			((HttpServletResponse) response).addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			((HttpServletResponse) response).addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(serviceResponse.toString());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;
	}
}
