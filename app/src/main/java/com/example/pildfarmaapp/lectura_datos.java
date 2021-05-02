package com.example.pildfarmaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class lectura_datos extends AppCompatActivity {

    ImageView imageView,mImage;
    Bitmap bitmap;
    TextRecognizer recognizer;
    EditText etMedicamento,etDosis,etFrecuencia,etViaAdmin,etDuracionTrata;
    Uri image_uri;
    char[] reconocimientoChar;
    String resultadoTexto,horas,nombreReceta;
    TextView NuevaFoto,NombreVerificacion,IntervaloFechas;
    Button guardar,aceptarVeri,cancelarVeri;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    File mImageFile;
    imageProvider mimageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_datos);


        mimageProvider = new imageProvider();
        etMedicamento = findViewById(R.id.NombreMedicamento);
        etDosis = findViewById(R.id.Dosis);
        etFrecuencia = findViewById(R.id.Frecuencia);
        etViaAdmin = findViewById(R.id.ViaAdministracion);
        etDuracionTrata = findViewById(R.id.DuracionTratamiento);
        imageView = findViewById(R.id.Foto);
        NuevaFoto = findViewById(R.id.NuevaFoto);
        guardar = findViewById(R.id.btn_ver_datos_guardar);
        startCropActivity();

        NuevaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();

            }
        });


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newConfirmDialog();
            }
        });
    }


    private void newConfirmDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.fragment_verificacion,null);
        NombreVerificacion = (TextView) contactPopupView.findViewById(R.id.Nombre_medicamento_verificacion);
        NombreVerificacion.setText(nombreReceta);
        IntervaloFechas = (TextView) contactPopupView.findViewById(R.id.intervalo_fechas_verfificacion);
        IntervaloFechas.setText(CalculoFecha());
        aceptarVeri = (Button) contactPopupView.findViewById(R.id.bttn_aceptar_verificacion);
        cancelarVeri = (Button) contactPopupView.findViewById(R.id.bttn_cancelar_verificacion);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        cancelarVeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        aceptarVeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String CalculoFecha(){
        String intervalo;

        Calendar today = Calendar.getInstance();
        intervalo = today.get(Calendar.DAY_OF_MONTH) +"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+"-";
        today.add(Calendar.DAY_OF_MONTH,Integer.parseInt(this.horas));
        intervalo +=today.get(Calendar.DAY_OF_MONTH) +"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR);

        return intervalo;
    }

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
                try {
                    mImageFile = FileUtil.from(this,image_uri);
                    saveImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                        encontrarNombre();
                        encontrarDosis();
                        encontrarFrecuecia();
                        encontrarViaAdministracion();
                        encontrarDuracionTrata();
                        //editText.setText(resultadoTexto);//TODO
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                //ImageView myImageView = findViewById(R.id.Foto);//TODO
                //myImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void saveImage(){
        mimageProvider.save(lectura_datos.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){

                }else{

                }
            }
        });
    }

    private void encontrarNombre(){
        int contador=0;
        StringBuilder sb = new StringBuilder();
        while (!Character.isDigit(reconocimientoChar[contador])) {
            if (Character.isAlphabetic(reconocimientoChar[contador])) {
                sb.append(reconocimientoChar[contador]);
            }
            contador++;
        }
        String nombreReceta = sb.toString();
        this.nombreReceta=nombreReceta;
        etMedicamento.setText(nombreReceta);
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
                    etDosis.setText(hola);
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
                    etDosis.setText(hola+" Comprimit");//TODO
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
                    etDosis.setText(hola);
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
                    etDosis.setText(hola);
                    estado=false;
                }
            }
        }else if(resultadoTexto.contains("Segons")){
            etDosis.setText("Segons pauta o segons evolució clínica");
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
            etFrecuencia.setText(horas);
        }
    }

    private void encontrarViaAdministracion(){
        if(resultadoTexto.contains("pulmonar")){
            etViaAdmin.setText("Via pulmonar");
        }else if(resultadoTexto.contains("oral")){
            etViaAdmin.setText("Via oral");
        }
    }

    private void encontrarDuracionTrata(){
        if(resultadoTexto.contains("dies")) {
            int posicion = resultadoTexto.indexOf("dies");
            int valor = posicion;
            boolean estado = true;
            String horas = "";

            try {


                while (estado) {

                    if (Character.isDigit(reconocimientoChar[valor - 1])) {
                        horas = reconocimientoChar[valor - 1] + horas;
                        valor--;
                    }
                    if (reconocimientoChar[valor - 1] == ' ') {
                        valor--;
                    } else {
                        estado = false;

                    }
                }
                this.horas=horas;
                etDuracionTrata.setText(horas + " dies");
            }

            catch (Exception e){
                etDuracionTrata.setText("No encontrado");
            }
    }}
}