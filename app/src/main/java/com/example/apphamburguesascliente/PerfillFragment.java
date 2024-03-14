package com.example.apphamburguesascliente;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;


import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.CarritoModelo;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfillFragment extends Fragment {

    private TextView txtUsuario;
    private TextView txtNombresUsuario;
    private TextView txtMail;
    private TextView txtNumeroTelefono;
    private TextView txtRazonSocial;
    private TextView txtCedula;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfill, container, false);

        // Encuentra las vistas
        txtUsuario = view.findViewById(R.id.txtUsuario);
        txtNombresUsuario = view.findViewById(R.id.txtNombresUsuario);
        txtMail = view.findViewById(R.id.txtMail);
        txtNumeroTelefono = view.findViewById(R.id.txtNumeroTelefono);
        txtRazonSocial = view.findViewById(R.id.txtRazonSocial);
        txtCedula = view.findViewById(R.id.txtCedula);


        // Configurar el clic para cerrar sesión
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpiar el carrito de compras antes de cerrar sesión
                CarritoModelo.getInstance().limpiarCarrito();

                // Limpiar las preferencias compartidas (cerrar sesión)
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();

                // Redirigir al usuario a la pantalla de inicio de sesión
                Intent intent = new Intent(getActivity(), PaginaPrincipalActivity.class); // Asegúrate de cambiar 'LoginActivity.class' por la actividad de inicio de sesión real de tu app
                startActivity(intent);
                getActivity().finish(); // Finaliza la actividad actual para que el usuario no pueda volver atrás
            }
        });



        // Configurar el clic para abrir la actividad de "Puntos"
        LinearLayout btnPuntos = view.findViewById(R.id.btnPuntos);
        btnPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de "Puntos"
                Intent intent = new Intent(getActivity(), RecompensasActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el clic para abrir la actividad de "Mis Ubicaciones"
        LinearLayout btnUbicaciones = view.findViewById(R.id.btnUbicaciones);
        btnUbicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la ID de usuario
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                int idCuenta = sharedPreferences.getInt("id_cuenta", 0);

                // Abrir la actividad de "Mis Ubicaciones" con la ID de usuario como extra
                Intent intent = new Intent(getActivity(), MisUbicacionesActivity.class);
                intent.putExtra("idUsuario", idCuenta);
                startActivity(intent);
            }
        });

        // Configurar el clic para abrir la actividad de "Editar Perfil"
        Button btnEditar = view.findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de "Editar Perfil"
                Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(intent);
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

        apiService = ApiClient.getInstance();

        // Asegúrate de convertir idCuenta a String si tu API espera una cadena
        Call<UserResponse> callUsuario = apiService.obtenerUsuario(String.valueOf(idCuenta));
        callUsuario.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    User usuario = response.body().getUsuario();
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
        // Establecer los valores del usuario en los TextView correspondientes
        txtUsuario.setText(usuario.getNombreUsuario());
        txtNombresUsuario.setText(usuario.getSnombre() + " " + usuario.getCapellido());
        txtNumeroTelefono.setText(usuario.getTelefono());
        txtRazonSocial.setText(usuario.getRazonSocial() != null ? usuario.getRazonSocial() : "Sin información");
        txtCedula.setText(usuario.getRucCedula() != null ? usuario.getRucCedula() : "Sin información");
    }
}
