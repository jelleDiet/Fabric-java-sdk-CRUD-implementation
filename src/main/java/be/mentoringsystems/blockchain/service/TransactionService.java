/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.service;

import be.mentoringsystems.blockchain.model.Transaction;

/**
 *
 * @author jellediet
 */
public interface TransactionService {

    Transaction getTransactionDetailsFromBlock(String transactionId, String channelName);

}
