package com.example.demo.controller;

import com.example.demo.model.ShoppingItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/")
public class ShoppingItemController {
    private final String BACKEND_URL = "http://localhost:8080/api/shoppingItems";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String getAllItems(Model model) {
        ResponseEntity<ShoppingItem[]> response = restTemplate.getForEntity(BACKEND_URL, ShoppingItem[].class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<ShoppingItem> items = Arrays.asList(response.getBody());
            model.addAttribute("items", items);
        } else {
            model.addAttribute("error", "Could not fetch items");
        }
        return "index";
    }

    @GetMapping("/search")
    public String searchItem(@RequestParam String name, Model model) {
        // Fetch all items to ensure the shopping list is always displayed
        ResponseEntity<ShoppingItem[]> allItemsResponse = restTemplate.getForEntity(BACKEND_URL, ShoppingItem[].class);
        model.addAttribute("items", allItemsResponse.getBody());
    
        // Fetch the searched item
        String url = BACKEND_URL + "/" + name;
        try {
            ResponseEntity<ShoppingItem> response = restTemplate.getForEntity(url, ShoppingItem.class);
            model.addAttribute("searchResult", response.getBody());
        } catch (Exception e) {
            model.addAttribute("error", "Item not found");
        }
    
        // Return the same page without a redirect to preserve the search result
        return "index";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam String name, @RequestParam int amount, Model model) {
        ShoppingItem item = new ShoppingItem();
        item.setName(name);
        item.setAmount(amount);

        ResponseEntity<ShoppingItem> response = restTemplate.postForEntity(BACKEND_URL, item, ShoppingItem.class);
        model.addAttribute("message", response.getStatusCode().is2xxSuccessful() ? "Item added" : "Failed to add item");
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateItem(@RequestParam String name, @RequestParam int amount) {
        ShoppingItem item = new ShoppingItem();
        item.setName(name);
        item.setAmount(amount);

        restTemplate.put(BACKEND_URL + "/" + name, item);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam String name) {
        restTemplate.delete(BACKEND_URL + "/" + name);
        return "redirect:/";
    }
}
