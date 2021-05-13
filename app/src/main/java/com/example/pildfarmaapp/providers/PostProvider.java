package com.example.pildfarmaapp.providers;

import com.example.pildfarmaapp.models.PostAlarma;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PostProvider {

    CollectionReference mCollection;
    AuthProvider mAuthProvider;

    public PostProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Posts");
        mAuthProvider = new AuthProvider();
    }

    public Task<Void> save(PostAlarma post) {
        return mCollection.document().set(post);
    }

    public Query getAll(){

        Query Medicamentos = mCollection.whereEqualTo("idusuario", mAuthProvider.getUid());

       return mCollection.whereEqualTo("estado", "Activo").whereEqualTo("idusuario", mAuthProvider.getUserSession());
    }
}
