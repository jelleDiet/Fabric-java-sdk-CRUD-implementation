/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.presentation.rest;

import be.mentoringsystems.blockchain.config.ChaincodeConfig;
import be.mentoringsystems.blockchain.user.UserContext;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jellediet
 */
@RestController
@RequestMapping("/chaincode")
public class ChaincodeController {

    @Autowired
    @Qualifier("channel1")
    Channel channel;

    @Autowired
    HFClient client;

    @Autowired
    UserContext admin;

    private void out(String message) {
        Logger.getLogger("ChaincodeController").log(Logger.Level.INFO, message);
    }

    @GetMapping
    //In development, not yet functional
    public void updateChaincode() throws ProposalException, InvalidArgumentException {
        ////////////////////////////
        // Install Proposal Request

        Collection<ProposalResponse> responses;
        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();
        String path = System.getProperty("user.dir") + "/src/main/resources/chaincode/";
        File chaincode = new File(path);

        out("Creating install proposal");
        ChaincodeID ccId;
        ChaincodeID.Builder chaincodeIDBuilder = ChaincodeID.newBuilder()
                .setName(ChaincodeConfig.CHAINCODE_1_NAME)
                .setVersion(ChaincodeConfig.CHAINCODE_1_VERSION);
        ccId = chaincodeIDBuilder.build();
        //Deprecated use v2.0 Lifecycle chaincode management.
        InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
        installProposalRequest.setChaincodeID(ccId);
        installProposalRequest.setChaincodeName("NodeChaincodeExample");
        installProposalRequest.setChaincodeVersion("0.2.3");
        installProposalRequest.setChaincodeSourceLocation(chaincode);
        installProposalRequest.setUserContext(admin);

        installProposalRequest.setChaincodeVersion(ChaincodeConfig.CHAINCODE_1_VERSION);
        installProposalRequest.setChaincodeLanguage(TransactionRequest.Type.NODE);

        out("Sending install proposal");

        ////////////////////////////
        // only a client from the same org as the peer can issue an install request
        int numInstallProposal = 0;
        //    Set<String> orgs = orgPeers.keySet();
        //   for (SampleOrg org : testSampleOrgs) {

        Collection<Peer> peers = channel.getPeers();
        numInstallProposal = numInstallProposal + peers.size();
        //Deprecated use v2.0 Lifecycle chaincode management.
        responses = client.sendInstallProposal(installProposalRequest, peers);

        for (ProposalResponse response : responses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                out("Successful install proposal response Txid: %s from peer %s" + response.getTransactionID() + " " + response.getPeer().getName());
                successful.add(response);
            } else {
                failed.add(response);
            }
        }

        //   }
        out("Received %d install proposal responses. Successful+verified: %d . Failed: %d" + numInstallProposal + " " + successful.size() + " " + failed.size());

        if (failed.size() > 0) {
            ProposalResponse first = failed.iterator().next();
            out("Not enough endorsers for install :" + successful.size() + ".  " + first.getMessage());
        }
    }

}
