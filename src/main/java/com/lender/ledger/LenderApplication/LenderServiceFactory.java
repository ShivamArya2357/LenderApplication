package com.lender.ledger.LenderApplication;

import com.lender.ledger.LenderApplication.enums.Operation;
import com.lender.ledger.LenderApplication.exception.LenderServiceException;
import com.lender.ledger.LenderApplication.model.Balance;
import com.lender.ledger.LenderApplication.model.Loan;
import com.lender.ledger.LenderApplication.model.Payment;
import com.lender.ledger.LenderApplication.service.balance.BalanceService;
import com.lender.ledger.LenderApplication.service.loan.LoanService;
import com.lender.ledger.LenderApplication.service.payment.PaymentService;
import com.lender.ledger.LenderApplication.validation.RequestValidator;
import com.lender.ledger.LenderApplication.validation.ValidationSchemaStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LenderServiceFactory {

    @Autowired
    private ValidationSchemaStore validationSchemaStore;

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ApplicationContext appContext;

    private final Map<String, List<Balance> > balanceMap = new HashMap<>();

    /**
     * This method will call the respective service based on the operation in the input
     *
     * @param inputs
     */
    public void callService(List<String> inputs) {

        try {
            for (String input : inputs) {
                try {
                    String[] req = input.split(" ");
                    switch (Operation.valueOf(req[0].toUpperCase())) {
                        case LOAN:
                            Loan loanInput = createLoanInput(req);
                            requestValidator.validate(loanInput, Operation.LOAN);
                            loanService.createLoan(loanInput);
                            break;
                        case PAYMENT:
                            Payment paymentInput = createPaymentInput(req);
                            requestValidator.validate(paymentInput, Operation.PAYMENT);
                            paymentService.makePayment(paymentInput);
                            break;
                        case BALANCE:
                            Balance balanceInput = createBalanceInput(req);
                            requestValidator.validate(balanceInput, Operation.BALANCE);
                            createBalanceMap(balanceInput);
                            break;
                        default:
                            System.out.println("Invalid operation");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            printAmountPaidAndRemainingEmis();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method will keep all the balance requests in balance map, where key will be combination of username and bankname
     *
     * @param balance
     */
    private void createBalanceMap(Balance balance) {

        String key = balance.getBorrowerName() + "_" + balance.getBankName();
        if (balanceMap.containsKey(key)) {
            List<Balance> temp = balanceMap.get(key);
            temp.add(balance);
        } else {
            List<Balance> temp = new ArrayList<>();
            temp.add(balance);
            balanceMap.put(key, temp);
        }
    }

    /**
     * This method will print the balance
     *
     */
    public void printAmountPaidAndRemainingEmis() {

        if (!balanceMap.isEmpty()) {
            for (Map.Entry<String, List<Balance> > entry : balanceMap.entrySet()) {
                balanceService.printAmountPaidAndRemainingEmis(entry.getValue());
            }
        }
    }

    /**
     * This method will create the input for checking balance
     *
     * @param input
     * @return Balance
     */
    private Balance createBalanceInput(String[] input) {

        Balance balance = new Balance();
        balance.setBankName(input[1]);
        balance.setBorrowerName(input[2]);
        balance.setEmiNo(Integer.parseInt(input[3]));
        return balance;
    }

    /**
     * This method will create the input for doing payment
     *
     * @param input
     * @return Payment
     */
    private Payment createPaymentInput(String[] input) {

        Payment payment = new Payment();
        payment.setBankName(input[1]);
        payment.setBorrowerName(input[2]);
        payment.setLumpSumAmount(Integer.parseInt(input[3]));
        payment.setEmiNo(Integer.parseInt(input[4]));
        return payment;
    }

    /**
     * This method will create the input for creating loan
     *
     * @param input
     * @return Loan
     */
    private Loan createLoanInput(String[] input) {

        Loan loan = new Loan();
        loan.setBankName(input[1]);
        loan.setBorrowerName(input[2]);
        loan.setAmount(Integer.parseInt(input[3]));
        loan.setYear(Integer.parseInt(input[4]));
        loan.setInterestRate(Integer.parseInt(input[5]));
        return loan;
    }
}
