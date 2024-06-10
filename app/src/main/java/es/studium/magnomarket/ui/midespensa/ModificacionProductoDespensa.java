package es.studium.magnomarket.ui.midespensa;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class ModificacionProductoDespensa extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ProductoDespensa producto;

    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private ConstraintLayout partialContent, fullContent;
    // contador de los fragmentos abiertos para controlar la visibilidad del menú de navegación
    private static int fragmentCount = 0;
    ArrayList<Categoria> categorias;

    // componentes de partialContent:
    EditText editTextModNombre, editTextModCantidad;
    ImageButton imageButtonModCantidadMinus, imageButtonModCantidadPlus;
    Spinner spinnerModUnidades;
    Button btnModFechaCaducidad, btnModAceptar, btnModAnadirListaCompra, btnModEliminar;

    // componentes de fullContent:
    ImageView productoPhotoMOD;
    EditText editTextModNombreProductoMOD, editTextCantidadMinMOD, editTextTiendaProcedenteMOD, editTextCantidadMOD;
    Spinner spinnerCategoriasMOD, spinnerUnidadesMOD;
    ImageButton imageButtonCantidadMinusMOD, imageButtonCantidadPlusMOD, imageButtonCantidadMinPlusMOD,imageButtonCantidadMinMinusMOD;
    Button btnFechaCaducidadMOD, btnAceptarMOD, btnAnadirListaCompraMOD, btnCancelarMOD;
    SwitchCompat switchAnadirAutoMOD;

    private Uri imageUri = null;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private DatePickerDialog datePickerDialog;
    BorradoProducto borradoProducto;
    private MiDespensaCallback callback;
    Toast toast;

    public ModificacionProductoDespensa(ProductoDespensa productoDespensa, MiDespensaCallback callback) {
        this.producto = productoDespensa;
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCount++;
        categorias = BDConexion.consultarCategorias();
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // eliminar la instancia del fragmento al pulsar el botón de atrás
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificacion_producto_despensa, container, false);
        // ocultar el menú de navegación
        ((MainActivity) getActivity()).hideBottomNavigationView();
        LinearLayout bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        partialContent = view.findViewById(R.id.partial_content);
        fullContent = view.findViewById(R.id.full_content);

        // componentes de partialContent:
        editTextModNombre = view.findViewById(R.id.editTextModNombre);
        editTextModNombre.setText(producto.getNombreProductoDespensa()); // mostrar el nombre del producto seleccionado
        editTextModCantidad = view.findViewById(R.id.editTextModCantidad);
        editTextModCantidad.setText(String.valueOf(producto.getCantidadProductoDespensa())); // mostrar la cantidad del producto seleccionado
        imageButtonModCantidadMinus = view.findViewById(R.id.imageButtonModCantidadMinus);
        imageButtonModCantidadMinus.setOnClickListener(this);
        imageButtonModCantidadPlus = view.findViewById(R.id.imageButtonModCantidadPlus);
        imageButtonModCantidadPlus.setOnClickListener(this);
        spinnerModUnidades = view.findViewById(R.id.spinnerModUnidades);
        SpinnerAdapter adapterUnidades = spinnerModUnidades.getAdapter();
        // establecer la unidad del producto seleccionado
        int pos = -1;
        for (int i = 0; i < adapterUnidades.getCount(); i++) {
            if (adapterUnidades.getItem(i).toString().equals(producto.getUnidadProductoDespensa())) {
                pos = i;
                break;
            }
        }
        // mostrar esta unidad en el spinner
        spinnerModUnidades.setSelection(pos);
        btnModFechaCaducidad = view.findViewById(R.id.btnModFechaCaducidad);
        btnModFechaCaducidad.setOnClickListener(this);
        // mostrar la fecha de caducidad del producto seleccionado
        btnModFechaCaducidad.setText(producto.getFechaCaducidadProductoDespensa().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
        btnModAceptar = view.findViewById(R.id.btnModAceptar);
        btnModAceptar.setOnClickListener(this);
        btnModAnadirListaCompra = view.findViewById(R.id.btnModAnadirListaCompra);
        btnModAnadirListaCompra.setOnClickListener(this);
        btnModEliminar = view.findViewById(R.id.btnModEliminar);
        btnModEliminar.setOnClickListener(this);

        // componentes de fullContent:
        productoPhotoMOD = view.findViewById(R.id.productoPhotoMOD);
        productoPhotoMOD.setOnClickListener(this);
        String imageUrl = producto.getImagenProductoDespensa();
        // mostrar la imagen del producto seleccionado
        if (imageUrl != null && !imageUrl.equals("null") && !imageUrl.isBlank()) {
            Glide.with(getContext())
                    .load(Uri.parse(imageUrl))
                    .placeholder(R.drawable.baseline_add_a_photo_24)
                    .error(R.drawable.baseline_add_a_photo_24)
                    .into(productoPhotoMOD);
        } else {
            productoPhotoMOD.setImageResource(R.drawable.baseline_add_a_photo_24);
        }
        editTextModNombreProductoMOD = view.findViewById(R.id.editTextModNombreProductoMOD);
        editTextModNombreProductoMOD.setText(producto.getNombreProductoDespensa()); // mostrar el nombre del producto seleccionado
        editTextCantidadMinMOD = view.findViewById(R.id.editTextCantidadMinMOD);
        editTextCantidadMinMOD.setText(String.valueOf(producto.getCantidadMinParaAnadirDespensa())); // mostrar la cantidad mínima para añadir del producto seleccionado
        editTextTiendaProcedenteMOD = view.findViewById(R.id.editTextTiendaProcedenteMOD);
        editTextTiendaProcedenteMOD.setText(producto.getTiendaProductoDespensa()); // mostrar la tienda procedente del producto seleccionado
        editTextCantidadMOD = view.findViewById(R.id.editTextCantidadMOD);
        editTextCantidadMOD.setText(String.valueOf(producto.getCantidadProductoDespensa())); // mostrar la cantidad del producto seleccionado

        // crear un arrayList para guardar todas las Categorias y mostrarlas en el Spinner
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(getResources().getString(R.string.spinnerPrompt));
        // añadir cada categoria al Spinner
        String chosenCat = "";
        for (Categoria c : categorias) {
            spinnerArray.add(c.toString());
            if (c.getIdCategoria() == producto.getIdCategoriaFK()) {
                chosenCat = c.getNombreCategoria();
            }
        }
        spinnerCategoriasMOD = view.findViewById(R.id.spinnerCategoriasMOD);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerCategoriasMOD.setAdapter(spinnerArrayAdapter);
        spinnerCategoriasMOD.setOnItemSelectedListener(this);
        // establecer la categoria del producto seleccionado
        int posCat = -1;
        for (int i = 0; i < spinnerArrayAdapter.getCount(); i++) {
            if (spinnerArrayAdapter.getItem(i).toString().equals(chosenCat)) {
                posCat = i;
                break;
            }
        }
        spinnerCategoriasMOD.setSelection(posCat);
        spinnerUnidadesMOD = view.findViewById(R.id.spinnerUnidadesMOD);
        spinnerUnidadesMOD.setSelection(pos); // establecer la unidad del producto seleccionado
        imageButtonCantidadMinusMOD = view.findViewById(R.id.imageButtonCantidadMinusMOD);
        imageButtonCantidadMinusMOD.setOnClickListener(this);
        imageButtonCantidadPlusMOD = view.findViewById(R.id.imageButtonCantidadPlusMOD);
        imageButtonCantidadPlusMOD.setOnClickListener(this);
        imageButtonCantidadMinPlusMOD = view.findViewById(R.id.imageButtonCantidadMinPlusMOD);
        imageButtonCantidadMinPlusMOD.setOnClickListener(this);
        imageButtonCantidadMinMinusMOD = view.findViewById(R.id.imageButtonCantidadMinMinusMOD);
        imageButtonCantidadMinMinusMOD.setOnClickListener(this);
        btnFechaCaducidadMOD = view.findViewById(R.id.btnFechaCaducidadMOD);
        btnFechaCaducidadMOD.setOnClickListener(this);
        btnFechaCaducidadMOD.setText(producto.getFechaCaducidadProductoDespensa().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
        btnAceptarMOD = view.findViewById(R.id.btnAceptarMOD);
        btnAceptarMOD.setOnClickListener(this);
        btnAnadirListaCompraMOD = view.findViewById(R.id.btnAnadirListaCompraMOD);
        btnAnadirListaCompraMOD.setOnClickListener(this);
        btnCancelarMOD = view.findViewById(R.id.btnCancelarMOD);
        btnCancelarMOD.setOnClickListener(this);
        switchAnadirAutoMOD = view.findViewById(R.id.switchAnadirAutoMOD);


        // callbacks para controlar el comportamiento del programa según las vistas del fragment de modificación
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // cuando el fragment está completamente abierto
                    ((MainActivity) getActivity()).hideBottomNavigationView();
                    fullContent.setVisibility(View.VISIBLE);
                    partialContent.setVisibility(View.GONE);
                    // establecer el título en la barra superior
                    if (getActivity() != null) {
                        // establecer el título de la barra superior
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.modificacion_producto_despensa_titulo);
                    }
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // cuando el fragment está abierto parcialmente
                    ((MainActivity) getActivity()).hideBottomNavigationView();
                    fullContent.setVisibility(View.GONE);
                    partialContent.setVisibility(View.VISIBLE);
                    // establecer el título en la barra superior
                    if (getActivity() != null) {
                        // establecer el título de la barra superior
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_mi_despensa);
                    }
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    // cuando el fragment está oculto - eliminar esta instancia del fragment
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                    // establecer el título en la barra superior
                    if (getActivity() != null) {
                        // establecer el título de la barra superior
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_mi_despensa);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });

        return view;
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

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    public void showBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentCount--;
        // cuando no queda ninguna instancia del fragment de modificación - mostrar de nuevo el menú de navigación
        if (fragmentCount == 0) {
            ((MainActivity) getActivity()).showBottomNavigationView();
        }
    }

    // método para mostrar el calendario
    private void showCalendar(Button b) {
        Calendar cal = null;
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                // mostrar la fecha seleccionada en el botón
                b.setText(date);
            }
        };
        cal = cal.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        // actualizar la fecha del calendario según la fecha de caducidad del producto
        datePickerDialog.updateDate(producto.getFechaCaducidadProductoDespensa().getYear(), producto.getFechaCaducidadProductoDespensa().getMonthValue()-1, producto.getFechaCaducidadProductoDespensa().getDayOfMonth());
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        // partialContent
        if (partialContent.getVisibility() == View.VISIBLE) {
            // controlar el comportamiento de los botones de la cantidad
            if (v.getId() == imageButtonModCantidadMinus.getId()) {
                if (!editTextModCantidad.getText().toString().isBlank()) {
                    int currentNumber = Integer.parseInt(editTextModCantidad.getText().toString());
                    if (currentNumber > 1) {
                        editTextModCantidad.setText(String.valueOf(currentNumber - 1));
                    }
                }
            } else if (v.getId() == imageButtonModCantidadPlus.getId()) {
                if (!editTextModCantidad.getText().toString().isBlank()) {
                    int currentNumber = Integer.parseInt(editTextModCantidad.getText().toString());
                    editTextModCantidad.setText(String.valueOf(currentNumber + 1));
                }
            } else if (v.getId() == btnModFechaCaducidad.getId()) {
                showCalendar(btnModFechaCaducidad);
            } else if (v.getId() == btnModAceptar.getId()) {
                // comprobar los datos; si todos están correctos - re-asignar los valores al producto
                if (comprobarDatos(editTextModNombre, editTextModCantidad)) {
                    producto.setNombreProductoDespensa(editTextModNombre.getText().toString());
                    producto.setCantidadProductoDespensa(Integer.parseInt(editTextModCantidad.getText().toString()));
                    producto.setUnidadProductoDespensa(spinnerModUnidades.getSelectedItem().toString());
                    String[] dateFromButton = (btnModFechaCaducidad.getText().toString()).split("/");
                    LocalDate fechaCad = LocalDate.of(Integer.parseInt(dateFromButton[2]), Integer.parseInt(dateFromButton[1]), Integer.parseInt(dateFromButton[0]));
                    producto.setFechaCaducidadProductoDespensa(fechaCad);
                    // método para realizar la modificación del producto en la BD, pasándo como parámetro el producto con valores actualizados
                    BDConexion.modificarProductoDespensa(producto, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                toast = Toast.makeText(getContext(), R.string.operacion_no_realizada, Toast.LENGTH_SHORT);
                                makeToast();
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (response.code() == 200) {
                                    // modificación realizada correctamente
                                    toast = Toast.makeText(getContext(), R.string.operacion_realizada, Toast.LENGTH_SHORT);
                                    makeToast();
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    // actualizar la vista de MiDespensaFragment
                                    callback.onOperacionCorrectaUpdated(true);
                                } else {
                                    toast = Toast.makeText(getContext(), R.string.operacion_no_realizada, Toast.LENGTH_SHORT);
                                    makeToast();
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                }
                            });
                        }
                    });
                } else {
                    toast = Toast.makeText(getContext(), R.string.toast_falta_nombre_producto, Toast.LENGTH_SHORT);
                    makeToast();
                }
            } else if (v.getId() == btnModAnadirListaCompra.getId()) {
                // to do: implementación del botón para añadir un producto a la lista de compra
            } else if (v.getId() == btnModEliminar.getId()) {
                // comprobar los datos; si todos están correctos - mostrar el fragment de borrado
                if (comprobarDatos(editTextModNombre, editTextModCantidad)) {
                    borradoProducto = new BorradoProducto(producto, callback);
                    borradoProducto.setCancelable(false);
                    borradoProducto.show(getActivity().getSupportFragmentManager(), "Borrado Producto");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        }
        // fullContent
        else if (fullContent.getVisibility() == View.VISIBLE) {
            if (v.getId() == productoPhotoMOD.getId()) {
                // mostrar el menú para añadir la imagen
                showInputImageDialog();
            }
            // controlar el comportamiento de los botones de cantidad
            else if (v.getId() == imageButtonCantidadMinusMOD.getId()) {
                if (!editTextCantidadMOD.getText().toString().isBlank()) {
                    int currentNumber = Integer.parseInt(editTextCantidadMOD.getText().toString());
                    if (currentNumber > 1) {
                        editTextCantidadMOD.setText(String.valueOf(currentNumber - 1));
                    }
                }
            } else if (v.getId() == imageButtonCantidadPlusMOD.getId()) {
                if (!editTextCantidadMOD.getText().toString().isBlank()) {
                    int currentNumber = Integer.parseInt(editTextCantidadMOD.getText().toString());
                    editTextCantidadMOD.setText(String.valueOf(currentNumber + 1));
                }
            } else if (v.getId() == btnFechaCaducidadMOD.getId()) {
                // mostrar el calendario y al seleccionar la fecha - mostrarla en el botón que se pasa como parámetro
                showCalendar(btnFechaCaducidadMOD);
            } else if (v.getId() == imageButtonCantidadMinMinusMOD.getId()) {
                if (!editTextCantidadMinMOD.getText().toString().isBlank()) {
                    int currentNumber = Integer.parseInt(editTextCantidadMinMOD.getText().toString());
                    if (currentNumber > 1) {
                        editTextCantidadMinMOD.setText(String.valueOf(currentNumber - 1));
                    }
                }
            } else if (v.getId() == imageButtonCantidadMinPlusMOD.getId()) {
                if (!editTextCantidadMinMOD.getText().toString().isBlank()) {
                    int currentNumber = Integer.parseInt(editTextCantidadMinMOD.getText().toString());
                    editTextCantidadMinMOD.setText(String.valueOf(currentNumber + 1));
                }
            } else if (v.getId() == btnAceptarMOD.getId()) {
                // si la comprobación de los datos devuelve true, re-establecer los valores de los atributos del producto
                if (comprobarDatos(editTextModNombreProductoMOD, editTextCantidadMOD, editTextCantidadMinMOD, spinnerCategoriasMOD)) {
                    int autoAnadirListaCompra = switchAnadirAutoMOD.isChecked() ? 1 : 0;
                    producto.setNombreProductoDespensa(editTextModNombreProductoMOD.getText().toString());
                    producto.setIdCategoriaFK(spinnerCategoriasMOD.getSelectedItemPosition());
                    producto.setCantidadProductoDespensa(Integer.parseInt(editTextCantidadMOD.getText().toString()));
                    producto.setUnidadProductoDespensa(spinnerUnidadesMOD.getSelectedItem().toString());
                    String[] dateFromButton = (btnFechaCaducidadMOD.getText().toString()).split("/");
                    LocalDate fechaCad = LocalDate.of(Integer.parseInt(dateFromButton[2]), Integer.parseInt(dateFromButton[1]), Integer.parseInt(dateFromButton[0]));
                    producto.setFechaCaducidadProductoDespensa(fechaCad);
                    producto.setAutoanadirAListaCompraDespensa(autoAnadirListaCompra);
                    producto.setCantidadMinParaAnadirDespensa(Integer.parseInt(editTextCantidadMinMOD.getText().toString()));
                    producto.setTiendaProductoDespensa(editTextTiendaProcedenteMOD.getText().toString());
                    // método para realizar la modificación del producto en la BD, pasándo como parámetro el producto con valores actualizados
                    BDConexion.modificarProductoDespensa(producto, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                toast = Toast.makeText(getContext(), R.string.operacion_no_realizada, Toast.LENGTH_SHORT);
                                makeToast();
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (response.code() == 200) {
                                    // modificación realizada correctamente
                                    toast = Toast.makeText(getContext(), R.string.operacion_realizada, Toast.LENGTH_SHORT);
                                    makeToast();
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    callback.onOperacionCorrectaUpdated(true);

                                } else {
                                    toast = Toast.makeText(getContext(), R.string.operacion_no_realizada, Toast.LENGTH_SHORT);
                                    makeToast();
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                }
                            });
                        }
                    });
                } else {
                    toast = Toast.makeText(getContext(), R.string.toast_falta_nombre_categoria_producto, Toast.LENGTH_SHORT);
                    makeToast();
                }
            } else if (v.getId() == btnAnadirListaCompraMOD.getId()) {
                // to do: implementación del botón para añadir un producto a la lista de compra
            } else if (v.getId() == btnCancelarMOD.getId()) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    }

    private boolean comprobarDatos(EditText et, EditText cantidad, EditText cantidadMin, Spinner sp) {
        if (!et.getText().toString().isBlank() && (sp.getSelectedItemPosition() != 0) && (!cantidad.getText().toString().isBlank()) && (!cantidadMin.getText().toString().isBlank())) {
            return true;
        }
        return false;
    }

    private boolean comprobarDatos(EditText et, EditText cantidad) {
        if (!(et.getText().toString().isBlank()) && !(cantidad.getText().toString().isBlank())) {
            return true;
        }
        return false;
    }

    // método para mostrar el menú para añadir una imagen
    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(getContext(), productoPhotoMOD);
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
                        producto.setImagenProductoDespensa(String.valueOf(imageUri));
                        // establecer la foto en la vista
                        if (imageUri.toString() != null && !imageUri.toString().equals("null") && !imageUri.toString().isBlank()) {
                            Glide.with(getContext())
                                    .load(imageUri)
                                    .placeholder(R.drawable.no_photo)
                                    .error(R.drawable.no_photo)
                                    .into(productoPhotoMOD);
                        } else {
                            productoPhotoMOD.setImageResource(R.drawable.no_photo);
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
                        producto.setImagenProductoDespensa(String.valueOf(imageUri));
                        // establecer la foto en la vista
                        if (imageUri.toString() != null && !imageUri.toString().equals("null") && !imageUri.toString().isBlank()) {
                            Glide.with(getContext())
                                    .load(imageUri)
                                    .placeholder(R.drawable.no_photo)
                                    .error(R.drawable.no_photo)
                                    .into(productoPhotoMOD);
                        } else {
                            productoPhotoMOD.setImageResource(R.drawable.no_photo);
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