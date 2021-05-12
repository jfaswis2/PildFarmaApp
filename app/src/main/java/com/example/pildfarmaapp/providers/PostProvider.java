package com.example.pildfarmaapp.providers;

import com.example.pildfarmaapp.models.PostAlarma;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Posts");
    }

    public Task<Void> save(PostAlarma post) {
        return mCollection.document().set(post);
    }
}
