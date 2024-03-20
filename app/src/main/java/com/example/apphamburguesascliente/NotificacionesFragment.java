package com.example.apphamburguesascliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionesFragment extends Fragment {

    private int idCliente = -1;

    private ApiService apiService;

    public NotificacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        apiService = ApiClient.getInstance();

        // Obtener el idCliente de los argumentos
        if (getArguments() != null) {
            idCliente = getArguments().getInt("idCliente", -1);
            // Mostrar el idCliente en Logcat
            Log.d("NotificacionesFragment", "El idCliente es: " + idCliente);
        }

        // Llamar al método para obtener el usuario con el ID correcto
        obtenerUsuario(idCliente);
        return view;
    }

    // Método para cargar un fragmento en el contenedor
    private void loadFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    // Método para obtener la información del usuario desde la API de forma asíncrona
    private void obtenerUsuario(int idCliente) {
        ApiService apiService = ApiClient.getInstance();
        Call<UserResponse> callUsuario = apiService.obtenerUsuario(String.valueOf(idCliente));
        callUsuario.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.getUsuario() != null) {
                        // Aquí puedes realizar las operaciones que necesites con el usuario obtenido
                        // Por ejemplo, puedes llamar a métodos para procesar los datos del usuario
                        procesarUsuario(userResponse);
                    } else {
                        Log.e("NotificacionesFragment", "La respuesta del servidor no contiene datos de usuario.");
                    }
                } else {
                    Log.e("NotificacionesFragment", "Error al obtener usuario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("NotificacionesFragment", "Error en la solicitud de obtención de usuario: " + t.getMessage());
            }
        });
    }

    // Método para procesar el usuario obtenido
    private void procesarUsuario(UserResponse userResponse) {
        User user = userResponse.getUsuario();

        // Aquí puedes realizar cualquier operación necesaria con el usuario obtenido
        Log.d("NotificacionesFragment", "Usuario obtenido: " + user.toString());

        // Luego, puedes llamar a los métodos para realizar las validaciones o cargar los fragmentos necesarios
        boolean usuarioIncompleto = usuarioIncompleto(userResponse);
        boolean ubicacionesNulas = ubicacionesNulas(userResponse);
        boolean pedidosPendientes = pedidosPendientes();

        Log.d("NotificacionesFragment", "usuarioIncompleto: " + usuarioIncompleto);
        Log.d("NotificacionesFragment", "ubicacionesNulas: " + ubicacionesNulas);
        Log.d("NotificacionesFragment", "pedidosPendientes: " + pedidosPendientes);

        // Llamar al método para cargar el fragmento adecuado según las condiciones
        cargarFragmento(usuarioIncompleto, ubicacionesNulas, pedidosPendientes);
    }

    // Método para cargar el fragmento adecuado según las condiciones
    private void cargarFragmento(boolean usuarioIncompleto, boolean ubicacionesNulas, boolean pedidosPendientes) {
        if (usuarioIncompleto && ubicacionesNulas && pedidosPendientes) {
            loadFragment(new NotificacionesPedidosUbicacionPerfilFragment());
        } else if (ubicacionesNulas && pedidosPendientes) {
            loadFragment(new NotificacionesUbicacionesPedidosFragment());
        } else if (usuarioIncompleto && pedidosPendientes) {
            loadFragment(new NotificacionesPerfilPedidosFragment());
        } else if (usuarioIncompleto && ubicacionesNulas) {
            loadFragment(new NotificacionesPerfilUbicacionesFragment());
        } else if (pedidosPendientes) {
            loadFragment(new NotificacionesPedidosFragment());
        } else if (ubicacionesNulas) {
            loadFragment(new NotificacionesUbicacionFragment());
        } else if (usuarioIncompleto) {
            loadFragment(new NotificacionesPerfilFragment());
        } else {
            // Si no se cumple ninguna condición carga el fragmento vacío
            loadFragment(new NotificacionesVacioFragment());
        }
    }

    // Método para verificar si el usuario tiene datos incompletos
    private boolean usuarioIncompleto(UserResponse userResponse) {
        User user = userResponse.getUsuario();
        return user == null ||
                user.getSnombre() == null ||
                user.getCapellido() == null ||
                user.getTelefono() == null ||
                user.getRucCedula() == null ||
                user.getRazonSocial() == null;
    }

    // Método para verificar si todas las ubicaciones del usuario son nulas
    private boolean ubicacionesNulas(UserResponse userResponse) {
        User user = userResponse.getUsuario();

        int numUbi = 0;

        if (user.getUbicacion1().getLatitud() != null) {
            numUbi++;
            Log.d("NotificacionesFragment", "Ubicación 1 no es nula: " + user.getUbicacion1().getLatitud());
        } else {
            Log.d("NotificacionesFragment", "Ubicación 1 es nula");
        }

        if (user.getUbicacion2().getLatitud() != null) {
            numUbi++;
            Log.d("NotificacionesFragment", "Ubicación 2 no es nula: " + user.getUbicacion2().getLatitud());
        } else {
            Log.d("NotificacionesFragment", "Ubicación 2 es nula");
        }

        if (user.getUbicacion3().getLatitud() != null) {
            numUbi++;
            Log.d("NotificacionesFragment", "Ubicación 3 no es nula: " + user.getUbicacion3().getLatitud());
        } else {
            Log.d("NotificacionesFragment", "Ubicación 3 es nula");
        }

        return numUbi == 0;
    }

    private boolean pedidosPendientes() {
        // Lógica para verificar si hay pedidos pendientes del usuario
        return false; // Por ahora asumimos que no hay pedidos pendientes
    }
}