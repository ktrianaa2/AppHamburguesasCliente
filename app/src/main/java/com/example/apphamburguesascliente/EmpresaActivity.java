package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.EmpresaInfo;
import com.example.apphamburguesascliente.Modelos.RespuestaEmpresa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpresaActivity extends AppCompatActivity {
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        apiService = ApiClient.getInstance();

        ImageView imageViewFlecha = findViewById(R.id.imageView2);
        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y regresa a la anterior
            }
        });

        obtenerInfoEmpresaDesdeAPI();

        CardView cardMasInfo = findViewById(R.id.cardRedesSociales);
        CardView cardSucursales = findViewById(R.id.cardSucursales);

        cardMasInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmpresaActivity.this, RedesSocialesActivity.class);
                startActivity(intent);
            }
        });

        cardSucursales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmpresaActivity.this, SucursalesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void obtenerInfoEmpresaDesdeAPI() {
        Call<RespuestaEmpresa> call = apiService.obtenerInfoEmpresa();
        call.enqueue(new Callback<RespuestaEmpresa>() {
            @Override
            public void onResponse(Call<RespuestaEmpresa> call, Response<RespuestaEmpresa> response) {
                if (response.isSuccessful()) {
                    RespuestaEmpresa respuesta = response.body();
                    if (respuesta != null && respuesta.getEmpresaInfo() != null) {
                        EmpresaInfo empresaInfo = respuesta.getEmpresaInfo();
                        Log.d("EmpresaActivity", "Datos de la empresa: " + empresaInfo.toString());
                        actualizarUI(empresaInfo);
                    } else {
                        Toast.makeText(EmpresaActivity.this, "La respuesta está vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("EmpresaActivity", "Error en la respuesta: " + response.errorBody());
                    Toast.makeText(EmpresaActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaEmpresa> call, Throwable t) {
                Log.e("EmpresaActivity", "Error en la solicitud: " + t.getMessage());
                Toast.makeText(EmpresaActivity.this, "Error al obtener datos de la empresa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarUI(EmpresaInfo info) {
        if (info != null) {
            StringBuilder textoConcatenado = new StringBuilder();
            textoConcatenado.append("Somos ").append(info.getNombre()).append("\n");
            textoConcatenado.append(info.getEslogan()).append("\n");
            textoConcatenado.append("Nos puedes encontrar en ").append(info.getDireccion()).append("\n");
            textoConcatenado.append("Contáctate con nosotros a través de ").append(info.getTelefono()).append("\n");
            textoConcatenado.append("O por nuestro correo electrónico ").append("\n");
            textoConcatenado.append(info.getCorreoElectronico());

            TextView textViewEmpresaInfo = findViewById(R.id.textInfo);
            textViewEmpresaInfo.setText(textoConcatenado.toString());

            TextView textViewEmpresaFecha = findViewById(R.id.textFecha);
            textViewEmpresaFecha.setText("Haciendo las mejores hamburguesas desde " + formatearFecha(info.getFechaFundacion()));

            if (info.getLogoBase64() != null && !info.getLogoBase64().isEmpty()) {
                ImageView imageView = findViewById(R.id.imageView);
                byte[] decodedString = Base64.decode(info.getLogoBase64(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedByte);
            }
        }
    }

    private String formatearFecha(String fecha) {
        try {
            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDate = formatoOriginal.parse(fecha);
            SimpleDateFormat formatoDeseado = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy", new Locale("es", "EC"));
            return formatoDeseado.format(fechaDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return fecha; // Si ocurre un error, devuelve la fecha sin formato
        }
    }

}
