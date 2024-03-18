package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CambiarContrasenaActivity extends AppCompatActivity {

    private EditText passwordActualEditText;
    private EditText passwordNuevaEditText;
    private EditText cpasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        // Inicializar vistas
        passwordActualEditText = findViewById(R.id.passwordActual);
        passwordNuevaEditText = findViewById(R.id.passwordNueva);
        cpasswordEditText = findViewById(R.id.cpassword);

        // Flecha retroceder
        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);
        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
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
                String passwordActual = passwordActualEditText.getText().toString(); //esta tienes que validar que sea la misma que ya tiene guardada
                String passwordNueva = passwordNuevaEditText.getText().toString();
                String confirmPassword = cpasswordEditText.getText().toString();

                // Validar si las contraseñas son iguales
                if (!passwordNueva.equals(confirmPassword)) {
                    Toast.makeText(CambiarContrasenaActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return; // aqui sale del listeer del boton
                }

                // Aquí haces el guardado de la contraseña
                // Ejemplo: Guardar la contraseña
                guardarContrasena(passwordNueva);
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
    }

    private void guardarContrasena(String contrasena) {
        // Aquí implementa la lógica para guardar la nueva contraseña, si no hay editar sobreescribela
        // tambien ves si lo regresa a la vista del perfil o del editado de perfil, me confirmas.
    }

}