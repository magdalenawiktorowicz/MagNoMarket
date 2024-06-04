package es.studium.magnomarket.ui.midespensa;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import es.studium.magnomarket.BDConexion;
import es.studium.magnomarket.Categoria;
import es.studium.magnomarket.MainActivity;
import es.studium.magnomarket.ProductoDespensa;
import es.studium.magnomarket.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NuevoProductoDespensa extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageView nuevoProductoPhoto;
    EditText editTextNombreProducto;
    Spinner spinnerCategorias;
    ArrayList<Categoria> categorias;
    ImageButton imageButtonCantidadMinus, imageButtonCantidadPlus;
    EditText editTextCantidad;
    Spinner spinnerUnidades;
    Button btnFechaCaducidad;
    SwitchCompat switchAnadirAuto;
    ImageButton imageButtonCantidadMinMinus, imageButtonCantidadMinPlus;
    EditText editTextCantidadMin;
    EditText editTextTiendaProcedente;
    Button btnAceptar, btnAnadirListaCompra, btnCancelar;
    private Uri imageUri = null;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private DatePickerDialog datePickerDialog;
    Toast toast;

    public NuevoProductoDespensa() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categorias = BDConexion.consultarCategorias();
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // pasar al MiDespensa fragment al pulsar el botón de atrás
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevo_producto_despensa, container, false);

        // ocultar el menú de navegación
        ((MainActivity) getActivity()).hideBottomNavigationView();

        nuevoProductoPhoto = view.findViewById(R.id.nuevoProductoPhoto);
        nuevoProductoPhoto.setOnClickListener(this);
        editTextNombreProducto = view.findViewById(R.id.editTextNombreProducto);

        // crear un arrayList para guardar todas las Categorias y mostrarlas en el Spinner
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(getResources().getString(R.string.spinnerPrompt));
        // añadir cada categoria al Spinner
        for (Categoria c : categorias) {
            spinnerArray.add(c.toString());
        }
        spinnerCategorias = view.findViewById(R.id.spinnerCategorias);
        // crear un adaptador para el Spinner y añadirle la lista de Categorías
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerCategorias.setAdapter(spinnerArrayAdapter);
        spinnerCategorias.setOnItemSelectedListener(this);
        imageButtonCantidadMinus = view.findViewById(R.id.imageButtonCantidadMinus);
        imageButtonCantidadMinus.setOnClickListener(this);
        imageButtonCantidadPlus = view.findViewById(R.id.imageButtonCantidadPlus);
        imageButtonCantidadPlus.setOnClickListener(this);
        editTextCantidad = view.findViewById(R.id.editTextCantidad);
        spinnerUnidades = view.findViewById(R.id.spinnerUnidades);
        btnFechaCaducidad = view.findViewById(R.id.btnFechaCaducidad);
        btnFechaCaducidad.setText(getTodayDate());
        btnFechaCaducidad.setOnClickListener(this);
        switchAnadirAuto = view.findViewById(R.id.switchAnadirAuto);
        imageButtonCantidadMinMinus = view.findViewById(R.id.imageButtonCantidadMinMinus);
        imageButtonCantidadMinMinus.setOnClickListener(this);
        imageButtonCantidadMinPlus = view.findViewById(R.id.imageButtonCantidadMinPlus);
        imageButtonCantidadMinPlus.setOnClickListener(this);
        editTextCantidadMin = view.findViewById(R.id.editTextCantidadMin);
        editTextTiendaProcedente = view.findViewById(R.id.editTextTiendaProcedente);
        btnAceptar = view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);
        btnAnadirListaCompra = view.findViewById(R.id.btnAnadirListaCompra);
        btnAnadirListaCompra.setOnClickListener(this);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        // establecer el título en la barra superior
        if (getActivity() != null) {
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.alta_producto_despensa_titulo);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // mostrar de nuevo el menú de navigación
        ((MainActivity) getActivity()).showBottomNavigationView();
    }

    // obtener la fecha de hoy
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == nuevoProductoPhoto.getId()) {
            // mostrar el menú para añadir la imagen
            showInputImageDialog();
        }
        // controlar el comportamiento de los botones de cantidad
        else if (v.getId() == imageButtonCantidadMinus.getId()) {
            int currentNumber = Integer.parseInt(editTextCantidad.getText().toString());
            if (currentNumber > 1) {
                editTextCantidad.setText(String.valueOf(currentNumber - 1));
            }
        } else if (v.getId() == imageButtonCantidadPlus.getId()) {
            int currentNumber = Integer.parseInt(editTextCantidad.getText().toString());
            editTextCantidad.setText(String.valueOf(currentNumber + 1));
        }
        // mostrar el calendario y al seleccionar la fecha
        else if (v.getId() == btnFechaCaducidad.getId()) {
            Calendar cal = null;
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    btnFechaCaducidad.setText(date);
                }
            };
            cal = cal.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
            datePickerDialog.show();
        } else if (v.getId() == imageButtonCantidadMinMinus.getId()) {
            int currentNumber = Integer.parseInt(editTextCantidadMin.getText().toString());
            if (currentNumber > 1) {
                editTextCantidadMin.setText(String.valueOf(currentNumber - 1));
            }
        } else if (v.getId() == imageButtonCantidadMinPlus.getId()) {
            int currentNumber = Integer.parseInt(editTextCantidadMin.getText().toString());
            editTextCantidadMin.setText(String.valueOf(currentNumber + 1));
        } else if (v.getId() == btnAceptar.getId()) {
            // comprobar los datos
            if (comprobarDatos()) {
                int autoAnadirListaCompra = switchAnadirAuto.isChecked() ? 1 : 0;
                String[] dateFromButton = (btnFechaCaducidad.getText().toString()).split("/");
                LocalDate fechaCad = LocalDate.of(Integer.parseInt(dateFromButton[2]), Integer.parseInt(dateFromButton[1]), Integer.parseInt(dateFromButton[0]));
                // crear una instancia de ProductoDespensa
                ProductoDespensa pd = new ProductoDespensa(
                        editTextNombreProducto.getText().toString(),
                        String.valueOf(imageUri),
                        fechaCad,
                        Integer.parseInt(editTextCantidad.getText().toString()),
                        spinnerUnidades.getSelectedItem().toString(),
                        autoAnadirListaCompra,
                        Integer.parseInt(editTextCantidadMin.getText().toString()),
                        editTextTiendaProcedente.getText().toString(),
                        spinnerCategorias.getSelectedItemPosition(),
                        MainActivity.idUsuario);
                // método para realizar el alta del producto en la BD, pasándo como parámetro el producto
                BDConexion.anadirProductoDespensa(pd, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            toast = Toast.makeText(getContext(), R.string.operacion_no_realizada, Toast.LENGTH_SHORT);
                            makeToast();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (response.code() == 200) {
                                // alta realizada correctamente
                                toast = Toast.makeText(getContext(), R.string.operacion_realizada, Toast.LENGTH_SHORT);
                                makeToast();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                getContext().startActivity(intent);
                            } else {
                                toast = Toast.makeText(getContext(), R.string.operacion_no_realizada, Toast.LENGTH_SHORT);
                                makeToast();                            }
                        });
                    }
                });
            } else {
                // falta datos a rellenar
                toast = Toast.makeText(getContext(), R.string.toast_falta_nombre_categoria_producto, Toast.LENGTH_SHORT);
                makeToast();            }
        } else if (v.getId() == btnAnadirListaCompra.getId()) {
            // to do
        } else if (v.getId() == btnCancelar.getId()) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean comprobarDatos() {
        if (!editTextNombreProducto.getText().toString().isBlank() && (spinnerCategorias.getSelectedItemPosition() != 0)) {
            return true;
        }
        return false;
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    // método para mostrar el menú para añadir una imagen
    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(getContext(), nuevoProductoPhoto);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Cámara"); // hacer una foto
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Galeria"); // seleccionar una imagén del móvil
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1) {
                    // comprobar los permisos de la cámara
                    if (checkCameraPermissions()) {
                        pickImageCamera(); // método para hacer una foto
                    } else {
                        requestCameraPermissions();
                    }
                } else if (item.getItemId() == 2) {
                    // comprobar los permisos a los archivos del móvil
                    if (checkStoragePermission()) {
                        pickImageGallery(); // método para seleccionar una imagen
                    } else {
                        requestStoragePermission();
                    }
                }
                return false;
            }
        });
    }

    // método para seleccionar una foto del móvil
    private void pickImageGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // al seleccionar la foto del móvil
                        imageUri = result.getData().getData(); // obtener su Uri
                        // asignarle al producto
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                            nuevoProductoPhoto.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );

    // método para hacer una foto
    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");
        imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // al hacer una foto, recibir la imagen
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // asignar la Uri de la foto al producto
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                            nuevoProductoPhoto.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );

    // comprobar los permisos a los archivos del móvil
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // pedir los permisos a los archivos del móvil
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    // comprobar los permisos a la cámara
    private boolean checkCameraPermissions() {
        boolean cameraResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraResult && storageResult;
    }

    // pedir los permisos a la cámara
    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    // método para personalizar un Toast
    private void makeToast() {
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextAppearance(R.style.ToastStyle);
        toastView.setBackground(getResources().getDrawable(R.drawable.dialog_background));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}