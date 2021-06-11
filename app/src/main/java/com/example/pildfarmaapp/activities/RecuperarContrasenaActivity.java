package com.example.pildfarmaapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    //Declaración de variables
    TextView textView;
    Button recuperarContrasena;
    EditText correo;
    private String email="";
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        correo = findViewById(R.id.editText_recuperar_cuenta_correo);
        mAuth = FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);

        recuperarContrasena = findViewById(R.id.btn_recuperarContrasena);
        recuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = correo.getText().toString();

                if(!email.isEmpty()){
                    mDialog.setMessage("Espere un momento");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                }else{
                    Toast.makeText(RecuperarContrasenaActivity.this, "Debe de ingresar un correo", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Tap en el texto INICIAR SESION
        textView = findViewById(R.id.bttntext_iniciar_sesion_acrecuperar_contrasena);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(RecuperarContrasenaActivity.this, "Se ha enviado un correo para restablecer su contraseña", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(RecuperarContrasenaActivity.this, "No se pudo restablecer la contraseña", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();
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