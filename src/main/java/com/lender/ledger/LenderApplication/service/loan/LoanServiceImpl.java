package com.lender.ledger.LenderApplication.service.loan;

import com.lender.ledger.LenderApplication.entity.BankEntity;
import com.lender.ledger.LenderApplication.entity.LoanEntity;
import com.lender.ledger.LenderApplication.entity.PersonEntity;
import com.lender.ledger.LenderApplication.helper.LenderHelper;
import com.lender.ledger.LenderApplication.model.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class LoanServiceImpl implements LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Autowired
    private LenderHelper lenderHelper;

    /**
     * This method will create the loan
     *
     * @param loan
     */
    @Override
    public void createLoan(Loan loan) {

        logger.info("Inside LoanServiceImpl..createLoan()...");
        BankEntity bankEntity = saveBankInfo(loan);
        PersonEntity personEntity = savePersonInfo(loan, bankEntity);
        int totalEmis = 12 * loan.getYear();
        int totalAmountToPay = loan.getAmount() + (int) Math.ceil(
                (loan.getAmount() * loan.getYear() * loan.getInterestRate()) / 100.0
        );
        int emiAmount = (int) Math.ceil(totalAmountToPay / (double) totalEmis);
        LoanEntity loanEntity = saveLoanInfo(loan, personEntity, totalEmis, emiAmount, totalAmountToPay);
        lenderHelper.createLoanMap(loanEntity, loan.getBorrowerName(), loan.getBankName());
        lenderHelper.createEmis(loanEntity, loan.getBorrowerName(), loan.getBankName(), emiAmount, totalEmis);
    }

    /**
     * This method will save all the loan related information
     *
     * @param loan
     * @param personEntity
     * @return LoanEntity
     */
    private LoanEntity saveLoanInfo(Loan loan, PersonEntity personEntity, int totalEmis, int emiAmount,
                                    int totalAmountToPay
    ) {

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setId(String.valueOf(ThreadLocalRandom.current().nextInt(10000)));
        loanEntity.setAmount(loan.getAmount());
        loanEntity.setYear(loan.getYear());
        loanEntity.setInterestRate(loan.getInterestRate());
        loanEntity.setPersonId(personEntity.getId());
        loanEntity.setTotalEmis(totalEmis);
        loanEntity.setEmiAmount(emiAmount);
        loanEntity.setTotalAmountToPay(totalAmountToPay);
        return loanEntity;
    }

    /**
     * This method will save all the information of a person
     *
     * @param loan
     * @param bankEntity
     * @return PersonEntity
     */
    private PersonEntity savePersonInfo(Loan loan, BankEntity bankEntity) {

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(String.valueOf(ThreadLocalRandom.current().nextInt(10000)));
        personEntity.setName(loan.getBorrowerName());
        personEntity.setBankId(bankEntity.getId());
        lenderHelper.createPersonMap(loan.getBorrowerName(), loan.getBankName(), personEntity);
        return personEntity;
    }

    /**
     * This method will save all the bank informations
     *
     * @param loan
     * @return BankEntity
     */
    private BankEntity saveBankInfo(Loan loan) {

        BankEntity bankEntity = new BankEntity();
        bankEntity.setId(String.valueOf(ThreadLocalRandom.current().nextInt(10000)));
        bankEntity.setName(loan.getBankName());
        return bankEntity;
    }
}
