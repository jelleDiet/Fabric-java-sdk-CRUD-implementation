/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.model;

/**
 *
 * @author jellediet
 */
public class Timestamp {

    private Seconds seconds;

    public Seconds getSeconds() {
        return seconds;
    }

    public void setSeconds(Seconds seconds) {
        this.seconds = seconds;
    }

    public String getNanos() {
        return nanos;
    }

    public void setNanos(String nanos) {
        this.nanos = nanos;
    }
    private String nanos;

}
