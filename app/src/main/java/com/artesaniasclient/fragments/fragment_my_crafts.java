package com.artesaniasclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.artesaniasclient.R;
import com.artesaniasclient.interfaces.Updateable;
import com.artesaniasclient.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class fragment_my_crafts extends Fragment {

    String id, name;
    Bundle bundle;
    SectionsPagerAdapter sectionsPagerAdapter;
    public ViewPager viewPager;
    TextView nameCompany;

    public fragment_my_crafts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_crafts, container, false);
        id = getArguments().getString("id");
        name = getArguments().getString("name");
        bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("name",name);
        //Codigo traido desde el de jose
        sectionsPagerAdapter = new SectionsPagerAdapter(view.getContext(), getChildFragmentManager(), bundle, this);
        viewPager = view.findViewById(R.id.view_pager_main);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabs_main);
        tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (sectionsPagerAdapter != null) {
                    Updateable fragment = (Updateable)sectionsPagerAdapter.getItem(tab.getPosition());
                    if (fragment != null) {
                        fragment. update();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        nameCompany = view.findViewById(R.id.lblTitleCompany);
        nameCompany.setText("Empresa: " + name);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}