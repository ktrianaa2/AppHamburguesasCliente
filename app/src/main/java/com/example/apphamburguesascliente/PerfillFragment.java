package com.example.apphamburguesascliente;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfillFragment extends Fragment {

    private Button btnEditar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfill, container, false);

        // Encuentra las vistas
        btnEditar = view.findViewById(R.id.btnEditar);

        // Configura los clics
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad de edición de perfil
                // Aquí deberías abrir la actividad de edición de perfil como lo hacías antes
            }
        });

        // Llamada a la API para obtener los datos del usuario
        obtenerUsuario();

        return view;
    }

    private void obtenerUsuario() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int idCuenta = sharedPreferences.getInt("id_cuenta", 0); // 0 es el valor predeterminado si no se encuentra

        // Verifica si idCuenta es 0, lo que significa que no se encontró o se guardó
        if (idCuenta == 0) {
            Log.e("PerfillFragment", "id_cuenta no encontrado en SharedPreferences.");
            return; // No continuar si no tenemos un id_cuenta válido
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://9jpn4ctd-8000.use2.devtunnels.ms/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        // Asegúrate de convertir idCuenta a String si tu API espera una cadena
        Call<UserResponse> callUsuario = service.obtenerUsuario(String.valueOf(idCuenta));
        callUsuario.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    User usuario = response.body().getUsuario();
                    // Imprimir los datos del usuario en la consola
                    Log.d("Usuario", "Nombre de usuario: " + usuario.getNombreUsuario());
                    Log.d("Usuario", "ID de cliente: " + usuario.getIdCliente());
                    Log.d("Usuario", "Teléfono: " + usuario.getTelefono());
                    Log.d("Usuario", "Razón Social: " + usuario.getRazonSocial());
                    Log.d("Usuario", "Tipo de cliente: " + usuario.getTipoCliente());
                    Log.d("Usuario", "Segundo nombre: " + usuario.getSnombre());
                    Log.d("Usuario", "Apellido: " + usuario.getCapellido());
                    Log.d("Usuario", "RUC/Cédula: " + usuario.getRucCedula());
                    // Agrega más campos según sea necesario

                    // Actualizar la interfaz de usuario con los datos del usuario
                    actualizarInterfazUsuario(usuario);
                } else {
                    Log.e("Error", "Error al obtener los datos del usuario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Error", "Error en la solicitud de obtención de usuario: " + t.getMessage());
            }
        });
    }

    // Método para actualizar la interfaz de usuario con los datos del usuario
    private void actualizarInterfazUsuario(User usuario) {
        // Aquí puedes actualizar la interfaz de usuario con los datos obtenidos del usuario
        // Por ejemplo, puedes establecer el nombre de usuario en un TextView
        // o cualquier otra vista según tu diseño
    }
}
