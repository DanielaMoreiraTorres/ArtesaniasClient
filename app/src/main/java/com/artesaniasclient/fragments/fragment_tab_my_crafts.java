package com.artesaniasclient.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.artesaniasclient.R;
import com.artesaniasclient.adapter.adpMyCrafts;
import com.artesaniasclient.controller.CompanyController;
import com.artesaniasclient.interfaces.Updateable;
import com.artesaniasclient.model.Company;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tab_my_crafts#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tab_my_crafts extends Fragment implements AdapterView.OnItemSelectedListener, Updateable {

    String id, name;
    private FirebaseFirestore refFireStore;
    private adpMyCrafts adapter;
    CompanyController controller;
    RecyclerView rcvCrafts;
    static String cat = "Todos";
    ArrayAdapter<CharSequence> adapterCat;
    Spinner cbbCategories;
    private ArrayList<Craft> craftList;
    private ArrayList<Craft> craftList2;
    ArrayList<Company> ac;
    ArrayAdapter<CharSequence> adp;
    String[] categories = new String[11];
    Bundle bundle;
    TabLayout tabLayout;
    fragment_my_crafts fragment_my_crafts;

    public fragment_tab_my_crafts(fragment_my_crafts fragment_my_crafts) {
        this.fragment_my_crafts = fragment_my_crafts;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_my_crafts, container, false);
        id = getArguments().getString("id");
        name = getArguments().getString("name");
        //codigo de daniela
        llenarSpinner();
        craftList = new ArrayList<>();

        adapterCat = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, categories);
        cbbCategories = (Spinner) view.findViewById(R.id.cbbCategory2);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //como se muestran los datos
        cbbCategories.setAdapter(adapterCat);
        cbbCategories.setOnItemSelectedListener(this);
        //Vincular instancia del recyclerview
        rcvCrafts = (RecyclerView) view.findViewById(R.id.rcvMyCrafts);
        //Definir la forma de la lista vertical
        rcvCrafts.setLayoutManager(new LinearLayoutManager(getContext()));
        refFireStore = FirebaseFirestore.getInstance();
        ac = getCompany();
        //getAllMyCrafts();
        return view;
    }

    private void llenarSpinner() {
        categories[0] = "Todos";
        adp = ArrayAdapter.createFromResource(getContext(), R.array.categoria, android.R.layout.simple_spinner_item);
        int i = 1;
        while (i < categories.length) {
            categories[i] = (String) adp.getItem(i - 1);
            i = i + 1;
        }
    }

    private ArrayList<Company> getCompany() {
        ArrayList<Company> list = new ArrayList<>();
        refFireStore.collection("company")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //list.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                            }
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return list;
    }

    private void getCrafts() {
        if (cat.equals("Todos"))
            getAllMyCrafts();
        else
            getCraftsbyCategory();
    }

    public ArrayList<Craft> getAllMyCrafts() {
        ArrayList<Craft> list = new ArrayList<>();
        refFireStore.collection("crafts")
                .whereEqualTo("company", id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        craftList.clear();
                        for (DocumentSnapshot document : value) {
                            Craft craft = document.toObject(Craft.class);
                            craft.setId(document.getId());
                            craftList.add(craft);
                            list.add(craft);
                        }
                        adapter = new adpMyCrafts(getContext(), craftList);
                        rcvCrafts.setAdapter(adapter);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int opcselec = rcvCrafts.getChildAdapterPosition(view);
                                bundle = new Bundle();
                                Craft craftSelected = craftList.get(opcselec);
                                bundle.putString("craftSelected", new Gson().toJson(craftSelected));
                                bundle.putBoolean("editable", true);
                                fragment_my_crafts.setArguments(Util.getBundleFusion(fragment_my_crafts.getArguments(), bundle));
                                fragment_my_crafts.viewPager.setCurrentItem(1);
                            }
                        });
                    }
                });
        return list;
    }

    private void getCraftsbyCategory() {
        ArrayList<Craft> list = new ArrayList<>();
        for(int c = 0; c < craftList.size(); c++){
            Craft craftModel = craftList.get(c);
            if(craftModel.getCategory().equals(cat)){
                list.add(craftModel);
            }
        }
        adapter = new adpMyCrafts(getContext(), list);
        rcvCrafts.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int opcselec = rcvCrafts.getChildAdapterPosition(view);
                bundle = new Bundle();
                Craft craftSelected = craftList.get(opcselec);
                bundle.putString("craftSelected", new Gson().toJson(craftSelected));
                bundle.putBoolean("editable", true);
                fragment_my_crafts.setArguments(Util.getBundleFusion(fragment_my_crafts.getArguments(), bundle));
                fragment_my_crafts.viewPager.setCurrentItem(1);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cat = parent.getItemAtPosition(position).toString();
        getCrafts();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        cat = "Todos";
    }

    @Override
    public void update() {
        Bundle bundle = getArguments();
        bundle.putString("craftSelected", null);
        bundle.putBoolean("editable", false);
        fragment_my_crafts.setArguments(Util.getBundleFusion(fragment_my_crafts.getArguments(), bundle));
    }
}