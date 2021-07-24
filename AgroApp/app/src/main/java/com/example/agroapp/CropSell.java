package com.example.agroapp;

public class CropSell {

    private String cropname;
    private String croptype;
    private String totalquantity;
    private String availablequantity;
    private String unit;
    private String price;
    private String farmerid;
    private String selldate;

    public CropSell(String cropname, String croptype, String totalquantity, String availablequantity, String unit, String price, String farmerid, String selldate) {
        this.cropname = cropname;
        this.croptype = croptype;
        this.totalquantity = totalquantity;
        this.availablequantity = availablequantity;
        this.unit = unit;
        this.price = price;
        this.farmerid = farmerid;
        this.selldate = selldate;
    }

    public String getCropname() {
        return cropname;
    }

    public String getCroptype() {
        return croptype;
    }

    public String getTotalquantity() {
        return totalquantity;
    }

    public String getAvailablequantity() {
        return availablequantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrice() {
        return price;
    }

    public String getFarmerid() {
        return farmerid;
    }

    public String getSelldate() {
        return selldate;
    }
}
