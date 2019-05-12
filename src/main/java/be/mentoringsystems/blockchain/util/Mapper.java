/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author jelle
 */
public enum Mapper {
  INSTANCE;
  private final ObjectMapper mapper = new ObjectMapper();

  private Mapper(){
    // Perform any configuration on the ObjectMapper here.
  }

  public ObjectMapper getObjectMapper() {
    return mapper;
  }
}