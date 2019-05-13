/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.persistence.blockchain;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.persistence.FishDAO;
import be.mentoringsystems.blockchain.util.ChaincodeExecuter;
import be.mentoringsystems.blockchain.util.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jelle
 */
@Repository
public class FishDAOImpl implements FishDAO {
    
    @Autowired
    ChaincodeExecuter chaincodeExecuter;

    @Override
    public Fish getById(int id) {
        String key = String.valueOf(id);
        String json = chaincodeExecuter.getObjectByKey(key);
        Fish fish = Mapper.INSTANCE.getObjectMapper().convertValue(json, Fish.class);
        return fish;
    }

    @Override
    public void save(Fish fish) {
        String json = "";
        try {
             json = Mapper.INSTANCE.getObjectMapper().writeValueAsString(fish);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        chaincodeExecuter.saveObject(String.valueOf(fish.getId()),json);
    }
    
}
