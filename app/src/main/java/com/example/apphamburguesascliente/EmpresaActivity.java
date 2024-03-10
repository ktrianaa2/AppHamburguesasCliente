package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.EmpresaInfo;
import com.example.apphamburguesascliente.Modelos.RespuestaEmpresa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmpresaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        ImageView imageViewFlecha = findViewById(R.id.imageView2);
        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y regresa a la anterior
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://9jpn4ctd-8000.use2.devtunnels.ms/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
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
        ((TextView) findViewById(R.id.nombreEmpresaTextView)).setText(info.getNombre());
        ((TextView) findViewById(R.id.direccionTextView)).setText(info.getDireccion());
        ((TextView) findViewById(R.id.telefonoTextView)).setText(info.getTelefono());
        ((TextView) findViewById(R.id.correoTextView)).setText(info.getCorreoElectronico());
        ((TextView) findViewById(R.id.fechaFundacionTextView)).setText(info.getFechaFundacion());
        ((TextView) findViewById(R.id.sitioWebTextView)).setText(info.getSitioWeb());
        ((TextView) findViewById(R.id.esloganTextView)).setText(info.getEslogan());

        if (info.getLogoBase64() != null && !info.getLogoBase64().isEmpty()) {
            ImageView imageView = findViewById(R.id.imageView);
            byte[] decodedString = Base64.decode(info.getLogoBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }
    }

}

