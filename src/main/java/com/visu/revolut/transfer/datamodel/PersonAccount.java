package com.visu.revolut.transfer.datamodel;

import java.math.BigInteger;
import java.util.List;

public class PersonAccount {
    private BigInteger accountId;
    private BigInteger accountNumber;
    private Person person;
    private List<BillingAccount> billingAccounts;

    public BigInteger getAccountId() {
        return accountId;
    }

    public void setAccountId(BigInteger accountId) {
        this.accountId = accountId;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<BillingAccount> getBillingAccounts() {
        return billingAccounts;
    }

    public void setBillingAccounts(List<BillingAccount> billingAccounts) {
        this.billingAccounts = billingAccounts;
    }

    @Override
    public String toString() {
        return "PersonAccount{" +
                "accountId=" + accountId +
                ", accountNumber=" + accountNumber +
                ", person=" + person +
                ", billingAccounts=" + billingAccounts +
                '}';
    }
}
