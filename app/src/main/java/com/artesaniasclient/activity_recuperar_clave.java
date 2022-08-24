package com.artesaniasclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artesaniasclient.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class activity_recuperar_clave extends AppCompatActivity {

    EditText correo_recuperacion;
    Button btn_recuperar;
    FirebaseAuth auth;
    String correo;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_clave);

        correo_recuperacion = (EditText) findViewById(R.id.correo);
        btn_recuperar = (Button) findViewById(R.id.btnrecuperar);
        auth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);
        getRecuperar();
    }

    private void getRecuperar() {
        btn_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = correo_recuperacion.getText().toString().trim();
                if (!correo.isEmpty()) {
                    progress.setMessage("Espere un momento...");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    EnviarCorreo();
                } else {
                    Toast.makeText(getApplicationContext(),"Error al enviar el correo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void EnviarCorreo() {
        //auth.setLanguageCode("es");
        auth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Profavor revise su correo para restaurar su clave", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity_recuperar_clave.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Error al enviar el correo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}