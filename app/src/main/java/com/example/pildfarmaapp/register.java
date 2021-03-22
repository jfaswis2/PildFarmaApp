package com.example.pildfarmaapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class register extends AppCompatActivity {

    TextView iniciar_sesion_acregister;

    private EditText mEditTextNombre;
    private EditText mEditTextEmail;
    private EditText mEditTextContrasena;
    private EditText mEditTextConContrasena;
    private Button bttn_registrar;

    private String nombre="";
    private String email="";
    private String contrasena="";
    private String concontrasena="";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextNombre = findViewById(R.id.editText_register_nombre);
        mEditTextEmail = findViewById(R.id.editText_register_correo);
        mEditTextContrasena = findViewById(R.id.editText_register_contrasena);
        mEditTextConContrasena = findViewById(R.id.editText_register_confirmar_contrasena);
        bttn_registrar = findViewById(R.id.bttn_crear_cuenta_acregister);

        //Tap en el boton para registrarse
        bttn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = mEditTextNombre.getText().toString().trim();
                email = mEditTextEmail.getText().toString().trim();
                contrasena = mEditTextContrasena.getText().toString().trim();
                concontrasena = mEditTextConContrasena.getText().toString().trim();

                if (!nombre.isEmpty() && !email.isEmpty() && !contrasena.isEmpty()){
                    if (contrasena.length()>=6){
                        registrarUsuario();
                    }
                    else{
                        Toast.makeText(register.this,"La contraseña debe de tener " +
                                "al menos 6 caracteres",Toast.LENGTH_SHORT);

                    }
                }
                else{
                    Toast.makeText(register.this,"Debe completar los campos"
                    ,Toast.LENGTH_SHORT);
                }

            }
        });


        //Tap en el texto INICIAR SESION
        iniciar_sesion_acregister = findViewById(R.id.bttntext_iniciar_sesion_acregister);
        iniciar_sesion_acregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), login.class);
                startActivity(intent);
            }
        });
    }

    private void registrarUsuario(){
        mAuth.createUserWithEmailAndPassword(email,contrasena).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Map<String,Object> map = new HashMap<>();
                    map.put("Nombre", nombre);
                    map.put("Email", email);
                    map.put("Contrasena", contrasena);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(register.this,login.class));
                                finish();
                            }
                            else{
                                Toast.makeText(register.this,"No se pudieron crear los datos" +
                                                "correctamente"
                                        ,Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(register.this,"No se pudo registrar este usuario"
                            ,Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}