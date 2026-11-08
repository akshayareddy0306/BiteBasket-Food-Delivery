package com.example.bitebasket.service;

import com.example.bitebasket.model.Order;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantSalesReport {
    public Map<String,Double> generate(List<Order> orders){
        Map<String,Double> report=new HashMap<>();
        for(Order order:orders){
            String name=order.getRestaurant().getName();
            report.put(name,report.getOrDefault(name,0.0)+order.getSubtotal());
        }
        return report;
    }
}
