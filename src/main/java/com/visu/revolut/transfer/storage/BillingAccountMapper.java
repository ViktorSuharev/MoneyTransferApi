package com.visu.revolut.transfer.storage;

import com.visu.revolut.transfer.datamodel.BillingAccount;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface BillingAccountMapper {
    @Select({"select id as billingAccountId, currency as currency, amount as amount from billing_accounts where id=#{id}"})
    BillingAccount getBillingAccountById(BigInteger id);

    @Select("select amount as amount from billing_accounts where id=#{id}")
    BigDecimal getAmountById(BigInteger id);

    @Update("update billing_accounts set amount=#{amount} where id=#{billingAccountId}")
    void updateAmountById(BillingAccount billingAccountId) throws Exception;

}