package com.artesaniasclient.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artesaniasclient.R;
import com.artesaniasclient.controller.CraftController;
import com.artesaniasclient.controller.OrderController;
import com.artesaniasclient.interfaces.ICraft;
import com.artesaniasclient.interfaces.IOrder;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.model.Order;
import com.artesaniasclient.utils.Util;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmet_detail_craft#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmet_detail_craft extends Fragment implements IOrder, ICraft {

    Craft craf;
    String craftSelec;

    OrderController orderController;
    CraftController craftController;

    ImageView imgCraft;
    TextView txtCraftCat;
    TextView txtCraftName;
    TextView txtCrafQuantity;
    TextView txtCraftPrice;
    TextView txtCraftDescription;
    EditText txtCantidadRequerida;
    Button btnComprar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmet_detail_craft() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmet_detail_craft.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmet_detail_craft newInstance(String param1, String param2) {
        fragmet_detail_craft fragment = new fragmet_detail_craft();
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

    static String auxUserCraftsman = "";
    static String auxCraftId = "";
    @Override
    public void onStart() {
        super.onStart();
        txtCraftCat.setText(craf.getCategory());
        txtCraftName.setText(craf.getNamecraft());
        txtCrafQuantity.setText(craf.getQuantity().toString());
        txtCraftPrice.setText(String.valueOf(craf.getPrice()));
        txtCraftDescription.setText(craf.getDescription());
        Picasso.get().load(craf.getImageurl()).into(imgCraft);
        auxUserCraftsman = craf.getUsercraftsman();
        auxCraftId = craf.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_craft, container, false);

        orderController = new OrderController(this);
        craftController = new CraftController(this);

        imgCraft = view.findViewById(R.id.imgartesania);
        txtCraftCat = view.findViewById(R.id.catartesania);
        txtCraftName = view.findViewById(R.id.nameartesania);
        txtCrafQuantity = view.findViewById(R.id.cantartesania);
        txtCraftPrice = view.findViewById(R.id.precioartesania);
        txtCraftDescription = view.findViewById(R.id.description);
        txtCantidadRequerida = view.findViewById(R.id.cantidadrequerida);
        btnComprar = view.findViewById(R.id.comprar);

        craf = new Craft();
        craftSelec = getArguments().getString("craftSelec");
        craf = new Gson().fromJson(craftSelec, Craft.class);

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarYregistrarOrder();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void hacerPedido() {
        Order order = new Order();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        order.setOrderdate(date);
        order.setOrdertime(new SimpleDateFormat("HH:MM:SS").format( new Date()));
        order.setState("Pendiente");
        order.setUsercraftsman(auxUserCraftsman);
        order.setUserclient(Util.getUserConnect(getContext()).getEmail());
        order.setCraft(auxCraftId);
        order.setPrice(Double.parseDouble(txtCraftPrice.getText().toString()) * Integer.parseInt(txtCantidadRequerida.getText().toString()));
        order.setQuantity(Integer.parseInt(txtCantidadRequerida.getText().toString()));
        craftController.updateSubtractQuantityCrafts(auxCraftId,Integer.parseInt(txtCrafQuantity.getText().toString()) - Integer.parseInt(txtCantidadRequerida.getText().toString()));
        orderController.addOrder(order);
    }

    public void validarYregistrarOrder () {
        if (!txtCantidadRequerida.getText().toString().equals("")) {
            if (Integer.parseInt(txtCantidadRequerida.getText().toString()) > 0 && Integer.parseInt(txtCantidadRequerida.getText().toString()) <= Integer.parseInt(txtCrafQuantity.getText().toString())) {
                hacerPedido();
            } else {
                Toast.makeText(getContext(),"Su valor es menor o igual a 0, o pide mas de lo que hay en Stock",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(),"Debe especificar la cantidad que requiere",Toast.LENGTH_SHORT).show();
        }
    }

    //Regresar a ver catálogo
    public void regresar(View view) {
        /* FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
        Fragment fragment = fragment_crafts.newInstance("","");
        this.getFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .addToBackStack(null) .commit(); */
    }

    @Override
    public void get_order_success(ArrayList<Order> order, String message) {

    }

    @Override
    public void add_order_success(Order order, String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
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

    @Override
    public void get_craft_success(ArrayList<Craft> crafts, String message) {

    }

    @Override
    public void add_craft_success(Craft craft, String message) {

    }

    @Override
    public void set_craft_success(Craft craft, String message) {

    }

    @Override
    public void delete_craft_success(Craft crafts, String message) {

    }

    @Override
    public void get_craft_by_company_success(ArrayList<Craft> crafts, String message) {

    }
}