package com.visu.revolut.transfer.api;

import com.visu.revolut.transfer.datamodel.TransferDetail;
import com.visu.revolut.transfer.storage.BillingAccountService;
import com.visu.revolut.transfer.utils.OperationResponse;
import com.visu.revolut.transfer.utils.OperationStatus;
import com.visu.revolut.transfer.validators.TransferInputDataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Transfer {
    private static final Logger logger = LogManager.getLogger(Transfer.class);
    private TransferDetail transferDetail;

    public Transfer(TransferDetail transferDetail) {
        this.transferDetail = transferDetail;
    }

    public OperationResponse transfer() {
        logger.debug(transferDetail.toString());
        TransferInputDataValidator validator = new TransferInputDataValidator(transferDetail);
        OperationResponse result = validator.validateInputData();
        if (OperationStatus.FAIL.equals(result.getStatus())) {
            return result;
        }

        BillingAccountService billingAccountService = new BillingAccountService();
        return billingAccountService.transfer(
                transferDetail.getAccountSender(),
                transferDetail.getAccountReceiver(),
                transferDetail.getTransferAmount());
    }
}
