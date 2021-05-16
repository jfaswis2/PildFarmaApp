package com.example.pildfarmaapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.activities.LoginActivity;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    //Declaración de variables
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        //Tap en el texto RECUPERAR CONTRASEÑA
        textView = findViewById(R.id.bttntext_iniciar_sesion_acrecuperar_contrasena);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //Acción del boton hacia atras
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}