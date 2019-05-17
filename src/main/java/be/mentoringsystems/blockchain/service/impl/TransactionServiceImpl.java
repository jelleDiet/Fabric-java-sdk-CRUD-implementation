/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.service.impl;

import be.mentoringsystems.blockchain.model.Transaction;
import be.mentoringsystems.blockchain.service.TransactionService;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author jellediet
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    @Qualifier("channel1")
    Channel channel1;

    @Autowired
    @Qualifier("channel2")
    Channel channel2;

    @Override
    public Transaction getTransactionDetailsFromBlock(String transactionId, String channelName) {
        Channel channel;
        switch (channelName) {
            case "channel1":
                channel = channel1;
                break;
            case "channel2":
                channel = channel2;
                break;
            default:
                channel = channel1;
                break;
        }
        BlockInfo returnedBlock = null;
        try {
            returnedBlock = channel.queryBlockByTransactionID(transactionId);
        } catch (InvalidArgumentException | ProposalException ex) {
            Logger.getLogger(TransactionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Transaction transaction = new Transaction();
        if (returnedBlock != null) {

            for (BlockInfo.EnvelopeInfo envelopeInfo : returnedBlock.getEnvelopeInfos()) {

                if (envelopeInfo.getTransactionID().equals(transactionId)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    transaction.setTimestamp(formatter.format(envelopeInfo.getTimestamp()));
                    transaction.setId(transactionId);
                    transaction.setChannelId(envelopeInfo.getChannelId());
                    transaction.setCreatorMsp(envelopeInfo.getCreator().getMspid());
                    transaction.setCreatorCertificate(envelopeInfo.getCreator().getId());
                    transaction.setBlockNumber(returnedBlock.getBlockNumber());
                }

            }
        }

        return transaction;
    }

}
