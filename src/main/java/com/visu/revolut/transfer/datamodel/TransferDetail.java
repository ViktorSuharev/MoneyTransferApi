package com.visu.revolut.transfer.datamodel;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransferDetail {
    private BigInteger accountSender;
    private BigInteger accountReceiver;
    private BigDecimal transferAmount;

    public BigInteger getAccountSender() {
        return accountSender;
    }

    public void setAccountSender(BigInteger accountSender) {
        this.accountSender = accountSender;
    }

    public BigInteger getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(BigInteger accountReceiver) {
        this.accountReceiver = accountReceiver;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
