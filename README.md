# BiteBasket – Food Delivery Platform

Individual Java + Android XML mini-project starter inspired by Swiggy/Zomato.

## Included modules and OOP concepts
- `Customer`, `Restaurant`, `MenuItem`, `Order`: classes, objects and encapsulation.
- `Order.Status`: enum for order lifecycle.
- `DiscountStrategy` and `CouponDiscount`: interface and polymorphism.
- `ArrayList`, `HashMap`, `LinkedHashMap`: Java collections.
- `RestaurantClosedException`: custom checked exception.
- `synchronized` methods in `OrderService` and `DeliveryPartner`: shared-state protection.
- `OrderProcessor implements Runnable`: background thread updates order status.
- `OrderRepository`: saves and reads order history using app-internal file handling.
- `RestaurantSalesReport`: restaurant-wise subtotal report helper.

## Open in Android Studio
1. Extract this ZIP.
2. Open Android Studio and choose **Open**.
3. Select the extracted `BiteBasket_Android_Studio_Project` folder.
4. Wait for Gradle Sync. Install Android SDK Platform 35 if prompted.
5. Create/start an Android emulator, then press **Run**.

## Demo behaviour
- Add food items to the cart.
- Optional SAVE10 coupon gives 10% off when subtotal is at least ₹300.
- Delivery fee is ₹30.
- Place an order and see status change: PLACED → PREPARING → OUT_FOR_DELIVERY → DELIVERED.
- Completed order summaries are saved in app-private `bitebasket_orders.txt`.

## Important
This is a starter Android implementation, not a claim of a tested APK. Build and run it in Android Studio, then verify the behaviour on your computer. Sample customer, restaurant and partner data are for demonstration. Live payments, GPS, cloud database, authentication and real restaurant APIs are not included.
