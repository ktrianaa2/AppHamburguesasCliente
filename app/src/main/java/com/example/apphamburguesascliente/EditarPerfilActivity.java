package com.example.apphamburguesascliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private int idCuentaUsuario;
    private ImageView imageView;
    private EditText razonSocialText;
    private EditText nombreEditText;
    private EditText apellidoEditText;
    private EditText telefonoEditText;
    private EditText identificacionEditText;
    private EditText correoEditText;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Inicializar vistas
        imageView = findViewById(R.id.imageView);
        razonSocialText = findViewById(R.id.razon_social);
        nombreEditText = findViewById(R.id.nombre);
        apellidoEditText = findViewById(R.id.apellido);
        telefonoEditText = findViewById(R.id.telefono);
        identificacionEditText = findViewById(R.id.identificaion);
        correoEditText = findViewById(R.id.cpassword);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        idCuentaUsuario = sharedPreferences.getInt("id_cuenta", -1);

        obtenerDatosUsuario();

        // Botón de retroceso
        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);
        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });

        // Botón de selección de imagen desde galería
        Button selectImageGaleriaButton = findViewById(R.id.selectImageGaleriaButton);
        selectImageGaleriaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    openImagePicker();
                }
            }
        });

        // Botón de captura de imagen desde cámara
        Button selectImageCamaraButton = findViewById(R.id.selectImageCamaraButton);
        selectImageCamaraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        // Botón de cambio de contraseña
        Button cambiarContrasenaButton = findViewById(R.id.cambiarContrasenaButton);
        cambiarContrasenaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfilActivity.this, CambiarContrasenaActivity.class);
                startActivity(intent);
            }
        });

        // Botón de cancelar
        Button cancelarButton = findViewById(R.id.cancelarButton);
        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });

        // Botón de guardar
        Button guardarButton = findViewById(R.id.guardarButton);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos editados y realizar la llamada a la API para actualizar
                String crazon_social = razonSocialText.getText().toString();
                String snombre = nombreEditText.getText().toString();
                String capellido = apellidoEditText.getText().toString();
                String ctelefono = telefonoEditText.getText().toString();
                String ruc_cedula = identificacionEditText.getText().toString();
                String id_cuenta = String.valueOf(idCuentaUsuario);

                // Realizar la llamada a la API para actualizar los datos del usuario
                apiService = ApiClient.getInstance();
                Call<UserResponse> call = apiService.actualizarUsuario(id_cuenta, snombre, capellido, ctelefono, ruc_cedula, crazon_social);

                // Manejar la respuesta de la API
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            // Manejar la respuesta exitosa
                            Toast.makeText(EditarPerfilActivity.this, "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
                            finish(); // Cierra la actividad y regresa
                        } else {
                            // Manejar errores de la API
                            Toast.makeText(EditarPerfilActivity.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        // Manejar fallos de conexión
                        Toast.makeText(EditarPerfilActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // Verificar y solicitar permisos necesarios
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionReadExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
                return false;
            }
        }
        return true;
    }

    // Abrir selector de imágenes de la galería
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Capturar imagen desde la cámara
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permiso denegado para acceder a la galería", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Procesar la imagen seleccionada de la galería
            Uri imageUri = data.getData();
            try {
                // Convertir la URI a un Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // Mostrar el bitmap en el ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Procesar la imagen capturada desde la cámara
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
    private void obtenerDatosUsuario() {
        // Llamada a la API para obtener los datos del usuario
        apiService = ApiClient.getInstance();
        Call<UserResponse> call = apiService.obtenerUsuario(String.valueOf(idCuentaUsuario));

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Obtener el usuario de la respuesta
                    User usuario = response.body().getUsuario();

                    // Llenar los campos de texto con los datos actuales del usuario
                    razonSocialText.setText(usuario.getRazonSocial());
                    nombreEditText.setText(usuario.getSnombre());
                    apellidoEditText.setText(usuario.getCapellido());
                    telefonoEditText.setText(usuario.getTelefono());
                    identificacionEditText.setText(usuario.getRucCedula());
                } else {
                    // Manejar errores de la API
                    Toast.makeText(EditarPerfilActivity.this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Manejar fallos de conexión
                Toast.makeText(EditarPerfilActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

}