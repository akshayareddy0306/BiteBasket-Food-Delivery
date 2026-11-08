package com.example.bitebasket.model;

public class Customer {
    private final int id;
    private final String name, phone;
    public Customer(int id, String name, String phone) {
        if (id <= 0 || name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Customer details are required.");
        this.id=id; this.name=name; this.phone=phone;
    }
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getPhone(){ return phone; }
}
