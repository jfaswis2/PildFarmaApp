package com.example.pildfarmaapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.providers.AuthProvider;
import com.example.pildfarmaapp.providers.ImageProvider;
import com.example.pildfarmaapp.providers.UsersProvider;

import java.io.File;

import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {

    //Declaración de variables
    ImageView mImageView;
    EditText mTextInputUsername;
    EditText mTextInputNacimiento;
    EditText mTextInputCap;
    EditText mTextInputTelefono;
    Button mButtonActualizar;

    String Nombre="";
    String Nacimiento="";
    String Cap="";
    String Telefono="";



    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    private final int GALLERY_REQUEST_CODE_PROFILE = 1;
    private final int GALLERY_REQUEST_CODE_COVER = 2;
    private final int PHOTO_REQUEST_CODE_PROFILE = 3;
    private final int PHOTO_REQUEST_CODE_COVER = 4;

    // FOTO 1
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

    // FOTO 2
    String mAbsolutePhotoPath2;
    String mPhotoPath2;
    File mPhotoFile2;

    File mImageFile;
    File mImageFile2;

    String mUsername = "";
    String mPhone = "";

    AlertDialog mDialog;

    ImageProvider mImageProvider;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_edit_profile);

        //Asignación de variables
        mTextInputUsername = findViewById(R.id.EditNombre);
        mTextInputNacimiento = findViewById(R.id.EditNacimiento);
        mTextInputCap = findViewById(R.id.EditCap);
        mTextInputTelefono = findViewById(R.id.EditTelefono);
        mButtonActualizar = findViewById(R.id.buttonActualizar);
        mImageView = findViewById(R.id.EditImage);

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[] {"Imagen de galeria", "Tomar foto"};

        mImageProvider = new ImageProvider();
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mButtonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEditProfile();
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage();
            }
        });

    }

    private void selectOptionImage() {
    }

    private void clickEditProfile() {
    }
}
