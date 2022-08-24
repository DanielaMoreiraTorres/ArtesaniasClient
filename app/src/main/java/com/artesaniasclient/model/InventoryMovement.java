package com.artesaniasclient.model;

public class InventoryMovement {

    private String id;
    private String craft;
    private String datemovement;
    private String movementtype;
    private Integer quantity;
    private String user;
    private String direcction;

    public InventoryMovement() {
    }

    public InventoryMovement(String id) {
        this.id = id;
    }

    public InventoryMovement(String id, String craft, String datemovement, String movementtype, Integer quantity, String user) {
        this.id = id;
        this.craft = craft;
        this.datemovement = datemovement;
        this.movementtype = movementtype;
        this.quantity = quantity;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCraft() {
        return craft;
    }

    public void setCraft(String craft) {
        this.craft = craft;
    }

    public String getDatemovement() {
        return datemovement;
    }

    public void setDatemovement(String datemovement) {
        this.datemovement = datemovement;
    }

    public String getMovementtype() {
        return movementtype;
    }

    public void setMovementtype(String movementtype) {
        this.movementtype = movementtype;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "InventoryMovement{" +
                "id='" + id + '\'' +
                ", craft='" + craft + '\'' +
                ", datemovement='" + datemovement + '\'' +
                ", movementtype='" + movementtype + '\'' +
                ", quantity=" + quantity +
                ", user='" + user + '\'' +
                '}';
    }
}
