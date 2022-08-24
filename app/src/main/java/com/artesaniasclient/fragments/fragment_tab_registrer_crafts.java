package com.artesaniasclient.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.artesaniasclient.R;
import com.artesaniasclient.controller.CraftController;
import com.artesaniasclient.interfaces.ICraft;
import com.artesaniasclient.interfaces.Updateable;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.utils.Util;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class fragment_tab_registrer_crafts extends Fragment implements ICraft, AdapterView.OnItemSelectedListener, Updateable {

    String id, name;
    String nombreimg;
    String cat = "Todos";

    View view = null;
    Craft craft;
    boolean isEditCraft = false;

    public static final int PICK_IMAGE = 111;
    private CraftController craftController;
    Button btnimagen;
    ImageView imagen;
    Uri imageUri;
    fragment_my_crafts fragment_my_crafts;
    private ProgressDialog progressDialog;

    EditText txtNameArte;
    EditText txtCantArte;
    EditText txtPrecioArte;
    EditText txtDescription;
    Spinner spinner;
    Button registerbutton;
    Button cancel;
    TextView txtTitleDesc;

    public fragment_tab_registrer_crafts(fragment_my_crafts fragment_my_crafts) {
        this.fragment_my_crafts = fragment_my_crafts;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_registrer_crafts, container, false);
        id = getArguments().getString("id");
        name = getArguments().getString("name");
        craftController = new CraftController(this);

        txtTitleDesc = view.findViewById(R.id.txtTitleDesc);
        imagen = (ImageView) view.findViewById(R.id.imgart);
        btnimagen = (Button) view.findViewById(R.id.seleccionarimg);
        registerbutton = view.findViewById(R.id.register);
        cancel = view.findViewById(R.id.cancel);
        spinner = (Spinner) view.findViewById(R.id.categoriaarte);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categoria, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        txtNameArte = view.findViewById(R.id.namearte);
        txtCantArte = view.findViewById(R.id.cantarte);
        txtPrecioArte = view.findViewById(R.id.precioarte);
        txtDescription = view.findViewById(R.id.description);

        btnimagen.setOnClickListener(v -> openGallery());

        registerbutton.setOnClickListener(v -> registrarcrafts());
        cancel.setOnClickListener(v -> cancelar());
        return view;
    }

    private void cancelar() {
        fragment_my_crafts.viewPager.setCurrentItem(0);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imagen.setImageURI(imageUri);
            @SuppressLint("Recycle") Cursor returnCursor = getContext().getContentResolver().query(imageUri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            nombreimg = returnCursor.getString(nameIndex);
        }
    }

    public void registrarcrafts() {
        if (Util.typeSuscriptionOfUser.equals("Free")) {
            if (Util.countCraftsOfUser < 5) {
                Util.countCraftsOfUser += 1;
                passregistrarcrafts();
            } else {
                Toast.makeText(getContext(),"Pásate a Primium para registrar más artesanías",Toast.LENGTH_SHORT).show();
            }
        } else {
            Util.countCraftsOfUser += 1;
            passregistrarcrafts();
        }
    }

    private void passregistrarcrafts() {
        if (craft == null) craft = new Craft();
        craft.setNamecraft(txtNameArte.getText().toString());
        craft.setCategory(cat);
        craft.setQuantity(Integer.parseInt(txtCantArte.getText().toString()));
        craft.setPrice(Double.parseDouble(txtPrecioArte.getText().toString()));
        craft.setDescription(txtDescription.getText().toString());
        craft.setCompany(id);
        craft.setUsercraftsman(Util.getUserConnect(getContext()).getEmail());
        if (!isEditCraft) {
            @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            craft.setDateregistry(date);
            craft.setImageName(nombreimg);
            //this.resetView();
        }
        progressDialog = ProgressDialog.show(getContext(), "Porfavor espere...",
                "Registrando cambios...", true, false);
        craftController.UploadFile(isEditCraft, craft, Util.getBytesImageView(imagen), getContext());
    }

    private void resetView() {
        txtTitleDesc.setText(R.string.registrarart);
        txtNameArte.setText("");
        txtCantArte.setText("");
        txtPrecioArte.setText("");
        txtDescription.setText("");
        registerbutton.setText(R.string.registrar);
        imagen.setImageResource(R.drawable.img_base);
    }

    private void action_success(){
        progressDialog.dismiss();
        if (!isEditCraft) {
            this.resetView();
        }
        fragment_my_crafts.viewPager.setCurrentItem(0);
    }

    @Override
    public void get_craft_success(ArrayList<Craft> crafts, String message) {

    }

    @Override
    public void add_craft_success(Craft craft, String message) {
        this.action_success();
    }

    @Override
    public void set_craft_success(Craft craft, String message) {
        this.action_success();
    }

    @Override
    public void delete_craft_success(Craft crafts, String message) {

    }

    @Override
    public void get_craft_by_company_success(ArrayList<Craft> crafts, String message) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cat = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        cat = "Todos";
    }

    @Override
    public void update() {
        assert getArguments() != null;
        String craftString = getArguments().getString("craftSelected");
        boolean editable = getArguments().getBoolean("editable");
        craft = new Gson().fromJson(craftString, Craft.class);
        if ((craft != null) && editable) {
            isEditCraft = true;
            Bundle bundle = getArguments();
            bundle.putString("craftSelected", null);
            bundle.putBoolean("editable", false);
            fragment_my_crafts.setArguments(Util.getBundleFusion(fragment_my_crafts.getArguments(), bundle));

            txtTitleDesc.setText(R.string.update_data_crafts);
            txtNameArte.setText(craft.getNamecraft());
            txtCantArte.setText(String.valueOf(craft.getQuantity()));
            txtPrecioArte.setText(String.valueOf(craft.getPrice()));
            txtDescription.setText(craft.getDescription());
            Picasso.get().load(craft.getImageurl()).into(imagen);
            spinner.setSelection(obtenerPosicionItem(spinner, craft.getCategory()));
            registerbutton.setText(R.string.modificar);
        } else {
            this.resetView();
        }
    }

    public static int obtenerPosicionItem(Spinner spinner, String categoria) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String categoria`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(categoria)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }
}