package com.visu.revolut.transfer.api;

import com.visu.revolut.transfer.datamodel.TransferDetail;
import com.visu.revolut.transfer.storage.AccountManager;
import com.visu.revolut.transfer.utils.OperationResponse;
import com.visu.revolut.transfer.utils.OperationStatus;
import com.visu.revolut.transfer.utils.ResultCode;
import com.visu.revolut.transfer.validators.TransferDetailValidator;

public class Transfer {

    private TransferDetail transferDetail;
    private AccountManager accountManager;

    public Transfer(TransferDetail transferDetail) {
        this.transferDetail = transferDetail;
    }

    public OperationResponse doTransfer() {
        TransferDetailValidator validator = new TransferDetailValidator(transferDetail);
        OperationResponse result = validator.validate();
        if (OperationStatus.FAIL.equals(result.getStatus())) {
            return result;
        }

        result = transfer();

        return result;
    }

    private OperationResponse transfer() {

        accountManager.transfer();

        OperationResponse result = new OperationResponse();
        result.setResultCode(ResultCode.SUCCESS);
        result.setStatus(OperationStatus.SUCCESS);
        return null;
    }

}
