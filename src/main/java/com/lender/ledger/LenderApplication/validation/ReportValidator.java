package com.lender.ledger.LenderApplication.validation;

import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.lender.ledger.LenderApplication.enums.ErrorCode;
import com.lender.ledger.LenderApplication.exception.LenderServiceException;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

@Component
public class ReportValidator {

	private static final Logger logger = LoggerFactory.getLogger(ReportValidator.class);

	/**
	 * @param report
	 */
	public void processReport(ProcessingReport report) {

		ProcessingMessage message;
		if (!report.isSuccess()) {
			Iterator<ProcessingMessage> itr = report.iterator();
			while (itr.hasNext()) {
				message = itr.next();
				if (LogLevel.ERROR.equals(message.getLogLevel()) || LogLevel.FATAL.equals(message.getLogLevel())) {
					String fieldValue = message.asJson().get("instance").get("pointer").toString();
					String val = fieldValue;
					String messageError = message.asJson().get("message").toString().replaceAll(
							"[\\\\/\"]", ""
					);
					logger.debug(messageError);
					throw new LenderServiceException(ErrorCode.REQUEST_VALIDATION_FAILED, val, messageError);
				}
			}
		}
	}

	public void processReport(Set<ValidationMessage> report) {

		if (null != report && !report.isEmpty()) {
			StringBuilder messageError = new StringBuilder();
			Iterator<ValidationMessage> iterator = report.iterator();
			while (iterator.hasNext()) {
				ValidationMessage message = iterator.next();
				message.getType();
				messageError.append(message.getMessage());
				messageError.append(" ");
			}
			logger.error("REQUEST_VALIDATION_FAILED with message {}", messageError.toString());
			throw new LenderServiceException(ErrorCode.REQUEST_VALIDATION_FAILED, messageError.toString());
		}
	}
}
