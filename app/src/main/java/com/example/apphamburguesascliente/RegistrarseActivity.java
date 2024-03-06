package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private TextView textLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.cpassword);
        textLoginTextView = findViewById(R.id.textlogin);

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
                // Realiza la validación de las contraseñas antes de registrar al usuario
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (password.equals(confirmPassword)) {
                    //lógica de registro de usuario
                } else {
                    // Muestra un mensaje de error o realiza alguna acción si las contraseñas no coinciden
                }
            }
        });
    }
}