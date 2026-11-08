package com.example.bitebasket.service;

import com.example.bitebasket.model.Order;

public class OrderProcessor implements Runnable {
    public interface StatusListener { void onStatusChanged(Order.Status status); }
    private final Order order;
    private final DeliveryPartner partner;
    private final StatusListener listener;
    public OrderProcessor(Order order, DeliveryPartner partner, StatusListener listener){ this.order=order; this.partner=partner; this.listener=listener; }
    @Override public void run(){
        try {
            update(Order.Status.PLACED); Thread.sleep(2200);
            update(Order.Status.PREPARING); Thread.sleep(2200);
            update(Order.Status.OUT_FOR_DELIVERY); Thread.sleep(2200);
            update(Order.Status.DELIVERED);
        } catch(InterruptedException e){ Thread.currentThread().interrupt(); }
        finally { if(partner!=null) partner.release(); }
    }
    private void update(Order.Status status){ order.setStatus(status); if(listener!=null) listener.onStatusChanged(status); }
}
