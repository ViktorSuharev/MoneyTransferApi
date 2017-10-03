package com.visu.revolut.transfer.utils;

public enum ResultCode {
    SUCCESS(100),
    SENDER_ACCOUNT_NOT_EXIST(1),
    RECEIVER_ACCOUNT_NOT_EXIST(2),
    SOME_OF_THE_PARAMETERS_ARE_EMPTY(3),
    NOT_ENOUGH_AMOUNT_FOR_TRANSFER(5),
    TRANSFER_AMOUNT_IS_NEGATIVE(6),
    TRANSFER_AMOUNT_IS_ZERO(7),
    INTER_EXCHANGE_TRANSFER_NOT_SUPPORTED(8),
    INTERNAL_EXCEPTION(9);

    private int errorCode;

    ResultCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
