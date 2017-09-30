package com.visu.revolut.transfer.datamodel;

import java.math.BigDecimal;

public class TransferDetail {
    private String accountSender;
    private String accountReceiver;
    private BigDecimal transferAmount;

    public String getAccountSender() {
        return accountSender;
    }

    public void setAccountSender(String accountSender) {
        this.accountSender = accountSender;
    }

    public String getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(String accountReceiver) {
        this.accountReceiver = accountReceiver;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
