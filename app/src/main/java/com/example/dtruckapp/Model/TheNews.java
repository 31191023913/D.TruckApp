package com.example.dtruckapp.Model;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

public class TheNews {
    private String IDnew;
    private String tenTT;
    private String ngayCN;
    private String tt;
    private String detailTT;
    private String imgurl;

    public TheNews() {
    }

    public TheNews(String IDnew,String tentt, String ngayCN, String tt, String detailTT, String imgurl) {
        this.IDnew = IDnew;
        this.tenTT = tentt;
        this.ngayCN = ngayCN;
        this.tt = tt;
        this.detailTT = detailTT;
        this.imgurl = imgurl;
    }

    public String getIDnew() {
        return IDnew;
    }

    public void setIDnew(String IDnew) {
        this.IDnew = IDnew;
    }

    public String getTenTT() {
        return tenTT;
    }

    public void setTenTT(String tenTT) {
        this.tenTT = tenTT;
    }

    public String getNgayCN() {
        return ngayCN;
    }

    public void setNgayCN(String ngayCN) {
        this.ngayCN = ngayCN;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getDetailTT() {
        return detailTT;
    }

    public void setDetailTT(String detailTT) {
        this.detailTT = detailTT;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
