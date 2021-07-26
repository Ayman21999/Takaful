package com.graduatio.project.takaful.Model;

import com.google.firebase.firestore.PropertyName;

public class Donations {

    @PropertyName("userID")
    String userID ;
    @PropertyName("donateforAds")
    String donateforAds;
    @PropertyName("total")
    int total;
    @PropertyName("paymethod")
    String paymethod;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDonateforAds() {
        return donateforAds;
    }

    public void setDonateforAds(String donateforAds) {
        this.donateforAds = donateforAds;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }
}
