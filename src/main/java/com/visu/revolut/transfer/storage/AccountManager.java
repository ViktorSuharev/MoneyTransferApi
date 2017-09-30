package com.visu.revolut.transfer.storage;

import com.visu.revolut.transfer.datamodel.BillingAccount;
import com.visu.revolut.transfer.datamodel.TransferDetail;

import java.math.BigDecimal;

public class AccountManager {

    private AccountStorage accountStorage;


    public void transfer(TransferDetail transferDetail) {
        BillingAccount sender = getAccountById(transferDetail.getAccountSender());
        BillingAccount receiver = getAccountById(transferDetail.getAccountReceiver());

        if (!isCurrencyEqual(sender, receiver)) {
        }
        transfer(sender, receiver);
    }

    public boolean isAccountExist(String accountId) {
        return getAccountById(accountId) == null ? false : true;
    }

    public boolean isAmountEnough(String accountId, BigDecimal requiredAmount) {
        BillingAccount billingAccount = getAccountById(accountId);
        BigDecimal currentAmount = billingAccount.getAmount();
        if (currentAmount != null) {
            // throw exception ?
            return false;
        }

        if (currentAmount.compareTo(requiredAmount) <= 0) {
            return false;
        }

        return true;
    }

    private BillingAccount getAccountById(String accountId) {
        // some logic depends on storage
        return null;
    }

    private boolean isCurrencyEqual(BillingAccount sender, BillingAccount receiver) {
        return sender.getCurrency().equals(receiver.getCurrency());
    }

    private void transfer(BillingAccount sender, BillingAccount receiver) {

    }

    private void exchangeСurrency() {

    }
}
