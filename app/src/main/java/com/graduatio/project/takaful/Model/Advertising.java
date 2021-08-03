package com.graduatio.project.takaful.Model;

import com.google.firebase.firestore.PropertyName;

public class Advertising {
        @PropertyName("add_ID")
    String add_ID;
    @PropertyName("address")
    String address;
    @PropertyName("description")
    String description;
    @PropertyName("image")
    String image;
    @PropertyName("title")
    String title;
    @PropertyName("userId")
    String userId;
    @PropertyName("target")
    int target;
    @PropertyName("remaining")
    int remaining;
    @PropertyName("Daynumber")
    int Daynumber;
    @PropertyName("Type")
    String Type;
    @PropertyName("userJob")
    String userJob;
    @PropertyName("name_of_Charity")
    String name_of_Charity;
    @PropertyName("media_account")
    String media_account;
    @PropertyName("whoarebenefit")
    String whoarebenefit;
    @PropertyName("userphone")
    String userphone;
    @PropertyName("isRejected")
    boolean isRejected;
    @PropertyName("donerid")
    String donerid;
    @PropertyName("total")
    String total;
    public String getName_of_Charity() {
        return name_of_Charity;
    }

    public void setName_of_Charity(String name_of_Charity) {
        this.name_of_Charity = name_of_Charity;
    }

    public String getAdd_ID() {
        return add_ID;
    }

    public void setAdd_ID(String add_ID) {
        this.add_ID = add_ID;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public int getDaynumber() {
        return Daynumber;
    }

    public void setDaynumber(int daynumber) {
        Daynumber = daynumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUserJob() {
        return userJob;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    public String getMedia_account() {
        return media_account;
    }

    public void setMedia_account(String media_account) {
        this.media_account = media_account;
    }

    public String getWhoarebenefit() {
        return whoarebenefit;
    }

    public void setWhoarebenefit(String whoarebenefit) {
        this.whoarebenefit = whoarebenefit;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public String getDonerid() {
        return donerid;
    }

    public void setDonerid(String donerid) {
        this.donerid = donerid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
