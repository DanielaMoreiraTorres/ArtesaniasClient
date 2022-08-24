package com.artesaniasclient.controller;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.artesaniasclient.interfaces.ICompanyComunication;
import com.artesaniasclient.interfaces.IOrder;
import com.artesaniasclient.model.Company;
import com.artesaniasclient.model.Order;
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

public class OrderController {

    private IOrder iOrder;
    private final String TAG = "OrderController";
    private FirebaseFirestore db;
    private Activity activity;
    private FirebaseAuth mAuth;
    ArrayList<Order> orderArrayList;

    public OrderController() {
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

    public OrderController(IOrder iOrder) {
        this.iOrder = iOrder;
        initFirebase();
    }

    public IOrder getiOrder() {
        return iOrder;
    }

    public void setiOrder(IOrder iOrder) {
        this.iOrder = iOrder;
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

    public void addOrder (Order order) {
        db.collection("orders")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        order.setId(documentReference.getId());
                        if (order.getId() == null) {
                            getiOrder().add_order_success(null, "Error al registrar el pedido");
                        } else {
                            getiOrder().add_order_success(order, "Pedido registrado exitosamente");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getiOrder().add_order_success(null, getMessageTask(e));
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void getAllMyOrders () {
        if (mAuth.getCurrentUser() != null) {
            String useremail = mAuth.getCurrentUser().getEmail();
            orderArrayList = new ArrayList<>();
            db.collection("orders")
                    .whereEqualTo("userclient", useremail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //companiesList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getId();
                                    String deliverydate = document.getString("deliverydate");
                                    String deliverytime = document.getString("deliverytime");
                                    String observations = document.getString("observations");
                                    String orderdate = document.getString("orderdate");
                                    String ordertime = document.getString("ordertime");
                                    String state = document.getString("state");
                                    String usercraftsman = document.getString("usercraftsman");
                                    String userclient = document.getString("userclient");
                                    String craft = document.getString("craft");
                                    Double price = Double.parseDouble(document.getString("price"));
                                    Integer quantity = Integer.parseInt(document.getString("quantity"));
                                    orderArrayList.add(new Order(id, deliverydate, deliverytime, observations, orderdate, ordertime, state, usercraftsman, userclient, craft, price, quantity));
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                iOrder.get_order_by_user_success(orderArrayList, "");
                            /*adapter = new adpCompany(getContext(),companiesList);
                            rcvCompanies.setAdapter(adapter);*/
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                                iOrder.get_order_by_user_success(null, task.getException().getMessage());
                            }
                        }
                    });
        } else {
            iOrder.get_order_by_user_success(null, "Usuario no encontrado");
        }
    }

    public void updateStateOrder (String idorder) {
        db.collection("orders").document(idorder)
                .update("state","Finalizado")
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

    public void UpdateOrderByCancellation (String idorder) {
        db.collection("orders").document(idorder)
                .update("state","Cancelado")
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

    private String getMessageTask(Exception exception) {
        String message = null;
        if (exception != null) {
            message = exception.getMessage();
        }
        return message;
    }

}
