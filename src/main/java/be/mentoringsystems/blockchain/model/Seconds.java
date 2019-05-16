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
public class Seconds {

    private Long low;
    private Long high;
    private String unsigned;

    public Long getLow() {
        return low;
    }

    public void setLow(Long low) {
        this.low = low * 1000L;
    }

    public Long getHigh() {
        return high;
    }

    public void setHigh(Long high) {
        this.high = high;
    }

    public String getUnsigned() {
        return unsigned;
    }

    public void setUnsigned(String unsigned) {
        this.unsigned = unsigned;
    }

}
