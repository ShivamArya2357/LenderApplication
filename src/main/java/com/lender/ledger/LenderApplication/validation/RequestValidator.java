package com.lender.ledger.LenderApplication.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.lender.ledger.LenderApplication.ObjectMapperFactory;
import com.lender.ledger.LenderApplication.enums.ErrorCode;
import com.lender.ledger.LenderApplication.enums.Operation;
import com.lender.ledger.LenderApplication.exception.LenderServiceException;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RequestValidator implements IRequestValidator<Object> {

	private static final Logger logger = LoggerFactory.getLogger(RequestValidator.class);

	@Autowired
   	private ValidationSchemaStore validationSchemaStore;

	@Autowired
	private ReportValidator reportValidator;

	@Override
	public void validate(Object input, Operation operation) {

		JsonNode jsonFile = ObjectMapperFactory.getMapper().convertValue(input, JsonNode.class);
		JsonSchema schema = validationSchemaStore.getSchema(operation.name());
		Set<ValidationMessage> report = null;
		try {
			report = schema.validate(jsonFile);
			reportValidator.processReport(report);
		} catch (LenderServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Schema processing failed!!", e);
			throw new LenderServiceException(ErrorCode.REQUEST_SCHEMA_PROCESSING_FAILED, operation.name());
		}
	}
}
