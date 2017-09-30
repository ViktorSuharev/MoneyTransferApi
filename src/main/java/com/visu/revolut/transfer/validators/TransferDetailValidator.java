package com.visu.revolut.transfer.validators;

import com.visu.revolut.transfer.datamodel.TransferDetail;
import com.visu.revolut.transfer.storage.AccountManager;
import com.visu.revolut.transfer.utils.OperationResponse;
import com.visu.revolut.transfer.utils.OperationStatus;
import com.visu.revolut.transfer.utils.ResultCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class TransferDetailValidator {

    private TransferDetail transferDetail;
    private AccountManager accountManager;
    private OperationResponse result = new OperationResponse();

    public TransferDetailValidator(TransferDetail transferDetail) {
        this.transferDetail = transferDetail;
    }

    public OperationResponse validate() {
        return validateTransferPossibility();
    }

    private OperationResponse validateTransferPossibility() {
        validateTransferDetailCorrectness();
        validateAmountPositiveness();
        validateAccountsExistence();
        validateAmountSufficiency();

        if (result.getResultCode() == null) {
            result.setStatus(OperationStatus.FAIL);
        }

        return result;
    }

    private void validateTransferDetailCorrectness() {
        if (transferDetail == null
                || StringUtils.isEmpty(transferDetail.getAccountSender())
                || StringUtils.isEmpty(transferDetail.getAccountReceiver())
                || transferDetail.getTransferAmount() == null) {
            result.setResultCode(ResultCode.SOME_OF_THE_PARAMETERS_ARE_EMPTY);
        }
    }

    private void validateAmountPositiveness() {
        BigDecimal amount = transferDetail.getTransferAmount();
        if (BigDecimal.ZERO.equals(amount)) {
            result.setResultCode(ResultCode.TRANSFER_AMOUNT_IS_ZERO);
        }
        if (BigDecimal.ZERO.compareTo(amount) < 0) {
            result.setResultCode(ResultCode.TRANSFER_AMOUNT_IS_NEGATIVE);
        }
    }

    private void validateAccountsExistence() {
        if (!isAccountExist(transferDetail.getAccountSender())) {
            result.setResultCode(ResultCode.SENDER_ACCOUNT_NOT_EXIST);
        }

        if (!isAccountExist(transferDetail.getAccountReceiver())) {
            result.setResultCode(ResultCode.RECEIVER_ACCOUNT_NOT_EXIST);
        }
    }

    private void validateAmountSufficiency() {
        if (!isAmountEnough()) {
            result.setResultCode(ResultCode.NOT_ENOUGH_AMOUNT_FOR_TRANSFER);
        }
    }

    private boolean isAccountExist(String accountNumber) {
        return accountManager.isAccountExist(accountNumber);
    }

    private boolean isAmountEnough() {
        return accountManager.isAmountEnough(transferDetail.getAccountSender(), transferDetail.getTransferAmount());
    }
}
