package com.example.dtruckapp.Model;

public class OrderReceive {
    private String orderuid,orderNamePublisher, dataTimePost, orderNumberPhone, orderCarProvived, orderDesc, orderCategory,orderTakenBy;

    public OrderReceive(){

    }

    public OrderReceive(String orderuid, String orderNamePublisher, String dataTimePost, String orderNumberPhone, String orderCarProvived, String orderDesc, String orderCategory, String orderTakenBy) {
        this.orderuid = orderuid;
        this.orderNamePublisher = orderNamePublisher;
        this.dataTimePost = dataTimePost;
        this.orderNumberPhone = orderNumberPhone;
        this.orderCarProvived = orderCarProvived;
        this.orderDesc = orderDesc;
        this.orderCategory = orderCategory;
        this.orderTakenBy = orderTakenBy;
    }


    public String getOrderuid() {
        return orderuid;
    }

    public void setOrderuid(String orderuid) {
        this.orderuid = orderuid;
    }

    public String getOrderNamePublisher() {
        return orderNamePublisher;
    }

    public void setOrderNamePublisher(String orderNamePublisher) {
        this.orderNamePublisher = orderNamePublisher;
    }

    public String getDataTimePost() {
        return dataTimePost;
    }

    public void setDataTimePost(String dataTimePost) {
        this.dataTimePost = dataTimePost;
    }

    public String getOrderNumberPhone() {
        return orderNumberPhone;
    }

    public void setOrderNumberPhone(String orderNumberPhone) {
        this.orderNumberPhone = orderNumberPhone;
    }

    public String getOrderCarProvived() {
        return orderCarProvived;
    }

    public void setOrderCarProvived(String orderCarProvived) {
        this.orderCarProvived = orderCarProvived;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getOrderTakenBy() {
        return orderTakenBy;
    }

    public void setOrderTakenBy(String orderTakenBy) {
        this.orderTakenBy = orderTakenBy;
    }
}
