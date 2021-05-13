package com.example.pildfarmaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class Alarmas_Activas_Fragment extends Fragment {

    RecyclerView mRecyclerView;
    View mView;
    PostProvider mPostProvider;
    PostAdapter mPostAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mView =  inflater.inflate(R.layout.fragment_alarmas, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerViewHome);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostProvider = new PostProvider();
        // Inflate the layout for this fragment

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getAll();
        FirestoreRecyclerOptions<DatosAlarma> options =
                new FirestoreRecyclerOptions.Builder<DatosAlarma>().setQuery(query, DatosAlarma.class)
                .build();
        mPostAdapter = new PostAdapter(options,getContext());
        mRecyclerView.setAdapter(mPostAdapter);
        mPostAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostAdapter.stopListening();
    }
}