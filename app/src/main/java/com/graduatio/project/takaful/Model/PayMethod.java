package com.graduatio.project.takaful.Model;

import com.google.firebase.firestore.PropertyName;

public class PayMethod {
    public static final int LayoutOne = 0;
    public static final int LayoutTwo = 1;
    private int viewType;
    private String text;
    private int icon;


    @PropertyName("cardtype")
    String cardtype;
    @PropertyName("cardNumber")
    String  cardNumber ;
    @PropertyName("cvvCode")
    String  cvvCode ;
    @PropertyName("enddate")
    String enddate;
    @PropertyName("img")
    int img;
    @PropertyName("email")
    String email;
    @PropertyName("pass")
    String pass;
    //Add new method design
    public PayMethod(int viewType, String text)
    {
        this.text = text;
        this.viewType = viewType;
    }
    public PayMethod(int viewType, int icon, String email,
                     String pass)
    {
        this.icon = icon;
        this.email = email;
        this.pass = pass;
        this.viewType = viewType;
    }


    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
