package com.visu.revolut.transfer.utils;

public class OperationResponse {
    private OperationStatus status;
    private ResultCode resultCode;

    public OperationResponse() {
    }

    public OperationResponse(OperationStatus status, ResultCode resultCode) {
        this.status = status;
        this.resultCode = resultCode;
    }

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

    @Override
    public String toString() {
        return "OperationResponse{" +
                "status=" + status +
                ", resultCode=" + resultCode +
                '}';
    }
}
