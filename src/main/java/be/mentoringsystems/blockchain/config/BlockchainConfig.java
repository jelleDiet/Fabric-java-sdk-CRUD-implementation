/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.config;

import be.mentoringsystems.blockchain.user.UserContext;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
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

        HFCAClient hfcaClient = createHFCAClient();
        UserContext adminUserContext = null;

        adminUserContext = new UserContext();
        adminUserContext.setName(Config.ADMIN_NAME); // admin username
        adminUserContext.setAffiliation(Config.ORG1_NAME); // affiliation
        adminUserContext.setMspId(Config.ORG1_MSP); // org1 mspid
        Enrollment adminEnrollment = hfcaClient.enroll(Config.ADMIN_NAME, Config.ADMIN_PASSWORD); //pass admin username and password, adminpw is the default for fabric
        adminUserContext.setEnrollment(adminEnrollment);

        return adminUserContext;
    }

    @Bean
    public HFClient createHFClient() throws Exception {
        UserContext adminUserContext = enrollAdmin();
        HFClient hfClient = HFClient.createNewInstance();
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        hfClient.setCryptoSuite(cryptoSuite);
        hfClient.setUserContext(adminUserContext);

        return hfClient;
    }

    @Bean
    public Channel createChannel() throws Exception {
        HFClient hfClient = createHFClient();
        Channel newChannel = hfClient.loadChannelFromConfig(Config.CHANNEL_NAME, createNetworkConfig());
        if (newChannel == null) {
            throw new RuntimeException("Channel " + Config.CHANNEL_NAME + " is not defined in the config file!");
        }

        return newChannel.initialize();
    }
}
