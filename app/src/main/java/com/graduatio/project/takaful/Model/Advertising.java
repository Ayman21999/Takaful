package com.graduatio.project.takaful.Model;

import com.google.firebase.firestore.PropertyName;

public class Advertising {
    @PropertyName("add_ID")
    String add_ID;
    @PropertyName("catagorey")
    String catagorey;
    @PropertyName("description")
    String description;
    @PropertyName("image")
    String image;
    @PropertyName("title")
    String title ;
    @PropertyName("userId")
    String userId ;
    @PropertyName("target")
    int target;
    @PropertyName("remaining")
    int remaining;
    @PropertyName("beginDate")
    long beginDate;
    @PropertyName("endDate")
    long endDate ;


    public String getAdd_ID() {
        return add_ID;
    }

    public void setAdd_ID(String add_ID) {
        this.add_ID = add_ID;
    }

    public String getCatagorey() {
        return catagorey;
    }

    public void setCatagorey(String catagorey) {
        this.catagorey = catagorey;
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

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
