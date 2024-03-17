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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
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
    private User user;
    private Spinner ubicacionesSpinner;
    private List<Sucursal> sucursalList;
    private String[] ubicacionesRetiro;

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

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        idCuentaUsuario = sharedPreferences.getInt("id_cuenta", -1); // -1 es un valor por defecto en caso de que no se encuentre nada

        Button btnPagar = findViewById(R.id.pagarButton);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                if (fragment instanceof PagoTransferenciaFragment) {
                    PagoTransferenciaFragment pagoTransferenciaFragment = (PagoTransferenciaFragment) fragment;
                    imageUri = pagoTransferenciaFragment.getImageUri();
                    Log.d("RealizarPagoActivity", "URI de la imagen: " + (imageUri != null ? imageUri.toString() : "null"));

                    // Lógica de realizarPago() y demás debe estar aquí dentro del if
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
        radioButtonRetiro.setChecked(true);

        // Inicializar el Spinner de ubicaciones
        ubicacionesSpinner = findViewById(R.id.ubicacionesSpinner);
        ubicacionesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Verificar si se ha seleccionado la opción de domicilio
                RadioButton radioButtonDomicilio = findViewById(R.id.radioButtonOptionDomicilio);
                if (radioButtonDomicilio.isChecked()) {
                    // Obtener la ubicación de domicilio seleccionada
                    String ubicacion = ubicacionesRetiro[position];

                    // Mostrar la información en el Logcat
                    if (ubicacion != null) {

                        Log.d("RealizarPagoActivity", "ubicacion: " + ubicacion);
                    }
                } else {
                    // Obtener la sucursal de retiro seleccionada
                    Sucursal sucursal = sucursalList.get(position);

                    // Mostrar la información en el Logcat
                    if (sucursal != null) {
                        int idSucursal = sucursal.getIdSucursal();
                        String nombre = sucursal.getRazonSocial();
                        // Agregar cualquier otra información relevante de la sucursal

                        Log.d("RealizarPagoActivity", "ID de sucursal: " + idSucursal);
                        Log.d("RealizarPagoActivity", "Nombre de sucursal: " + nombre);
                        // Agregar más logs según sea necesario
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se necesita hacer nada aquí
            }
        });

        // Configurar el listener para el radioGroup
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Actualizar el contenido del Spinner según la selección del usuario
                if (checkedId == R.id.radioButtonOptionDomicilio) {
                    User usuario = obtenerUsuarioPorIdCuenta(idCuentaUsuario);
                    if (usuario != null) {
                        Ubicacion ubicacion1 = usuario.getUbicacion1();
                        Ubicacion ubicacion2 = usuario.getUbicacion2();
                        Ubicacion ubicacion3 = usuario.getUbicacion3();

                        // Crear lista de ubicaciones domicilio
                        List<Ubicacion> ubicacionesDomicilio = new ArrayList<>();
                        if (ubicacion1 != null) ubicacionesDomicilio.add(ubicacion1);
                        if (ubicacion2 != null) ubicacionesDomicilio.add(ubicacion2);
                        if (ubicacion3 != null) ubicacionesDomicilio.add(ubicacion3);

                        // Establecer las ubicaciones domicilio en el spinner
                        cargarUbicacionesDomicilio(ubicacionesDomicilio);
                    }
                } else {
                    // Cargar las ubicaciones de retiro en el spinner
                    cargarUbicacionesSucursales(sucursalList);
                }
            }
        });
        sucursalList = new ArrayList<>();
        obtenerSucursales();
    }

    private User obtenerUsuarioPorIdCuenta(int idCuenta) {
        apiService.obtenerUsuario(String.valueOf(idCuenta)).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse usuarioResponse = response.body();
                    if (usuarioResponse != null) {
                        user = usuarioResponse.getUsuario(); // Asignar el usuario obtenido a la variable de instancia
                        // Llamar a otro método para realizar cualquier acción necesaria con el objeto User
                        procesarUsuario(user);
                    } else {
                        Log.e("RealizarPagoActivity", "Respuesta de usuario nula");
                    }
                } else {
                    Log.e("RealizarPagoActivity", "Error al obtener el usuario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("RealizarPagoActivity", "Error al obtener el usuario", t);
            }
        });
        return user;
    }

    // Método para procesar el objeto User obtenido
    private void procesarUsuario(User usuario) {
        // Aquí puedes realizar cualquier acción con el objeto de usuario obtenido
        if (usuario != null) {
            // Hacer algo con el usuario
            Log.d("RealizarPagoActivity", "Usuario obtenido: " + usuario);
        } else {
            Log.e("RealizarPagoActivity", "Usuario no encontrado");
        }
    }

    // Método para cargar las ubicaciones de las sucursales en el spinner
    private void cargarUbicacionesSucursales(List<Sucursal> sucursales) {
        List<String> nombresSucursales = new ArrayList<>();
        for (Sucursal sucursal : sucursales) {
            nombresSucursales.add(sucursal.getRazonSocial());
        }

        ArrayAdapter<String> ubicacionesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresSucursales);
        ubicacionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicacionesSpinner.setAdapter(ubicacionesAdapter);
    }

    // Método para cargar las ubicaciones de las ubicaciones de domicilio en el spinner
    private void cargarUbicacionesDomicilio(List<Ubicacion> ubicaciones) {
        List<String> ubicacionesConNombres = new ArrayList<>();
        for (int i = 0; i < ubicaciones.size(); i++) {
            String nombreUbicacion;
            switch (i) {
                case 0:
                    nombreUbicacion = "Casa";
                    break;
                case 1:
                    nombreUbicacion = "Trabajo";
                    break;
                case 2:
                    nombreUbicacion = "Otro";
                    break;
                default:
                    nombreUbicacion = "Ubicación " + (i + 1);
                    break;
            }
            ubicacionesConNombres.add(nombreUbicacion);
        }

        ArrayAdapter<String> ubicacionesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ubicacionesConNombres);
        ubicacionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicacionesSpinner.setAdapter(ubicacionesAdapter);
    }

    private void obtenerSucursales() {
        apiService.obtenerSucursales().enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                SucursalResponse sucursalResponse = response.body();
                if (sucursalResponse != null && sucursalResponse.getSucursalList() != null) {
                    sucursalList = sucursalResponse.getSucursalList();
                    ubicacionesRetiro = obtenerNombresSucursales();
                    cargarUbicaciones(ubicacionesRetiro);
                }
            }

            @Override
            public void onFailure(Call<SucursalResponse> call, Throwable t) {
                Log.e("RealizarPagoActivity", "Error al obtener las sucursales", t);
            }
        });
    }

    private String[] obtenerNombresSucursales() {
        String[] nombresSucursales = new String[sucursalList.size()];
        for (int i = 0; i < sucursalList.size(); i++) {
            nombresSucursales[i] = sucursalList.get(i).getRazonSocial();
        }
        return nombresSucursales;
    }

    private void cargarUbicaciones(String[] ubicaciones) {
        ArrayAdapter<String> ubicacionesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ubicaciones);
        ubicacionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicacionesSpinner.setAdapter(ubicacionesAdapter);
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
        // Obtén los datos necesarios para realizar el pago
        Intent intent = getIntent();
        int idCliente = intent.getIntExtra("idCliente", -1);
        int totalPuntos = intent.getIntExtra("totalPuntos", 0);
        double totalAPagar = intent.getDoubleExtra("totalAPagar", 0.0);
        DetallesPedido detallesPedidoObjeto = (DetallesPedido) intent.getSerializableExtra("detallesPedidoObjeto");

        final String[] imageBase64 = {null};  // Declarar un array de tamaño 1

        if (detallesPedidoObjeto != null) {
            // Crear Gson object
            Gson gson = new Gson();

            if (imageUri != null) {
                try {
                    // Obtener el arreglo de bytes de la imagen seleccionada
                    byte[] imageBytes = getImageBytes(imageUri);

                    // Convertir los bytes de la imagen a Base64 y guardar en el array
                    imageBase64[0] = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    // Imprimir el contenido de la imagen en el Logcat en formato Base64
                    Log.d("RealizarPagoActivity", "Imagen en Base64: " + imageBase64[0]);
                } catch (IOException e) {
                    Log.e("RealizarPagoActivity", "Error al transformar la imagen a bytes", e);
                }
            }

            // Convertir detallesPedidoObjeto a JSON String
            String detallesPedidoJson = gson.toJson(detallesPedidoObjeto);

            if (idCuentaUsuario != -1) {
                apiService.obtenerUsuario(String.valueOf(idCuentaUsuario)).enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        UserResponse usuarioResponse = response.body();
                        if (usuarioResponse != null) {
                            User usuario = usuarioResponse.getUsuario();
                            if (usuario != null) {
                                Ubicacion ubicacion = usuario.getUbicacion1();
                                if (ubicacion != null) {
                                    String latitud = ubicacion.getLatitud();
                                    String longitud = ubicacion.getLongitud();
                                    Log.d("Pago", "Latitud: " + latitud + ", Longitud: " + longitud);

                                    // Obtener el tipo de pedido seleccionado (R para retiro, D para domicilio)
                                    RadioGroup radioGroup = findViewById(R.id.radioGroup);
                                    String tipoPedido;
                                    if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonOptionRetiro) {
                                        tipoPedido = "R";
                                    } else {
                                        tipoPedido = "D";
                                    }
                                    Log.d("Pago", "Tipo de Pedido: " + tipoPedido);

                                    // Obtener el método de pago seleccionado (E para efectivo, T para transferencia, F para fraccionado)
                                    Spinner metodosPagoSpinner = findViewById(R.id.metodosPagoSpinner);
                                    String metodoPago = metodosPagoSpinner.getSelectedItem().toString().substring(0, 1);
                                    Log.d("Pago", "Método de Pago: " + metodoPago);

                                    // Hacer la solicitud para obtener la sucursal
                                    obtenerSucursal(totalPuntos, totalAPagar, tipoPedido, metodoPago, latitud, longitud, detallesPedidoObjeto, detallesPedidoJson, imageBase64[0]);
                                } else {
                                    Log.e("Pago", "Ubicación no disponible en la respuesta del usuario");
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

    private void obtenerSucursal(int totalPuntos, double totalAPagar, String tipoPedido, String metodoPago, String latitud, String longitud, DetallesPedido detallesPedidoObjeto, String detallesPedidoJson, String imagenBase64) {
        apiService.obtenerSucursalPorUbicacion(latitud, longitud).enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                SucursalResponse sucursalResponse = response.body();
                if (sucursalResponse != null && sucursalResponse.getSucursalList() != null && !sucursalResponse.getSucursalList().isEmpty()) {
                    int idSucursal = sucursalResponse.getSucursalList().get(0).getIdSucursal();
                    Log.d("Pago", "ID Sucursal: " + idSucursal);

                    if (idSucursal != 0) {
                        // Llamar a realizarPedido con el ID de la sucursal
                        Log.d("Pago", "ID Sucursal a enviar: " + idSucursal);
                        realizarPedido(totalPuntos, totalAPagar, tipoPedido, metodoPago, idSucursal, latitud, longitud, detallesPedidoObjeto, detallesPedidoJson, imagenBase64);
                    } else {
                        Log.e("Pago", "ID de sucursal inválido: " + idSucursal);
                    }
                } else {
                    Log.e("Pago", "Sucursal no disponible en la respuesta o lista vacía");
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