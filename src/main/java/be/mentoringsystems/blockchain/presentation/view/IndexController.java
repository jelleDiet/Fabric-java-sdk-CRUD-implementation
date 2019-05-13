/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.presentation.view;

import be.mentoringsystems.blockchain.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author jellediet
 */
@Controller
public class IndexController {

    @Autowired
    FishService fishService;

    @RequestMapping("/")
    public String welcome(Model model) {
        model.addAttribute("fishes", fishService.getAll());
        return "welcome";
    }

    @RequestMapping("/createFish")
    public String createFish(Model model) {
        return "add-fish";
    }

    @RequestMapping("/queryFish")
    public String queryFish(Model model) {
        return "query-fish";
    }

}
