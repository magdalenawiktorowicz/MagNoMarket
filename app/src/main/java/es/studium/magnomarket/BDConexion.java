package es.studium.magnomarket;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import es.studium.magnomarket.ui.midespensa.MiDespensaFragment;
import okhttp3.*;

public class BDConexion {

    public static String BASE_URL = "http://10.0.2.2/ApiRestMagno/";

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
                .url(BASE_URL + "usuarios.php")
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
                        String jsonResponse = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            loginSuccess = jsonObject.getBoolean("success");
                            if (loginSuccess) {
                                idUsuario = jsonObject.getInt("userId");
                            }
                            // Notificar al callback con el resultado
                            callback.onLoginResult(loginSuccess, idUsuario);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onLoginResult(loginSuccess, idUsuario);
                        }
                    } else {
                        callback.onLoginResult(loginSuccess, idUsuario);
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("ErrorCallback", Log.getStackTraceString(e));
                    callback.onLoginResult(false, 0);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Consulta ProductoDespensa
    public static ArrayList<ProductoDespensa> consultarProductosDespensa(int idUsuario, MiDespensaFragment.ProductosDespensaCallback callback) {

        ArrayList<ProductoDespensa> productosDespensa = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "productosdespensa.php?idUsuarioFK=" + idUsuario)
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
                        callback.onProductosDespensaLoaded(productosDespensa);

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

    // Consulta Categorías
    public static ArrayList<Categoria> consultarCategorias() {

        ArrayList<Categoria> categorias = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "categorias.php")
                .build();
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
    public static void anadirProductoDespensa(ProductoDespensa productoDespensa, Callback callback) {
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
                .url(BASE_URL + "productosdespensa.php")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", e.getMessage());
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("MainActivity", String.valueOf(response));
                callback.onResponse(call, response);
            }
        });
    }

    // ProductoDespensa - modificación
    public static void modificarProductoDespensa(ProductoDespensa producto, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.parse(BASE_URL + "productosdespensa.php").newBuilder();
        queryUrlBuilder.addQueryParameter("idProductoDespensa", String.valueOf(producto.getIdProductoDespensa()));
        queryUrlBuilder.addQueryParameter("nombreProductoDespensa", producto.getNombreProductoDespensa().toString());
        queryUrlBuilder.addQueryParameter("imagenProductoDespensa", producto.getImagenProductoDespensa().toString());
        queryUrlBuilder.addQueryParameter("fechaCaducidadProductoDespensa", producto.getFechaCaducidadProductoDespensa().toString());
        queryUrlBuilder.addQueryParameter("cantidadProductoDespensa", String.valueOf(producto.getCantidadProductoDespensa()));
        queryUrlBuilder.addQueryParameter("unidadProductoDespensa", producto.getUnidadProductoDespensa().toString());
        queryUrlBuilder.addQueryParameter("autoanadirAListaCompraDespensa", String.valueOf(producto.getAutoanadirAListaCompraDespensa()));
        queryUrlBuilder.addQueryParameter("cantidadMinParaAnadirDespensa", String.valueOf(producto.getCantidadMinParaAnadirDespensa()));
        queryUrlBuilder.addQueryParameter("tiendaProductoDespensa", producto.getTiendaProductoDespensa().toString());
        queryUrlBuilder.addQueryParameter("idCategoriaFK", String.valueOf(producto.getIdCategoriaFK()));
        queryUrlBuilder.addQueryParameter("idUsuarioFK", String.valueOf(producto.getIdUsuarioFK()));
        RequestBody requestBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    // ProductoDespensa - baja
    public static void borrarProductoDespensa(ProductoDespensa producto, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "productosdespensa.php?idProductoDespensa=" + producto.getIdProductoDespensa())
                .delete()
                .build();
        client.newCall(request).enqueue(callback);
    }

}
