/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.presentation.view;

import be.mentoringsystems.blockchain.model.Fish;
import be.mentoringsystems.blockchain.model.RichQuery;
import be.mentoringsystems.blockchain.service.FishService;
import be.mentoringsystems.blockchain.service.TransactionService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jellediet
 */
@Controller
public class IndexController {

    @Autowired
    FishService fishService;
    @Autowired
    TransactionService transactionService;

    @RequestMapping("/")
    public String welcome(Model model) {
        return "index";
    }

    @RequestMapping("/products/add")
    public String createFish(Model model) {
        model.addAttribute("fish", new Fish());
        return "create";
    }

    @RequestMapping("/products/edit")
    public String editFish(@RequestParam UUID id, Model model, @RequestParam(defaultValue = "channel1") String channel) {
        model.addAttribute("fish", fishService.getById(id, channel));
        model.addAttribute("channel", channel);
        return "edit";
    }

    @RequestMapping("/products/search")
    public String searchFish(Model model) {
        model.addAttribute("fish", new Fish());
        return "search";
    }

    @RequestMapping("/products/query")
    public String queryFish(@RequestParam(required = false) String type, @RequestParam(required = false) BigDecimal price, @RequestParam(required = false) Double weight, Model model, @RequestParam(defaultValue = "channel1") String channel) {
        RichQuery query = new RichQuery();
        Map<String, Object> selector = new HashMap<>();
        if (type != null && !type.isEmpty()) {
            selector.put("type", type);
        }

        if (price != null && !price.equals(BigDecimal.ZERO)) {

            selector.put("price", price);
        }

        if (weight != null && weight != 0.0) {
            selector.put("weight", weight);
        }

        query.setSelector(selector);

        model.addAttribute("fishes", fishService.query(query, channel));
        model.addAttribute("channel", channel);
        return "products";
    }

    @RequestMapping("/products")
    public String getAllFish(Model model, @RequestParam(defaultValue = "channel1") String channel) {
        model.addAttribute("fishes", fishService.getAll(channel));
        model.addAttribute("channel", channel);
        return "products";
    }

    @RequestMapping("/products/getHistory")
    public String getFishHistory(Model model, @RequestParam UUID id, @RequestParam(defaultValue = "channel1") String channel) {
        model.addAttribute("fish", fishService.getById(id, channel));
        model.addAttribute("history", fishService.getHistory(id, channel));
        model.addAttribute("channel", channel);
        return "history";
    }

    @RequestMapping("/products/transactiondetails")
    public String getFishHistory(Model model, @RequestParam String transactionId, @RequestParam(defaultValue = "channel1") String channel) {
        model.addAttribute("transaction", transactionService.getTransactionDetailsFromBlock(transactionId, channel));
        model.addAttribute("channel", channel);
        return "transactiondetails";
    }

    @RequestMapping("/products/delete")
    public String deleteFish(@RequestParam UUID id, @RequestParam(defaultValue = "channel1") String channel) {
        fishService.delete(id, channel);
        return "redirect:/products?channel=" + channel;
    }

    @RequestMapping("/products/save")
    public String saveFish(@RequestParam UUID id, @RequestParam String type, @RequestParam BigDecimal price, @RequestParam Double weight, @RequestParam(defaultValue = "channel1") String channel) {
        Fish fish;
        if (id == null) {
            fish = new Fish();
        } else {
            fish = fishService.getById(id, channel);
        }

        fish.setType(type);

        fish.setPrice(price);

        fish.setWeight(weight);

        fishService.save(fish, channel);

        return "redirect:/products?channel=" + channel;
    }

}
