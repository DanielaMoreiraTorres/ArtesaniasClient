package com.artesaniasclient.controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.artesaniasclient.interfaces.ICompanyComunication;
import com.artesaniasclient.model.Company;
import com.artesaniasclient.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CompanyController {

    private final String TAG = "CompanyController";
    private ICompanyComunication iCompanyComunication;
    private FirebaseFirestore db;
    private Activity activity;
    ArrayList<Company> companiesList;
    private FirebaseAuth mAuth;

    public CompanyController() {
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public CompanyController(ICompanyComunication iCompanyComunication) {
        this.iCompanyComunication = iCompanyComunication;
        initFirebase();
    }

    public ICompanyComunication getiCompanyComunication() {
        return iCompanyComunication;
    }

    public void setiCompanyComunication(ICompanyComunication iCompanyComunication) {
        this.iCompanyComunication = iCompanyComunication;
    }

    public void setup() {
        // [START get_firestore_instance]
        db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    public void addCompany(Company company, Context context) {
        // Add a new document with a generated ID
        db.collection("company")
                .add(company)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        company.setId(documentReference.getId());
                        updateCounCompaniesOfUser(Util.getUserConnect(context).getId(), Util.countCompaniesOfUser);
                        if (company.getId() == null) {
                            getiCompanyComunication().add_company_success(null, "Error al crear la empresa");
                        } else {
                            getiCompanyComunication().add_company_success(company, "Empresa creada exitosamente");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getiCompanyComunication().add_company_success(null, getMessageTask(e));
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void getAllCompanies() {
        // [START get_all_companies]
        db.collection("company")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Company> companies = new ArrayList<>();
                            QuerySnapshot result = task.getResult();
                            assert result != null;
                            for (QueryDocumentSnapshot document : result) {
                                //Aqui vienen los datos
                                //Aqui hay que especificar como queren recibir los datos
                                Company c = document.toObject(Company.class);
                                c.setId(document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                companies.add(c);
                            }
                            getiCompanyComunication().get_company_success(companies, null);
                        } else {
                            getiCompanyComunication().get_company_success(null, getMessageTask(task.getException()));
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // [END get_all_companies]
    }

    public void getAllMyCompanies(){
        if (mAuth.getCurrentUser() != null) {
            String useremail = mAuth.getCurrentUser().getEmail();
            companiesList = new ArrayList<>();
            db.collection("company")
                    .whereEqualTo("useremail", useremail)
                    .whereEqualTo("isactive", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //companiesList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getId();
                                    String address = document.getString("address");
                                    String businessname = document.getString("businessname");
                                    String city = document.getString("city");
                                    String dateregistry = document.getString("dateregistry");
                                    boolean isactive = Boolean.parseBoolean(document.get("isactive").toString());
                                    String ruc = document.getString("ruc");
                                    String useremail = document.getString("useremail");
                                    companiesList.add(new Company(id, address, businessname, city, dateregistry, isactive, ruc, useremail));
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                iCompanyComunication.get_companies_by_useremail_success(companiesList, "");
                            /*adapter = new adpCompany(getContext(),companiesList);
                            rcvCompanies.setAdapter(adapter);*/
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                                iCompanyComunication.get_companies_by_useremail_success(null, task.getException().getMessage());
                            }
                        }
                    });
        } else {
            iCompanyComunication.get_companies_by_useremail_success(null,"Usuario no Autenticado");
        }
    }

    public void updateCounCompaniesOfUser (String iduser, int quantity) {
        db.collection("user").document(iduser)
                .update("countcompanies",quantity)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /*Log.d(TAG, "DocumentSnapshot successfully written!");
                        getiCraft().set_craft_success(new Craft(), "Artesania editada exitosamente");*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        /*Log.w(TAG, "Error writing document", e);
                        getiCraft().set_craft_success(null, Util.getMessageTask(e));*/
                    }
                });
    }

    public void deleteDocument(String idCompany) {
        // [START delete_document]
        db.collection("company").document(idCompany)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getiCompanyComunication().delete_company_success(new Company(), null);
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getiCompanyComunication().delete_company_success(null, getMessageTask(e));
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // [END delete_document]
    }

    private String getMessageTask(Exception exception) {
        String message = null;
        if (exception != null) {
            message = exception.getMessage();
        }
        return message;
    }

}
