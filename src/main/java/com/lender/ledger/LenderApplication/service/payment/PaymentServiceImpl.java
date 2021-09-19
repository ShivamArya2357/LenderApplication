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

import static com.lender.ledger.LenderApplication.enums.Status.COMPLETED;

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
        EmiEntity[] emiEntities = lenderHelper.getEmisWithPersonNameAndBankName(payment.getBorrowerName(),
                payment.getBankName()
        );
        if (emiEntities != null && emiEntities.length > 0) {
            LoanEntity loanEntity = lenderHelper.getLoan(payment.getBorrowerName(), payment.getBankName());
            int alreadyPaidAmt = getAlreadyPaidAmt(emiEntities, payment);
            if (payment.getEmiNo() <= loanEntity.getTotalEmis() && alreadyPaidAmt < loanEntity.getTotalAmountToPay()
                && payment.getLumpSumAmount() >= loanEntity.getEmiAmount()
            ) {
                reduceAmountIfMore(payment, alreadyPaidAmt, loanEntity.getTotalAmountToPay());
                savePaymentAndEmiInfo(payment);
            } else if (payment.getLumpSumAmount() < loanEntity.getEmiAmount()) {
                throw new LenderServiceException(ErrorCode.LUMP_SUM_AMOUNT_IS_LESS_THAN_EMI_AMOUNT,
                        String.valueOf(payment.getLumpSumAmount()), String.valueOf(loanEntity.getEmiAmount())
                );
            } else if (alreadyPaidAmt >= loanEntity.getTotalAmountToPay()) {
                throw new LenderServiceException(ErrorCode.LUMP_SUM_AMOUNT_MORE_THAN_TOTAL_AMOUNT,
                        String.valueOf(payment.getLumpSumAmount()), String.valueOf(alreadyPaidAmt),
                        String.valueOf(loanEntity.getTotalAmountToPay())
                );
            } else {
                throw new LenderServiceException(ErrorCode.INVALID_EMI_NO, String.valueOf(payment.getEmiNo()));
            }
        } else {
            throw new LenderServiceException(ErrorCode.LOAN_IS_NOT_CREATED_WITH_THIS_PERSON_BANK_NAME,
                    payment.getBorrowerName(), payment.getBankName()
            );
        }
    }

    /**
     * This method will reduce lumpSumAmount if this is more than the total amount
     *
     * @param payment
     * @param totalAmntToPay
     */
    private void reduceAmountIfMore(Payment payment, int alreadyPaidAmt, int totalAmntToPay) {

        if (alreadyPaidAmt + payment.getLumpSumAmount() > totalAmntToPay) {
            payment.setLumpSumAmount(totalAmntToPay - alreadyPaidAmt);
        }
    }

    /**
     * This method will return alreadyPaid amount
     *
     * @param emiEntities
     * @param payment
     * @return alreadyPaidAmount
     */
    private int getAlreadyPaidAmt(EmiEntity[] emiEntities, Payment payment) {

        int alreadyPaidAmt = 0;
        for (int i = 1; i < payment.getEmiNo(); i++) {
            alreadyPaidAmt += emiEntities[i].getAmount();
        }
        return alreadyPaidAmt;
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

        EmiEntity emiEntity = lenderHelper.getEmiWithEmiNo(payment.getBorrowerName(), payment.getBankName(),
                payment.getEmiNo()
        );
        emiEntity.setAmount(payment.getLumpSumAmount() + emiEntity.getAmount());
        emiEntity.setEmiNo(payment.getEmiNo());
        emiEntity.setStatus(COMPLETED);
    }
}
