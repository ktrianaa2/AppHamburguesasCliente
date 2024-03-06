package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IniciarSesionActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

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
                    // Validación de usuario y contraseña

                    if (username.equals("usuario") && password.equals("123456")) {
                        Toast.makeText(IniciarSesionActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        // Abrir la actividad de la página principal
                        Intent intent = new Intent(IniciarSesionActivity.this, PaginaPrincipalActivity.class);
                        startActivity(intent);
                        finish(); // Cierra la actividad actual para evitar que el usuario regrese al inicio de sesión al presionar el botón "Atrás"
                    } else {
                        Toast.makeText(IniciarSesionActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
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
}
