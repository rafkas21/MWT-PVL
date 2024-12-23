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

@RestController
@RequestMapping("/api/shoppingItems")
public class ShoppingItemController {
    
    private final ShoppingItemRepository repository;

    public ShoppingItemController(ShoppingItemRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<ShoppingItem> getAllItem(){
        return repository.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<ShoppingItem> getItemByName(@PathVariable String name) {
        Optional<ShoppingItem> item = repository.findById(name);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShoppingItem> addItem(@RequestBody ShoppingItem item) {
        return ResponseEntity.status(201).body(repository.save(item));
    }

    @PutMapping("/{name}")
    public ResponseEntity<ShoppingItem> updateItem(@PathVariable String name, @RequestBody ShoppingItem item) {
        return repository.findById(name)
            .map(existingItem -> {
                existingItem.setAmount(item.getAmount());
                return ResponseEntity.ok(repository.save(existingItem));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteItem(@PathVariable String name) {
        if (repository.existsById(name)) {
            repository.deleteById(name);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
