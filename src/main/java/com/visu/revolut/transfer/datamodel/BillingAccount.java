package com.visu.revolut.transfer.datamodel;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BillingAccount {
    private BigInteger billingAccountId;
    private Currency currency;
    private BigDecimal amount;

    public BillingAccount() {
    }

    public BillingAccount(BigInteger billingAccountId, Currency currency, BigDecimal amount) {
        this.billingAccountId = billingAccountId;
        this.currency = currency;
        this.amount = amount;
    }

    public BigInteger getBillingAccountId() {
        return billingAccountId;
    }

    public void setBillingAccountId(BigInteger billingAccountId) {
        this.billingAccountId = billingAccountId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BillingAccount{" +
                "billingAccountId='" + billingAccountId + '\'' +
                ", currency=" + currency +
                ", amount=" + amount +
                '}';
    }
}
