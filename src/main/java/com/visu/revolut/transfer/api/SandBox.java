package com.visu.revolut.transfer.api;

import com.visu.revolut.transfer.datamodel.TransferDetail;
import com.visu.revolut.transfer.storage.BillingAccountService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;

public class SandBox {
    public static String QUERY_CREATE_BILLING_ACCOUNTS_TABLE =
            "CREATE TABLE billing_accounts \n" +
            "   (id bigint, \n" +
            "   currency varchar(3),\n" +
            "   amount decimal)";

    public static String QUERY_INSERT_BA_TEST_DATA_USER1 =
            "INSERT INTO billing_accounts \n" +
            "   (id, currency, amount)" +
            "   VALUES ( 1, 'EUR', 1000)";

    public static String QUERY_INSERT_BA_TEST_DATA_USER2 =
            "INSERT INTO billing_accounts \n" +
                    "   (id, currency, amount)" +
                    "   VALUES ( 2, 'EUR', 1000)";

    public static String QUERY_INSERT_BA_TEST_DATA_USER3 =
            "INSERT INTO billing_accounts \n" +
                    "   (id, currency, amount)" +
                    "   VALUES ( 3, 'EUR', 1000)";

    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection("jdbc:h2:mem:test");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(QUERY_CREATE_BILLING_ACCOUNTS_TABLE);
            stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER1);
            stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER2);
            stmt.executeUpdate(QUERY_INSERT_BA_TEST_DATA_USER3);

            ResultSet rs = stmt.executeQuery("SELECT * FROM billing_accounts");
            System.out.println(rs.toString());
            while(rs.next()) {
                String name = rs.getString("amount");
                System.out.println( name );
            }


            System.out.println("separator");

            BillingAccountService billingAccountService = new BillingAccountService();
            System.out.println(billingAccountService.getBillingAccountById(BigInteger.ONE));
            System.out.println(billingAccountService.getBillingAccountById(BigInteger.TWO));
            System.out.println(billingAccountService.getBillingAccountById(BigInteger.valueOf(3L)));


            TransferDetail transferDetail = new TransferDetail();
            transferDetail.setAccountSender(BigInteger.ONE);
            transferDetail.setAccountReceiver(BigInteger.TWO);
            transferDetail.setTransferAmount(BigDecimal.valueOf(500L));

            Transfer transfer = new Transfer(transferDetail);
            System.out.println(transfer.transfer());


            System.out.println(billingAccountService.getBillingAccountById(BigInteger.ONE));
            System.out.println(billingAccountService.getBillingAccountById(BigInteger.TWO));
            System.out.println(billingAccountService.getBillingAccountById(BigInteger.valueOf(3L)));
        }
        catch(Exception e) {
            System.out.println(e.getMessage() + " is not found ");
        }
    }
}