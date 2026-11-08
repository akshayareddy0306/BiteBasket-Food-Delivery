package com.example.bitebasket.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Order {
    public enum Status { PLACED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED }
    private final int orderId;
    private final Customer customer;
    private final Restaurant restaurant;
    private final Map<MenuItem,Integer> items = new LinkedHashMap<>();
    private Status status = Status.PLACED;
    public Order(int orderId, Customer customer, Restaurant restaurant){ this.orderId=orderId; this.customer=customer; this.restaurant=restaurant; }
    public void addItem(MenuItem item, int quantity){
        if(quantity<=0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        items.put(item, items.getOrDefault(item,0)+quantity);
    }
    public double getSubtotal(){
        double total=0;
        for(Map.Entry<MenuItem,Integer> e:items.entrySet()) total += e.getKey().getPrice()*e.getValue();
        return total;
    }
    public int getOrderId(){ return orderId; }
    public Customer getCustomer(){ return customer; }
    public Restaurant getRestaurant(){ return restaurant; }
    public Map<MenuItem,Integer> getItems(){ return Collections.unmodifiableMap(items); }
    public Status getStatus(){ return status; }
    public void setStatus(Status status){ this.status=status; }
}
