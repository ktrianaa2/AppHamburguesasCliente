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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.apphamburguesascliente.Modelos.SucursalResponse;
import com.example.apphamburguesascliente.Modelos.Ubicacion;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealizarPagoActivity extends AppCompatActivity {
    private ApiService apiService;
    private int idCuentaUsuario;
    private int selectedHour = 0;
    private int selectedMinute = 0;
    private Uri imageUri;

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
                } else {
                    Log.e("RealizarPagoActivity", "El fragmento no es una instancia de PagoTransferenciaFragment");
                }
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment;
                if (checkedId == R.id.radioButtonOptionDomicilio) {
                    fragment = new DomicilioUbicacionFragment();
                } else {
                    fragment = new RetiroSucursalFragment();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerSelector, fragment)
                        .commit();
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
        // Obtén los datos necesarios para realizar el pago
        Intent intent = getIntent();
        int idCliente = intent.getIntExtra("idCliente", -1);
        int totalPuntos = intent.getIntExtra("totalPuntos", 0);
        double totalAPagar = intent.getDoubleExtra("totalAPagar", 0.0);
        DetallesPedido detallesPedidoObjeto = (DetallesPedido) intent.getSerializableExtra("detallesPedidoObjeto");

        if (detallesPedidoObjeto != null) {
            // Crear Gson object
            Gson gson = new Gson();

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
                                    obtenerSucursal(idCliente, totalPuntos, totalAPagar, tipoPedido, metodoPago, latitud, longitud, detallesPedidoObjeto, detallesPedidoJson);
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

    private void obtenerSucursal(int idCliente, int totalPuntos, double totalAPagar, String tipoPedido, String metodoPago, String latitud, String longitud, DetallesPedido detallesPedidoObjeto, String detallesPedidoJson) {
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
                        realizarPedido(idCliente, totalPuntos, totalAPagar, tipoPedido, metodoPago, idSucursal, latitud, longitud, detallesPedidoObjeto, detallesPedidoJson);
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

    private void realizarPedido(int idCliente, int totalPuntos, double totalAPagar, String tipoPedido, String metodoPago, int idSucursal, String latitud, String longitud, DetallesPedido detallesPedidoObjeto, String detallesPedidoJson) {
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

        // Crear MultipartBody.Part para la imagen si existe
        MultipartBody.Part imagenPart = null;
        if (tipoPedido.equals("T")) {  // Si el tipo de pago es "T", entonces hay una imagen
            if (imageUri != null) {  // Asumiendo que imageUri es la Uri de la imagen seleccionada
                File file = new File(getRealPathFromURI(imageUri));  // Obtener el archivo real desde la Uri
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                imagenPart = MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);

                // Log para verificar si la imagen está presente
                Log.d("RealizarPagoActivity", "Imagen encontrada: " + file.getName());
            } else {
                // Log si no se encuentra la imagen
                Log.d("RealizarPagoActivity", "Imagen no encontrada");
            }
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
                imagenPart
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
    private String getRealPathFromURI(Uri contentUri) {
        String filePath = null;
        try {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    filePath = cursor.getString(index);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("RealizarPagoActivity", "Error al obtener la ruta real de la imagen", e);
        }
        return filePath;
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
