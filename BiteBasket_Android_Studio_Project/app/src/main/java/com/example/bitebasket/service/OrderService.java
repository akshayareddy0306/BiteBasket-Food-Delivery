package com.example.bitebasket.service;

import com.example.bitebasket.exception.RestaurantClosedException;
import com.example.bitebasket.model.Customer;
import com.example.bitebasket.model.MenuItem;
import com.example.bitebasket.model.Order;
import com.example.bitebasket.model.Restaurant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final Map<Integer,Order> orders = new HashMap<>();
    private final List<DeliveryPartner> partners = new ArrayList<>();
    private final DiscountStrategy discount = new CouponDiscount(10);
    public synchronized Order placeOrder(int id, Customer customer, Restaurant restaurant, Map<MenuItem,Integer> cart) throws RestaurantClosedException {
        if(!restaurant.isOpen()) throw new RestaurantClosedException("Restaurant is closed.");
        if(cart==null || cart.isEmpty()) throw new IllegalArgumentException("Cart is empty.");
        Order order=new Order(id,customer,restaurant);
        for(Map.Entry<MenuItem,Integer> e:cart.entrySet()) order.addItem(e.getKey(),e.getValue());
        orders.put(id,order);
        return order;
    }
    public synchronized void addPartner(DeliveryPartner partner){ partners.add(partner); }
    public synchronized DeliveryPartner assignPartner(){ for(DeliveryPartner p:partners) if(p.assign()) return p; return null; }
    public double calculateTotal(Order order, boolean coupon, double deliveryFee){
        double subtotal=order.getSubtotal();
        double reduction=coupon ? discount.calculateDiscount(subtotal) : 0;
        return subtotal-reduction+deliveryFee;
    }
    public synchronized Order findOrder(int id){ return orders.get(id); }
    public synchronized List<Order> getOrders(){ return Collections.unmodifiableList(new ArrayList<>(orders.values())); }
}
