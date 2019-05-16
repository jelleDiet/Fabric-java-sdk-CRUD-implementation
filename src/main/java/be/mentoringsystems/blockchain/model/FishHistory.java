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
public class FishHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Fish value;
    private String txId;
    private Timestamp timestamp;
    private String isDelete;

    public Fish getValue() {
        return value;
    }

    public void setValue(Fish value) {
        this.value = value;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

}
