package com.example.pildfarmaapp.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.models.User;
import com.example.pildfarmaapp.providers.AuthProvider;
import com.example.pildfarmaapp.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    //Declaración de variables
    TextView iniciar_sesion_acregister;

    private EditText mEditTextNombre;
    private EditText mEditTextEmail;
    private EditText mEditTextContrasena;
    private EditText mEditTextConContrasena;
    private Button bttn_registrar;


    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Asignación de variables
        mEditTextNombre = findViewById(R.id.editText_register_nombre);
        mEditTextEmail = findViewById(R.id.editText_register_correo);
        mEditTextContrasena = findViewById(R.id.editText_register_contrasena);
        mEditTextConContrasena = findViewById(R.id.editText_register_confirmar_contrasena);
        bttn_registrar = findViewById(R.id.bttn_crear_cuenta_acregister);
        iniciar_sesion_acregister = findViewById(R.id.bttntext_iniciar_sesion_acregister);
        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        //Tap en el boton para registrarse
        bttn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        //Tap en el texto INICIAR SESION
        iniciar_sesion_acregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Comprueba campos de registro
    private void register(){
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
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Las contraseña no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Insertaste todos los campos pero el correo no es valido", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    //Crea el usuario (sin google)
    private void createUser(final String email, String password, String userName) {

        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuthProvider.getUid();

                    User user = new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(userName);

                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                mAuthProvider.logout();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "No se pudo almacenar el usuario en la base de datos",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Se comprueba si el email es valido
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Acción del boton hacia atras
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}