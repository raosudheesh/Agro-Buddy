package com.example.agroapp;

public class CropTrade {
    private String cropname;
    private String quantity;
    private String amount;
    private String farmerid;
    private String supplierid;
    private String tradedate;

    public CropTrade(String cropname, String quantity, String amount, String farmerid, String supplierid, String tradedate) {
        this.cropname = cropname;
        this.quantity = quantity;
        this.amount = amount;
        this.farmerid = farmerid;
        this.supplierid = supplierid;
        this.tradedate = tradedate;
    }

    public String getCropname() {
        return cropname;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getAmount() {
        return amount;
    }

    public String getFarmerid() {
        return farmerid;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public String getTradedate() {
        return tradedate;
    }
}
