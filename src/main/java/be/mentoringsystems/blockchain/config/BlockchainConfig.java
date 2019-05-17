/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.config;

import be.mentoringsystems.blockchain.user.UserContext;
import be.mentoringsystems.blockchain.util.UserContextUtil;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author jelle
 */
@Configuration
public class BlockchainConfig {

    @Bean
    public NetworkConfig createNetworkConfig() {
        NetworkConfig networkConfig = null;
        String connectionProfilePath = System.getProperty("user.dir") + "/src/main/resources/connectiondetails/connectionPlatform.json";
        File connectionFile = new File(connectionProfilePath);
        try {
            networkConfig = NetworkConfig.fromJsonFile(connectionFile);
        } catch (InvalidArgumentException | IOException | NetworkConfigurationException ex) {
            Logger.getLogger(BlockchainConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        return networkConfig;
    }

    @Bean
    public HFCAClient createHFCAClient() throws Exception {

        NetworkConfig networkConfig = createNetworkConfig();
        NetworkConfig.OrgInfo clientOrg = networkConfig.getClientOrganization();
        NetworkConfig.CAInfo caInfo = clientOrg.getCertificateAuthorities().get(0);

        //Certificate authority client
        HFCAClient hfcaClient = HFCAClient.createNewInstance(caInfo);

        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();

        hfcaClient.setCryptoSuite(cryptoSuite);

        return hfcaClient;
    }

    @Bean(name = "AdminUserContext")
    public UserContext enrollAdmin() throws Exception {

        return UserContextUtil.readUserContext(ChaincodeConfig.ORG1_NAME, ChaincodeConfig.ADMIN_NAME);
    }

//    @Bean(name = "registerUser3")
//
//    public UserContext registerUser3() throws Exception {
//
//        HFCAClient hfcaClient = createHFCAClient();
//
//        UserContext user = new UserContext();
//        user.setName("user3"); // admin username
//        user.setAffiliation(ChaincodeConfig.ORG1_NAME); // affiliation
//        user.setMspId(ChaincodeConfig.ORG1_MSP); // org1 mspid
//        String secret = MembershipServiceProvider.registerUser(user, hfcaClient, enrollAdmin());
//        MembershipServiceProvider.enrollUser(user, hfcaClient, secret);
//        UserContextUtil.writeUserContext(user);
//
//        return user;
//    }
//    @Bean(name = "getUser3")
//    public UserContext getUser3() throws Exception {
//
//        return UserContextUtil.readUserContext(ChaincodeConfig.ORG1_NAME, "user3");
//    }
    @Bean
    public HFClient createHFClient() throws Exception {
        UserContext userContext = enrollAdmin();
        HFClient hfClient = HFClient.createNewInstance();
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        hfClient.setCryptoSuite(cryptoSuite);
        hfClient.setUserContext(userContext);
        return hfClient;
    }

    @Bean(name = "channel1")
    public Channel createChannel1() throws Exception {
        HFClient hfClient = createHFClient();
        Channel newChannel = hfClient.loadChannelFromConfig(ChaincodeConfig.CHANNEL_1_NAME, createNetworkConfig());
        if (newChannel == null) {
            throw new RuntimeException("Channel " + ChaincodeConfig.CHANNEL_1_NAME + " is not defined in the config file!");
        }

        return newChannel.initialize();
    }

    @Bean(name = "channel2")
    public Channel createChannel2() throws Exception {
        HFClient hfClient = createHFClient();
        Channel newChannel = hfClient.loadChannelFromConfig(ChaincodeConfig.CHANNEL_2_NAME, createNetworkConfig());
        if (newChannel == null) {
            throw new RuntimeException("Channel " + ChaincodeConfig.CHANNEL_2_NAME + " is not defined in the config file!");
        }

        return newChannel.initialize();
    }
}
