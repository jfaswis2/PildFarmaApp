package com.example.pildfarmaapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.adapters.PostsAdapter;
import com.example.pildfarmaapp.models.PostAlarma;
import com.example.pildfarmaapp.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class AlarmasActivasFragment extends Fragment {

    //Declaración de variables
    RecyclerView mRecyclerView;
    View mView;
    PostProvider mPostProvider;
    PostsAdapter mPostAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //Asignación de variables
        mView =  inflater.inflate(R.layout.fragment_alarmas, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerViewHome);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostProvider = new PostProvider();

        return mView;
    }

    //Se comprueba en la base de datos las alarmas activas y se infla la cardview
    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getAll();
        FirestoreRecyclerOptions<PostAlarma> options =
                new FirestoreRecyclerOptions.Builder<PostAlarma>().setQuery(query, PostAlarma.class)
                .build();
        mPostAdapter = new PostsAdapter(options,getContext());
        mRecyclerView.setAdapter(mPostAdapter);
        mPostAdapter.startListening();
    }

    //Para de comprobar en la base de datos
    @Override
    public void onStop() {
        super.onStop();
        mPostAdapter.stopListening();
    }
}