package com.example.dtruckapp.Model;

public class Order {
    private String orderuid;
    private String dataTimePost;
    private String orderUserImg;
    private String orderNameUser;
    private String orderNumberPhone;
    private String orderDesc;
    private String orderCarRequired;
    private String orderCategory;

    public Order() {
    }

    public Order(String orderuid,String dataTimePost, String orderUserImg, String orderNameUser, String orderNumberPhone, String orderDesc,String orderCarRequired,String orderCategory) {
        this.orderuid = orderuid;
        this.dataTimePost = dataTimePost;
        this.orderUserImg = orderUserImg;
        this.orderNameUser = orderNameUser;
        this.orderNumberPhone = orderNumberPhone;
        this.orderDesc = orderDesc;
        this.orderCarRequired = orderCarRequired;
        this.orderCategory = orderCategory;
    }

    public String getOrderuid() {
        return orderuid;
    }

    public void setOrderuid(String orderuid) {
        this.orderuid = orderuid;
    }

    public String getDataTimePost() {
        return dataTimePost;
    }

    public void setDataTimePost(String dataTimePost) {
        this.dataTimePost = dataTimePost;
    }

    public String getOrderUserImg() {
        return orderUserImg;
    }

    public void setOrderUserImg(String orderUserImg) {
        this.orderUserImg = orderUserImg;
    }

    public String getOrderNameUser() {
        return orderNameUser;
    }

    public void setOrderNameUser(String orderNameUser) {
        this.orderNameUser = orderNameUser;
    }

    public String getOrderNumberPhone() {
        return orderNumberPhone;
    }

    public void setOrderNumberPhone(String orderNumberPhone) {
        this.orderNumberPhone = orderNumberPhone;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderCarRequired() {
        return orderCarRequired;
    }

    public void setOrderCarRequired(String orderCarRequired) {
        this.orderCarRequired = orderCarRequired;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }
}
