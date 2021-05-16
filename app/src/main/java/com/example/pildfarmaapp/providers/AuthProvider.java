package com.example.pildfarmaapp.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

//Clase provee funciones para distintas tareas
public class AuthProvider {

    //Declaración de variables
    private FirebaseAuth mAuth;

    public AuthProvider() {
        mAuth = FirebaseAuth.getInstance();
    }

    //Registro del usuario
    public Task<AuthResult> register(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    //Login del usuario
    public Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    //Login / registro del usuario por Google
    public Task<AuthResult> googleLogin(GoogleSignInAccount googleSignInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return mAuth.signInWithCredential(credential);
    }

    //Devuelve el email del usuario actual
    public String getEmail() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getEmail();
        }
        else {
            return null;
        }
    }

    //Devuelve el id del usuario actual
    public String getUid() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getUid();
        }
        else {
            return null;
        }
    }

    //Devuelve el nombre del usuario actual
    public String getName(){
        if(mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser().getDisplayName();
        }
        else {
            return null;
        }
    }

    //Devuelve la sesión del usuario actual
    public FirebaseUser getUserSession() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser();
        }
        else {
            return null;
        }
    }

    //Cierre de sesión del usuario actual
    public void logout() {
        if (mAuth != null) {
            mAuth.signOut();
        }
    }
}
