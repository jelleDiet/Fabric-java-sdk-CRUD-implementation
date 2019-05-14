# Fabric-sdk-java basic CRUD implementation

Example of using the fabric-java-sdk to connect to your hyperledger fabric blockchain network and call chaincode

## Getting Started

For an example of java chaincode that goes along with this sdk implementation, see [java chaincode](https://github.com/jelleDiet/Java_chaincode_example)

For local development and testing I recommend using the IBM VScode blockchain plugin to create a network, package and install the chaincode. The plugin can be found [here](https://marketplace.visualstudio.com/items?itemName=IBMBlockchain.ibm-blockchain-platform)

If you would like to write your own chaincode you can follow this [tutorial](https://developer.ibm.com/tutorials/ibm-blockchain-platform-vscode-smart-contract/)

### Prerequisites

Java 1.8

Maven 3.5

A running blockchain network with chaincode instantiated. 

### Installing

A step by step series of examples that tell you how to get a development env running

1. Start your blockchain network
2. Register an admin with username "admin" and pw "adminpw". Most fabric networks (including plugin) do this automatically
3. Get your chaincode and package, install and instantiate it on your peer
4. Get your network details from your peer using export connection details
5. Replace the connecion.json file with your connection details (the file provided is the details IBM plugin uses)
6. Fill out the Config class with your network and chaincode info
7. Clone this repository and build it using maven

### Running

Run the project on a server (for example on tomcat), it will deploy to 8081/blockchain

### Rich querying

The default way to query the database is by key. Fabric networks using couchDB also allow rich querying to give more flexibility in the way the blockchain can be queried. There is a small example in the code, for more info see:
https://medium.com/wearetheledger/hyperledger-fabric-couchdb-fantastic-queries-and-where-to-find-them-f8a3aecef767

## Built With

* Java 1.8
* [Maven](https://maven.apache.org/) - Dependency Management
* [Fabric-java-sdk](https://github.com/hyperledger/fabric-sdk-java/) 1.4.1

## Authors

* **Jelle Diet** 


## Acknowledgments

* Much of the code here was inspired from the examples over at https://github.com/hyperledger/fabric-sdk-java
* More info about the java sdk can be found there


