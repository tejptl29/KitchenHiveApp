package com.example.kitchenhive;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String productId, storeId;
    private String productName;
    private double price;
    private int quantity;
    private String image;
    private double total;
    private String veg;

    public CartItem(String productId, String storeId, String productName, double price, int quantity, String image, double total, String veg) {
        this.productId = productId;
        this.storeId = storeId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.total = total;
        this.veg = veg;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getVeg() { return veg; }
    public void setVeg(String veg) { this.veg = veg; }
}


