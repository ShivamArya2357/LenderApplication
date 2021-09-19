package com.lender.ledger.LenderApplication.exception;

import com.lender.ledger.LenderApplication.enums.ErrorCode;

public class LenderServiceException extends RuntimeException {

    private static final long serialVersionUID = 8323034147015186649L;

    private ErrorCode errorCode;

    private String referenceKey;

    private String serviceName;

    private String[] arguments;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public LenderServiceException(ErrorCode errorCode, String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace
    ) {

        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public LenderServiceException(ErrorCode errorCode, String message, Throwable cause) {

        super(message, cause);
        this.errorCode = errorCode;
    }

    public LenderServiceException(ErrorCode errorCode, Throwable cause) {

        super(cause);
        this.errorCode = errorCode;
    }

    public LenderServiceException(Throwable cause) {
        super(cause);
    }

    public LenderServiceException(ErrorCode errorCode, Throwable cause, String ... arguments) {

        super(cause);
        this.errorCode = errorCode;
        this.arguments = arguments;
    }

    public LenderServiceException(ErrorCode errorCode, String... arguments) {

        super(errorCode.name());
        this.errorCode = errorCode;
        this.arguments = arguments;
    }

    public String getReferenceKey() {
        return referenceKey == null ? "UNKNOWN" : referenceKey;
    }


    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }


    public String getServiceName() {
        return serviceName;
    }


    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
