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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class register extends AppCompatActivity {

    TextView iniciar_sesion_acregister;

    private EditText mEditTextNombre;
    private EditText mEditTextEmail;
    private EditText mEditTextContrasena;
    private EditText mEditTextConContrasena;
    private Button bttn_registrar;



    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mEditTextNombre = findViewById(R.id.editText_register_nombre);
        mEditTextEmail = findViewById(R.id.editText_register_correo);
        mEditTextContrasena = findViewById(R.id.editText_register_contrasena);
        mEditTextConContrasena = findViewById(R.id.editText_register_confirmar_contrasena);
        bttn_registrar = findViewById(R.id.bttn_crear_cuenta_acregister);

        //Tap en el boton para registrarse
        bttn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEditTextNombre.getText().toString();
                String email = mEditTextEmail.getText().toString().trim();
                String password  = mEditTextContrasena.getText().toString();
                String confirmPassword = mEditTextConContrasena.getText().toString();

                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                    if (isEmailValid(email)) {
                        if (password.equals(confirmPassword)) {
                            if (password.length() >= 6) {
                                createUser(email, password,username);
                            }
                            else {
                                Toast.makeText(v.getContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(v.getContext(), "Las contraseña no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(v.getContext(), "Insertaste todos los campos pero el correo no es valido", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(v.getContext(), "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
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
                finish();
            }
        });
    }

    private void createUser(final String email, String password, String userName) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id=mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("Name",userName);
                    map.put("Email",email);
                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(register.this, "El usuario se guardó correctamente en la base de datos", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(register.this, "El usuario no se pudo guardar en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(register.this, "El usuario se registro correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(register.this, login.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(register.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}