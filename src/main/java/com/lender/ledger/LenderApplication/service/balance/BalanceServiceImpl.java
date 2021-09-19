package com.lender.ledger.LenderApplication.service.balance;

import com.lender.ledger.LenderApplication.entity.EmiEntity;
import com.lender.ledger.LenderApplication.entity.LoanEntity;
import com.lender.ledger.LenderApplication.enums.ErrorCode;
import com.lender.ledger.LenderApplication.enums.Status;
import com.lender.ledger.LenderApplication.exception.LenderServiceException;
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
        if (emiEntities != null && emiEntities.length > 0) {
            LoanEntity loanEntity = lenderHelper.getLoan(personName, bankName);
            int emiAmount = loanEntity.getEmiAmount();
            int totalAmountToPay = loanEntity.getTotalAmountToPay();
            for (int i = 1; i < emiEntities.length; i++) {
                emiEntities[i].setAmount(emiEntities[i].getAmount() + emiEntities[i - 1].getAmount());
            }
            for (Balance balance : balances) {
                int amountPaid = emiEntities[balance.getEmiNo()].getAmount();
                EmiEntity emi = lenderHelper.getEmiWithEmiNo(balance.getBorrowerName(), balance.getBankName(),
                        balance.getEmiNo()
                );
                if (amountPaid > totalAmountToPay) {
                    emi.setStatus(Status.NOT_REQUIRED);
                    throw new LenderServiceException(ErrorCode.INVALID_EMI_NO, String.valueOf(balance.getEmiNo()));
                } else {
                    int remainingEmis = (int) Math.ceil((totalAmountToPay - amountPaid) / (double) emiAmount);
                    emi.setStatus(Status.COMPLETED);
                    System.out.println(balance + "==>" + amountPaid + " " + remainingEmis);
                }
            }
        } else {
            throw new LenderServiceException(ErrorCode.LOAN_IS_NOT_CREATED_WITH_THIS_PERSON_BANK_NAME, personName,
                    bankName
            );
        }
    }
}
