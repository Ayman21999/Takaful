package com.graduatio.project.takaful.Model;

import com.google.firebase.firestore.PropertyName;

public class PayMethod {

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
}
