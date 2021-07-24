package com.example.agroapp;

public class CropBuy {

    private String cropname;
    private String quantity;
    private String unit;
    private String price;
    private String cropid;
    private String farmerid;
    private String selldate;

    public CropBuy(String cropname, String quantity, String unit, String price, String cropid, String farmerid, String selldate) {
        this.cropname = cropname;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.cropid = cropid;
        this.farmerid = farmerid;
        this.selldate = selldate;
    }

    public String getCropname() {
        return cropname;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrice() {
        return price;
    }

    public String getCropid() {
        return cropid;
    }

    public String getFarmerid() {
        return farmerid;
    }

    public String getSelldate() {
        return selldate;
    }
}
