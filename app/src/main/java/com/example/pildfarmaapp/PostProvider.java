package com.example.pildfarmaapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Posts");
    }

    public Task<Void> save(DatosAlarma post){ return mCollection.document().set(post);
    }

    public Query getAll(){
        return mCollection.orderBy("medicamento",Query.Direction.DESCENDING);
    }
}
