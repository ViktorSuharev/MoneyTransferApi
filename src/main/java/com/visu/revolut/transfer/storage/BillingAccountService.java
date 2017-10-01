package com.visu.revolut.transfer.storage;

import com.visu.revolut.transfer.datamodel.BillingAccount;
import com.visu.revolut.transfer.utils.OperationResponse;
import com.visu.revolut.transfer.utils.OperationStatus;
import com.visu.revolut.transfer.utils.ResultCode;
import com.visu.revolut.transfer.utils.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BillingAccountService {
    private static final Logger logger = LogManager.getLogger(BillingAccountService.class);
    private BillingAccountMapper baMapper;

    public BillingAccount getBillingAccountById(BigInteger id) {
        logger.debug("Getting billing account by id {}", id);
        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession()) {
            baMapper = sqlSession.getMapper(BillingAccountMapper.class);
            return baMapper.getBillingAccountById(id);
        }
    }

    public OperationResponse transfer(BigInteger senderBaId, BigInteger receiverBaId, BigDecimal amount) {
        logger.debug("Transfer money amount {} from {} to {}", amount, senderBaId, receiverBaId);
        OperationResponse result;
        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession()) {
            baMapper = sqlSession.getMapper(BillingAccountMapper.class);

            BillingAccount sender = baMapper.getBillingAccountById(senderBaId);
            BillingAccount receiver = baMapper.getBillingAccountById(receiverBaId);

            result = validateTransferPossibility(sender, receiver, amount);
            if (OperationStatus.FAIL.equals(result.getStatus())) {
                return result;
            }
            logger.debug("Transfer is possible to execute");

            updateAmount(baMapper, sender, amount.negate());
            updateAmount(baMapper, receiver, amount);

            sqlSession.commit();
        } catch (Exception e) {
            logger.error("Internal error occurred during transfer execution", e);
            return new OperationResponse(OperationStatus.FAIL, ResultCode.INTERNAL_EXCEPTION);
        }

        result.setResultCode(ResultCode.SUCCESS);
        result.setStatus(OperationStatus.SUCCESS);

        return result;
    }

    private void updateAmount(BillingAccountMapper baMapper, BillingAccount account, BigDecimal amountDif) {
        BigDecimal currentAmount = baMapper.getAmountById(account.getBillingAccountId());
        BigDecimal updatedAmount = currentAmount.add(amountDif);
        logger.debug("BA's amount {} will be updated to {}", account.getBillingAccountId(), updatedAmount);
        account.setAmount(updatedAmount);
        baMapper.updateAmountById(account);
    }

    private OperationResponse validateTransferPossibility(BillingAccount sender, BillingAccount receiver,
                                                          BigDecimal amountDif) {
        if (sender == null) {
            logger.error("Sender billing account does not exist");
            return new OperationResponse(OperationStatus.FAIL, ResultCode.SENDER_ACCOUNT_NOT_EXIST);
        }

        if (!isAmountEnough(sender.getAmount(), amountDif)) {
            logger.error("Not enough money for transfer from billing account. Required {}, but actual {}",
                    amountDif, sender.getAmount());
            return new OperationResponse(OperationStatus.FAIL, ResultCode.NOT_ENOUGH_AMOUNT_FOR_TRANSFER);
        }

        if (receiver == null) {
            logger.error("Receiver billing account does not exist");
            return new OperationResponse(OperationStatus.FAIL, ResultCode.RECEIVER_ACCOUNT_NOT_EXIST);
        }

        return new OperationResponse();
    }

    private boolean isAmountEnough(BigDecimal currentSenderAmount, BigDecimal amountDif) {
        return currentSenderAmount.compareTo(amountDif) > 0;
    }
}