package com.example.bitebasket.service;

import android.content.Context;
import com.example.bitebasket.model.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OrderRepository {
    private static final String FILE_NAME="bitebasket_orders.txt";
    public void save(Context context, Order order, double total) throws IOException {
        File file=new File(context.getFilesDir(),FILE_NAME);
        try(FileWriter writer=new FileWriter(file,true)){
            writer.write("Order #"+order.getOrderId()+" | Customer: "+order.getCustomer().getName()+" | Restaurant: "+order.getRestaurant().getName()+" | Total: Rs. "+String.format(java.util.Locale.US,"%.2f",total)+" | Status: "+order.getStatus()+System.lineSeparator());
        }
    }
    public String history(Context context) throws IOException {
        File file=new File(context.getFilesDir(),FILE_NAME);
        if(!file.exists()) return "No previous orders.";
        StringBuilder text=new StringBuilder();
        try(BufferedReader reader=new BufferedReader(new FileReader(file))){ String line; while((line=reader.readLine())!=null) text.append(line).append("\n"); }
        return text.length()==0 ? "No previous orders." : text.toString();
    }
}
