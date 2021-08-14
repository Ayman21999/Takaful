package com.graduatio.project.takaful.Model;

import com.google.firebase.firestore.PropertyName;

public class User {

    @PropertyName("userId")
    String userId;
    @PropertyName("email")
    String email;
    @PropertyName("password")
    String password;
    @PropertyName("isHasCharity")
    boolean isHasCharity;
    @PropertyName("userImage")
    String userImage;
    @PropertyName("role")
    String role;
    @PropertyName("payMethod")
    String payMethod;
    @PropertyName("isHasActivity")
    boolean isHasActivity;
    @PropertyName("phone")
    String phone;
    @PropertyName("firstName")
    String firstName;
    @PropertyName("lastName")
    String lastName;
    @PropertyName("isBlocked")
    boolean isBlocked;
    @PropertyName("isSuspend")
    boolean isSuspend;
    @PropertyName("adsID")
    String adsID;
    @PropertyName("donateRequestTotal")
    int donateRequestTotal;
    @PropertyName("age")
    String age;
    @PropertyName("familynum")
    String familynum;
    @PropertyName("identity")
    String identity;
    @PropertyName("social")
    String social;
    @PropertyName("havework")
    String havework;

    @PropertyName("soialmedai")
    String soialmedai;
    @PropertyName("salary")
    String salary;

    @PropertyName("attachment")
    String attachment;
    public boolean isHasActivity() {
        return isHasActivity;
    }

    public void setHasActivity(boolean hasActivity) {
        isHasActivity = hasActivity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasCharity() {
        return isHasCharity;
    }

    public void setHasCharity(boolean hasCharity) {
        isHasCharity = hasCharity;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isSuspend() {
        return isSuspend;
    }

    public void setSuspend(boolean suspend) {
        isSuspend = suspend;
    }

    public String getAdsID() {
        return adsID;
    }

    public void setAdsID(String adsID) {
        this.adsID = adsID;
    }

    public int getDonateRequestTotal() {
        return donateRequestTotal;
    }

    public void setDonateRequestTotal(int donateRequestTotal) {
        this.donateRequestTotal = donateRequestTotal;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFamilynum() {
        return familynum;
    }

    public void setFamilynum(String familynum) {
        this.familynum = familynum;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getSoialmedai() {
        return soialmedai;
    }

    public void setSoialmedai(String soialmedai) {
        this.soialmedai = soialmedai;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getHavework() {
        return havework;
    }

    public void setHavework(String havework) {
        this.havework = havework;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
