package com.artesaniasclient.interfaces;

import com.artesaniasclient.model.Order;

import java.util.ArrayList;

public interface IOrder {
    void get_order_success(ArrayList<Order> order, String message);
    void add_order_success(Order order, String message);
    void set_order_success(Order order, String message);
    void delete_order_success(Order order, String message);
    void get_order_by_user_success(ArrayList<Order> order, String message);
}
