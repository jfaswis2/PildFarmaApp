package com.example.pildfarmaapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {


    private TextView bttn_text_crearcuenta;
    private TextView bttn_text_recuperarcontrasena;
    private Button bttn_entrar;

    private EditText editEmail;
    private EditText editContrasena;

    private String email;
    private String contrasena;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.editText_login_correo);
        editContrasena = findViewById(R.id.editText_login_contrasena);

        //Tap en el texto CREAR CUENTA
        bttn_text_crearcuenta = findViewById(R.id.bttntext_crear_cuenta_aclogin);
        bttn_text_crearcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), register.class);
                startActivity(intent);
            }
        });

        //Tap en el texto RECUPERAR CONTRASEÑA
        bttn_text_recuperarcontrasena = findViewById(R.id.bttntext_recuperar_contraseña_aclogin);
        bttn_text_recuperarcontrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), password_reset.class);
                startActivity(intent);
            }
        });

        bttn_entrar = findViewById(R.id.bttn_entrar_aclogin);
        bttn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editEmail.getText().toString().trim();
                contrasena = editContrasena.getText().toString().trim();

                if(!email.isEmpty() && !contrasena.isEmpty()){
                    loginUsuario();
                }
                else{
                    Toast.makeText(login.this,"Complete los campos",
                            Toast.LENGTH_SHORT);
                }

            }
        });



    }

    private void loginUsuario(){



        mAuth.signInWithEmailAndPassword(email,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(login.this,aplicacion_base.class));
                    finish();
                }
                else {
                    Toast.makeText(login.this,"No se pudo iniciar sesión, complete" +
                                    "los datos", Toast.LENGTH_SHORT);
                }
            }
        });


    }


}