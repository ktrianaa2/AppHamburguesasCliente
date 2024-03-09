package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.LoginRequest;
import com.example.apphamburguesascliente.Modelos.LoginResponse;
import com.example.apphamburguesascliente.Modelos.RolResponse;
import com.example.apphamburguesascliente.Modelos.TokenRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IniciarSesionActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://9jpn4ctd-8000.use2.devtunnels.ms/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        TextView loginButton = findViewById(R.id.loginbtn);
        TextView forgotPasswordText = findViewById(R.id.forgotpass);
        TextView registerText = findViewById(R.id.textregister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(IniciarSesionActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    Call<LoginResponse> call = apiService.iniciarSesion(new LoginRequest(username, password));
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                LoginResponse loginResponse = response.body();
                                if (loginResponse != null) {
                                    final String token = loginResponse.getToken();

                                    // Ahora, verifica el rol con otra llamada a la API
                                    Call<RolResponse> callRol = apiService.verificarRol(new TokenRequest(token));
                                    callRol.enqueue(new Callback<RolResponse>() {
                                        @Override
                                        public void onResponse(Call<RolResponse> call, Response<RolResponse> response) {
                                            if (response.isSuccessful()) {
                                                RolResponse rolResponse = response.body();
                                                if (rolResponse != null && "C".equals(rolResponse.getRol())) {
                                                    String token = loginResponse.getToken();
                                                    String nombreUsuario = loginResponse.getNombreusuario();
                                                    int idCuenta = loginResponse.getId_cuenta();

                                                    mostrarNotificacion();

                                                    Intent intent = new Intent(IniciarSesionActivity.this, PaginaPrincipalActivity.class);
                                                    intent.putExtra("idCliente", idCuenta); // Pasar el idCuenta
                                                    startActivity(intent);
                                                    finish(); // Cerrar esta actividad

                                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                    myEdit.putInt("id_cuenta", loginResponse.getId_cuenta());
                                                    myEdit.apply();                                                } else {
                                                    // Rol no permitido
                                                    Toast.makeText(IniciarSesionActivity.this, "Esta cuenta no tiene el rol para acceder.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(IniciarSesionActivity.this, "Error al verificar el rol", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<RolResponse> call, Throwable t) {
                                            Toast.makeText(IniciarSesionActivity.this, "Error en la red", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(IniciarSesionActivity.this, "Error en inicio de sesión", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(IniciarSesionActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IniciarSesionActivity.this, OlvidoContrasenaActivity.class);
                startActivity(intent);
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IniciarSesionActivity.this, RegistrarseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void mostrarNotificacion() {
        // Aquí puedes mostrar una notificación al usuario
        Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
    }
}
