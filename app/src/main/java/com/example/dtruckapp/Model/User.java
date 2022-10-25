package com.example.dtruckapp.Model;

public class User {
    private String FullName, Email, Password, PhoneNumber, DriveLicense, urlImage, Biography;

    public User() {
    }

    public User(String fullname,String email, String password, String phoneNumber, String driveLicense, String urlImage, String biography) {
        this.FullName = fullname;
        this.Email = email;
        this.Password = password;
        this.PhoneNumber = phoneNumber;
        this.DriveLicense = driveLicense;
        this.urlImage = urlImage;
        this.Biography = biography;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullname) {
        FullName = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDriveLicense() {
        return DriveLicense;
    }

    public void setDriveLicense(String driveLicense) {
        DriveLicense = driveLicense;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getBiography() {
        return Biography;
    }

    public void setBiography(String biography) {
        Biography = biography;
    }
}
