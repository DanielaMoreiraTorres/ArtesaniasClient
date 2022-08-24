package com.artesaniasclient.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.artesaniasclient.R;
import com.artesaniasclient.adapter.adpMyOrders;
import com.artesaniasclient.adapter.adpMySales;
import com.artesaniasclient.controller.OrderController;
import com.artesaniasclient.interfaces.IOrder;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.model.Order;
import com.artesaniasclient.model.User;
import com.artesaniasclient.utils.Util;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_my_sales#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_my_sales extends Fragment implements AdapterView.OnItemSelectedListener, IOrder {

    private FirebaseFirestore refFireStore;
    private adpMySales adapter;
    RecyclerView rcvSales;
    private ArrayList<Order> orderList;
    ArrayList<Craft> craft;
    ArrayList<User> users;
    ArrayAdapter<CharSequence> adp;
    Spinner spinner;
    String state = "Todos";
    OrderController orderController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_my_sales() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_my_sales.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_my_sales newInstance(String param1, String param2) {
        fragment_my_sales fragment = new fragment_my_sales();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sales, container, false);

        orderController = new OrderController(this);
        //Vincular instancia del recyclerview
        rcvSales = (RecyclerView) view.findViewById(R.id.rcvSales);
        //Definir la forma de la lista vertical
        rcvSales.setLayoutManager(new LinearLayoutManager(getContext()));
        refFireStore = FirebaseFirestore.getInstance();
        orderList = new ArrayList<>();
        spinner = view.findViewById(R.id.cbbState);
        adp = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp.add("Todos");
        adp.add("Pendiente");
        adp.add("Finalizado");
        //adp.add("Cancelado");
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(this);
        //users = getUsers();
        craft = getCraft();
        //getAllMySales();
        // Inflate the layout for this fragment
        return view;
    }

    private void getSales(){
        if (state.equals("Todos")){
            getAllMySales();
        }
        else {
            getMySalesByState();
        }
    }

    public ArrayList<Craft> getCraft(){
        ArrayList<Craft> list = new ArrayList<>();
        refFireStore.collection("crafts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            if (doc.getId() != null) {
                                Craft craft = doc.toObject(Craft.class);
                                craft.setId(doc.getId());
                                list.add(craft);
                            }
                        }
                    }
                });
        return list;
    }

    public void getAllMySales(){
        refFireStore.collection("orders")
                .whereEqualTo("usercraftsman", Util.getUserConnect(getContext()).getEmail())
                .whereNotEqualTo("state","Cancelado")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        if(orderList != null) orderList.clear();
                        for (DocumentSnapshot doc : value) {
                            if (doc.getId() != null) {
                                Order ord = doc.toObject(Order.class);
                                ord.setId(doc.getId());
                                orderList.add(ord);
                            }
                        }
                        adapter = new adpMySales(getContext(), orderList, craft);
                        rcvSales.setAdapter(adapter);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int itemSelect = rcvSales.getChildAdapterPosition(v);
                                Order o = orderList.get(itemSelect);
                                //o.setState("Enviado");
                                //llamar mètdo update state
                                if (o.getState().equals("Pendiente")) {
                                    orderController.updateStateOrder(o.getId());
                                    spinner.setSelection(2);
                                } else {
                                    Toast.makeText(getContext(), "La orden ya se finalizó anteriormente", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }

    private void getMySalesByState(){
        ArrayList<Order> order = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++){
            Order ord = orderList.get(i);
            if (orderList.get(i).getState().equals(state)){
                order.add(ord);
            }
        }
        adapter = new adpMySales(getContext(), order, craft);
        rcvSales.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemSelect = rcvSales.getChildAdapterPosition(view);
                Order o = order.get(itemSelect);
                //o.setState("Finalizado");
                //llamar método update state
                if (o.getState().equals("Pendiente")) {
                    orderController.updateStateOrder(o.getId());
                    spinner.setSelection(2);
                } else {
                    Toast.makeText(getContext(), "La orden ya se finalizó anteriormente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        state = adapterView.getItemAtPosition(i).toString();
        getSales();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void get_order_success(ArrayList<Order> order, String message) {

    }

    @Override
    public void add_order_success(Order order, String message) {

    }

    @Override
    public void set_order_success(Order order, String message) {

    }

    @Override
    public void delete_order_success(Order order, String message) {

    }

    @Override
    public void get_order_by_user_success(ArrayList<Order> order, String message) {

    }
}