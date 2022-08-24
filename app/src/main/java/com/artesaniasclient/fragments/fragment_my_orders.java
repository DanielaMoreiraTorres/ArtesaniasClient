package com.artesaniasclient.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.artesaniasclient.R;
import com.artesaniasclient.adapter.adpCrafts;
import com.artesaniasclient.adapter.adpMyOrders;
import com.artesaniasclient.adapter.adpMySales;
import com.artesaniasclient.model.Company;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.model.Order;
import com.artesaniasclient.model.User;
import com.artesaniasclient.utils.Util;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_my_orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_my_orders extends Fragment implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore refFireStore;
    private adpMyOrders adapter;
    RecyclerView rcvOrders;
    private ArrayList<Order> orderList;
    ArrayList<Craft> craft;
    ArrayList<User> users;
    ArrayAdapter<CharSequence> adp;
    Spinner spinner;
    String state = "Todos";
    Bundle bundle;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_my_orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_my_orders.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_my_orders newInstance(String param1, String param2) {
        fragment_my_orders fragment = new fragment_my_orders();
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
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        //Vincular instancia del recyclerview
        rcvOrders = (RecyclerView) view.findViewById(R.id.rcvOrders);
        //Definir la forma de la lista vertical
        rcvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        refFireStore = FirebaseFirestore.getInstance();
        orderList = new ArrayList<>();
        users = getUsers();
        craft = getCraft();
        spinner = view.findViewById(R.id.cbbState2);
        adp = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp.add("Todos");
        adp.add("Pendiente");
        adp.add("Finalizado");
        adp.add("Cancelado");
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(this);

        //getAllMyOrders();
        // Inflate the layout for this fragment
        return view;
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

    public ArrayList<User> getUsers(){
        ArrayList<User> list = new ArrayList<>();
        refFireStore.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            if (doc.getId() != null) {
                                User user = doc.toObject(User.class);
                                user.setId(doc.getId());
                                list.add(user);
                            }
                        }
                    }
                });
        return list;
    }

    private void getOrders(){
        if (state.equals("Todos")){
            getAllMyOrders();
        }
        else {
            getMyOrdersByState();
        }
    }

    public void getAllMyOrders(){
        refFireStore.collection("orders")
                .whereEqualTo("userclient", Util.getUserConnect(getContext()).getEmail())
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
                                //ord.setCraft(namecraft);
                                orderList.add(ord);
                            }
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        adapter = new adpMyOrders(getContext(), orderList, craft);
                        rcvOrders.setAdapter(adapter);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int itemSelect = rcvOrders.getChildAdapterPosition(v);
                                Order o = orderList.get(itemSelect);

                                if (o.getState().equals("Pendiente")) {
                                    String namecraft = "";
                                    String numberUser = "";
                                    int cantidadcraft = 0;
                                    for (int c = 0; c < craft.size(); c++) {
                                        Craft craftModel = craft.get(c);
                                        if (craftModel.getId().equals(o.getCraft())) {
                                            namecraft = craftModel.getNamecraft();
                                            cantidadcraft = craftModel.getQuantity();
                                        }
                                    }

                                    for (int c = 0; c < users.size(); c++) {
                                        User userModel = users.get(c);
                                        if (userModel.getEmail().equals(o.getUsercraftsman())) {
                                            numberUser = userModel.getPhone();
                                        }
                                    }
                                    bundle = new Bundle();
                                    bundle.putString("oderSelec", new Gson().toJson(o));
                                    bundle.putString("namecraft", namecraft);
                                    bundle.putString("numberUser", numberUser);
                                    bundle.putInt("cantidadcraft",cantidadcraft);
                                    // Crea el nuevo fragmento y la transacción.
                                    Fragment nuevoFragmento = new fragment_detail_order();
                                    nuevoFragmento.setArguments(bundle);
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction()
                                            .replace(R.id.content, nuevoFragmento);
                                    // Commit a la transacción
                                    transaction.commit();
                                } else {
                                    Toast.makeText(getContext(), "El producto ya fue entregado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }

    private void getMyOrdersByState(){
        ArrayList<Order> order = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++){
            Order ord = orderList.get(i);
            if (orderList.get(i).getState().equals(state)){
                order.add(ord);
            }
        }
        adapter = new adpMyOrders(getContext(), order, craft);
        rcvOrders.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemSelect = rcvOrders.getChildAdapterPosition(view);
                Order o = order.get(itemSelect);

                if (o.getState().equals("Pendiente")) {
                    String namecraft = "";
                    String numberUser = "";
                    int cantidadcraft = 0;
                    for (int c = 0; c < craft.size(); c++) {
                        Craft craftModel = craft.get(c);
                        if (craftModel.getId().equals(o.getCraft())) {
                            namecraft = craftModel.getNamecraft();
                            cantidadcraft = craftModel.getQuantity();
                        }
                    }

                    for (int c = 0; c < users.size(); c++) {
                        User userModel = users.get(c);
                        if (userModel.getEmail().equals(o.getUsercraftsman())) {
                            numberUser = userModel.getPhone();
                        }
                    }
                    bundle = new Bundle();
                    bundle.putString("oderSelec", new Gson().toJson(o));
                    bundle.putString("namecraft", namecraft);
                    bundle.putString("numberUser", numberUser);
                    bundle.putInt("cantidadcraft",cantidadcraft);
                    // Crea el nuevo fragmento y la transacción.
                    Fragment nuevoFragmento = new fragment_detail_order();
                    nuevoFragmento.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction()
                            .replace(R.id.content, nuevoFragmento);
                    // Commit a la transacción
                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "El producto ya fue entregado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        state = adapterView.getItemAtPosition(i).toString();
        getOrders();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        state = "Todos";
    }
}