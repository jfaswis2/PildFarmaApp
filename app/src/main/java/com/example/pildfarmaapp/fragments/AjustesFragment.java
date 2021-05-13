package com.example.pildfarmaapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ajustes_fragment extends Fragment {
    View mView;
    CardView cardView;
    login MAuthlogin;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Button aceptar,cancelar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_ajustes, container, false);
        cardView = mView.findViewById(R.id.cardViewCerrarSesion);
        MAuthlogin = new login();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newConfirmDialog();
            }
        });

        // Inflate the layout for this fragment

        return mView;
    }

    private void newConfirmDialog(){
        dialogBuilder = new AlertDialog.Builder(mView.getContext());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.fragment_veri_cerrar_sesion,null);
        aceptar = (Button) contactPopupView.findViewById(R.id.bttn_aceptar_cerrar_sesion);
        cancelar = (Button) contactPopupView.findViewById(R.id.bttn_cancelar_cerrar_sesion);

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

    private void logout(){
        MAuthlogin.logout();
        Intent intent = new Intent(getContext(),login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
