package com.example.bitebasket.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bitebasket.R;
import com.example.bitebasket.exception.RestaurantClosedException;
import com.example.bitebasket.model.Customer;
import com.example.bitebasket.model.MenuItem;
import com.example.bitebasket.model.Order;
import com.example.bitebasket.model.Restaurant;
import com.example.bitebasket.service.DeliveryPartner;
import com.example.bitebasket.service.OrderProcessor;
import com.example.bitebasket.service.OrderRepository;
import com.example.bitebasket.service.OrderService;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {
    private static final double DELIVERY_FEE=30.0;
    private final Map<MenuItem,Integer> cart=new LinkedHashMap<>();
    private final Handler handler=new Handler(Looper.getMainLooper());
    private Restaurant restaurant;
    private Customer customer;
    private OrderService orderService;
    private OrderRepository repository;
    private DeliveryPartner partner;
    private LinearLayout menuContainer;
    private TextView cartText,statusText,historyText;
    private CheckBox couponCheck;
    private Button placeButton;
    private int nextOrderId=1024;

    @Override protected void onCreate(Bundle state){
        super.onCreate(state); setContentView(R.layout.activity_main);
        menuContainer=findViewById(R.id.menuContainer); cartText=findViewById(R.id.txtCart);
        statusText=findViewById(R.id.txtOrderStatus); historyText=findViewById(R.id.txtHistory);
        couponCheck=findViewById(R.id.checkCoupon); placeButton=findViewById(R.id.btnPlaceOrder);
        setupData(); showMenu(); refreshCart(); loadHistory();
        placeButton.setOnClickListener(v->placeOrder());
    }
    private void setupData(){
        restaurant=new Restaurant(1,"Spice Route Kitchen");
        restaurant.addMenuItem(new MenuItem(101,"Paneer Butter Masala","Creamy tomato gravy",220));
        restaurant.addMenuItem(new MenuItem(102,"Veg Biryani","Aromatic basmati rice",180));
        restaurant.addMenuItem(new MenuItem(103,"Dal Tadka","Yellow lentils with spices",140));
        restaurant.addMenuItem(new MenuItem(104,"Butter Naan","Soft tandoor-baked bread",45));
        customer=new Customer(1,"P. Akshaya Reddy","0000000000");
        orderService=new OrderService(); repository=new OrderRepository();
        partner=new DeliveryPartner(1,"Riya"); orderService.addPartner(partner);
    }
    private void showMenu(){
        menuContainer.removeAllViews();
        for(MenuItem item:restaurant.getMenu()){
            LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(16,12,16,12); card.setBackgroundColor(0xFFFFFFFF);
            TextView name=new TextView(this); name.setText(item.getName()+"  •  "+money(item.getPrice())); name.setTextSize(16); name.setTextColor(0xFF17365D); name.setTypeface(null,1);
            TextView desc=new TextView(this); desc.setText(item.getDescription()); desc.setTextSize(13); desc.setTextColor(0xFF64748B);
            Button add=new Button(this); add.setText("ADD TO CART"); add.setOnClickListener(v->{ cart.put(item,cart.getOrDefault(item,0)+1); refreshCart(); });
            card.addView(name); card.addView(desc); card.addView(add);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2); params.setMargins(0,0,0,12); menuContainer.addView(card,params);
        }
    }
    private void refreshCart(){
        if(cart.isEmpty()){ cartText.setText("Your cart is empty."); return; }
        StringBuilder text=new StringBuilder(); double subtotal=0;
        for(Map.Entry<MenuItem,Integer> e:cart.entrySet()){
            double line=e.getKey().getPrice()*e.getValue(); subtotal+=line;
            text.append(e.getKey().getName()).append(" x ").append(e.getValue()).append(" = ").append(money(line)).append("\n");
        }
        text.append("\nSubtotal: ").append(money(subtotal)).append("\nDelivery fee: ").append(money(DELIVERY_FEE));
        cartText.setText(text.toString());
    }
    private void placeOrder(){
        if(cart.isEmpty()){ Toast.makeText(this,"Please add food items first.",Toast.LENGTH_SHORT).show(); return; }
        try{
            Order order=orderService.placeOrder(nextOrderId++,customer,restaurant,cart);
            DeliveryPartner assigned=orderService.assignPartner();
            if(assigned==null){ Toast.makeText(this,"No delivery partner is available right now.",Toast.LENGTH_LONG).show(); return; }
            double total=orderService.calculateTotal(order,couponCheck.isChecked(),DELIVERY_FEE);
            String partnerName=assigned.getName();
            statusText.setText("Order #"+order.getOrderId()+"\nPartner: "+partnerName+"\nTotal: "+money(total)+"\nStatus: PLACED");
            cart.clear(); couponCheck.setChecked(false); refreshCart(); placeButton.setEnabled(false);
            new Thread(new OrderProcessor(order,assigned,status->{
                handler.post(()->{
                    statusText.setText("Order #"+order.getOrderId()+"\nPartner: "+partnerName+"\nTotal: "+money(total)+"\nStatus: "+status.toString().replace('_',' '));
                    if(status==Order.Status.DELIVERED){
                        try{ repository.save(MainActivity.this,order,total); loadHistory(); }
                        catch(IOException e){ Toast.makeText(MainActivity.this,"History save failed: "+e.getMessage(),Toast.LENGTH_LONG).show(); }
                        placeButton.setEnabled(true);
                    }
                });
            })).start();
        }catch(RestaurantClosedException | IllegalArgumentException e){ Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show(); }
    }
    private void loadHistory(){ try{ historyText.setText(repository.history(this)); }catch(IOException e){ historyText.setText("Unable to load order history."); } }
    private String money(double value){ return String.format(Locale.getDefault(),"₹%.2f",value); }
}
