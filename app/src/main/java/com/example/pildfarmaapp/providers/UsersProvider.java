package com.example.pildfarmaapp.providers;

import com.example.pildfarmaapp.models.PostAlarma;
import com.example.pildfarmaapp.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Clase provee los usuarios
public class UsersProvider {

    //Declaración de variables
    private CollectionReference mCollection;

    public UsersProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id) {
        return mCollection.document(id).get();
    }

    public Task<Void> create(User user) {
        return mCollection.document(user.getId()).set(user);
    }

    public Task<Void> update(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("numero_telefono", user.getNumeroTel());
        map.put("timestamp", new Date().getTime());
        map.put("image_profile", user.getImageProfile());
        map.put("fecha_nacimiento",user.getFechaNacimiento());
        map.put("cap", user.getCap());

        return mCollection.document(user.getId()).update(map);
    }

}
