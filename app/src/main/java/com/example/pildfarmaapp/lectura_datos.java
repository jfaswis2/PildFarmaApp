package com.example.pildfarmaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class lectura_datos extends AppCompatActivity {

    ImageView imageView;
    CropImageView cropImageView;
    Bitmap bitmap;
    TextRecognizer recognizer;
    EditText editText;
    Uri image_uri;
    char[] reconocimientoChar;
    String resultadoTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_datos);

        editText = findViewById(R.id.TextoReconocido);
        imageView = findViewById(R.id.Foto);
        startCropActivity();
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);






            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    //imageView.setImageURI(resultUri);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
                    bitmap = bitmapDrawable.getBitmap();

                    ///
                    Log.i("TAG", "Result->" + bitmap);
                    InputImage image = InputImage.fromBitmap(bitmap, 0);
                    recognizer = TextRecognition.getClient();
                    Task<Text> resultText = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            String resultText = text.getText();
                            editText.setText(resultText);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }

    }

    private void camara() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(cameraIntent, 100);
        }
    }*/

    private void startCropActivity(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                image_uri = result.getUri();
                imageView.setImageURI(image_uri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
                bitmap = bitmapDrawable.getBitmap();

                Log.i("TAG", "Result->" + bitmap);
                InputImage image = InputImage.fromBitmap(bitmap, 0);
                recognizer = TextRecognition.getClient();
                Task<Text> resultText = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        resultadoTexto = text.getText();
                        reconocimientoChar=resultadoTexto.toCharArray();
                        //encontrarNombre();
                        //encontrarDosis();
                        //encontrarFrecuecia();
                        encontrarViaAdministracion();
                        //editText.setText(resultadoTexto);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                //ImageView myImageView = findViewById(R.id.Foto);
                //myImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void encontrarNombre(){
        int contador=0;
        StringBuilder sb = new StringBuilder();
        while (!Character.isDigit(reconocimientoChar[contador])){
            sb.append(reconocimientoChar[contador]);
            contador++;

        }

        String nombreReceta = sb.toString();
        editText.setText(nombreReceta);
    }

    private void encontrarDosis(){
        if(resultadoTexto.contains("Inhalació")){
            int posicion=resultadoTexto.indexOf("Inhalació");
            int valor=posicion;
            boolean estado = true;
            while (estado){
                if(Character.isAlphabetic(reconocimientoChar[valor])){
                    valor--;
                }else{
                    String hola= Character.toString(reconocimientoChar[valor-1]);
                    editText.setText(hola);
                    estado=false;
                }


            }
        }else if(resultadoTexto.contains("Comprimit")){
            int posicion=resultadoTexto.indexOf("Comprimit");
            int valor=posicion;
            boolean estado = true;
            while (estado){
                if(Character.isAlphabetic(reconocimientoChar[valor])){
                    valor--;
                }else{
                    String hola= Character.toString(reconocimientoChar[valor-1]);
                    editText.setText(hola+" Comprimit");//TODO
                    estado=false;
                }


            }
        }else if(resultadoTexto.contains("Unitat")){
            int posicion=resultadoTexto.indexOf("Unitat");
            int valor=posicion;
            boolean estado = true;
            while (estado){
                if(Character.isAlphabetic(reconocimientoChar[valor])){
                    valor--;
                }else{
                    String hola= Character.toString(reconocimientoChar[valor-1]);
                    editText.setText(hola);
                    estado=false;
                }


            }
        }else if(resultadoTexto.contains("Aplicació")){
            int posicion=resultadoTexto.indexOf("Aplicació");
            int valor=posicion;
            boolean estado = true;
            while (estado){
                if(Character.isAlphabetic(reconocimientoChar[valor])){
                    valor--;
                }else{
                    String hola= Character.toString(reconocimientoChar[valor-1]);
                    editText.setText(hola);
                    estado=false;
                }


            }
        }else if(resultadoTexto.contains("Segons")){
            editText.setText("Segons pauta o segons evolució clínica");
        }
    }

    private void encontrarFrecuecia(){
        if(resultadoTexto.contains("Hores")){
            int posicion=resultadoTexto.indexOf("Hores");
            int valor=posicion;
            boolean estado = true;
            String horas="";
            while (estado){
                if(Character.isDigit(reconocimientoChar[valor-2])){
                    horas=reconocimientoChar[valor-2] + horas;
                    valor--;
                }else{
                    estado=false;

                }


            }
            editText.setText(horas);
        }
    }

    private void encontrarViaAdministracion(){
        if(resultadoTexto.contains("pulmonar")){
            editText.setText("Via pulmonar");
        }else if(resultadoTexto.contains("oral")){
            editText.setText("Via oral");
        }
    }


}