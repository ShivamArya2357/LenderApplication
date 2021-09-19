package com.lender.ledger.LenderApplication.service.balance;

import com.lender.ledger.LenderApplication.model.Balance;

import java.util.List;

public interface BalanceService {

    void printAmountPaidAndRemainingEmis(List<Balance> balances);
}
