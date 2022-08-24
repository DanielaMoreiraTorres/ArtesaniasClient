package com.artesaniasclient.interfaces;

import com.artesaniasclient.model.Craft;

import java.util.ArrayList;

public interface ICraft {
    void get_craft_success(ArrayList<Craft> crafts, String message);
    void add_craft_success(Craft craft, String message);
    void set_craft_success(Craft craft, String message);
    void delete_craft_success(Craft crafts, String message);
    void get_craft_by_company_success(ArrayList<Craft> crafts, String message);
}
