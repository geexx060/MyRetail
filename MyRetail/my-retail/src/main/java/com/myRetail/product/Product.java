package com.myRetail.product;

public class Product {

    private String id;
    private String name;
    private CurrentPrice currentPrice;

    public Product(){}

    public Product(String id, String name, CurrentPrice currentPrice) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CurrentPrice getCurrentPrice() {
        return currentPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentPrice(CurrentPrice currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice.toString() +
                '}';
    }
}