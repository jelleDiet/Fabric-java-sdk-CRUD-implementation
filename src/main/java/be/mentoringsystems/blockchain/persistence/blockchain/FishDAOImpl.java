/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.persistence.blockchain;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.model.FishHistory;
import be.mentoringsystems.blockchain.model.RichQuery;
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
    public Fish getById(UUID id, String channelName) {
        String key = String.valueOf(id);
        String json = chaincodeExecuter.getObjectByKey(key, channelName);
        Fish fish = null;
        if (json != null && !json.isEmpty()) {
            try {
                fish = Mapper.INSTANCE.getObjectMapper().readValue(json, Fish.class);
            } catch (IOException ex) {
                Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fish;
    }

    @Override
    public void save(Fish fish, String channelName) {
        if (fish.getId() == null) {
            fish.setId(UUID.randomUUID());
        }
        String json = "";
        try {
            json = Mapper.INSTANCE.getObjectMapper().writeValueAsString(fish);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        chaincodeExecuter.saveObject(String.valueOf(fish.getId()), json, channelName);
    }

    @Override
    public List<Fish> query(RichQuery query, String channelName) {
        List<Fish> fish = new ArrayList<>();
        TypeReference<List<Fish>> listType = new TypeReference<List<Fish>>() {
        };

        List<String> index = new ArrayList<>();
        index.add("_design/indexFishType");
        index.add("indexFishType");
        query.setUse_index(index);

        String json = chaincodeExecuter.query(query, channelName);

        try {
            fish = Mapper.INSTANCE.getObjectMapper().readValue(json, listType);
        } catch (IOException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fish;
    }

    @Override
    public void delete(UUID id, String channelName) {
        chaincodeExecuter.deleteObject(String.valueOf(id), channelName);
    }

    @Override
    public List<Fish> getAll(String channelName) {
        List<Fish> fish = new ArrayList<>();
        TypeReference<List<Fish>> listType = new TypeReference<List<Fish>>() {
        };

        RichQuery query = new RichQuery();
        Map<String, Object> selector = new HashMap<>();
        selector.put("docType", "fish");
        query.setSelector(selector);

        String json = chaincodeExecuter.query(query, channelName);

        try {
            fish = Mapper.INSTANCE.getObjectMapper().readValue(json, listType);
        } catch (IOException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fish;
    }

    @Override
    public List<Fish> queryWithPagination(RichQuery query, int pageSize, String bookmark, String channelName) {
        List<Fish> fish = new ArrayList<>();
        TypeReference<List<Fish>> listType = new TypeReference<List<Fish>>() {
        };

        List<String> index = new ArrayList<>();
        index.add("_design/indexFishType");
        index.add("indexFishType");
        query.setUse_index(index);

        String json = chaincodeExecuter.queryWithPagination(query, pageSize, bookmark, channelName);

        try {
            fish = Mapper.INSTANCE.getObjectMapper().readValue(json, listType);
        } catch (IOException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fish;
    }

    @Override
    public List<Fish> getAllWithPagination(int pageSize, String bookmark, String channelName) {
        List<Fish> fish = new ArrayList<>();
        TypeReference<List<Fish>> listType = new TypeReference<List<Fish>>() {
        };

        RichQuery query = new RichQuery();
        Map<String, Object> selector = new HashMap<>();
        selector.put("docType", "fish");
        query.setSelector(selector);

        String json = chaincodeExecuter.queryWithPagination(query, pageSize, bookmark, channelName);

        try {
            fish = Mapper.INSTANCE.getObjectMapper().readValue(json, listType);
        } catch (IOException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fish;
    }

    @Override
    public List<FishHistory> getHistory(UUID id, String channelName) {
        List<FishHistory> history = new ArrayList<>();
        TypeReference<List<FishHistory>> listType = new TypeReference<List<FishHistory>>() {
        };
        String key = String.valueOf(id);
        String json = chaincodeExecuter.getObjectHistory(key, channelName);
        try {
            history = Mapper.INSTANCE.getObjectMapper().readValue(json, listType);
        } catch (IOException ex) {
            Logger.getLogger(FishDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return history;
    }

}
