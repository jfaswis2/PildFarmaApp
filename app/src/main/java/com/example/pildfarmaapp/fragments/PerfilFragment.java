package com.example.pildfarmaapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.activities.EditProfileActivity;

public class PerfilFragment extends Fragment {

    //Declaración de variables
    View mView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Asignación de variables
        textView = mView.findViewById(R.id.tv_editar);
        mView = inflater.inflate(R.layout.fragment_perfil, container, false);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfile();
            }
        });

        return mView;
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }
}
