package com.lender.ledger.LenderApplication.service.balance;

import com.lender.ledger.LenderApplication.entity.EmiEntity;
import com.lender.ledger.LenderApplication.entity.LoanEntity;
import com.lender.ledger.LenderApplication.helper.LenderHelper;
import com.lender.ledger.LenderApplication.model.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BalanceServiceImpl implements BalanceService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);

    @Autowired
    private LenderHelper lenderHelper;

    /**
     * This method will print the total amount paid and remaining amis
     *
     * @param balances
     */
    @Override
    public void printAmountPaidAndRemainingEmis(List<Balance> balances) {

        logger.info("Inside BalanceServiceImpl..printBalance()...");
        String personName = balances.get(0).getBorrowerName();
        String bankName = balances.get(0).getBankName();
        EmiEntity[] emiEntities = lenderHelper.getEmisWithPersonNameAndBankName(
                personName, bankName
        );
        LoanEntity loanEntity = lenderHelper.getLoan(personName, bankName);
        int totalLoanAmnt = loanEntity.getAmount();
        int emiAmount = loanEntity.getEmiAmount();
        for (int i = 1; i < emiEntities.length; i++) {
            emiEntities[i].setAmount(emiEntities[i].getAmount() + emiEntities[i - 1].getAmount());
        }
        for (Balance balance : balances) {
            int amountPaid = emiEntities[balance.getEmiNo() - 1].getAmount();
            int remainingEmis = (int) Math.ceil((totalLoanAmnt - amountPaid) / (double) emiAmount);
            System.out.println(amountPaid + " " + remainingEmis);
        }
    }
}
