/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.persistence;

import be.mentoringsystems.blockchain.model.Fish;

/**
 *
 * @author jelle
 */
public interface FishDAO {
    
    Fish getById(int id);
    void save(Fish fish);
    
}
