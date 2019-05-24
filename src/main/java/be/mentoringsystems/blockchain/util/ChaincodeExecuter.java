/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.util;

/**
 *
 * @author jellediet
 */
import be.mentoringsystems.blockchain.config.BlockchainNetworkAttributes;
import be.mentoringsystems.blockchain.model.RichQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChaincodeExecuter {

    private String chaincodeName;
    private String version;
    private ChaincodeID ccId;
    private long waitTime = 6000; //Milliseconds

    @Autowired
    @Qualifier("channel1")
    Channel channel1;

    @Autowired
    @Qualifier("channel2")
    Channel channel2;

    @Autowired
    HFClient hfClient;

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public String executeTransaction(Channel channel, boolean invoke, String func, String... args) throws InvalidArgumentException, ProposalException, UnsupportedEncodingException, InterruptedException, ExecutionException, TimeoutException {

        ChaincodeID.Builder chaincodeIDBuilder = ChaincodeID.newBuilder()
                .setName(BlockchainNetworkAttributes.CHAINCODE_1_NAME)
                .setVersion(BlockchainNetworkAttributes.CHAINCODE_1_VERSION);
        ccId = chaincodeIDBuilder.build();

        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(ccId);
        transactionProposalRequest.setChaincodeLanguage(TransactionRequest.Type.JAVA);

        transactionProposalRequest.setFcn(func);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setProposalWaitTime(waitTime);
        String payload = "";

        List<ProposalResponse> successful = new LinkedList();
        List<ProposalResponse> failed = new LinkedList();

        // Java sdk will send transaction proposal to all peers, if some peer down but the response still meet the endorsement policy of chaincode,
        // there is no need to retry. If not, you should re-send the transaction proposal.
        Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.INFO, String.format("Sending transactionproposal to chaincode: function = " + func + " args = " + String.join(" , ", args)));
        Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
        for (ProposalResponse response : transactionPropResp) {

            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                payload = new String(response.getChaincodeActionResponsePayload());
                Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.INFO, String.format("[√] Got success response from peer " + response.getPeer().getName() + " => Message : " + response.getMessage() + " Payload: %s ", payload));
                successful.add(response);
            } else {
                String status = response.getStatus().toString();
                String msg = response.getMessage();
                Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, String.format("[×] Got failed response from peer " + response.getPeer().getName() + " => Message : " + msg + " Status :" + status));
                failed.add(response);
            }
        }

        if (invoke) {
            Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.INFO, "Sending transaction to orderers...");
            // Java sdk tries all orderers to send transaction, so don't worry about one orderer gone.
            try {
                CompletableFuture<TransactionEvent> future = channel.sendTransaction(successful);
                if (future.isDone()) {
                    TransactionEvent transactionEvent = future.get();
                    Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.INFO, "Orderer response: txid: " + transactionEvent.getTransactionID());
                    Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.INFO, "Orderer response: block number: " + transactionEvent.getBlockEvent().getBlockNumber());
                    return null;
                }
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, "Orderer exception happened: " + ex);
                return null;
            }

        }
        return payload;
    }

    public String saveObject(String key, String json, String channelName) {

        String result = "";
        Channel channel = null;
        switch (channelName) {
            case "channel1":
                channel = channel1;
                break;
            case "channel2":
                channel = channel2;
                break;
            default:
                Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, "Channel not found");
        }
        String[] args = {key, json};
        try {
            result = executeTransaction(channel, true, "set", args);
        } catch (InvalidArgumentException | ProposalException | UnsupportedEncodingException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public String getObjectByKey(String key, String channelName) {
        String result = "";
        Channel channel = getChannelByName(channelName);
        try {
            result = executeTransaction(channel, false, "get", key);
        } catch (InvalidArgumentException | ProposalException | UnsupportedEncodingException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public String getObjectHistory(String key, String channelName) {
        String result = "";
        Channel channel = getChannelByName(channelName);
        try {
            result = executeTransaction(channel, false, "getHistory", key);
        } catch (InvalidArgumentException | ProposalException | UnsupportedEncodingException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public String deleteObject(String key, String channelName) {
        String result = "";
        Channel channel = getChannelByName(channelName);
        try {
            result = executeTransaction(channel, true, "delete", key);
        } catch (InvalidArgumentException | ProposalException | UnsupportedEncodingException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public String query(RichQuery query, String channelName) {
        String result = "";
        Channel channel = getChannelByName(channelName);
        try {
            String[] args = {Mapper.INSTANCE.getObjectMapper().writeValueAsString(query)};
            result = executeTransaction(channel, false, "query", args);
        } catch (JsonProcessingException | InvalidArgumentException | ProposalException | UnsupportedEncodingException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public String queryWithPagination(RichQuery query, int pageSize, String bookmark, String channelName) {
        String result = "";
        Channel channel = getChannelByName(channelName);
        try {
            String[] args = {Mapper.INSTANCE.getObjectMapper().writeValueAsString(query), String.valueOf(pageSize), bookmark};
            result = executeTransaction(channel, false, "queryWithPagination", args);
        } catch (JsonProcessingException | InvalidArgumentException | ProposalException | UnsupportedEncodingException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "[" + result + "]";
    }

    private Channel getChannelByName(String channelName) {
        Channel channel = null;
        switch (channelName) {
            case "channel1":
                channel = channel1;
                break;
            case "channel2":
                channel = channel2;
                break;
            default:
                Logger.getLogger(ChaincodeExecuter.class.getName()).log(Level.SEVERE, "Channel not found");
        }
        return channel;
    }

}
