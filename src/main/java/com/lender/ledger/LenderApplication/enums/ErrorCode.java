package com.lender.ledger.LenderApplication.enums;

public enum ErrorCode {

    // 500 errors
    INTERNAL_EXCEPTION("500"),
    REQUEST_SCHEMA_PROCESSING_FAILED("500.01"),

    // HTTP errors
    REQUEST_FORBIDDEN("403"),

    // 400 errors
    REQUEST_VALIDATION_FAILED("400.01"),
    REQUEST_VALIDATOR_NOT_FOUND("400.02"),
    INVALID_EMI_NO("400.03")
    ;

    private String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
