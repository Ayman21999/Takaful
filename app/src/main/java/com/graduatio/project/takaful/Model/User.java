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
}
