package com.example.divinebusinesstracker.models;

import android.widget.TableLayout;

public class Entry {
    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }



    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    private String sno;
    private String productName;
    private String quantity;
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Entry(String sno, String productName, String quantity, String amount) {
        this.sno = sno;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
    }


    public String getSno() {
        return sno;
    }

    public String getProductName() {
        return productName;
    }




    public String getQuantity() {
        return quantity;
    }


}
