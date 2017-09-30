package com.visu.revolut.transfer.storage;

import com.visu.revolut.transfer.datamodel.BillingAccount;
import com.visu.revolut.transfer.datamodel.PersonAccount;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class AccountStorage {
    private final Map<BigInteger, PersonAccount> accountStorage = new HashMap<>();

    public Map<BigInteger, PersonAccount> getAccountStorage() {
        return accountStorage;
    }

}
