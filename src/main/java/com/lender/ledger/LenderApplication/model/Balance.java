package com.lender.ledger.LenderApplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance extends BaseModel {

    @JsonProperty("bankName")
    private String bankName;

    @JsonProperty("borrowerName")
    private String borrowerName;

    @JsonProperty("emiNo")
    private Integer emiNo;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public Integer getEmiNo() {
        return emiNo;
    }

    public void setEmiNo(Integer emiNo) {
        this.emiNo = emiNo;
    }
}
