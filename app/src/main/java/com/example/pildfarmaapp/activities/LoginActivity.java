package com.example.pildfarmaapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.models.User;
import com.example.pildfarmaapp.providers.AuthProvider;
import com.example.pildfarmaapp.providers.UsersProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    //Declaración de variables
    private TextView bttn_text_crearcuenta;
    private TextView bttn_text_recuperarcontrasena;
    private Button bttn_entrar;
    private EditText editEmail;
    private EditText editContrasena;

    private AuthProvider mAuthProvider;
    private SignInButton mButtonGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private UsersProvider mUsersProvider;
    private final int REQUEST_CODE_GOOGLE = 1;
    private boolean Cerrar = false;

    android.app.AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Asignación de variables
        editEmail = findViewById(R.id.editText_login_correo);
        editContrasena = findViewById(R.id.editText_login_contrasena);
        mButtonGoogle = findViewById(R.id.bttn_identificarse_google_aclogin);
        bttn_text_crearcuenta = findViewById(R.id.bttntext_crear_cuenta_aclogin);
        bttn_text_recuperarcontrasena = findViewById(R.id.bttntext_recuperar_contraseña_aclogin);
        bttn_entrar = findViewById(R.id.bttn_entrar_aclogin);

        mAuthProvider = new AuthProvider();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mUsersProvider = new UsersProvider();

        mButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signGoogle();
            }
        });

        //Tap en el texto CREAR CUENTA
        bttn_text_crearcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        //Tap en el texto RECUPERAR CONTRASEÑA//TODO
        bttn_text_recuperarcontrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecuperarContrasenaActivity.class);
                startActivity(intent);
            }
        });


        bttn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUsuario();
            }
        });
    }

    private void signGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }

    //Login con google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERROR", "Google sign in failed", e);
            }
        }
    }

    //Login con google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mAuthProvider.googleLogin(acct).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuthProvider.getUid();
                    checkUserExist(id);
                }
                else {
                    Log.w("ERROR", "signInWithCredential:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "No se pudo iniciar sesión con Google", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Comprueba si el usuario existe, si no lo registra
    private void checkUserExist(String id){
        mDialog.show();
        mUsersProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Intent intent = new Intent(LoginActivity.this, AplicacionBaseActivity.class);
                    mDialog.dismiss();
                    startActivity(intent);
                }
                else {
                    String email = mAuthProvider.getEmail();
                    String username = mAuthProvider.getName();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    user.setUsername(username);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, AplicacionBaseActivity.class);
                                startActivity(intent);
                                mDialog.dismiss();
                            }
                            else {
                                mDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "No se pudo almacenar la información del usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //Login de usuario
    private void loginUsuario(){
        String email = editEmail.getText().toString();
        String password = editContrasena.getText().toString();
        mDialog.show();
        mAuthProvider.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, AplicacionBaseActivity.class);
                    mDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "El email y/o la contraseña no son correctas", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("CAMPO", "email: " + email);
        Log.d("CAMPO", "password: " + password);
    }


    //Acción del boton hacia atras
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_NoActionBar);
        builder.setTitle("SALIR");
        builder.setMessage("¿Estas seguro que deseas salir de la aplicación?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cerrar = true;
                salirApp(Cerrar);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cerrar = false;
                salirApp(Cerrar);
            }
        });

        builder.create();
        builder.show();
    }

    private void salirApp(boolean cerrrar){
        if (Cerrar == true){
            Toast.makeText(this,"Vuelve pronto",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }else{
            Toast.makeText(this,"Gracias por seguir aquí",Toast.LENGTH_SHORT).show();
        }
    }

    //Comprueba si el usuario ya tiene una sesión iniciada
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuthProvider.getUserSession() != null){
            mDialog.show();
            Intent intent = new Intent (LoginActivity.this, AplicacionBaseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mDialog.dismiss();
            startActivity(intent);
        }
    }
}