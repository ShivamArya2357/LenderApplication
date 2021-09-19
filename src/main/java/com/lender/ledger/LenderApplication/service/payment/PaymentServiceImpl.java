package com.lender.ledger.LenderApplication.service.payment;

import com.lender.ledger.LenderApplication.entity.EmiEntity;
import com.lender.ledger.LenderApplication.entity.LoanEntity;
import com.lender.ledger.LenderApplication.entity.TransactionEntity;
import com.lender.ledger.LenderApplication.enums.ErrorCode;
import com.lender.ledger.LenderApplication.exception.LenderServiceException;
import com.lender.ledger.LenderApplication.helper.LenderHelper;
import com.lender.ledger.LenderApplication.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

import static com.lender.ledger.LenderApplication.enums.Status.SUCCESS;

@Component
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private LenderHelper lenderHelper;

    /**
     * This method will make the actual payment
     *
     * @param payment
     */
    @Override
    public void makePayment(Payment payment) {

        logger.info("Inside PaymentServiceImpl..makePayment()...");
        LoanEntity loanEntity = lenderHelper.getLoan(payment.getBorrowerName(), payment.getBankName());
        EmiEntity[] emiEntities = lenderHelper.getEmisWithPersonNameAndBankName(payment.getBorrowerName(),
                payment.getBankName()
        );
        if (loanEntity.getTotalEmis() <= payment.getEmiNo()) {
            reduceAmountIfMore(emiEntities, payment, loanEntity.getTotalAmountToPay());
            savePaymentAndEmiInfo(payment);
        } else {
            throw new LenderServiceException(ErrorCode.INVALID_EMI_NO, String.valueOf(payment.getEmiNo()));
        }
    }

    private void reduceAmountIfMore(EmiEntity[] emiEntities, Payment payment, int totalAmntToPay) {

        int alreadyPaidAmt = 0;
        for (int i = 0; i < payment.getEmiNo() - 1; i++) {
            alreadyPaidAmt += emiEntities[i].getAmount();
        }
        if (alreadyPaidAmt + payment.getLumpSumAmount() > totalAmntToPay) {
            payment.setLumpSumAmount(totalAmntToPay - alreadyPaidAmt);
        }
    }

    /**
     * This method will save payment information and call emi information
     *
     * @param payment
     */
    private void savePaymentAndEmiInfo(Payment payment) {

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(String.valueOf(ThreadLocalRandom.current().nextInt(10000)));
        transactionEntity.setAmount(payment.getLumpSumAmount());
        transactionEntity.setPersonId(lenderHelper.getPerson(payment.getBorrowerName(), payment.getBankName()).getId());
        saveEmiInfo(payment);
    }

    /**
     * This method will save emi information
     *
     * @param payment
     */
    private void saveEmiInfo(Payment payment) {

        EmiEntity emiEntity = lenderHelper.getEmiWithEmiNo(payment.getEmiNo());
        emiEntity.setAmount(payment.getLumpSumAmount());
        emiEntity.setStatus(SUCCESS);
    }
}
