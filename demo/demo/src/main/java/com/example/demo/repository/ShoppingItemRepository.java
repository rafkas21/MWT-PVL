package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.ShoppingItem;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, String> {
    
}
