package com.visu.revolut.transfer;

public class TestQueries {
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
}
