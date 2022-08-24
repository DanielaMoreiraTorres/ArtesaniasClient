package com.artesaniasclient.controller;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.artesaniasclient.interfaces.ILogin;
import com.artesaniasclient.interfaces.IUserComunication;
import com.artesaniasclient.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserController {

    private final String TAG = "UserController";
    private IUserComunication iUserComunication;
    private ILogin iLogin;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Activity activity;

    public UserController() {
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    public void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public UserController(ILogin iLogin) {
        this.iLogin = iLogin;
        initFirebase();
    }

    public UserController(IUserComunication iUserComunication) {
        this.iUserComunication = iUserComunication;
        initFirebase();
    }

    public UserController(IUserComunication iUserComunication, ILogin iLogin) {
        this.iUserComunication = iUserComunication;
        this.iLogin = iLogin;
        initFirebase();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public IUserComunication getiUserComunication() {
        return iUserComunication;
    }

    public void setiUserComunication(IUserComunication iUserComunication) {
        this.iUserComunication = iUserComunication;
    }

    public ILogin getiLogin() {
        return iLogin;
    }

    public void setiLogin(ILogin iLogin) {
        this.iLogin = iLogin;
    }

    public void setup() {
        // [START get_firestore_instance]
        db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    public void addUser(@NotNull User user) {
        // Add a new document with a generated ID
        db.collection("user")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        user.setId(documentReference.getId());
                        if (user.getId() == null) {
                            getiUserComunication().add_user_success(null, "Error al crear un usuario");
                        } else {
                            getiUserComunication().add_user_success(user, "Usuario creado exitosamente");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getiUserComunication().add_user_success(null, getMessageTask(e));
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void createUserFirebaseAuth(@NotNull User userCreate) {
        mAuth.createUserWithEmailAndPassword(userCreate.getEmail(), userCreate.getPassword())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            addUser(userCreate);
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            getiUserComunication().add_user_success(null, getMessageTask(task.getException()));
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                        // ...
                    }
                });
    }

    public void updatePasswordOfUser(String iduser, String newPassword) {
        db.collection("user").document(iduser)
                .update("password", newPassword)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /*Log.d(TAG, "DocumentSnapshot successfully written!");
                        getiCraft().set_craft_success(new Craft(), "Artesania editada exitosamente");*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        /*Log.w(TAG, "Error writing document", e);
                        getiCraft().set_craft_success(null, Util.getMessageTask(e));*/
                    }
                });
    }

    public void updateUser(User user) {
        db.collection("user").document(user.getId())
            .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      iUserComunication.set_user_success(user,"Se han actualizado los datos correctamente");
                  }
              }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iUserComunication.set_user_success(null,"Error al actualizar los datos");
                }
            });
    }

    public void getUserForEmail(@NotNull String email) {
        // [START listen_for_users]
        // Listen for users born before 1900.
        //
        // You will get a first snapshot with the initial results and a new
        // snapshot each time there is a change in the results.
        db.collection("user")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        assert result != null;
                        DocumentSnapshot docSnap = result.getDocuments().get(0);
                        User user = docSnap.toObject(User.class);
                        assert user != null;
                        user.setId(docSnap.getId());
                        getiLogin().login(user, null);
                    } else {
                        getiLogin().login(null, getMessageTask(task.getException()));
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        // [END listen_for_users]
    }

    public void getAllUsers() {
        // [START get_all_users]
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<User> users = new ArrayList<>();
                            QuerySnapshot result = task.getResult();
                            assert result != null;
                            for (QueryDocumentSnapshot document : result) {
                                //Aqui vienen los datos
                                //Aqui hay que especificar como queren recibir los datos
                                User u = document.toObject(User.class);
                                u.setId(document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                users.add(u);
                            }
                            getiUserComunication().get_users_success(users, null);
                        } else {
                            getiUserComunication().get_users_success(null, getMessageTask(task.getException()));
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // [END get_all_users]
    }

    public void deleteDocument(String idUser) {
        // [START delete_document]
        db.collection("user").document(idUser)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getiUserComunication().delete_user_success(new User(), null);
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getiUserComunication().delete_user_success(null, getMessageTask(e));
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // [END delete_document]
    }

    public void updatePaymentData (String iduser, String datepayment, int countmonth, String typesuscryption) {
        Map<String, Object> data = new HashMap<>();
        data.put("datepayment", datepayment);
        data.put("countmonth", countmonth);
        data.put("suscriptiontype", typesuscryption);
        db.collection("user").document(iduser)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /*Log.d(TAG, "DocumentSnapshot successfully written!");
                        getiCraft().set_craft_success(new Craft(), "Artesania editada exitosamente");*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        /*Log.w(TAG, "Error writing document", e);
                        getiCraft().set_craft_success(null, Util.getMessageTask(e));*/
                    }
                });
    }

    public String getMessageTask(Exception exception) {
        String message = null;
        if (exception != null) {
            message = exception.getMessage();
        }
        return message;
    }
}
