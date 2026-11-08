package com.example.bitebasket.model;

public class MenuItem {
    private final int id;
    private final String name, description;
    private final double price;
    public MenuItem(int id, String name, String description, double price) {
        if (id <= 0 || name == null || name.trim().isEmpty() || price < 0) throw new IllegalArgumentException("Invalid menu item.");
        this.id=id; this.name=name; this.description=description; this.price=price;
    }
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getDescription(){ return description; }
    public double getPrice(){ return price; }
}
