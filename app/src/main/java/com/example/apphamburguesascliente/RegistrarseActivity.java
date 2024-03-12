package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.RegistroRequest;
import com.example.apphamburguesascliente.Modelos.RegistroResponse;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, telefonoEditText, nombreEditText, apellidoEditText;
    private TextView textLoginTextView;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.cpassword);
        telefonoEditText = findViewById(R.id.telefono);
        nombreEditText = findViewById(R.id.nombre);
        apellidoEditText = findViewById(R.id.apellido);
        textLoginTextView = findViewById(R.id.textlogin);

        apiService = ApiClient.getInstance();

        textLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarseActivity.this, IniciarSesionActivity.class);
                startActivity(intent);
            }
        });

        // onClickListener al botón "Regístrate"
        findViewById(R.id.signupbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén los valores de los campos de texto
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String telefono = telefonoEditText.getText().toString();
                String nombre = nombreEditText.getText().toString();
                String apellido = apellidoEditText.getText().toString();

                if (password.equals(confirmPassword)) {
                    // Primero valida usuario y teléfono antes de registrar
                    validarUsuarioYTelefono(username, password, telefono, nombre, apellido);
                } else {
                    Toast.makeText(RegistrarseActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void validarUsuarioYTelefono(final String username, final String password, final String telefono, final String nombre, final String apellido) {
        Map<String, String> usuarioMap = new HashMap<>();
        usuarioMap.put("nombreusuario", username);

        apiService.verificarUsuarioExistente(usuarioMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String mensajeUsuario = response.body().get("mensaje").getAsString();
                    if ("0".equals(mensajeUsuario)) {
                        // El usuario no existe, proceder a verificar el teléfono
                        Map<String, String> telefonoMap = new HashMap<>();
                        telefonoMap.put("ctelefono", telefono);

                        apiService.verificarTelefonoExistente(telefonoMap).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    String mensajeTelefono = response.body().get("mensaje").getAsString();
                                    if ("0".equals(mensajeTelefono)) {
                                        // El teléfono no existe, proceder con el registro
                                        registrarUsuario(username, password, telefono, nombre, apellido);
                                    } else {
                                        Toast.makeText(RegistrarseActivity.this, "El teléfono ya está registrado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Toast.makeText(RegistrarseActivity.this, "Error al verificar el teléfono", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegistrarseActivity.this, "El nombre de usuario ya está registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegistrarseActivity.this, "Error al verificar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void registrarUsuario(String username, String password, String telefono, String nombre, String apellido) {
        Call<RegistroResponse> call = apiService.registrarUsuario(new RegistroRequest(username, password, telefono, nombre, apellido));
        call.enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful()) {
                    RegistroResponse registroResponse = response.body();
                    if (registroResponse != null) {
                        // Registro exitoso, realiza la acción deseada, como redirigir a otra actividad
                        Toast.makeText(RegistrarseActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrarseActivity.this, PaginaPrincipalActivity.class);
                        startActivity(intent);
                        finish(); // Cierra esta actividad para evitar que el usuario regrese con el botón "Atrás"
                    }
                } else {
                    Toast.makeText(RegistrarseActivity.this, "Error al registrar, intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(RegistrarseActivity.this, "Error en la solicitud de registro", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
