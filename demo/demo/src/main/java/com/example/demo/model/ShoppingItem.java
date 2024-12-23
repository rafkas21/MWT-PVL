package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ShoppingItem {
    
    @Id
    private String name;
    private int amount;

    public String getName() {
        return name;
    }
    public int getAmount() {
        return amount;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    
}
