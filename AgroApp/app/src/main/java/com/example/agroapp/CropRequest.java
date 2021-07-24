package com.example.agroapp;

public class CropRequest {

    private String cropname;
    private String croptype;
    private String quantity;
    private String unit;
    private String buyerid;
    private String requestdate;
    private String acceptstatus;

    public CropRequest(String cropname, String croptype, String quantity, String unit, String buyerid, String requestdate, String acceptstatus) {
        this.cropname = cropname;
        this.croptype = croptype;
        this.quantity = quantity;
        this.unit = unit;
        this.buyerid = buyerid;
        this.requestdate = requestdate;
        this.acceptstatus = acceptstatus;
    }

    public String getCropname() {
        return cropname;
    }

    public String getCroptype() {
        return croptype;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public String getRequestdate() {
        return requestdate;
    }

    public String getAcceptstatus() {
        return acceptstatus;
    }
}
