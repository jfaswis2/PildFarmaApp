package com.example.pildfarmaapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.fragments.AlarmasActivasFragment;
import com.example.pildfarmaapp.fragments.HistorialFragment;
import com.example.pildfarmaapp.fragments.AjustesFragment;
import com.example.pildfarmaapp.fragments.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AplicacionBaseActivity extends AppCompatActivity {

    //Declaración de variables
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicacion_base);

        if (ContextCompat.checkSelfPermission(AplicacionBaseActivity.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AplicacionBaseActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },100);
        }

        //asignación de variables
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AlarmasActivasFragment()).commit();

        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Bitmap captureImage = (Bitmap) intent.getExtras().get("data");
                intent.putExtra("imagen",captureImage);*/
                Intent intent = new Intent(AplicacionBaseActivity.this, LecturaDatosActivity.class);
                startActivity(intent);

            }
        });
    }

    //Se selecciona cada fragment
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.home:
                            selectedFragment = new AlarmasActivasFragment();
                            break;
                        case R.id.historial:
                            selectedFragment = new HistorialFragment();
                            break;
                        case R.id.perfil:
                            selectedFragment = new PerfilFragment();
                            break;
                        case R.id.ajustes:
                            selectedFragment = new AjustesFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }

            };

}