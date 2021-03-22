package com.example.pildfarmaapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button button1;
    Button button2;

    private boolean cerrar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tap en el boton de CREAR CUENTA
        button1 = findViewById(R.id.bttn_crear_cuenta_acmain);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),register.class);
                startActivity(intent);
                finish();
            }
        });

        //Tap en el boton ENTRAR
        button2 = findViewById(R.id.bttn_entrar_acmain);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SALIR");
        builder.setMessage("¿Estas seguro que deseas salir de la aplicación?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cerrar = true;
                salirApp(cerrar);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cerrar = false;
                salirApp(cerrar);
            }
        });

        builder.create();
        builder.show();
    }

    private void salirApp(boolean cerrrar){
        if (cerrar == true){
            Toast.makeText(this,"Vuelve pronto",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }else{
            Toast.makeText(this,"Gracias por seguir aquí",Toast.LENGTH_SHORT).show();
        }
    }

}