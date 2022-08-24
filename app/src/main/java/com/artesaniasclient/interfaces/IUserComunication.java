package com.artesaniasclient.interfaces;

import com.artesaniasclient.model.User;

import java.util.ArrayList;

public interface IUserComunication {

    void get_users_success(ArrayList<User> users, String message);
    void add_user_success(User u, String message);
    void set_user_success(User u, String message);
    void delete_user_success(User u, String message);
}
