/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.service.impl;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.model.FishHistory;
import be.mentoringsystems.blockchain.model.query.RichQuery;
import be.mentoringsystems.blockchain.persistence.FishDAO;
import be.mentoringsystems.blockchain.service.FishService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jelle
 */
@Service
public class FishServiceImpl implements FishService {

    @Autowired
    FishDAO fishDAO;

    @Override
    public Fish getById(UUID id, String channelName) {
        return fishDAO.getById(id, channelName);
    }

    @Override
    public void save(Fish fish, String channelName) {
        fishDAO.save(fish, channelName);
    }

    @Override
    public List<Fish> query(RichQuery query, String channelName) {
        return fishDAO.query(query, channelName);
    }

    @Override
    public void delete(UUID id, String channelName) {
        fishDAO.delete(id, channelName);
    }

    @Override
    public List<Fish> getAll(String channelName) {
        return fishDAO.getAll(channelName);
    }

    @Override
    public List<Fish> queryWithPagination(RichQuery query, int pageSize, String bookmark, String channelName) {
        return fishDAO.queryWithPagination(query, pageSize, bookmark, channelName);
    }

    @Override
    public List<Fish> getAllWithPagination(int pageSize, String bookmark, String channelName) {
        return fishDAO.getAllWithPagination(pageSize, bookmark, channelName);
    }

    @Override
    public List<FishHistory> getHistory(UUID id, String channelName) {
        return fishDAO.getHistory(id, channelName);
    }

}
