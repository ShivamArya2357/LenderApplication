package com.lender.ledger.LenderApplication.helper;

import com.lender.ledger.LenderApplication.entity.EmiEntity;
import com.lender.ledger.LenderApplication.entity.LoanEntity;
import com.lender.ledger.LenderApplication.entity.PersonEntity;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

import static com.lender.ledger.LenderApplication.enums.Status.PENDING;

@Component
public class LenderHelper {

    private final Map<String, LoanEntity> loanEntityMap = new HashMap<>();
    private final Map<Integer, EmiEntity> emisWithEmiNoMap = new HashMap<>();
    private final Map<String, EmiEntity[]> emisWithPersonNameAndBankNameMap = new HashMap<>();
    private final Map<String, PersonEntity> personEntitiesMap = new HashMap<>();

    /**
     * This method will create loan map
     *
     * @param loanEntity
     * @param personName
     * @param bankName
     */
    public void createLoanMap(LoanEntity loanEntity, String personName, String bankName) {
        loanEntityMap.put(personName + "_" + bankName, loanEntity);
    }

    /**
     * This method will return loan from map
     *
     * @param personName
     * @param bankName
     * @return
     */
    public LoanEntity getLoan(String personName, String bankName) {
        return loanEntityMap.get(personName + "_" + bankName);
    }

    /**
     * This method will create all the emis for a loan
     *
     * @param loanEntity
     * @param personName
     * @param bankName
     * @param emiAmount
     * @param totalEmis
     */
    public void createEmis(LoanEntity loanEntity, String personName, String bankName, int emiAmount, int totalEmis) {

        EmiEntity[] emis = new EmiEntity[totalEmis];
        for (int i = 0; i < totalEmis; i++) {
            EmiEntity emiObj = new EmiEntity();
            emiObj.setEmiNo(i + 1);
            emiObj.setAmount(emiAmount);
            emiObj.setLoanId(loanEntity.getId());
            emiObj.setStatus(PENDING);
            emisWithEmiNoMap.put(i + 1, emiObj);
        }
        emisWithPersonNameAndBankNameMap.put(personName + "_" + bankName, emis);
    }

    /**
     * This method will return an emi with emiNo
     *
     * @param emiNo
     * @return EmiEntity
     */
    public EmiEntity getEmiWithEmiNo(int emiNo) {

        return emisWithEmiNoMap.get(emiNo);
    }

    /**
     * This method will return all the emis of a person that are associated to a bank
     *
     * @param personName
     * @param bankName
     * @return EmiEntity[]
     */
    public EmiEntity[] getEmisWithPersonNameAndBankName(String personName, String bankName) {

        return emisWithPersonNameAndBankNameMap.get(personName + "_" + bankName);
    }

    /**
     * This method will create a person map
     *
     * @param personName
     * @param bankName
     * @param personEntity
     */
    public void createPersonMap(String personName, String bankName, PersonEntity personEntity) {

        personEntitiesMap.put(personName + "_" + bankName, personEntity);
    }

    /**
     * This method will return a person
     *
     * @param personName
     * @param bankName
     * @return PersonEntity
     */
    public PersonEntity getPerson(String personName, String bankName) {

        return personEntitiesMap.get(personName + "_" + bankName);
    }
}
