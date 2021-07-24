package com.example.agroapp;

public class SellHistory {

    private String cropname;
    private String quantity;
    private String price;
    private String selldate;

    public SellHistory(String cropname, String quantity, String price, String selldate) {
        this.cropname = cropname;
        this.quantity = quantity;
        this.price = price;
        this.selldate = selldate;
    }

    public String getCropname() {
        return cropname;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getSelldate() {
        return selldate;
    }
}
