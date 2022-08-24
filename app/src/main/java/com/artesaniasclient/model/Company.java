package com.artesaniasclient.model;

import java.io.Serializable;

public class Company implements Serializable {

    private String id;
    private String address;
    private String businessname;
    private String city;
    private String dateregistry;
    private boolean isactive;
    private String ruc;
    private String useremail;

    public Company() {
    }

    public Company(String id) {
        this.id = id;
    }

    public Company(String id, String address, String businessname, String city, String dateregistry, boolean isactive, String ruc, String useremail) {
        this.id = id;
        this.address = address;
        this.businessname = businessname;
        this.city = city;
        this.dateregistry = dateregistry;
        this.isactive = isactive;
        this.ruc = ruc;
        this.useremail = useremail;
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

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDateregistry() {
        return dateregistry;
    }

    public void setDateregistry(String dateregistry) {
        this.dateregistry = dateregistry;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", businessname='" + businessname + '\'' +
                ", city='" + city + '\'' +
                ", dateregistry='" + dateregistry + '\'' +
                ", isactive=" + isactive +
                ", ruc='" + ruc + '\'' +
                ", useremail='" + useremail + '\'' +
                '}';
    }

    public String DataHtml() {
        return "<h3>" + this.businessname + "</h3>" +
                "<ul>" +
                "<li>id: " + id + "</li>" +
                "<li>address: " + address + "</li>" +
                "<li>city: " + city + "</li>" +
                "<li>dateregistry: " + dateregistry + "</li>" +
                "<li>isactive: " + isactive + "</li>" +
                "<li>ruc: " + ruc + "</li>" +
                "<li>useremail: " + useremail + "</li>" +
                "</ul>";
    }
}
