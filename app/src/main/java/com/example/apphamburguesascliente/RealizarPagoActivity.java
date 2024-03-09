package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;

import com.example.apphamburguesascliente.PagoEfectivoFragment;
import com.example.apphamburguesascliente.PagoFranccionadoFragment;
import com.example.apphamburguesascliente.PagoTransferenciaFragment;
import com.example.apphamburguesascliente.R;

public class RealizarPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pago);

        // Inicializa el Spinner y define las opciones
        Spinner metodosPagoSpinner = findViewById(R.id.metodosPagoSpinner);
        String[] opcionesMetodoPago = {"Transferencia", "Efectivo", "Fraccionado"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesMetodoPago);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metodosPagoSpinner.setAdapter(adapter);

        // Maneja la selección del Spinner
        metodosPagoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtiene el fragmento seleccionado
                Fragment fragment = obtenerFragmentoSegunSeleccion(position);

                // Reemplaza el fragmento actual con el nuevo fragmento
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No es necesario hacer nada aquí
            }
        });

        // Por defecto, muestra el fragmento de Transferencia
        Fragment defaultFragment = obtenerFragmentoSegunSeleccion(0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, defaultFragment)
                .commit();
    }

    // Método para obtener el fragmento según la posición seleccionada en el Spinner
    private Fragment obtenerFragmentoSegunSeleccion(int position) {
        switch (position) {
            case 0:
                return new PagoTransferenciaFragment();
            case 1:
                return new PagoEfectivoFragment();
            case 2:
                return new PagoFranccionadoFragment();
            default:
                return new PagoTransferenciaFragment();
        }
    }
}
