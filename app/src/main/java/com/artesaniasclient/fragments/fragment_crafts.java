package com.artesaniasclient.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.artesaniasclient.R;
import com.artesaniasclient.adapter.adpCrafts;
import com.artesaniasclient.controller.CompanyController;
import com.artesaniasclient.model.Company;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.model.User;
import com.artesaniasclient.utils.Util;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_crafts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_crafts extends Fragment implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore refFireStore;
    private adpCrafts adapter;
    RecyclerView rcvCrafts;
    String cat = "Todos";
    ArrayAdapter<CharSequence> adapterCat;
    Spinner cbbCategories;
    private ArrayList<Craft> craftList;
    ArrayList<Company> ac;
    ArrayAdapter<CharSequence> adp;
    String[] categories = new String[11];
    Craft craftSelected = new Craft();
    Bundle bundle;
    String datos;
    User user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_crafts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_crafts.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_crafts newInstance(String param1, String param2) {
        fragment_crafts fragment = new fragment_crafts();
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
        View view = inflater.inflate(R.layout.fragment_crafts, container, false);
        refFireStore = FirebaseFirestore.getInstance();
        craftList = new ArrayList<>();
        ac = getCompany();
        llenarSpinner();
        adapterCat = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, categories);
        cbbCategories = (Spinner)view.findViewById(R.id.cbbCategory);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //como se muestran los datos
        cbbCategories.setAdapter(adapterCat);
        cbbCategories.setOnItemSelectedListener(this);
        //Vincular instancia del recyclerview
        rcvCrafts = (RecyclerView)view.findViewById(R.id.rcvCrafts);
        //Definir la forma de la lista vertical
        rcvCrafts.setLayoutManager(new GridLayoutManager(getContext(),2));
        // Inflate the layout for this fragment
        return view;
    }

    private void llenarSpinner(){
        categories[0] = "Todos";
        adp = ArrayAdapter.createFromResource(getContext(),R.array.categoria, android.R.layout.simple_spinner_item);
        int i = 1;
        while(i<categories.length){
            categories[i] = (String) adp.getItem(i-1);
            i=i+1;
        }
    }

    private ArrayList<Company> getCompany(){
        ArrayList<Company> list = new ArrayList<>();
        refFireStore.collection("company")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot document : value) {
                            String id = document.getId();
                            String address = document.getString("address");
                            String businessname = document.getString("businessname");
                            String city = document.getString("city");
                            String dateregistry = document.getString("dateregistry");
                            boolean isactive = Boolean.parseBoolean(document.get("isactive").toString());
                            String ruc = document.getString("ruc");
                            String useremail = document.getString("useremail");
                            list.add(new Company(id, address, businessname, city, dateregistry, isactive, ruc, useremail));
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        //adapter = new adpCrafts(getContext(),craftList);
                        //rcvCrafts.setAdapter(adapter);
                    }
                });
        return list;
    }

    private void getCrafts(){
        craftList = new ArrayList<>();
        if (cat.equals("Todos"))
            getAllCrafts();
        else
            getCraftsbyCategory();
    }

    private void getAllCrafts(){
        refFireStore.collection("crafts")
                .whereNotEqualTo("quantity",0)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        //if(craftList != null) craftList.clear();
                        for (DocumentSnapshot doc : value) {
                            if (doc.getId() != null) {
                                String idcompany = doc.getString("company");
                                String company = "";
                                for(int c = 0; c < ac.size(); c++){
                                    Company companyModel = ac.get(c);
                                    if(companyModel.getId().equals(idcompany)){
                                        company = companyModel.getBusinessname();
                                    }
                                }
                                Craft craft = doc.toObject(Craft.class);
                                craft.setId(doc.getId());
                                craft.setCompany(company);
                                craftList.add(craft);
                            }
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        adapter = new adpCrafts(getContext(),craftList);
                        rcvCrafts.setAdapter(adapter);
                        /*adapter = new adpCrafts(getContext(), craftList);
                        rcvCrafts.setAdapter(adapter);*/
                        if (getArguments() != null) {
                            user = new User();
                            datos = getArguments().getString("datos");
                            user = new Gson().fromJson(datos, User.class);
                        }
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int opcselec = rcvCrafts.getChildAdapterPosition(view);
                                craftSelected = craftList.get(opcselec);
                                if (user != null) {
                                    if (user.getUsertype().equals("Cliente")) {
                                        bundle = new Bundle();
                                        bundle.putString("craftSelec", new Gson().toJson(craftSelected));

                                        // Crea el nuevo fragmento y la transacción.
                                        Fragment nuevoFragmento = new fragmet_detail_craft();
                                        nuevoFragmento.setArguments(bundle);
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction()
                                                .replace(R.id.content, nuevoFragmento);
                                        // Commit a la transacción
                                        transaction.commit();
                                    }
                                }
                            }
                        });
                    }
                });
    }

    private void getCraftsbyCategory(){
        refFireStore.collection("crafts")
                .whereEqualTo("category", cat)
                .whereNotEqualTo("quantity",0)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                        }
                        else {
                        //craftList.clear();
                        for (DocumentSnapshot doc : value) {
                            if (doc.getId() != null) {
                                String idcompany = doc.getString("company");
                                String company = "";
                                for(int c = 0; c < ac.size(); c++){
                                    Company companyModel = ac.get(c);
                                    if(companyModel.getId().equals(idcompany)){
                                        company = companyModel.getBusinessname();
                                    }
                                }
                                Craft craft = doc.toObject(Craft.class);
                                craft.setId(doc.getId());
                                craft.setCompany(company);
                                craftList.add(craft);
                            }
                        }}
                        adapter = new adpCrafts(getContext(), craftList);
                        rcvCrafts.setAdapter(adapter);
                        if (getArguments() != null) {
                            user = new User();
                            datos = getArguments().getString("datos");
                            user = new Gson().fromJson(datos, User.class);
                        }
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int opcselec = rcvCrafts.getChildAdapterPosition(view);
                                craftSelected = craftList.get(opcselec);
                                if (user != null) {
                                    if (user.getUsertype().equals("Cliente")) {
                                        bundle = new Bundle();
                                        bundle.putString("craftSelec", new Gson().toJson(craftSelected));

                                        // Crea el nuevo fragmento y la transacción.
                                        Fragment nuevoFragmento = new fragmet_detail_craft();
                                        nuevoFragmento.setArguments(bundle);
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction()
                                                .replace(R.id.content, nuevoFragmento);
                                        // Commit a la transacción
                                        transaction.commit();
                                    }
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cat = adapterView.getItemAtPosition(i).toString();
        getCrafts();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {cat = "Todos"; }

}