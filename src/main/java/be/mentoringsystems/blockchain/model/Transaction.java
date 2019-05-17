/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 *
 * @author jellediet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String validationCode;
    private String channelId;
    private String creatorMsp;
    private long blockNumber;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCreatorMsp() {
        return creatorMsp;
    }

    public void setCreatorMsp(String creatorMsp) {
        this.creatorMsp = creatorMsp;
    }

    public String getCreatorCertificate() {
        return creatorCertificate;
    }

    public void setCreatorCertificate(String creatorCertificate) {
        this.creatorCertificate = creatorCertificate;
    }
    private String creatorCertificate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
