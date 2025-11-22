package com.kstu.myapplication;

public class Food {
    private String name;

    private char currency;
    private int price;
    private String imageUrl;

    public Food(String name, char currency, int price, String imageUrl) {
        this.name = name;
        this.currency = currency;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public char getCurrency() {
        return currency;
    }
}
