package com.visu.revolut.transfer.validators;

import com.visu.revolut.transfer.datamodel.TransferDetail;
import com.visu.revolut.transfer.utils.OperationResponse;
import com.visu.revolut.transfer.utils.OperationStatus;
import com.visu.revolut.transfer.utils.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class TransferInputDataValidator {
    private static final Logger logger = LogManager.getLogger(TransferInputDataValidator.class);
    private TransferDetail transferDetail;
    private OperationResponse result;

    public TransferInputDataValidator(TransferDetail transferDetail) {
        this.transferDetail = transferDetail;
    }

    public OperationResponse validateInputData() {
        validateTransferDetailCorrectness();
        validateAmountPositiveness();

        return result != null ? result : new OperationResponse();
    }

    private void validateTransferDetailCorrectness() {
        if (transferDetail == null
                || (transferDetail.getAccountSender() == null)
                || (transferDetail.getAccountReceiver() == null)
                || transferDetail.getTransferAmount() == null) {
            result = new OperationResponse(OperationStatus.FAIL, ResultCode.SOME_OF_THE_PARAMETERS_ARE_EMPTY);
            logger.error("Some of the parameters are empty: {}", transferDetail.toString());
        }
    }

    private void validateAmountPositiveness() {
        BigDecimal amount = transferDetail.getTransferAmount();
        if (BigDecimal.ZERO.equals(amount)) {
            result = new OperationResponse(OperationStatus.FAIL, ResultCode.TRANSFER_AMOUNT_IS_ZERO);
            logger.error("Transfer amount is zero");
        }
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            result = new OperationResponse(OperationStatus.FAIL, ResultCode.TRANSFER_AMOUNT_IS_NEGATIVE);
            logger.error("Transfer amount is negative");
        }
    }
}
