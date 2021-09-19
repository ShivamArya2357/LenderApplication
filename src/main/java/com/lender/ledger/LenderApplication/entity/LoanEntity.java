package com.lender.ledger.LenderApplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanEntity extends BaseEntity {

    @JsonProperty("name")
    private String name;

    @JsonProperty("personId")
    private String personId;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("interestRate")
    private Integer interestRate;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("amountPaidSoFar")
    private Integer amountPaidSoFar;

    @JsonProperty("totalEmis")
    private Integer totalEmis;

    @JsonProperty("emiAmount")
    private Integer emiAmount;

    @JsonProperty("totalAmountToPay")
    private Integer totalAmountToPay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getAmountPaidSoFar() {
        return amountPaidSoFar;
    }

    public void setAmountPaidSoFar(Integer amountPaidSoFar) {
        this.amountPaidSoFar = amountPaidSoFar;
    }

    public Integer getTotalEmis() {
        return totalEmis;
    }

    public void setTotalEmis(Integer totalEmis) {
        this.totalEmis = totalEmis;
    }

    public Integer getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(Integer emiAmount) {
        this.emiAmount = emiAmount;
    }

    public Integer getTotalAmountToPay() {
        return totalAmountToPay;
    }

    public void setTotalAmountToPay(Integer totalAmountToPay) {
        this.totalAmountToPay = totalAmountToPay;
    }
}
