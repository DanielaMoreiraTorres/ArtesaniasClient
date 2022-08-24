package com.artesaniasclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.artesaniasclient.model.Contacts;

import java.util.ArrayList;
import com.artesaniasclient.adapter.adpContact;
import com.artesaniasclient.ui.login.LoginActivity;

import java.util.List;

public class activity_contacts extends AppCompatActivity {

    RecyclerView rcvContacts;
    Toolbar toolbar;
    ImageView imgToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        imgToolbar = findViewById(R.id.imgToolbar);

        imgToolbar.setImageResource(R.drawable.iconarte2);
        imgToolbar.setPadding(5,5,5,5);
        imgToolbar.setTranslationX(-16);

        rcvContacts = findViewById(R.id.rcvContacts);
        LinearLayoutManager linear = new LinearLayoutManager(getApplicationContext());
        linear.setOrientation(LinearLayoutManager.VERTICAL);
        rcvContacts.setLayoutManager(linear);
        rcvContacts.hasFixedSize();

        adpContact.showShimmer = true;
        List<Contacts> list = new ArrayList<Contacts>();
        adpContact adpContact = new adpContact(list);
        rcvContacts.setAdapter(adpContact);

        Contacts c1 = new Contacts("Burbano Parraga Cristhian","+593980395656","cristhian.burbano2016@uteq.edu.ec");
        Contacts c2 = new Contacts("García López Jose","+593979094938","jose.garcial2016@uteq.edu.ec");
        Contacts c3 = new Contacts("Cheves Caicedo Kevin","+593979094938","kevin.cheves2016@uteq.edu.ec");
        Contacts c4 = new Contacts("Moreira Torres Daniela","+593979094938","daniela.moreira2015@uteq.edu.ec");

        final List<Contacts> finalList = new ArrayList<Contacts>();
        finalList.add(c1);
        finalList.add(c2);
        finalList.add(c3);
        finalList.add(c4);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adpContact adpContacts = new adpContact(finalList);
                rcvContacts.setAdapter(adpContacts);
                adpContacts.showShimmer = false;
                adpContacts.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnLogIn) {
            Intent intent = new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        if(id == R.id.btnContacts) {
            Intent intent = new Intent(this, activity_contacts.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}