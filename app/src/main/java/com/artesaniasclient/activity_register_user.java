package com.artesaniasclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.artesaniasclient.controller.UserController;
import com.artesaniasclient.interfaces.IUserComunication;
import com.artesaniasclient.model.User;
import com.artesaniasclient.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Date;

public class activity_register_user extends AppCompatActivity implements IUserComunication {

    private UserController userController;

    private EditText textFistName;
    private EditText textLastName;
    private EditText textUsername;
    private EditText textPassword;
    private EditText textPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        userController = new UserController(this);
        userController.initFirebaseAuth();
        userController.setActivity(this);

        textFistName = findViewById(R.id.firstname);
        textLastName = findViewById(R.id.lastname);
        textUsername = findViewById(R.id.username);
        textPassword = findViewById(R.id.password);
        textPhone = findViewById(R.id.phone);


    }

    public void onRegisterUserClick(View v) {
        User user = new User();
        user.setFirstname(textFistName.getText().toString());
        user.setLastname(textLastName.getText().toString());
        user.setUsername(textUsername.getText().toString());
        user.setPassword(textPassword.getText().toString());
        user.setPhone(textPhone.getText().toString());
        user.setEmail(user.getUsername());
        user.setSuscriptiontype("Free");
        user.setIsactive(true);
        user.setUsertype("Cliente");
        user.setDateregistry(new Date().toString());
        user.setCountcrafts(0);
        user.setCountcompanies(0);
        userController.createUserFirebaseAuth(user);
    }

    public void onCancelActivityClick(View view) {
        redirect_login();
    }

    @Override
    public void get_users_success(ArrayList<User> users, String message) {
    }

    @Override
    public void add_user_success(User u, String message) {
        if (u == null && u.getId() == null) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            message = "Usuario registrado exitosamente";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            redirect_login();
        }
    }

    private void redirect_login() {
        Intent intent = new Intent(activity_register_user.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void set_user_success(User u, String message) {

    }

    @Override
    public void delete_user_success(User u, String message) {

    }
}