package com.artesaniasclient.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.artesaniasclient.R;
import com.artesaniasclient.controller.CraftController;
import com.artesaniasclient.controller.OrderController;
import com.artesaniasclient.interfaces.ICraft;
import com.artesaniasclient.interfaces.IOrder;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.model.Order;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_detail_order#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_detail_order extends Fragment implements IOrder, ICraft {

    TextView txtNombreArtesania;
    TextView txtCantidad;
    TextView txtPrecio;
    TextView txtFecha;
    Button btnComprar;
    Button btnCancelar;
    Order order;
    static String oderSelec;
    static String namecraft = "";
    static String numberUser = "";
    static int cantidadCraft = 0;

    OrderController orderController;
    CraftController craftController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_detail_order() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_detail_order.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_detail_order newInstance(String param1, String param2) {
        fragment_detail_order fragment = new fragment_detail_order();
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
    public void onStart() {
        super.onStart();
        txtNombreArtesania.setText(namecraft);
        txtCantidad.setText(order.getQuantity().toString());
        txtPrecio.setText(order.getPrice().toString());
        txtFecha.setText(order.getOrderdate());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_order, container, false);
        txtNombreArtesania = view.findViewById(R.id.nombreartesania);
        txtCantidad = view.findViewById(R.id.cantidad);
        txtPrecio = view.findViewById(R.id.precio);
        txtFecha = view.findViewById(R.id.fecha);
        btnComprar = view.findViewById(R.id.comprar);
        btnCancelar = view.findViewById(R.id.cancelar);

        orderController = new OrderController(this);
        craftController = new CraftController(this);
        order = new Order();
        oderSelec = getArguments().getString("oderSelec");
        order = new Gson().fromJson(oderSelec, Order.class);
        namecraft = getArguments().getString("namecraft");
        numberUser = getArguments().getString("numberUser");
        cantidadCraft = getArguments().getInt("cantidadcraft");

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp(numberUser, "Hola! He realizado un pedido en ARTESANÍAS ECUADOR, con el detalle:\n\n" +
                        "CÓDIGO PEDIDO: " + order.getId() +
                        "\nFECHA: " + order.getOrderdate() +
                        "\nARTESANÍA: " + namecraft +
                        "\nCANTIDAD: " + order.getQuantity() +
                        "\nVALOR TOTAL: $" + order.getPrice() +
                        "\n\nCUENTA ARTESANÍAS ECUADOR:\n" + order.getUserclient());
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrders();
            }
        });

        return view;
    }

    private void openWhatsApp(String numero, String mensaje) {
        try{
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=+593 "+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            //i.putExtra(Intent.EXTRA_STREAM, Uri.parse("https://firebasestorage.googleapis.com/v0/b/artesanias-304016.appspot.com/o/images%2F36070IMG-20210207-WA0000.jpeg?alt=media&token=81b5233c-94d7-4a4e-b8bd-faade8fa3ef8"));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Toast.makeText(getContext(), "Error NO whatsapp", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            System.out.println("ERROR WHATSAPP" + e.toString());
            Toast.makeText(getContext(), "Error NO whatsapp", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelOrders () {
        orderController.UpdateOrderByCancellation(order.getId());
        craftController.updateSubtractQuantityCrafts(order.getCraft(), cantidadCraft + Integer.parseInt(txtCantidad.getText().toString()));
        Toast.makeText(getContext(), "Pedido cancelado", Toast.LENGTH_SHORT).show();
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