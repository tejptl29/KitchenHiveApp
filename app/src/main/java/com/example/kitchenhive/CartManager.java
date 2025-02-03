package com.example.kitchenhive;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public CartManager(Context context) {
        sharedPreferences = context.getSharedPreferences(new Utility().PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Save cart items
    public void saveCart(List<CartItem> cartItems) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(cartItems);
        editor.putString(new Utility().CART_KEY, json);
        editor.apply();
    }

    // Retrieve cart items
    public List<CartItem> getCart() {
        String json = sharedPreferences.getString(new Utility().CART_KEY, null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    // Add item to cart
    public void addItem(CartItem item) {
        List<CartItem> cartItems = getCart();
        cartItems.add(item);
        saveCart(cartItems);
    }

    // Remove item from cart by product ID
    public void removeItem(String productId) {
        List<CartItem> cartItems = getCart();
        cartItems.removeIf(item -> item.getProductId().equals(productId));
        saveCart(cartItems);
    }


    // Remove item from cart by product ID
    public int getItemQuantity(String productId) {
        List<CartItem> cartItems = getCart();
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if(item.getProductId().equals(productId)){
                return item.getQuantity();
            }
        }
        return 0;
    }

    // Remove item from cart by product ID
    public void setItemQuantity(String productId, int qty){
        List<CartItem> cartItems = getCart();
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if(item.getProductId().equals(productId)){
                item.setQuantity(qty);
            }
        }
        saveCart(cartItems);
    }

    // Remove item from cart by product ID
    public boolean product_exist(String productId){
        List<CartItem> cartItems = getCart();
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if(item.getProductId().equals(productId)){
                return true;
            }
        }
        return false;
    }

    // Clear cart
    public void clearCart() {
        sharedPreferences.edit().remove(new Utility().CART_KEY).apply();
    }
}
