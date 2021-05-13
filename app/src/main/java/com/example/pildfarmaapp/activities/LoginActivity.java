package com.example.pildfarmaapp;

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

import com.example.pildfarmaapp.activities.RegisterActivity;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {


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
    private final int RC_SIGN_IN=9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Intent intent = new Intent(v.getContext(), password_reset.class);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERROR", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        /*AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id=mAuth.getCurrentUser().getUid();
                            checkUserExist(id);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "No se pudo iniciar sesión con Google",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });*/
        mAuthProvider.googleLogin(acct).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuthProvider.getUid();
                    checkUserExist(id);
                }
                else {
                    Log.w("ERROR", "signInWithCredential:failure", task.getException());
                    Toast.makeText(login.this, "No se pudo iniciar sesión con Google", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUserExist(String id){
        /*mFirestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Intent intent = new Intent(login.this,aplicacion_base.class);
                    startActivity(intent);
                    finish();
                }else{
                    String email=mAuth.getCurrentUser().getEmail();
                    String userName=mAuth.getCurrentUser().getDisplayName();
                    Map<String,Object> map = new HashMap<>();
                    map.put("Name",userName);
                    map.put("Email",email);
                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(login.this,aplicacion_base.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(login.this, "No se pudo almacenar la inforamción del usuario",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });*/
        mUsersProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Intent intent = new Intent(login.this, aplicacion_base.class);
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
                                Intent intent = new Intent(login.this, aplicacion_base.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(login.this, "No se pudo almacenar la información del usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void loginUsuario(){
        String email = editEmail.getText().toString();
        String password = editContrasena.getText().toString();

        mAuthProvider.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(login.this, aplicacion_base.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(login.this, "El email y/o la contraseña no son correctas", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("CAMPO", "email: " + email);
        Log.d("CAMPO", "password: " + password);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    protected void onStart() {
        super.onStart();
    }
}