package com.example.demo.controller;

import com.example.demo.model.ShoppingItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

// This class is a Spring MVC controller that handles web requests for ShoppingItems
@Controller
@RequestMapping("/") // Base URL for all endpoints in this controller
public class ShoppingItemController {
    
    // URL of the backend API service
    private final String BACKEND_URL = "http://localhost:8080/api/shoppingItems";
    private final RestTemplate restTemplate = new RestTemplate(); // RestTemplate for making HTTP requests

    // Endpoint to display all ShoppingItems on the main page
    @GetMapping
    public String getAllItems(Model model) {
        // Fetch all items from the backend API
        ResponseEntity<ShoppingItem[]> response = restTemplate.getForEntity(BACKEND_URL, ShoppingItem[].class);
        
        // Check if the response is successful and contains a body
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<ShoppingItem> items = Arrays.asList(response.getBody()); // Convert array to list
            model.addAttribute("items", items); // Add items to the model for display
        } else {
            model.addAttribute("error", "Could not fetch items"); // Add error message to the model
        }
        return "index"; // Return the view name (index.html)
    }

    // Endpoint to search for a specific ShoppingItem by name
    @GetMapping("/search")
    public String searchItem(@RequestParam String name, Model model) {
        // Fetch all items to ensure the shopping list is always displayed
        ResponseEntity<ShoppingItem[]> allItemsResponse = restTemplate.getForEntity(BACKEND_URL, ShoppingItem[].class);
        model.addAttribute("items", allItemsResponse.getBody());
    
        // Construct the URL for searching an item by name
        String url = BACKEND_URL + "/" + name;
        try {
            // Fetch the specific item from the backend API
            ResponseEntity<ShoppingItem> response = restTemplate.getForEntity(url, ShoppingItem.class);
            model.addAttribute("searchResult", response.getBody()); // Add search result to the model
        } catch (Exception e) {
            model.addAttribute("error", "Item not found"); // Add error message if item is not found
        }
    
        // Return the view (index.html) without a redirect to preserve the search result
        return "index";
    }

    // Endpoint to add a new ShoppingItem
    @PostMapping("/add")
    public String addItem(@RequestParam String name, @RequestParam int amount, Model model) {
        // Create a new ShoppingItem object and set its properties
        ShoppingItem item = new ShoppingItem();
        item.setName(name);
        item.setAmount(amount);

        // Send a POST request to the backend API to add the new item
        ResponseEntity<ShoppingItem> response = restTemplate.postForEntity(BACKEND_URL, item, ShoppingItem.class);
        
        // Add a success or failure message to the model based on the response status
        model.addAttribute("message", response.getStatusCode().is2xxSuccessful() ? "Item added" : "Failed to add item");
        
        // Redirect to the main page after adding the item
        return "redirect:/";
    }

    // Endpoint to update an existing ShoppingItem
    @PostMapping("/update")
    public String updateItem(@RequestParam String name, @RequestParam int amount) {
        // Create a new ShoppingItem object with updated values
        ShoppingItem item = new ShoppingItem();
        item.setName(name);
        item.setAmount(amount);

        // Send a PUT request to update the item in the backend API
        restTemplate.put(BACKEND_URL + "/" + name, item);
        
        // Redirect to the main page after updating the item
        return "redirect:/";
    }

    // Endpoint to delete a ShoppingItem by name
    @PostMapping("/delete")
    public String deleteItem(@RequestParam String name) {
        // Send a DELETE request to the backend API to remove the item
        restTemplate.delete(BACKEND_URL + "/" + name);
        
        // Redirect to the main page after deleting the item
        return "redirect:/";
    }
}