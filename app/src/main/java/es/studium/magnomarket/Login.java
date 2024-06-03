package es.studium.magnomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1001;

    // componentes de GUI
    EditText editTextUsuario, editTextClave;
    Button btnAcceder;
    SwitchCompat switchGuardarCredenciales;

    // shared preferences para guardar las preferencias
    public static final String LoginCredenciales = "MagnoLogin";
    // nombres de los claves en las preferencias compartidas
    public static final String Usuario = "usuarioKey";
    public static final String Clave = "claveKey";
    public static final String UserID = "usuarioID";
    SharedPreferences sharedpreferences;
    String usuarioInput;
    String contrasenaInput;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comprobar si las credenciales están guardadas en sharedPreferences
        sharedpreferences = getSharedPreferences(LoginCredenciales, Context.MODE_PRIVATE);
        String isSharedUsuario = sharedpreferences.getString(Usuario, "");
        String isSharedClave = sharedpreferences.getString(Clave, "");

        // en el caso positivo, saltar el Login y mostrar directamente MainActivity
        if (!(isSharedUsuario.isEmpty()) && !(isSharedClave.isEmpty())) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("idUsuario", sharedpreferences.getInt("usuarioID", 0));
            startActivity(intent);
            finish(); // deshabilitar el botón back
        } else {
            setContentView(R.layout.activity_login); // establecer la pantalla de login
            assignBackgroundGradient();
            editTextUsuario = findViewById(R.id.correoElectronico);
            editTextClave = findViewById(R.id.contrasena);
            switchGuardarCredenciales = findViewById(R.id.switchSaveCredentials);
            switchGuardarCredenciales.setChecked(false);
            btnAcceder = findViewById(R.id.btnAcceder);
            btnAcceder.setEnabled(false);
            btnAcceder.setOnClickListener(this);
            // objeto textWatcher nos permite supervisar cuando el contenido de los editTexts cambia
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                // si ocurre un cambio dentro de los editText
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // si los los editTexts no están vacíos - se habilita el botón 'Acceder'
                    if (!(editTextUsuario.getText().toString().isBlank()) && !(editTextClave.getText().toString().isBlank())) {
                        btnAcceder.setEnabled(true);
                    } else {
                        btnAcceder.setEnabled(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            };
            editTextUsuario.addTextChangedListener(textWatcher);
            editTextClave.addTextChangedListener(textWatcher);

            // método para comprobar permisos
            checkPermissions();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);
            } else {
                // esperar hasta un click
            }
        } else {
            // esperar hasta un click
        }
    }

    private void comprobarCredenciales() {
        // llamar al método de BDConexion para comprobar las credenciales del usuario de la app
        BDConexion.comprobarCredenciales(usuarioInput, contrasenaInput, new BDConexion.LoginCallback() {
            @Override
            public void onLoginResult(boolean success, int idUsuario) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (success) {
                                // si el usuario existe en la tabla 'usuarios' de DB 'magno', seguir a entrar en sesión
                                performLogin(usuarioInput, contrasenaInput, idUsuario);
                            } else {
                                // credenciales incorrectas
                                toast = Toast.makeText(getApplication(), R.string.credenciales_incorrectas, Toast.LENGTH_SHORT);
                                View toastView = toast.getView();
                                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                toastMessage.setTextAppearance(R.style.ToastStyle);
                                toastView.setBackground(getResources().getDrawable(R.drawable.dialog_background));
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_INTERNET) {
            // Comprobar si la app tiene los permisos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // en caso positivo, seguir a comprobar las credenciales
                comprobarCredenciales();
            } else {
                // caso negativo
                toast = Toast.makeText(this, "Internet permission denied", Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextAppearance(R.style.ToastStyle);
                toastView.setBackground(getResources().getDrawable(R.drawable.dialog_background));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private void performLogin(String usuario, String contrasena, int idUsuario) {
        MainActivity.idUsuario = idUsuario;
        // si el switch para guardar las credenciales está activado
        if (switchGuardarCredenciales.isChecked()) {
            // guardar las credenciales en sharedPreferences
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Usuario, usuario);
                editor.putString(Clave, contrasena);
                editor.putInt(UserID, MainActivity.idUsuario);
                editor.commit();
            }

        // pasar a la ventana principal a través del Intent
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.putExtra("correoUsuario", usuarioInput);
        intentMain.putExtra("contrasenaUsuario", contrasenaInput);
        intentMain.putExtra("idUsuario", MainActivity.idUsuario);
        intentMain.putExtra("sharedPrefsClicked", switchGuardarCredenciales.isChecked());
        startActivity(intentMain);
        finish();
    }

    @Override
    public void onClick(View v) {
        // Get username and password when the login button is clicked
        if (v.getId() == R.id.btnAcceder) {
            usuarioInput = editTextUsuario.getText().toString();
            contrasenaInput = editTextClave.getText().toString();
            if (!usuarioInput.isEmpty() && !contrasenaInput.isEmpty()) {
                comprobarCredenciales();
            } else {
                toast = Toast.makeText(this, "Por favor, introduce usuario y contraseña", Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextAppearance(R.style.ToastStyle);
                toastView.setBackground(getResources().getDrawable(R.drawable.dialog_background));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private void assignBackgroundGradient() {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColors(
                new int[] {
                        getResources().getColor(R.color.azul_claro),
                        getResources().getColor(R.color.white)
                }
        );
        gradient.setOrientation(
                GradientDrawable.Orientation.BOTTOM_TOP
        );
        gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradient.setShape(GradientDrawable.RECTANGLE);

        findViewById(R.id.login).setBackground(gradient);
    }
}