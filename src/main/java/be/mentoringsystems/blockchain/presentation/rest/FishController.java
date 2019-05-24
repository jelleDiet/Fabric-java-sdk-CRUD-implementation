/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.presentation.rest;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.model.FishHistory;
import be.mentoringsystems.blockchain.model.RichQuery;
import be.mentoringsystems.blockchain.service.FishService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    Fish getFish(@RequestParam UUID id, @RequestParam(defaultValue = "channel1") String channel) {
        return fishService.getById(id, channel);
    }

    //Get all fish
    @RequestMapping("/getAll")
    List<Fish> getAllFish(@RequestParam(defaultValue = "channel1") String channel) {
        return fishService.getAll(channel);

    }

    //Saves a fish using id as key
    @RequestMapping("/save")
    Fish saveFish(@RequestParam(required = false) String type, @RequestParam(required = false) Double weight, @RequestParam(required = false) BigDecimal price, @RequestParam(defaultValue = "channel1") String channel) {

        Fish fish = new Fish();
        fish.setType(type);
        fish.setPrice(price);
        if (weight != null) {
            fish.setWeight(weight);
        }
        fishService.save(fish, channel);
        return fish;
    }

    //Deletes an object by its key
    @RequestMapping("/delete")
    String deleteFish(@RequestParam UUID id, @RequestParam(defaultValue = "channel1") String channel) {
        fishService.delete(id, channel);

        return id.toString();
    }

    //Search all documents in the blockchain with docType fish and the given selector parameters
    @RequestMapping("/query")
    List<Fish> queryFish(@RequestParam(required = false) String type, @RequestParam(required = false) Double weight, @RequestParam(required = false) BigDecimal price, @RequestParam(defaultValue = "channel1") String channel) {
        RichQuery query = new RichQuery();
        Map<String, Object> selector = new HashMap<>();
        if (type != null && !type.isEmpty()) {
            selector.put("type", type);
        }
        if (weight != null) {
            selector.put("weight", weight);
        }
        if (price != null) {
            selector.put("price", price);
        }
        selector.put("docType", "fish");
        query.setSelector(selector);

        return fishService.query(query, channel);
    }

    //Not functional yet
    @RequestMapping("/queryWithPagination")
    List<Fish> queryWithPagination(@RequestParam(required = false) String bookmark, @RequestParam(defaultValue = "channel1") String channel) {
        return fishService.getAllWithPagination(5, bookmark, channel);
    }

    //Not functional yet
    @RequestMapping("/getHistory")
    List<FishHistory> getHistory(@RequestParam UUID id, @RequestParam(defaultValue = "channel1") String channel) {
        return fishService.getHistory(id, channel);

    }

}
