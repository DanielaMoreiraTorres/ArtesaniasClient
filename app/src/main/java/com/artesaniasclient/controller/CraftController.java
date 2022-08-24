package com.artesaniasclient.controller;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.artesaniasclient.interfaces.ICraft;
import com.artesaniasclient.model.Craft;
import com.artesaniasclient.model.User;
import com.artesaniasclient.utils.Util;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class CraftController {

    private final String TAG = "CraftController";
    private ICraft iCraftComunication;
    private FirebaseFirestore db;
    private ArrayList<Craft> craftList;

    public CraftController() {
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    public CraftController(ICraft iCraftComunication) {
        this.iCraftComunication = iCraftComunication;
        initFirebase();
    }

    public ICraft getiCraft() {
        return iCraftComunication;
    }

    public void setiCraft(ICraft iCraftComunication) {
        this.iCraftComunication = iCraftComunication;
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

    public void addCraft(Craft craft, Context context) {
        // Add a new document with a generated ID
        db.collection("crafts")
                .add(craft)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        craft.setId(documentReference.getId());
                        updateCounCraftsOfUser(Util.getUserConnect(context).getId(), Util.countCraftsOfUser);
                        if (craft.getId() == null) {
                            getiCraft().add_craft_success(null, "Error al crear la artesania");
                        } else {
                            getiCraft().add_craft_success(craft, "Artesania creada exitosamente");

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getiCraft().add_craft_success(null, Util.getMessageTask(e));
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void UploadFile(boolean isEditCraft, Craft craft, byte[] imageBytes, Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String UriImage = "images/" + craft.getImageName();
        StorageReference riversRef = storageRef.child(UriImage);

        UploadTask uploadTask = riversRef.putBytes(imageBytes);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    craft.setImageurl(downloadUri.toString());
                    if (isEditCraft) edit_craft(craft);
                    else addCraft(craft, context);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void edit_craft(Craft craft) {
        db.collection("crafts").document(craft.getId())
                .set(craft)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        getiCraft().set_craft_success(new Craft(), "Artesania editada exitosamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        getiCraft().set_craft_success(null, Util.getMessageTask(e));
                    }
                });
    }

    public void updateCounCraftsOfUser (String iduser, int quantity) {
        db.collection("user").document(iduser)
                .update("countcrafts",quantity)
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

    public void updateSubtractQuantityCrafts (String idcraft, int quantity) {
        db.collection("crafts").document(idcraft)
                .update("quantity",quantity)
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

    public void updateQuantityCraftsByCancellation (String idcraft, int quantity) {
        db.collection("crafts").document(idcraft)
                .update("quantity",quantity)
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

    public void getAllMyCrafts(String id) {
        db.collection("crafts")
                .whereEqualTo("company", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            craftList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Craft craft = document.toObject(Craft.class);
                                craft.setId(document.getId());
                                craftList.add(craft);
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            iCraftComunication.get_craft_by_company_success(craftList, "");
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                            iCraftComunication.get_craft_by_company_success(null, "No existen artesanías en la empresa");
                        }
                    }
                });
    }

}
