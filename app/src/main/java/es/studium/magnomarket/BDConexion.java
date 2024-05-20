package es.studium.magnomarket;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import okhttp3.*;

public class BDConexion {

    public interface LoginCallback {
        void onLoginResult(boolean success, int idUsuario);
    }


    public static void comprobarCredenciales(String correo, String contrasena, final LoginCallback callback) {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("email", correo)
                .add("password", contrasena)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/APIRestMagno/usuarios.php")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        try {
            call.enqueue(new Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    boolean loginSuccess = false;
                    int idUsuario = 0;
                    if (response.isSuccessful()) {
                        // Parse JSON response
                        String jsonResponse = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            loginSuccess = jsonObject.getBoolean("success");
                            if (loginSuccess) {
                                idUsuario = jsonObject.getInt("userId");
                            }
                            // Notify the callback with the result
                            callback.onLoginResult(loginSuccess, idUsuario);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            callback.onLoginResult(loginSuccess, idUsuario); // Notify callback about failure
                        }
                    } else {
                        // Handle unsuccessful response
                        callback.onLoginResult(loginSuccess, idUsuario); // Notify callback about failure
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    // Handle failure
                    Log.e("A", Log.getStackTraceString(e));
                    callback.onLoginResult(false, 0); // Notify callback about failure
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // consulta ProductoDespensa
    public static ArrayList<ProductoDespensa> consultarProductosDespensa(int idUsuario) {

        ArrayList<ProductoDespensa> productosDespensa = new ArrayList<>();

// Crear una instancia de OkHttpClient
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiRestMagno/productosdespensa.php?idUsuarioFK=" + idUsuario)
                .build();

// Crear una llamada asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray result = new JSONArray(response.body().string());
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            int idProductoDespensa = jsonObject.getInt("idProductoDespensa");
                            String nombreProductoDespensa = jsonObject.getString("nombreProductoDespensa");
                            String imagenProductoDespensa = jsonObject.getString("imagenProductoDespensa");
                            //String fechaCaducidadProductoDespensa = jsonObject.getString("fechaCaducidadProductoDespensa");
                            String[] fc = jsonObject.getString("fechaCaducidadProductoDespensa").split("-");
                            LocalDate fechaCaducidadProductoDespensa = LocalDate.of(Integer.parseInt(fc[0]), Integer.parseInt(fc[1]), Integer.parseInt(fc[2]));
                            int cantidadProductoDespensa = jsonObject.getInt("cantidadProductoDespensa");
                            String unidadProductoDespensa = jsonObject.getString("unidadProductoDespensa");
                            int autoanadirAListaCompraDespensa = jsonObject.getInt("autoanadirAListaCompraDespensa");
                            int cantidadMinParaAnadirDespensa = jsonObject.getInt("cantidadMinParaAnadirDespensa");
                            String tiendaProductoDespensa = jsonObject.getString("tiendaProductoDespensa");
                            int idCategoriaFK = jsonObject.getInt("idCategoriaFK");
                            int idUsuarioFK = jsonObject.getInt("idUsuarioFK");

                            productosDespensa.add(new ProductoDespensa(idProductoDespensa, nombreProductoDespensa, imagenProductoDespensa, fechaCaducidadProductoDespensa, cantidadProductoDespensa, unidadProductoDespensa, autoanadirAListaCompraDespensa, cantidadMinParaAnadirDespensa, tiendaProductoDespensa, idCategoriaFK, idUsuarioFK));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("MainActivity", response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", e.getMessage());
            }
        });

        return productosDespensa;
    }

    // consulta categorías
    public static ArrayList<Categoria> consultarCategorias() {

        ArrayList<Categoria> categorias = new ArrayList<>();

    // Crear una instancia de OkHttpClient
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiRestMagno/categorias.php")
                .build();

// Crear una llamada asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray result = new JSONArray(response.body().string());
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            int idCategoria = jsonObject.getInt("idCategoria");
                            String nombreCategoria = jsonObject.getString("nombreCategoria");

                            categorias.add(new Categoria(idCategoria, nombreCategoria));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("MainActivity", response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", e.getMessage());
            }
        });

        return categorias;
    }

    // ProductoDespensa - Alta
    public static int altaProductoDespensa(ProductoDespensa productoDespensa, Callback callback) {
        int resultado = 0;
        //String[] expiryDateArray = productoDespensa.getFechaCaducidadProductoDespensa().split("/");
        //String expiryDate = expiryDateArray[2] + "-" + expiryDateArray[1] + "-" + expiryDateArray[0];
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("nombreProductoDespensa", productoDespensa.getNombreProductoDespensa())
                .add("imagenProductoDespensa", productoDespensa.getImagenProductoDespensa())
                .add("fechaCaducidadProductoDespensa", productoDespensa.getFechaCaducidadProductoDespensa().toString())
                .add("cantidadProductoDespensa", String.valueOf(productoDespensa.getCantidadProductoDespensa()))
                .add("unidadProductoDespensa", productoDespensa.getUnidadProductoDespensa())
                .add("autoanadirAListaCompraDespensa", String.valueOf(productoDespensa.getAutoanadirAListaCompraDespensa()))
                .add("cantidadMinParaAnadirDespensa", String.valueOf(productoDespensa.getCantidadMinParaAnadirDespensa()))
                .add("tiendaProductoDespensa", productoDespensa.getTiendaProductoDespensa())
                .add("idCategoriaFK", String.valueOf(productoDespensa.getIdCategoriaFK()))
                .add("idUsuarioFK", String.valueOf(productoDespensa.getIdUsuarioFK()))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiRestMagno/productosdespensa.php")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", e.getMessage());
                callback.onFailure(call, e); // Forward the failure to the provided callback
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int resultado = response.code();
                Log.i("MainActivity", String.valueOf(response));
                callback.onResponse(call, response); // Forward the response to the provided callback
            }
        });
        return resultado;
    }
}
