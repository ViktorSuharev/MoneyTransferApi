package com.visu.revolut.transfer.utils;

public class OperationResponse {
    private OperationStatus status;
    private ResultCode resultCode;

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
