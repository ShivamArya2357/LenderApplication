package com.lender.ledger.LenderApplication.validation;

import com.lender.ledger.LenderApplication.enums.ErrorCode;
import com.lender.ledger.LenderApplication.exception.LenderServiceException;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationSchemaStore {

	private static final String JSON_SCHEMA_LOCATION = "json.schema.location";

	private static final Logger logger = LoggerFactory.getLogger(ValidationSchemaStore.class);

	private Map<String, JsonSchema> requestValidationSchemaMap = null;

	@Autowired
	private ApplicationContext context;

	private String jsonSchemaLocation;

	@Autowired
	private Environment env;


	@PostConstruct
	private void loadFiles() {

		try {
			jsonSchemaLocation = env.getProperty(JSON_SCHEMA_LOCATION);
			requestValidationSchemaMap = new HashMap<>();
			loadJsonSchema();
		} catch (IOException e) {
			logger.error("Loading JSON schemas failed!!!", e);
			throw new LenderServiceException(e);
		}
	}

	/**
	 * Load json schema.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadJsonSchema() throws IOException {

		Resource[] resources = context.getResources(jsonSchemaLocation + "/*_request.json");
		if (resources.length > 0) {
			for (Resource resource : resources) {
				if (resource.exists()) {
					String fileName = resource.getFilename();
					logger.info("loading JSON schema " + fileName);
					String[] fileNameSplit = fileName.split("_");
					String key = fileNameSplit[0];
					Path path = Paths.get(resource.getURI());
					byte[] fileBytes = Files.readAllBytes(path);
					JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
					JsonSchema schema = factory.getSchema(new String(fileBytes));
					requestValidationSchemaMap.put(key, schema);
				}
			}
		}
	}

	/**
	 * @param operation
	 * @return scehma
	 */
	public JsonSchema getSchema(String operation) {

		JsonSchema schema = this.requestValidationSchemaMap.get(operation);
		if (null == schema) {
			throw new LenderServiceException(ErrorCode.REQUEST_VALIDATOR_NOT_FOUND, operation);
		}
		return schema;
	}
}
