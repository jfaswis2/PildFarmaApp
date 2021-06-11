package com.example.pildfarmaapp.providers;

import com.example.pildfarmaapp.models.PostAlarma;
import com.example.pildfarmaapp.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Clase para hacer las consultas de los medicamentos para colocarlos en las cardviews
public class PostProvider {

    //Declaracion de variables
    CollectionReference mCollection;
    AuthProvider mAuthProvider;

    public PostProvider() {
        //Asiganación de variables
        mCollection = FirebaseFirestore.getInstance().collection("Posts");
        mAuthProvider = new AuthProvider();
    }

    public Task<Void> save(PostAlarma post) {
        return mCollection.document().set(post);
    }

    public Query getFinalizados(){
        Query Medicamentos = mCollection.whereEqualTo("idusuario", mAuthProvider.getUid()).whereEqualTo("estado", "Finalizado");
        return Medicamentos;
    }

    public Query getAll(){

        Query Medicamentos = mCollection.whereEqualTo("idusuario", mAuthProvider.getUid()).whereEqualTo("estado", "Activo");

       return Medicamentos;
    }

    public Task<Void> saveBroast(PostAlarma post) {
        return mCollection.document().set(post.getBroadcaster());
    }

    public Task<Void> update(PostAlarma post) {
        Map<String, Object> map = new HashMap<>();
        map.put("Broadcaster", post.getBroadcaster());

        return mCollection.document(post.getID()).update(map);
    }
}
