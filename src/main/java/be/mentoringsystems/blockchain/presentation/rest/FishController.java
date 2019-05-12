/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.presentation.rest;

import be.mentoringsystems.blockchain.model.Fish;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jelle
 */
@RestController
@RequestMapping("/fish")
public class FishController {

    @GetMapping
    Fish getFish() {
        Fish fish = new Fish();
        fish.setId(1);
        fish.setType("salmon");
        return fish;
    }

    @RequestMapping("/save")
    Fish saveFish() {

        Fish fish = new Fish();
        fish.setId(1);
        fish.setType("salmon");
        return fish;
    }

    @RequestMapping("/delete")
    int deleteFish() {
        return 1;
    }

}
