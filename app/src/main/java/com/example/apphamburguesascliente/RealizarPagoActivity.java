package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.CarritoModelo;
import com.example.apphamburguesascliente.Modelos.DetallesPedido;
import com.example.apphamburguesascliente.Modelos.Pedido;
import com.example.apphamburguesascliente.Modelos.Sucursal;
import com.example.apphamburguesascliente.Modelos.SucursalResponse;
import com.example.apphamburguesascliente.Modelos.Ubicacion;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealizarPagoActivity extends AppCompatActivity {
    private ApiService apiService;
    private int idCuentaUsuario;
    private int selectedHour = 0;
    private int selectedMinute = 0;
    private Uri imageUri;
    Spinner ubicacionesSpinner;
    private int idSucursalSeleccionada = -1; // Inicializa con un valor no válido
    private List<Sucursal> sucursales = new ArrayList<>(); // Declaración de la lista
    List<CarritoModelo.Producto> listaDeProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pago);

        apiService = ApiClient.getInstance();

        Spinner metodosPagoSpinner = findViewById(R.id.metodosPagoSpinner);
        String[] opcionesMetodoPago = {"Transferencia", "Efectivo", "Fraccionado"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesMetodoPago);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metodosPagoSpinner.setAdapter(adapter);

        ubicacionesSpinner = findViewById(R.id.ubicacionesSpinner); // Asegúrate de que este ID sea correcto.

        listaDeProductos = new ArrayList<>(); // Aquí se inicializa la lista

        // Configurar el Spinner con las opciones de sucursales
        List<String> opcionesSucursales = new ArrayList<>();
        ArrayAdapter<String> sucursalesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesSucursales);
        sucursalesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicacionesSpinner.setAdapter(sucursalesAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        idCuentaUsuario = sharedPreferences.getInt("id_cuenta", -1); // -1 es un valor por defecto en caso de que no se encuentre nada// Obtener el idCliente de los argumentos
        Log.d("Realizar Pago", "El idCliente es: " + idCuentaUsuario);
        Button btnPagar = findViewById(R.id.pagarButton);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                if (fragment instanceof PagoTransferenciaFragment) {
                    PagoTransferenciaFragment pagoTransferenciaFragment = (PagoTransferenciaFragment) fragment;
                    imageUri = pagoTransferenciaFragment.getImageUri();
                    Log.d("RealizarPagoActivity", "URI de la imagen: " + (imageUri != null ? imageUri.toString() : "null"));
                    realizarPago();
                } else if (fragment instanceof PagoEfectivoFragment) {
                    // Aquí manejas el caso cuando el fragmento es PagoEfectivoFragment
                    Log.d("RealizarPagoActivity", "Pago en efectivo seleccionado, realizar el pago en efectivo");
                    realizarPago();
                } else {
                    Log.e("RealizarPagoActivity", "El fragmento no es una instancia de PagoTransferenciaFragment ni PagoEfectivoFragment");
                }
            }
        });

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

        // Establecer por defecto que el radioButtonOptionRetiro esté marcado
        RadioButton radioButtonRetiro = findViewById(R.id.radioButtonOptionRetiro);
        RadioButton radioButtonDomicilio = findViewById(R.id.radioButtonOptionDomicilio);
        radioButtonRetiro.setChecked(true);

        radioButtonRetiro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cargarSucursalesEnSpinner();
                }
            }
        });

        radioButtonDomicilio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String[] opcionesDomicilio = {"Casa", "Trabajo", "Otro"};
                    ArrayAdapter<String> domicilioAdapter = new ArrayAdapter<>(RealizarPagoActivity.this, android.R.layout.simple_spinner_item, opcionesDomicilio);
                    domicilioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ubicacionesSpinner.setAdapter(domicilioAdapter);
                    ubicacionesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            String ubicacionSeleccionada = (String) parentView.getItemAtPosition(position);
                            apiService.obtenerUsuario(String.valueOf(idCuentaUsuario)).enqueue(new Callback<UserResponse>() {
                                @Override
                                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                    UserResponse usuarioResponse = response.body();
                                    if (usuarioResponse != null) {
                                        User usuario = usuarioResponse.getUsuario();
                                        if (usuario != null) {
                                            // Obtener la latitud y longitud según la ubicación seleccionada
                                            switch (ubicacionSeleccionada) {
                                                case "Casa":
                                                    Ubicacion ubicacionCasa = usuario.getUbicacion1();
                                                    if (ubicacionCasa != null) {
                                                        String latitudCasa = ubicacionCasa.getLatitud();
                                                        String longitudCasa = ubicacionCasa.getLongitud();
                                                        Log.d("Pago", "Ubicación Casa - Latitud: " + latitudCasa + ", Longitud: " + longitudCasa);
                                                        obtenerSucursalPorUbicacion(latitudCasa, longitudCasa, ubicacionSeleccionada);

                                                    }
                                                    break;
                                                case "Trabajo":
                                                    Ubicacion ubicacionTrabajo = usuario.getUbicacion2();
                                                    if (ubicacionTrabajo != null) {
                                                        String latitudTrabajo = ubicacionTrabajo.getLatitud();
                                                        String longitudTrabajo = ubicacionTrabajo.getLongitud();
                                                        Log.d("Pago", "Ubicación Trabajo - Latitud: " + latitudTrabajo + ", Longitud: " + longitudTrabajo);
                                                        obtenerSucursalPorUbicacion(latitudTrabajo, longitudTrabajo, ubicacionSeleccionada);

                                                    }
                                                    break;
                                                case "Otro":
                                                    Ubicacion ubicacionOtro = usuario.getUbicacion3();
                                                    if (ubicacionOtro != null) {
                                                        String latitudOtro = ubicacionOtro.getLatitud();
                                                        String longitudOtro = ubicacionOtro.getLongitud();
                                                        Log.d("Pago", "Ubicación Otro - Latitud: " + latitudOtro + ", Longitud: " + longitudOtro);
                                                        obtenerSucursalPorUbicacion(latitudOtro, longitudOtro, ubicacionSeleccionada);
                                                    }
                                                    break;
                                            }
                                        } else {
                                            Log.e("Pago", "Usuario no disponible en la respuesta");
                                        }
                                    } else {
                                        Log.e("Pago", "Respuesta de usuario nula");
                                    }
                                }
                                @Override
                                public void onFailure(Call<UserResponse> call, Throwable t) {
                                    Log.e("Pago", "Error al obtener la ubicación del usuario", t);
                                }
                            });
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });
                }
            }
        });

        // Configurar el Spinner con las sucursales al inicio de la actividad
        apiService.obtenerSucursales().enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sucursales = response.body().getSucursalList(); // Actualiza la lista sucursales
                    cargarSucursalesEnSpinner();
                } else {
                    Log.e("Pago", "Error al obtener la lista de sucursales: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SucursalResponse> call, Throwable t) {
                Log.e("Pago", "Error al obtener la lista de sucursales", t);
            }
        });

    }

    private void cargarSucursalesEnSpinner() {
        List<String> opcionesRetiro = new ArrayList<>();
        for (Sucursal sucursal : sucursales) {
            opcionesRetiro.add(sucursal.getRazonSocial());
        }
        ArrayAdapter<String> retiroAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesRetiro);
        retiroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicacionesSpinner.setAdapter(retiroAdapter);
        ubicacionesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nombreSucursal = parent.getItemAtPosition(position).toString();
                for (Sucursal sucursal : sucursales) {
                    if (sucursal.getRazonSocial().equals(nombreSucursal)) {
                        idSucursalSeleccionada = sucursal.getIdSucursal(); // Actualiza idSucursalSeleccionada
                        Log.d("Pago", "ID de la Sucursal seleccionada: " + idSucursalSeleccionada);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No es necesario hacer nada aquí
            }
        });
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
    public void realizarPago() {
        Intent intent = getIntent();
        int idCliente = intent.getIntExtra("idCliente", -1);
        int totalPuntos = intent.getIntExtra("totalPuntos", 0);
        double totalAPagar = intent.getDoubleExtra("totalAPagar", 0.0);
        DetallesPedido detallesPedidoObjeto = (DetallesPedido) intent.getSerializableExtra("detallesPedidoObjeto");
        final String[] imageBase64 = {null};

        if (detallesPedidoObjeto != null) {
            Gson gson = new Gson();
            if (imageUri != null) {
                try {
                    byte[] imageBytes = getImageBytes(imageUri);
                    imageBase64[0] = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                } catch (IOException e) {
                    Log.e("RealizarPagoActivity", "Error al transformar la imagen a bytes", e);
                }
            }
            String detallesPedidoJson = gson.toJson(detallesPedidoObjeto);

            if (idCuentaUsuario != -1) {
                apiService.obtenerUsuario(String.valueOf(idCuentaUsuario)).enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        UserResponse usuarioResponse = response.body();
                        if (usuarioResponse != null) {
                            User usuario = usuarioResponse.getUsuario();
                            if (usuario != null) {
                                String latitud = "";
                                String longitud = "";

                                Spinner ubicacionesSpinner = findViewById(R.id.ubicacionesSpinner);
                                String ubicacionSeleccionada = ubicacionesSpinner.getSelectedItem().toString();

                                switch (ubicacionSeleccionada) {
                                    case "Casa":
                                        Ubicacion ubicacionCasa = usuario.getUbicacion1();
                                        if (ubicacionCasa != null) {
                                            latitud = ubicacionCasa.getLatitud();
                                            longitud = ubicacionCasa.getLongitud();
                                        }
                                        break;
                                    case "Trabajo":
                                        Ubicacion ubicacionTrabajo = usuario.getUbicacion2();
                                        if (ubicacionTrabajo != null) {
                                            latitud = ubicacionTrabajo.getLatitud();
                                            longitud = ubicacionTrabajo.getLongitud();
                                        }
                                        break;
                                    case "Otro":
                                        Ubicacion ubicacionOtro = usuario.getUbicacion3();
                                        if (ubicacionOtro != null) {
                                            latitud = ubicacionOtro.getLatitud();
                                            longitud = ubicacionOtro.getLongitud();
                                        }
                                        break;
                                }
                                Log.d("Pago", "Latitud: " + latitud + ", Longitud: " + longitud);
                                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                                String tipoPedido;
                                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonOptionRetiro) {
                                    tipoPedido = "R";
                                    Spinner metodosPagoSpinner = findViewById(R.id.metodosPagoSpinner);
                                    String metodoPago = metodosPagoSpinner.getSelectedItem().toString().substring(0, 1);
                                    Log.d("Pago", "Método de Pago: " + metodoPago);

                                    if (idSucursalSeleccionada != -1) { // Asegúrate de que tenga un valor válido
                                        realizarPedido(totalPuntos, totalAPagar, tipoPedido, metodoPago, idSucursalSeleccionada, "", "", detallesPedidoObjeto, detallesPedidoJson, imageBase64[0]);
                                    } else {
                                        Log.e("Pago", "ID de sucursal inválido: " + idSucursalSeleccionada);
                                    }
                                } else {
                                    tipoPedido = "D";
                                    Spinner metodosPagoSpinner = findViewById(R.id.metodosPagoSpinner);
                                    String metodoPago = metodosPagoSpinner.getSelectedItem().toString().substring(0, 1);
                                    Log.d("Pago", "Método de Pago: " + metodoPago);

                                    obtenerSucursal(totalPuntos, totalAPagar, tipoPedido, metodoPago, latitud, longitud, detallesPedidoObjeto, detallesPedidoJson, imageBase64[0]);
                                }
                            } else {
                                Log.e("Pago", "Usuario no disponible en la respuesta");
                            }
                        } else {
                            Log.e("Pago", "Respuesta de usuario nula");
                        }
                    }
                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Log.e("Pago", "Error al obtener la ubicación del usuario", t);
                    }
                });
            } else {
                Log.e("Pago", "ID de usuario no disponible");
            }
        } else {
            Log.e("Pago", "Detalles del pedido son nulos");
        }
    }
    private void obtenerSucursalPorUbicacion(String latitud, String longitud, String ubicacionSeleccionada) {
        apiService.obtenerSucursalPorUbicacion(latitud, longitud).enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                SucursalResponse sucursalResponse = response.body();
                if (sucursalResponse != null && sucursalResponse.getSucursalList() != null && !sucursalResponse.getSucursalList().isEmpty()) {
                    Sucursal sucursal = sucursalResponse.getSucursalList().get(0); // Suponiendo que solo necesitas la primera sucursal
                    String nombreSucursal = sucursal.getRazonSocial();
                    String direccionSucursal = sucursal.getDireccion();

                    Log.d("Pago", "Sucursal: " + nombreSucursal + ", Dirección: " + direccionSucursal);
                } else {
                    Log.e("Pago", "No se encontraron sucursales para las coordenadas: " + latitud + ", " + longitud);
                }
            }

            @Override
            public void onFailure(Call<SucursalResponse> call, Throwable t) {
                Log.e("Pago", "Error al obtener la sucursal por ubicación", t);
            }
        });
    }
    private void obtenerSucursal(int totalPuntos, double totalAPagar, String tipoPedido, String metodoPago, String latitud, String longitud, DetallesPedido detallesPedidoObjeto, String detallesPedidoJson, String imagenBase64) {
        apiService.obtenerSucursalPorUbicacion(latitud, longitud).enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                SucursalResponse sucursalResponse = response.body();
                if (sucursalResponse != null && sucursalResponse.getSucursalList() != null && !sucursalResponse.getSucursalList().isEmpty()) {
                    Sucursal sucursal = sucursalResponse.getSucursalList().get(0); // Suponiendo que solo necesitas la primera sucursal
                    int idSucursal = sucursal.getIdSucursal();
                    Log.d("Pago", "ID Sucursal: " + idSucursal);

                    if (idSucursal != 0) {
                        // Llamar a realizarPedido con el ID de la sucursal
                        Log.d("Pago", "ID Sucursal a enviar: " + idSucursal);
                        realizarPedido(totalPuntos, totalAPagar, tipoPedido, metodoPago, idSucursal, latitud, longitud, detallesPedidoObjeto, detallesPedidoJson, imagenBase64);
                    } else {
                        Log.e("Pago", "ID de sucursal inválido: " + idSucursal);
                    }
                } else {
                    Log.e("Pago", "No se encontraron sucursales para las coordenadas: " + latitud + ", " + longitud);
                }
            }

            @Override
            public void onFailure(Call<SucursalResponse> call, Throwable t) {
                Log.e("Pago", "Error al obtener la sucursal por ubicación", t);
            }
        });
    }

    private void realizarPedido(int totalPuntos, double totalAPagar, String tipoPedido, String metodoPago, int idSucursal, String latitud, String longitud, DetallesPedido detallesPedidoObjeto, String detallesPedidoJson, String imagenBase64) {
        // Crear RequestBody para los parámetros de texto
        RequestBody puntos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(totalPuntos));
        RequestBody precio = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(totalAPagar));
        RequestBody tipoPedidoRB = RequestBody.create(MediaType.parse("text/plain"), tipoPedido);
        RequestBody metodoPagoRB = RequestBody.create(MediaType.parse("text/plain"), metodoPago);
        RequestBody estadoPedidoRB = RequestBody.create(MediaType.parse("text/plain"), "O");
        RequestBody idSucursalRB = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idSucursal));
        RequestBody latitudRB = RequestBody.create(MediaType.parse("text/plain"), latitud);
        RequestBody longitudRB = RequestBody.create(MediaType.parse("text/plain"), longitud);
        RequestBody estadoPagoRB = RequestBody.create(MediaType.parse("text/plain"), "En revisión");
        RequestBody fechaHoraRB = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedHour));
        RequestBody fechaMinutosRB = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedMinute));
        RequestBody detallesPedidoJsonRB = RequestBody.create(MediaType.parse("text/plain"), detallesPedidoJson);

        // Crear RequestBody para la imagen base64
        RequestBody imagenBase64RB = null;
        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            imagenBase64RB = RequestBody.create(MediaType.parse("text/plain"), imagenBase64);
        }

        // Hacer la solicitud para realizar el pedido
        apiService.realizarPedido(
                String.valueOf(idCuentaUsuario),
                puntos,
                precio,
                tipoPedidoRB,
                metodoPagoRB,
                estadoPedidoRB,
                idSucursalRB,
                latitudRB,
                longitudRB,
                estadoPagoRB,
                fechaHoraRB,
                fechaMinutosRB,
                detallesPedidoJsonRB,
                imagenBase64RB
        ).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    // Éxito en la solicitud, procesar la respuesta si es necesario
                    Log.d("Pago", "Solicitud de pedido exitosa");
                    Toast.makeText(RealizarPagoActivity.this, "Pedido realizado exitosamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RealizarPagoActivity.this, PagoConfirmadoActivity.class);
                    intent.putExtra("idCliente", idCuentaUsuario); // Pasar el idCuenta
                    startActivity(intent);
                    limpiarCarrito();
                    finish();
                } else {
                    // Error en la solicitud
                    Log.e("Pago", "Error al realizar el pedido: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Pago", "Error al realizar el pedido", t);
            }
        });
    }
    private void limpiarCarrito() {
        CarritoModelo carritoModelo = CarritoModelo.getInstance();
        carritoModelo.limpiarCarrito();
        listaDeProductos.clear();

        // Reemplazar el fragmento actual por el fragmento vacío
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, new CarritoVacioFragment());
        transaction.commit();
    }

    // Método para obtener el arreglo de bytes de una imagen a partir de su URI
    private byte[] getImageBytes(Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // Lectura y escritura de la imagen en un buffer de bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

    public void showTimePickerDialog(View v) {
        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        builder.setTimeFormat(TimeFormat.CLOCK_24H);
        builder.setTitleText("Seleccionar hora");

        MaterialTimePicker materialTimePicker = builder.build();
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedHour = materialTimePicker.getHour();
                selectedMinute = materialTimePicker.getMinute();

                String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);

                TextInputEditText editTextTime = findViewById(R.id.editTextTime);
                editTextTime.setText(selectedTime);
            }
        });
        materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER");
    }
}
