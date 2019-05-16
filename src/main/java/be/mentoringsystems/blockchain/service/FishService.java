/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.service;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.model.FishHistory;
import be.mentoringsystems.blockchain.model.query.RichQuery;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author jelle
 */
public interface FishService {

    Fish getById(UUID id, String channelName);

    void save(Fish fish, String channelName);

    List<Fish> query(RichQuery query, String channelName);

    List<Fish> queryWithPagination(RichQuery query, int pageSize, String bookmark, String channelName);

    List<Fish> getAllWithPagination(int pageSize, String bookmark, String channelName);

    void delete(UUID id, String channelName);

    List<Fish> getAll(String channelName);

    List<FishHistory> getHistory(UUID id, String channelName);
}
