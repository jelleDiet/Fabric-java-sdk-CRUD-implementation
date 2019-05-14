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
    Fish getFish(@RequestParam UUID id) {
        return fishService.getById(id);
    }

    //Gets a fish using id as key
    @RequestMapping("/getAll")
    List<Fish> getAllFish() {
        return fishService.getAll();
    }

    //Saves a fish using id as key
    @RequestMapping("/save")
    Fish saveFish(@RequestParam(required = false) String type, @RequestParam(required = false) Double weight, @RequestParam(required = false) BigDecimal price) {

        Fish fish = new Fish();
        fish.setId(UUID.randomUUID());
        fish.setType(type);
        fish.setPrice(price);
        if (weight != null) {
            fish.setWeight(weight);
        }
        fishService.save(fish);
        return fish;
    }

    //Deletes an object by its key
    @RequestMapping("/delete")
    String deleteFish(@RequestParam UUID id) {
        fishService.delete(id);

        return id.toString();
    }

    //Returns all fish of type tuna
    @RequestMapping("/query")
    List<Fish> queryFish(@RequestParam(required = false) String type, @RequestParam(required = false) Double weight, @RequestParam(required = false) BigDecimal price) {
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

        return fishService.query(query);
    }

}
