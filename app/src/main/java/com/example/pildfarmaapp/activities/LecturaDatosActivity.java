package com.example.pildfarmaapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.alarm_management.Alarm;
import com.example.pildfarmaapp.models.PostAlarma;
import com.example.pildfarmaapp.providers.AuthProvider;
import com.example.pildfarmaapp.providers.ImageProvider;
import com.example.pildfarmaapp.providers.PostProvider;
import com.example.pildfarmaapp.providers.UsersProvider;
import com.example.pildfarmaapp.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class LecturaDatosActivity extends AppCompatActivity {

    //Declaración de variables
    private ImageView imageView;
    private Bitmap bitmap;
    private TextRecognizer recognizer;
    private EditText etMedicamento,etDosis,etFrecuencia,etViaAdmin,etDuracionTrata;
    private Uri image_uri;
    private char[] reconocimientoChar;
    private String resultadoTexto,horas,nombreReceta;
    private TextView NuevaFoto,NombreVerificacion,IntervaloFechas;
    private Button guardar,aceptarVeri,cancelarVeri;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private File mImageFile;
    private PostProvider mPostProvider;
    private ImageProvider mimageProvider;
    private AuthProvider mAuthProvider;
    private static int broadcastCode=0;
    private int horasFrecuencia;
    private int diasTratamiento;
    private UsersProvider mUsersProvider;
    private int numeroBroad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_datos);

        //Asiganción de variables
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
        mimageProvider = new ImageProvider();
        etMedicamento = findViewById(R.id.NombreMedicamento);
        etDosis = findViewById(R.id.Dosis);
        etFrecuencia = findViewById(R.id.Frecuencia);
        etViaAdmin = findViewById(R.id.ViaAdministracion);
        etDuracionTrata = findViewById(R.id.DuracionTratamiento);
        imageView = findViewById(R.id.Foto);
        NuevaFoto = findViewById(R.id.NuevaFoto);
        mUsersProvider = new UsersProvider();
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


    //Confirmacion del guardado de la receta
    private void newConfirmDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.fragment_verificacion,null);
        NombreVerificacion = (TextView) contactPopupView.findViewById(R.id.Nombre_medicamento_verificacion);
        NombreVerificacion.setText(nombreReceta);
        IntervaloFechas = (TextView) contactPopupView.findViewById(R.id.intervalo_fechas_verfificacion);
        IntervaloFechas.setText(CalculoFecha());
        aceptarVeri = (Button) contactPopupView.findViewById(R.id.bttn_aceptar_cerrar_sesion);
        cancelarVeri = (Button) contactPopupView.findViewById(R.id.bttn_cancelar_cerrar_sesion);

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
                guardarDatosFireBaseReceta();
            }
        });
    }

    private void guardarDatosFireBaseReceta(){
        String Nombre= etMedicamento.getText().toString();
        String Dosis=etDosis.getText().toString();
        String Frecuencia=etFrecuencia.getText().toString();
        String ViaAdministracion=etViaAdmin.getText().toString();
        String Duracion=etDuracionTrata.getText().toString();
        try {
            mImageFile = FileUtil.from(this,image_uri);
            saveImage(Nombre,Dosis,Frecuencia,ViaAdministracion,Duracion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Se calcula la fecha de inicio y fin de la toma del medicamento
    private String CalculoFecha(){
        String intervalo;

        Calendar today = Calendar.getInstance();
        intervalo = today.get(Calendar.DAY_OF_MONTH) +"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+"-";
        today.add(Calendar.DAY_OF_MONTH,Integer.parseInt(this.horas));
        intervalo +=today.get(Calendar.DAY_OF_MONTH) +"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR);

        return intervalo;
    }

    //Se asignan las lineas guias de la foto
    private void startCropActivity(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }


    //Se hace uso del OCR para reconocer el texto e identificar los datos que necesitamos
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


    //Se guarda la foto del usuario en la base de datos
    private void saveImage(String Nombre, String Dosis, String Frecuencia, String ViaAdmin, String DuracionTrata){
        mimageProvider.save(LecturaDatosActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mimageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            LoginActivity login = new LoginActivity();
                            String url = uri.toString();
                            PostAlarma post = new PostAlarma();
                            post.setImagen(url);
                            post.setMedicamento(Nombre);
                            post.setDosis(Dosis);
                            post.setFrecuencia(Frecuencia);
                            post.setViaAdministracion(ViaAdmin);
                            post.setDuracionTratamiento(DuracionTrata);
                            post.setIDUsuario(mAuthProvider.getUid());
                            post.setEstado("Activo");
                            post.setBroadcaster(String.valueOf(broadcastCode));
                            mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    if(taskSave.isSuccessful()){
                                        Toast.makeText(LecturaDatosActivity.this,"La información se almacenó correctamente y se ha activado" +
                                                        "la alarma",
                                                Toast.LENGTH_LONG).show();
                                        ActivacionAlarma();
                                        broadcastCode++;
                                        Intent intent = new Intent(LecturaDatosActivity.this, AplicacionBaseActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(LecturaDatosActivity.this,"No se pudo almacenar la información",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(LecturaDatosActivity.this, "Hubo un error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ActivacionAlarma() {
        getBroadcastCode();
        Intent intent = new Intent(getApplicationContext(), Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(LecturaDatosActivity.this,
                numeroBroad,intent,0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+horasFrecuencia*1000,horasFrecuencia*1000,
                pendingIntent);
        Toast.makeText(this, "Alarm set in 3 seconds", Toast.LENGTH_SHORT).show();
    }

    public void getBroadcastCode(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("Broadcaster")){
                        String NumBroadcaster = documentSnapshot.getString("Broadcaster");
                        numeroBroad = Integer.parseInt(NumBroadcaster);
                    }
                }
            }
        });
    }

    //Algoritmo para encontrar el nombre del medicamento
    private void encontrarNombre(){
        int contador=0;
        StringBuilder sb = new StringBuilder();
        while (!Character.isDigit(reconocimientoChar[contador])) {
                sb.append(reconocimientoChar[contador]);
            contador++;
        }
        String nombreReceta = sb.toString();
        this.nombreReceta=nombreReceta;
        etMedicamento.setText(nombreReceta);
    }

    //Algortimo para encontrar la dosis del medicamento
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

    //Algortimo para encontrar la frecuencia del medicamento
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

            horasFrecuencia = Integer.getInteger(horas);
            etFrecuencia.setText("cada " +horas + " " +"hores");
        }
    }

    //Algortimo para encontrar la via de administración del medicamento
    private void encontrarViaAdministracion(){
        if(resultadoTexto.contains("pulmonar")){
            etViaAdmin.setText("Via pulmonar");
        }else if(resultadoTexto.contains("oral")){
            etViaAdmin.setText("Via oral");
        }
    }

    //Algortimo para encontrar la duración del tratamiento
    private void encontrarDuracionTrata(){ //TODO
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
                diasTratamiento = Integer.getInteger(horas);
                etDuracionTrata.setText(horas + " dies");
            }

            catch (Exception e){
                etDuracionTrata.setText("No encontrado");
            }
    }}
}