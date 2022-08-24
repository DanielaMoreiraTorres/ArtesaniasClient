package com.artesaniasclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.artesaniasclient.fragments.fragment_crafts;
import com.artesaniasclient.fragments.fragment_my_companies;
import com.artesaniasclient.fragments.fragment_my_info;
import com.artesaniasclient.fragments.fragment_my_orders;
import com.artesaniasclient.fragments.fragment_my_sales;
import com.artesaniasclient.fragments.fragment_pasar_premium;
import com.artesaniasclient.fragments.fragment_register_company;
import com.artesaniasclient.model.User;
import com.artesaniasclient.ui.login.LoginActivity;
import com.artesaniasclient.utils.Util;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class activity_principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Bundle bundle = new Bundle();
    Toolbar toolbar;
    NavigationView navView;
    ImageView imgToolbar;
    DrawerLayout drawerLayout;
    Fragment fragment;
    boolean fragmentTransaction;
    TextView txtUserFooter;
    ImageView imgTipoSuscripcion;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    User user = null;
    static int groupMenu = 1;
    final int itemMyInfo = 1, itemVerCatalogo = 2, itemCrearEmpresa = 3, itemPedidos = 4, itemCrearArtesania = 5, itemVerMisEmpresas = 5, itemVentas = 6, itemVerMisArtesanias = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Intent i = getIntent();
        //user = (User) i.getSerializableExtra("user");
        txtUserFooter = findViewById(R.id.txtUserFooter);
        imgTipoSuscripcion = findViewById(R.id.profile_image);

        toolbar = findViewById(R.id.toolbar);
        imgToolbar = findViewById(R.id.imgToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_menu);
        getSupportActionBar().setTitle("Catálogo");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_crafts);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        fragmentTransaction = false;
        fragment = null;
        String titulo = "";
        switch (menuItem.getItemId()) {
            case itemMyInfo:
                fragment = new fragment_my_info();
                fragment.setArguments(bundle);
                fragmentTransaction = true;
                titulo = "Mi Información";
                break;
            case itemVerCatalogo:
                fragment = new fragment_crafts();
                fragment.setArguments(bundle);
                fragmentTransaction = true;
                titulo = "Catálogo";
                break;
            case itemCrearEmpresa:
                fragment = new fragment_register_company();
                fragmentTransaction = true;
                titulo = "Registrar Empresa";
                break;
            case itemPedidos:
                fragment = new fragment_my_orders();
                fragmentTransaction = true;
                titulo = "Mis Pedidos";
                break;
            case itemVerMisEmpresas:
                fragment = new fragment_my_companies();
                fragmentTransaction = true;
                titulo = "Mis Empresas";
                break;
            case itemVentas:
                fragment = new fragment_my_sales();
                fragmentTransaction = true;
                titulo = "Mis Ventas";
                break;
        }
        if (fragmentTransaction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            menuItem.setChecked(true);
            getSupportActionBar().setTitle(titulo);
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        String email = "";
        Menu m = navView.getMenu();
        m.removeGroup(1);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            email = currentUser.getDisplayName();
            user = Util.getUserConnect(getApplicationContext());
            /*Gson gson = new Gson();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String userJSON = sharedPreferences.getString(getString(R.string.CURRENT_USER_KEY_STORE), gson.toJson(new User()));
            user = gson.fromJson(userJSON, User.class);*/
            Util.typeSuscriptionOfUser = user.getSuscriptiontype();
            Util.countCraftsOfUser = user.getCountcrafts();
            Util.countCompaniesOfUser = user.getCountcompanies();
            bundle.putString("datos", new Gson().toJson(user));
            fragment = new fragment_crafts();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            if (user != null) {
                txtUserFooter.setText("Usuario: " + user.getUsername());
                if (user.getUsertype() != null && user.getUsertype().equals("Artesano")) {
                    if (user.getSuscriptiontype().equals("Premium")) {
                        imgTipoSuscripcion.setImageResource(R.drawable.icon_premium);
                    }
                    m.add(groupMenu, itemMyInfo, itemMyInfo, "Mi Información").setIcon(R.drawable.icon_my_info);
                    m.add(groupMenu, itemVerCatalogo, itemVerCatalogo, "Ver Catálogo").setIcon(R.drawable.icon_catalogue);
                    m.add(groupMenu, itemCrearEmpresa, itemCrearEmpresa, "Registrar Empresa").setIcon(R.drawable.icon_addcompany);
                    m.add(groupMenu, itemVerMisEmpresas, itemVerMisEmpresas, "Mis Empresas").setIcon(R.drawable.icon_bussiness);
                    m.add(groupMenu, itemVentas, itemVentas, "Mis Ventas").setIcon(R.drawable.icon_sales);
                } else {
                    m.add(groupMenu, itemMyInfo, itemMyInfo, "Mi Información").setIcon(R.drawable.icon_my_info);
                    m.add(groupMenu, itemVerCatalogo, itemVerCatalogo, "Ver Catálogo").setIcon(R.drawable.icon_catalogue);
                    m.add(groupMenu, itemCrearEmpresa, itemCrearEmpresa, "Registrar Empresa").setIcon(R.drawable.icon_addcompany);
                    m.add(groupMenu, itemPedidos, itemPedidos, "Mis Pedidos").setIcon(R.drawable.icon_orders);
                }
            } else {
                logout();
            }
        } else {   //en caso de no estar logeado se deshabilita el navigation view
            getSupportActionBar().setTitle("");
            imgToolbar.setImageResource(R.drawable.iconarte2);
            imgToolbar.setPadding(5, 5, 5, 5);
            imgToolbar.setTranslationX(-16);
            fragment = new fragment_crafts();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            toolbar.setNavigationIcon(null);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        //redirectActivity(currentUser);
        //updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem ml = menu.findItem(R.id.btnLogIn);
        if (user != null && user.getId() != null && user.getId().length() > 0) {
            ml.setIcon(R.drawable.icon_logout);
            ml.setTitle("Cerrar Sesión");
        } else {
            ml.setIcon(R.drawable.icon_login);
            ml.setTitle("Iniciar Sesión");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        if (id == R.id.btnLogIn) {
            if (user != null && user.getId() != null && user.getId().length() > 0) {
                logout();
            } else {
                Intent intent = new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }
        if (id == R.id.btnContacts) {
            Intent intent = new Intent(this, activity_contacts.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getString(R.string.CURRENT_USER_KEY_STORE));
        editor.apply();
        //Intent intent = new Intent(this, activity_principal.class);
        //startActivity(intent);
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case fragment_pasar_premium.LOAD_PAYMENT_DATA_REQUEST_CODE:
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}