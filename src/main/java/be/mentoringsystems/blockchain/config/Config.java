package be.mentoringsystems.blockchain.config;

public class Config {

    public static final String ORG1_MSP = "Org1MSP";

    public static final String ORG1_NAME = "Org1";

    public static final String ADMIN_NAME = "admin";

    public static final String ADMIN_PASSWORD = "adminpw";;
    
    public static final String CA_ORG1_URL = "http://localhost:17054";
    
    public static final String CA_ORG1_CANAME = "ca.org1.example.com";
    
    public static final String ORDERER_URL = "grpc://localhost:17050";

    public static final String ORDERER_NAME = "orderer.example.com";

    public static final String CHANNEL_NAME = "mychannel";

    public static final String ORG1_PEER_0 = "peer0.org1.example.com";

    public static final String ORG1_PEER_0_URL = "grpc://localhost:17051";

    public static final String CHAINCODE_1_NAME = "MavenPackage";

    public static final String CHAINCODE_1_VERSION = "21";

}
