package com.artesaniasclient.model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String address;
    private String dateregistry;
    private String email;
    private boolean isactive;
    private String lastname;
    private String firstname;
    private String username;
    private String password;
    private String phone;
    private String usertype;
    private String suscriptiontype;
    private int countcrafts;
    private int countcompanies;
    private String datepayment;
    private int countmonth;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String address, String dateregistry, String email, boolean isactive, String lastname, String firstname, String username, String password, String phone, String usertype, String suscriptiontype, int countcrafts, int countcompanies, String datepayment, int countmonth) {
        this.id = id;
        this.address = address;
        this.dateregistry = dateregistry;
        this.email = email;
        this.isactive = isactive;
        this.lastname = lastname;
        this.firstname = firstname;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.usertype = usertype;
        this.suscriptiontype = suscriptiontype;
        this.countcrafts = countcrafts;
        this.countcompanies = countcompanies;
        this.datepayment = datepayment;
        this.countmonth = countmonth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateregistry() {
        return dateregistry;
    }

    public void setDateregistry(String dateregistry) {
        this.dateregistry = dateregistry;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getSuscriptiontype() {
        return suscriptiontype;
    }

    public void setSuscriptiontype(String suscriptiontype) {
        this.suscriptiontype = suscriptiontype;
    }

    public int getCountcrafts() {
        return countcrafts;
    }

    public void setCountcrafts(int countcrafts) {
        this.countcrafts = countcrafts;
    }

    public int getCountcompanies() {
        return countcompanies;
    }

    public void setCountcompanies(int countcompanies) {
        this.countcompanies = countcompanies;
    }

    public String getDatepayment() {
        return datepayment;
    }

    public void setDatepayment(String datepayment) {
        this.datepayment = datepayment;
    }

    public int getCountmonth() {
        return countmonth;
    }

    public void setCountmonth(int countmonth) {
        this.countmonth = countmonth;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", dateregistry='" + dateregistry + '\'' +
                ", email='" + email + '\'' +
                ", isactive=" + isactive +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", usertype='" + usertype + '\'' +
                ", suscriptiontype='" + suscriptiontype + '\'' +
                ", countcrafts=" + countcrafts +
                ", countcompanies=" + countcompanies +
                ", datepayment='" + datepayment + '\'' +
                ", countmonth=" + countmonth +
                '}';
    }
}
