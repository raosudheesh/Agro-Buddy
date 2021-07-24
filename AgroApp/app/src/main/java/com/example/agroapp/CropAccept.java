package com.example.agroapp;

public class CropAccept {

    private String requestid;
    private String cropname;
    private String croptype;
    private String quantity;
    private String buyerid;
    private String requestdate;
    private String acceptstatus;

    public CropAccept(String requestid, String cropname, String croptype, String quantity, String buyerid, String requestdate, String acceptstatus) {
        this.requestid = requestid;
        this.cropname = cropname;
        this.croptype = croptype;
        this.quantity = quantity;
        this.buyerid = buyerid;
        this.requestdate = requestdate;
        this.acceptstatus = acceptstatus;
    }

    public String getRequestid() {
        return requestid;
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
