/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.service;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.model.query.RichQuery;
import java.util.List;

/**
 *
 * @author jelle
 */
public interface FishService {

    Fish getById(int id);

    void save(Fish fish);

    List<Fish> query(RichQuery query);
}
