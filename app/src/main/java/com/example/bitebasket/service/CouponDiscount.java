package com.example.bitebasket.service;

public class CouponDiscount implements DiscountStrategy {
    private final double percentage;
    public CouponDiscount(double percentage){
        if(percentage<0 || percentage>50) throw new IllegalArgumentException("Discount must be between 0 and 50%.");
        this.percentage=percentage;
    }
    @Override public double calculateDiscount(double subtotal){ return subtotal>=300 ? subtotal*percentage/100.0 : 0.0; }
}
