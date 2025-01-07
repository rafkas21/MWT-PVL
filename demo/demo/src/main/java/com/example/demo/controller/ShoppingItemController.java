package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ShoppingItem;
import com.example.demo.repository.ShoppingItemRepository;

// This class is a REST controller that handles CRUD operations for ShoppingItems
@RestController
@RequestMapping("/api/shoppingItems") // Base path for all endpoints in this controller
public class ShoppingItemController {
    
    private final ShoppingItemRepository repository; // Repository to interact with the database

    // Constructor for dependency injection of the repository
    public ShoppingItemController(ShoppingItemRepository repository){
        this.repository = repository;
    }

    // Endpoint to get all ShoppingItems
    @GetMapping
    public List<ShoppingItem> getAllItem(){
        return repository.findAll(); // Fetch all items from the database
    }

    // Endpoint to get a ShoppingItem by its name
    @GetMapping("/{name}")
    public ResponseEntity<ShoppingItem> getItemByName(@PathVariable String name) {
        Optional<ShoppingItem> item = repository.findById(name); // Look for the item by name
        // Return the item if found, otherwise return 404 Not Found
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to add a new ShoppingItem
    @PostMapping
    public ResponseEntity<ShoppingItem> addItem(@RequestBody ShoppingItem item) {
        // Save the new item in the database and return it with status 201 Created
        return ResponseEntity.status(201).body(repository.save(item));
    }

    // Endpoint to update an existing ShoppingItem by its name
    @PutMapping("/{name}")
    public ResponseEntity<ShoppingItem> updateItem(@PathVariable String name, @RequestBody ShoppingItem item) {
        // Find the item by name and update it if it exists
        return repository.findById(name)
            .map(existingItem -> {
                existingItem.setAmount(item.getAmount()); // Update the amount of the existing item
                return ResponseEntity.ok(repository.save(existingItem)); // Save and return the updated item
            })
            // Return 404 Not Found if the item does not exist
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to delete a ShoppingItem by its name
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteItem(@PathVariable String name) {
        // Check if the item exists in the database
        if (repository.existsById(name)) {
            repository.deleteById(name); // Delete the item if it exists
            return ResponseEntity.ok().build(); // Return 200 OK if deletion was successful
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the item does not exist
        }
    }
}