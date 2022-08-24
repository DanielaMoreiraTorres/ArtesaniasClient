package com.artesaniasclient.model;

public class Order {
    private String id;
    private String deliverydate;
    private String deliverytime;
    private String observations;
    private String orderdate;
    private String ordertime;
    private String state;
    private String usercraftsman;
    private String userclient;
    private String craft;
    private Double price;
    private Integer quantity;

    public Order() {
    }

    public Order(String id, String deliverydate, String deliverytime, String observations, String orderdate, String ordertime, String state, String usercraftsman, String userclient, String craft, Double price, Integer quantity) {
        this.id = id;
        this.deliverydate = deliverydate;
        this.deliverytime = deliverytime;
        this.observations = observations;
        this.orderdate = orderdate;
        this.ordertime = ordertime;
        this.state = state;
        this.usercraftsman = usercraftsman;
        this.userclient = userclient;
        this.craft = craft;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getDeliverytime() {
        return deliverytime;
    }

    public void setDeliverytime(String deliverytime) {
        this.deliverytime = deliverytime;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsercraftsman() {
        return usercraftsman;
    }

    public void setUsercraftsman(String usercraftsman) {
        this.usercraftsman = usercraftsman;
    }

    public String getUserclient() {
        return userclient;
    }

    public void setUserclient(String userclient) {
        this.userclient = userclient;
    }

    public String getCraft() {
        return craft;
    }

    public void setCraft(String craft) {
        this.craft = craft;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", deliverydate='" + deliverydate + '\'' +
                ", deliverytime='" + deliverytime + '\'' +
                ", observations='" + observations + '\'' +
                ", orderdate='" + orderdate + '\'' +
                ", ordertime='" + ordertime + '\'' +
                ", state='" + state + '\'' +
                ", usercraftsman='" + usercraftsman + '\'' +
                ", userclient='" + userclient + '\'' +
                ", craft='" + craft + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
