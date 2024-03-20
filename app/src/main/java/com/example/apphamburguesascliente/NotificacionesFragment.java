package com.example.apphamburguesascliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class NotificacionesFragment extends Fragment {

    private int idCliente = -1;
    public NotificacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        // Obtener el idCliente de los argumentos
        if (getArguments() != null) {
            idCliente = getArguments().getInt("idCliente", -1);
            // Mostrar el idCliente en Logcat
            Log.d("NotificacionesFragment", "El idCliente es: " + idCliente);
        }

        if (usuarioIncompleto()) {
            loadFragment(new NotificacionesPerfilFragment());
        } else if (ubicacionesNulas()) {
            loadFragment(new NotificacionesUbicacionFragment());
        } else if (pedidosPendientes()) {
            loadFragment(new NotificacionesPedidosFragment());
        } else if (usuarioIncompleto() & ubicacionesNulas()) {
            loadFragment(new NotificacionesPerfilUbicacionesFragment());
        } else if (usuarioIncompleto() & pedidosPendientes()) {
            loadFragment(new NotificacionesPerfilPedidosFragment());
        } else if (ubicacionesNulas() & pedidosPendientes()) {
            loadFragment(new NotificacionesUbicacionesPedidosFragment());
        } else if (usuarioIncompleto() & ubicacionesNulas() & pedidosPendientes()) {
            loadFragment(new NotificacionesPedidosUbicacionPerfilFragment());
        } else {
            // Si no se cumple ninguna condición carga el fragmento vacío
            loadFragment(new NotificacionesVacioFragment());
        }

        return view;
    }

    // Método para cargar un fragmento en el contenedor
    private void loadFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    // Métodos hipotéticos para verificar condiciones
    private boolean usuarioIncompleto() {
        // Lógica para verificar si el usuario tiene el perfil incompleto
        return false;
    }

    private boolean ubicacionesNulas() {
        // Lógica para verificar si las ubicaciones del usuario están nulas
        return false;
    }

    private boolean pedidosPendientes() {
        // Lógica para verificar si hay pedidos pendientes del usuario
        return false;
    }

}