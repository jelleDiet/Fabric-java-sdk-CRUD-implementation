/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.presentation.rest;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.model.query.RichQuery;
import be.mentoringsystems.blockchain.service.FishService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jelle
 */
@RestController
@RequestMapping("/fish")
public class FishController {

    @Autowired
    FishService fishService;

    //Gets a fish using id as key
    @RequestMapping("/get")
    Fish getFish() {
        return fishService.getById(5);
    }

    //Saves a fish using id as key
    @RequestMapping("/save")
    Fish saveFish() {

        Fish fish = new Fish();
        fish.setId(1);
        fish.setType("tuna");
        fish.setPrice(BigDecimal.ONE);
        fish.setWeight(1.2);
        fishService.save(fish);
        return fish;
    }

    //Deletes an object by its key
    @RequestMapping("/delete")
    int deleteFish() {
        return 1;
    }

    //Returns all fish of type tuna
    @RequestMapping("/query")
    List<Fish> queryFish() {
        RichQuery query = new RichQuery();
        Map<String, Object> selector = new HashMap<>();
        selector.put("type", "tuna");
        selector.put("docType", "fish");
        String functionNameRichQuery = "query";
        query.setSelector(selector);

        return fishService.query(query);
    }

}
