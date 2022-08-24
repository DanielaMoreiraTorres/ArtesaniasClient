package com.artesaniasclient.interfaces;

import com.artesaniasclient.model.Company;

import java.util.ArrayList;

public interface ICompanyComunication {

    void add_company_success(Company c, String message);
    void get_company_success(ArrayList<Company> companies, String message);
    void delete_company_success(Company c, String message);
    void get_companies_by_useremail_success(ArrayList<Company> companiesList, String message);
}
