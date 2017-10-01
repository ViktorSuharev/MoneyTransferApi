package com.visu.revolut.transfer;

import com.visu.revolut.transfer.api.Transfer;
import com.visu.revolut.transfer.datamodel.TransferDetail;
import com.visu.revolut.transfer.storage.BillingAccountService;
import com.visu.revolut.transfer.utils.OperationResponse;
import com.visu.revolut.transfer.utils.OperationStatus;
import com.visu.revolut.transfer.utils.ResultCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static com.visu.revolut.transfer.TestQueries.*;
import static org.junit.Assert.assertEquals;

public class TransferTest {

    @Before
    public void setUp() throws SQLException{
        final String DB_CONNECTION_URL = "jdbc:h2:mem:test";
        try (Connection con = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(QUERY_CREATE_BILLING_ACCOUNTS_TABLE);
            stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER1);
            stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER2);
            stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER3);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void transferTest() {
        final BigDecimal TRANSFER_AMOUNT = BigDecimal.valueOf(500L);

        BillingAccountService billingAccountService = new BillingAccountService();
        BigDecimal oldAmountOfFirstBa = billingAccountService.getBillingAccountById(BigInteger.ONE).getAmount();
        BigDecimal oldAmountOfSecondBa = billingAccountService.getBillingAccountById(BigInteger.TWO).getAmount();

        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setAccountSender(BigInteger.ONE);
        transferDetail.setAccountReceiver(BigInteger.TWO);
        transferDetail.setTransferAmount(TRANSFER_AMOUNT);

        Transfer transfer = new Transfer(transferDetail);
        OperationResponse response = transfer.transfer();
        assertEquals(response.getStatus(), OperationStatus.SUCCESS);
        assertEquals(response.getResultCode(), ResultCode.SUCCESS);



        assertEquals(billingAccountService.getBillingAccountById(BigInteger.ONE).getAmount(),
                oldAmountOfFirstBa.subtract(TRANSFER_AMOUNT));
        assertEquals(billingAccountService.getBillingAccountById(BigInteger.ONE).getAmount(),
                oldAmountOfSecondBa.add(TRANSFER_AMOUNT));
        billingAccountService.getBillingAccountById(BigInteger.valueOf(3L));
    }
}
