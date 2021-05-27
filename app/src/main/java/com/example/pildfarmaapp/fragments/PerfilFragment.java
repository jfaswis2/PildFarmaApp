package com.example.pildfarmaapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.activities.EditProfileActivity;
import com.example.pildfarmaapp.providers.AuthProvider;
import com.example.pildfarmaapp.providers.PostProvider;
import com.example.pildfarmaapp.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class PerfilFragment extends Fragment {

    //Declaración de variables
    View mView;
    TextView textView;
    TextView mTextInputUsername;
    TextView mTextInputNacimiento;
    TextView mTextInputCap;
    TextView mTextInputTelefono;
    ImageView mImageViewCover;
    TextView mTextInputEmail;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Asignación de variables
        mView = inflater.inflate(R.layout.fragment_perfil, container, false);
        mTextInputUsername = mView.findViewById(R.id.NombrePerfil);
        mTextInputNacimiento = mView.findViewById(R.id.FechaNacimientoPerfil);;
        mTextInputCap = mView.findViewById(R.id.CapPerfil);;
        mTextInputTelefono = mView.findViewById(R.id.NumeroPerfil);;
        mImageViewCover = mView.findViewById(R.id.circleImageProfile);;
        mTextInputEmail = mView.findViewById(R.id.EmailPerfil);;

        mTextInputCap.setText("");
        mTextInputUsername.setText("");
        mTextInputNacimiento.setText("");
        mTextInputTelefono.setText("");
        mTextInputEmail.setText("");


        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        getUser();

        textView = mView.findViewById(R.id.tv_editar);
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

    private void getUser() {
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("email")) {
                        String email = documentSnapshot.getString("email");
                        mTextInputEmail.setText(email);
                    }
                    if (documentSnapshot.contains("numero_telefono")) {
                        String phone = documentSnapshot.getString("numero_telefono");
                        mTextInputTelefono.setText(phone);
                    }
                    if (documentSnapshot.contains("username")) {
                        String username = documentSnapshot.getString("username");
                        mTextInputUsername.setText(username);
                    }
                    if (documentSnapshot.contains("image_profile")) {
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if (imageProfile != null) {
                            if (!imageProfile.isEmpty()) {
                                Picasso.with(getContext()).load(imageProfile).into(mImageViewCover);
                            }
                        }
                    }
                    if (documentSnapshot.contains("cap")) {
                        String username = documentSnapshot.getString("cap");
                        mTextInputCap.setText(username);
                    }
                    if (documentSnapshot.contains("fecha_nacimiento")) {
                        String username = documentSnapshot.getString("fecha_nacimiento");
                        mTextInputNacimiento.setText(username);
                    }
                }
            }
        });
    }
}
