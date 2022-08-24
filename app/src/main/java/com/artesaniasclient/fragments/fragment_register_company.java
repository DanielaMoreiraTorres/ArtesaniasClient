package com.artesaniasclient.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artesaniasclient.R;
import com.artesaniasclient.activity_principal;
import com.artesaniasclient.controller.CompanyController;
import com.artesaniasclient.interfaces.ICompanyComunication;
import com.artesaniasclient.model.Company;
import com.artesaniasclient.utils.Util;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class fragment_register_company extends Fragment implements ICompanyComunication {

    FirebaseAuth auth;
    private CompanyController companyController;

    private EditText txtNameBussines;
    private EditText txtRuc;
    private EditText txtCity;
    private EditText txtAddress;
    Button buttonRegistry;
    Button buttonCancel;
    View view;

    public fragment_register_company() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_company, container, false);
        companyController = new CompanyController(this);
        buttonRegistry = view.findViewById(R.id.registrar_company);
        buttonCancel = view.findViewById(R.id.cancelar);
        txtNameBussines = view.findViewById(R.id.namebussines);
        txtRuc = view.findViewById(R.id.ruc);
        txtCity = view.findViewById(R.id.city);
        txtAddress = view.findViewById(R.id.address);
        auth = FirebaseAuth.getInstance();

        buttonRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registry_company();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_company();
            }
        });

        return view;
    }

    public void registry_company() {
        if (Util.typeSuscriptionOfUser.equals("Free")) {
            if (Util.countCompaniesOfUser < 1) {
                Util.countCompaniesOfUser += 1;
                passregistry_company();
            } else {
                Toast.makeText(getContext(), "Pásate a Primium para registrar más empresas", Toast.LENGTH_SHORT).show();
            }
        } else {
            Util.countCompaniesOfUser += 1;
            passregistry_company();
        }
    }

    private void passregistry_company() {
        Company company = new Company();
        company.setBusinessname(txtNameBussines.getText().toString());
        company.setRuc(txtRuc.getText().toString());
        company.setCity(txtCity.getText().toString());
        company.setAddress(txtAddress.getText().toString());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        company.setDateregistry(date);
        company.setIsactive(false);
        company.setUseremail(auth.getCurrentUser().getEmail());
        companyController.addCompany(company, getContext());
        Session session = Session.getInstance(Util.getPropertiesJavaMail(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Util.adminMail, Util.adminPassword);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Util.adminMail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Util.adminMail.trim()));
            message.setSubject("Solicitud de registro empresa: " + company.getBusinessname());
            message.setText(company.toString());
            new SendEmail().execute(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void SendRegistrationRequestCompany(String toEmail, String subject, String message) {
        // Defino mi Intent y hago uso del objeto ACTION_SEND
        Intent intent = new Intent(Intent.ACTION_SEND);
        // Defino los Strings Email, Asunto y Mensaje con la función putExtra
        intent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{toEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        // Establezco el tipo de Intent
        intent.setType("message/rfc822");
        // Lanzo el selector de cliente de Correo
        getActivity().startActivity(Intent.createChooser(intent, "Elije un cliente de Correo..."));
    }

    public void cancel_company() {
        Intent intent = new Intent(getContext(), activity_principal.class);
        startActivity(intent);
    }

    @Override
    public void add_company_success(Company c, String message) {
        if (c == null && c.getId() == null) {
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            message = "Empresa registrada exitosamente";
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), activity_principal.class);
            startActivity(intent);
        }
    }

    @Override
    public void get_company_success(ArrayList<Company> companies, String message) {

    }

    @Override
    public void delete_company_success(Company c, String message) {

    }

    @Override
    public void get_companies_by_useremail_success(ArrayList<Company> companiesList, String message) {

    }

    private class SendEmail extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(), "Porfavor espere...",
                    "Enviando email...", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s.equals("Success")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Success</font>"));
                builder.setMessage("Email enviado correctamente");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), activity_principal.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getContext(), "Hubo un error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}