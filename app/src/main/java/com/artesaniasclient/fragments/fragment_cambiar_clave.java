package com.artesaniasclient.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.artesaniasclient.R;
import com.artesaniasclient.controller.UserController;
import com.artesaniasclient.model.User;
import com.artesaniasclient.ui.login.LoginActivity;
import com.artesaniasclient.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_cambiar_clave#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_cambiar_clave extends Fragment {

    User user;
    TextView txtUserEmail;
    EditText txtClaveActual;
    EditText txtClaveNueva;
    EditText txtRepClave;
    Button btnCambiarClave;
    FirebaseUser uss;
    UserController userController;
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_cambiar_clave() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_cambiar_clave.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_cambiar_clave newInstance(String param1, String param2) {
        fragment_cambiar_clave fragment = new fragment_cambiar_clave();
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
        txtUserEmail.setText(user.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambiar_clave, container, false);;
        userController = new UserController();
        mAuth = FirebaseAuth.getInstance();
        user = Util.getUserConnect(getContext());
        /*String datos = getArguments().getString("datos");
        user = new Gson().fromJson(datos, User.class);*/
        txtUserEmail = view.findViewById(R.id.email);
        txtClaveActual = view.findViewById(R.id.claveactual);
        txtClaveNueva = view.findViewById(R.id.clavenueva);
        txtRepClave = view.findViewById(R.id.repnueva);
        btnCambiarClave = view.findViewById(R.id.fcambiarclave);

        btnCambiarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comparacionYcambio(uss, txtClaveNueva.getText().toString(), txtClaveActual.getText().toString(), user);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void updatePassword(FirebaseUser user, String newPassword, User userclas) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userController.updatePasswordOfUser(userclas.getId(), newPassword);
                            mAuth.signOut();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(getString(R.string.CURRENT_USER_KEY_STORE));
                            editor.apply();
                            Toast.makeText(getContext(),"Clave de usuario actualizada",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void comparacionYcambio(FirebaseUser user, String newPassword, String claveActual, User us) {
        if (us.getPassword().equals(claveActual)){
            if (txtRepClave.getText().toString().equals(newPassword)) {
                updatePassword(user, newPassword, us);
            } else {
                Toast.makeText(getContext(),"La nueva clave no coincide",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(),"La clave que ha escrito no es la suya",Toast.LENGTH_SHORT).show();
        }
    }

}