package com.visu.revolut.transfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visu.revolut.transfer.datamodel.TransferDetail;
import com.visu.revolut.transfer.rest.TransferRestApi;
import com.visu.revolut.transfer.storage.BillingAccountService;
import com.visu.revolut.transfer.utils.OperationResponse;
import com.visu.revolut.transfer.utils.OperationStatus;
import com.visu.revolut.transfer.utils.ResultCode;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.visu.revolut.transfer.TestQueries.*;
import static org.junit.Assert.assertEquals;

public class TransferRestApiTest extends JerseyTest {

    private Connection connection;
    private Statement stmt;
    private final String WS_PATH = "/revolut/transfer";
    private final BigDecimal TRANSFER_AMOUNT = BigDecimal.valueOf(500L);
    private final BigInteger FIRST_BA_ID = BigInteger.ONE;
    private final BigInteger SECOND_BA_ID = BigInteger.valueOf(2L);
    private final BigInteger THREE_BA_ID = BigInteger.valueOf(3L);
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUpRest() throws SQLException {
        String DB_CONNECTION_URL = "jdbc:h2:mem:test";
        connection = DriverManager.getConnection(DB_CONNECTION_URL);
        stmt = connection.createStatement();

        stmt.executeUpdate(QUERY_CREATE_BILLING_ACCOUNTS_TABLE);
        stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER1);
        stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER2);
        stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER3);
    }

    @After
    public void tearDownRest() throws SQLException {
        stmt.executeUpdate(QUERY_DROP_BILLING_ACCOUNTS_TABLE);
        connection.close();
        stmt.close();
    }
    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(TransferRestApi.class);
    }

    @Test
    public void testTransferPositive() {
        BillingAccountService billingAccountService = new BillingAccountService();
        BigDecimal initialFirstBaAmount = billingAccountService.getBillingAccountById(FIRST_BA_ID).getAmount();
        BigDecimal initialSecondBaAmount = billingAccountService.getBillingAccountById(SECOND_BA_ID).getAmount();

        TransferDetail transferDetail = new TransferDetail(FIRST_BA_ID, SECOND_BA_ID, TRANSFER_AMOUNT);
        Response output = target(WS_PATH)
                .request()
                .post(Entity.entity(transferDetail, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        ObjectMapper mapper = new ObjectMapper();
        OperationResponse result = new OperationResponse();
        try {
            result = mapper.readValue(output.readEntity(String.class), OperationResponse.class);
        } catch (IOException e) {
            Assert.fail("Deserialization failed");
        }

        assertEquals("Operation status should be SUCCESS", OperationStatus.SUCCESS, result.getStatus());
        assertEquals(ResultCode.SUCCESS, result.getResultCode());

        assertEquals("First BA amount should be equal 500",
                billingAccountService.getBillingAccountById(FIRST_BA_ID).getAmount(),
                initialFirstBaAmount.subtract(TRANSFER_AMOUNT));
        assertEquals("Second BA amount should be equal 1500",
                billingAccountService.getBillingAccountById(SECOND_BA_ID).getAmount(),
                initialSecondBaAmount.add(TRANSFER_AMOUNT));
    }

    @Test
    public void testTransferExceededAmount() {
        BigDecimal EXCEEDED_AMOUNT = BigDecimal.valueOf(5000L);
        BillingAccountService billingAccountService = new BillingAccountService();
        BigDecimal initialFirstBaAmount = billingAccountService.getBillingAccountById(FIRST_BA_ID).getAmount();
        BigDecimal initialSecondBaAmount = billingAccountService.getBillingAccountById(SECOND_BA_ID).getAmount();

        TransferDetail transferDetail = new TransferDetail(FIRST_BA_ID, SECOND_BA_ID, EXCEEDED_AMOUNT);
        Response output = target(WS_PATH)
                .request()
                .post(Entity.entity(transferDetail, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        OperationResponse result = new OperationResponse();
        try {
            result = mapper.readValue(output.readEntity(String.class), OperationResponse.class);
        } catch (IOException e) {
            Assert.fail("Deserialization failed");
        }

        assertEquals("Operation status should be FAIL", OperationStatus.FAIL, result.getStatus());
        assertEquals(ResultCode.NOT_ENOUGH_AMOUNT_FOR_TRANSFER, result.getResultCode());

        assertEquals("First BA amount should not change",
                billingAccountService.getBillingAccountById(FIRST_BA_ID).getAmount(), initialFirstBaAmount);
        assertEquals("Second BA amount should not change",
                billingAccountService.getBillingAccountById(SECOND_BA_ID).getAmount(), initialSecondBaAmount);
    }

    @Test
    public void testTransferAccountNotExist() {
        BigInteger INCORRECT_BA_ID = BigInteger.TEN;
        BillingAccountService billingAccountService = new BillingAccountService();
        BigDecimal initialSecondBaAmount = billingAccountService.getBillingAccountById(SECOND_BA_ID).getAmount();

        TransferDetail transferDetail = new TransferDetail(INCORRECT_BA_ID, SECOND_BA_ID, TRANSFER_AMOUNT);
        Response output = target(WS_PATH)
                .request()
                .post(Entity.entity(transferDetail, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        OperationResponse result = new OperationResponse();
        try {
            result = mapper.readValue(output.readEntity(String.class), OperationResponse.class);
        } catch (IOException e) {
            Assert.fail("Deserialization failed");
        }

        assertEquals("Operation status should be FAIL", OperationStatus.FAIL, result.getStatus());
        assertEquals(ResultCode.SENDER_ACCOUNT_NOT_EXIST, result.getResultCode());

        assertEquals("Second BA amount should not change",
                billingAccountService.getBillingAccountById(SECOND_BA_ID).getAmount(), initialSecondBaAmount);
    }

    @Test
    public void testInterExchangeTransfer() {
        BillingAccountService billingAccountService = new BillingAccountService();
        BigDecimal initialSecondBaAmount = billingAccountService.getBillingAccountById(SECOND_BA_ID).getAmount();

        TransferDetail transferDetail = new TransferDetail(THREE_BA_ID, SECOND_BA_ID, TRANSFER_AMOUNT);
        Response output = target(WS_PATH)
                .request()
                .post(Entity.entity(transferDetail, MediaType.APPLICATION_JSON));

        assertEquals("Should return status 200", 200, output.getStatus());
        OperationResponse result = new OperationResponse();
        try {
            result = mapper.readValue(output.readEntity(String.class), OperationResponse.class);
        } catch (IOException e) {
            Assert.fail("Deserialization failed");
        }

        assertEquals("Operation status should be FAIL", OperationStatus.FAIL, result.getStatus());
        assertEquals(ResultCode.INTER_EXCHANGE_TRANSFER_NOT_SUPPORTED, result.getResultCode());

        assertEquals("Second BA amount should not change",
                billingAccountService.getBillingAccountById(SECOND_BA_ID).getAmount(), initialSecondBaAmount);
    }
}