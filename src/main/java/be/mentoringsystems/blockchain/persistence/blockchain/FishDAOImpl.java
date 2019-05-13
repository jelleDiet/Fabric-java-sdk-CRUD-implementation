/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.persistence.blockchain;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.model.query.RichQuery;
import be.mentoringsystems.blockchain.persistence.FishDAO;
import be.mentoringsystems.blockchain.util.ChaincodeExecuter;
import be.mentoringsystems.blockchain.util.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    public Fish getById(UUID id) {
        String key = String.valueOf(id);
        String json = chaincodeExecuter.getObjectByKey(key);
        Fish fish = null;
        try {
            fish = Mapper.INSTANCE.getObjectMapper().readValue(json, Fish.class);
        } catch (IOException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        chaincodeExecuter.saveObject(String.valueOf(fish.getId()), json);
    }

    @Override
    public List<Fish> query(RichQuery query) {
        List<Fish> fish = new ArrayList<>();
        TypeReference<List<Fish>> listType = new TypeReference<List<Fish>>() {
        };

        String json = chaincodeExecuter.query(query);

        try {
            fish = Mapper.INSTANCE.getObjectMapper().readValue(json, listType);
        } catch (IOException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fish;
    }

    @Override
    public void delete(int id) {
        chaincodeExecuter.deleteObject(String.valueOf(id));
    }

    @Override
    public List<Fish> getAll() {
        List<Fish> fish = new ArrayList<>();
        TypeReference<List<Fish>> listType = new TypeReference<List<Fish>>() {
        };

        RichQuery query = new RichQuery();
        Map<String, Object> selector = new HashMap<>();
        selector.put("docType", "fish");
        query.setSelector(selector);

        String json = chaincodeExecuter.query(query);

        try {
            fish = Mapper.INSTANCE.getObjectMapper().readValue(json, listType);
        } catch (IOException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fish;
    }

}
