package com.artesaniasclient.model;

import java.io.Serializable;

public class Craft implements Serializable {

    private String id;
    String category;
    private String company;
    private String datedisabled;
    private String dateregistry;
    private String description;
    private String imageurl;
    private boolean isactive;
    private String namecraft;
    private double price;
    private Integer quantity;
    private String imageName;
    private String usercraftsman;

    public Craft() {

    }

    public Craft(String id) {
        this.id = id;
    }

    public Craft(String id, String category, String company, String datedisabled, String dateregistry,
                 String description, String imageurl, boolean isactive, String namecraft, double price,
                 Integer quantity, String imageName, String usercraftsman) {
        this.id = id;
        this.category = category;
        this.company = company;
        this.datedisabled = datedisabled;
        this.dateregistry = dateregistry;
        this.description = description;
        this.imageurl = imageurl;
        this.isactive = isactive;
        this.namecraft = namecraft;
        this.price = price;
        this.quantity = quantity;
        this.imageName = imageName;
        this.usercraftsman = usercraftsman;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDatedisabled() {
        return datedisabled;
    }

    public void setDatedisabled(String datedisabled) {
        this.datedisabled = datedisabled;
    }

    public String getDateregistry() {
        return dateregistry;
    }

    public void setDateregistry(String dateregistry) {
        this.dateregistry = dateregistry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getNamecraft() {
        return namecraft;
    }

    public void setNamecraft(String namecraft) {
        this.namecraft = namecraft;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageName() { return imageName; }

    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getUsercraftsman() {
        return usercraftsman;
    }

    public void setUsercraftsman(String usercraftsman) {
        this.usercraftsman = usercraftsman;
    }

    @Override
    public String toString() {
        return "Craft{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", company='" + company + '\'' +
                ", datedisabled='" + datedisabled + '\'' +
                ", dateregistry='" + dateregistry + '\'' +
                ", description='" + description + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", isactive=" + isactive +
                ", namecraft='" + namecraft + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", imageName='" + imageName + '\'' +
                ", usercraftsman='" + usercraftsman + '\'' +
                '}';
    }
}
