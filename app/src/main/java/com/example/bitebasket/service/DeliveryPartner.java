package com.example.bitebasket.service;

public class DeliveryPartner {
    private final int id;
    private final String name;
    private boolean available=true;
    public DeliveryPartner(int id,String name){ this.id=id; this.name=name; }
    public synchronized boolean assign(){ if(!available) return false; available=false; return true; }
    public synchronized void release(){ available=true; }
    public synchronized boolean isAvailable(){ return available; }
    public int getId(){ return id; }
    public String getName(){ return name; }
}
