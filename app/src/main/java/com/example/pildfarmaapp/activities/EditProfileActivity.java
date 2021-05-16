package com.example.pildfarmaapp.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pildfarmaapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    //Declaración de variables
    ImageView mImageView;
    EditText mTextInputUsername;
    EditText mTextInputNacimiento;
    EditText mTextInputCap;
    EditText mTextInputTelefono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_edit_profile);

        //Asignación de variables
        mTextInputUsername = findViewById(R.id.EditNombre);
        mTextInputNacimiento = findViewById(R.id.EditNacimiento);
        mTextInputCap = findViewById(R.id.EditCap);
        mTextInputTelefono = findViewById(R.id.EditTelefono);


    }
}
