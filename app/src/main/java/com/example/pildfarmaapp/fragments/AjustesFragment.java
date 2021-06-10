package com.example.pildfarmaapp.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.activities.LoginActivity;
import com.example.pildfarmaapp.providers.AuthProvider;

import dmax.dialog.SpotsDialog;

public class AjustesFragment extends Fragment {

    //Declaración de variables
    View mView;
    CardView cardView;
    AuthProvider MAuthlogin;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    AlertDialog dialog2;
    Button aceptar,cancelar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Asignación de variables
        mView = inflater.inflate(R.layout.fragment_ajustes, container, false);
        cardView = mView.findViewById(R.id.cardViewCerrarSesion);
        MAuthlogin = new AuthProvider();
        dialog2 = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newConfirmDialog();
            }
        });

        return mView;
    }

    //Confirmación de cerrar sesión
    private void newConfirmDialog(){
        dialogBuilder = new AlertDialog.Builder(mView.getContext());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.fragment_veri_cerrar_sesion,null);
        aceptar = contactPopupView.findViewById(R.id.bttn_aceptar_cerrar_sesion);
        cancelar = contactPopupView.findViewById(R.id.bttn_cancelar_cerrar_sesion);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                dialog.dismiss();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //Cerrar sesión
    private void logout(){
        dialog2.show();
        MAuthlogin.logout();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        dialog2.dismiss();
        startActivity(intent);
    }
}
