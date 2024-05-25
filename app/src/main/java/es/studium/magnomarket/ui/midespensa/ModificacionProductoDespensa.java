package es.studium.magnomarket.ui.midespensa;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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


    public ModificacionProductoDespensa(ProductoDespensa productoDespensa) {
        this.producto = productoDespensa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCount++;
        categorias = BDConexion.consultarCategorias();
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificacion_producto_despensa, container, false);
        ((MainActivity) getActivity()).hideBottomNavigationView();
        LinearLayout bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        partialContent = view.findViewById(R.id.partial_content);
        fullContent = view.findViewById(R.id.full_content);

        // componentes de partialContent:
        editTextModNombre = view.findViewById(R.id.editTextModNombre);
        editTextModNombre.setText(producto.getNombreProductoDespensa());
        editTextModCantidad = view.findViewById(R.id.editTextModCantidad);
        editTextModCantidad.setText(String.valueOf(producto.getCantidadProductoDespensa()));
        imageButtonModCantidadMinus = view.findViewById(R.id.imageButtonModCantidadMinus);
        imageButtonModCantidadMinus.setOnClickListener(this);
        imageButtonModCantidadPlus = view.findViewById(R.id.imageButtonModCantidadPlus);
        imageButtonModCantidadPlus.setOnClickListener(this);
        spinnerModUnidades = view.findViewById(R.id.spinnerModUnidades);
        SpinnerAdapter adapterUnidades = spinnerModUnidades.getAdapter();
        int pos = -1;
        for (int i = 0; i < adapterUnidades.getCount(); i++) {
            if (adapterUnidades.getItem(i).toString().equals(producto.getUnidadProductoDespensa())) {
                pos = i;
                break;
            }
        }
        spinnerModUnidades.setSelection(pos);
        btnModFechaCaducidad = view.findViewById(R.id.btnModFechaCaducidad);
        btnModFechaCaducidad.setOnClickListener(this);
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
        editTextModNombreProductoMOD.setText(producto.getNombreProductoDespensa());
        editTextCantidadMinMOD = view.findViewById(R.id.editTextCantidadMinMOD);
        editTextCantidadMinMOD.setText(String.valueOf(producto.getCantidadMinParaAnadirDespensa()));
        editTextTiendaProcedenteMOD = view.findViewById(R.id.editTextTiendaProcedenteMOD);
        editTextTiendaProcedenteMOD.setText(producto.getTiendaProductoDespensa());
        editTextCantidadMOD = view.findViewById(R.id.editTextCantidadMOD);
        editTextCantidadMOD.setText(String.valueOf(producto.getCantidadProductoDespensa()));

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
        // set the category of the product
        int posCat = -1;
        for (int i = 0; i < spinnerArrayAdapter.getCount(); i++) {
            if (spinnerArrayAdapter.getItem(i).toString().equals(chosenCat)) {
                posCat = i;
                break;
            }
        }
        spinnerCategoriasMOD.setSelection(posCat);

        spinnerUnidadesMOD = view.findViewById(R.id.spinnerUnidadesMOD);
        spinnerUnidadesMOD.setSelection(pos);
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


        // Set up BottomSheetBehavior callbacks
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // When bottom sheet is fully expanded
                    ((MainActivity) getActivity()).hideBottomNavigationView();
                    fullContent.setVisibility(View.VISIBLE);
                    partialContent.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // When bottom sheet is collapsed or hidden
                    ((MainActivity) getActivity()).hideBottomNavigationView();
                    fullContent.setVisibility(View.GONE);
                    partialContent.setVisibility(View.VISIBLE);

                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        return view;
    }

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
        if (fragmentCount == 0) {
            ((MainActivity) getActivity()).showBottomNavigationView();
        }
    }

    private void showCalendar(Button b) {
        Calendar cal = null;
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                b.setText(date);
            }
        };
        cal = cal.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        datePickerDialog.updateDate(producto.getFechaCaducidadProductoDespensa().getYear(), producto.getFechaCaducidadProductoDespensa().getMonthValue()-1, producto.getFechaCaducidadProductoDespensa().getDayOfMonth());
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        // partialContent
        if (partialContent.getVisibility() == View.VISIBLE) {
            if (v.getId() == imageButtonModCantidadMinus.getId()) {
                int currentNumber = Integer.parseInt(editTextModCantidad.getText().toString());
                if (currentNumber > 1) {
                    editTextModCantidad.setText(String.valueOf(currentNumber - 1));
                }
            } else if (v.getId() == imageButtonModCantidadPlus.getId()) {
                int currentNumber = Integer.parseInt(editTextModCantidad.getText().toString());
                editTextModCantidad.setText(String.valueOf(currentNumber + 1));
            } else if (v.getId() == btnModFechaCaducidad.getId()) {
                showCalendar(btnModFechaCaducidad);
            } else if (v.getId() == btnModAceptar.getId()) {
                // COMPROBAR LOS DATOS
                if (comprobarDatos(editTextModNombre)) {
                    producto.setNombreProductoDespensa(editTextModNombre.getText().toString());
                    producto.setCantidadProductoDespensa(Integer.parseInt(editTextModCantidad.getText().toString()));
                    producto.setUnidadProductoDespensa(spinnerModUnidades.getSelectedItem().toString());
                    String[] dateFromButton = (btnModFechaCaducidad.getText().toString()).split("/");
                    LocalDate fechaCad = LocalDate.of(Integer.parseInt(dateFromButton[2]), Integer.parseInt(dateFromButton[1]), Integer.parseInt(dateFromButton[0]));
                    producto.setFechaCaducidadProductoDespensa(fechaCad);
                    // MODIFICAR
                    BDConexion.modificacionProducto(producto, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Toast.makeText(getContext(), "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (response.code() == 200) {
                                    // alta realizada correctamente
                                    Toast.makeText(getContext(), "La operación se ha realizado correctamente.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    getContext().startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Introduce el nombre del producto.", Toast.LENGTH_SHORT).show();
                }
            } else if (v.getId() == btnModAnadirListaCompra.getId()) {
                // COMPROBAR LOS DATOS
                // AÑADIR A LA LISTA DE COMPRA
                // PREGUNTAR SOBRE LA CANTIDAD????
                // HIDE THE BOTTOM SHEET
            } else if (v.getId() == btnModEliminar.getId()) {
                // COMPROBAR LOS DATOS
                if (comprobarDatos(editTextModNombre)) {
                    borradoProducto = new BorradoProducto(producto);
                    borradoProducto.setCancelable(false);
                    borradoProducto.show(getActivity().getSupportFragmentManager(), "Borrado Producto");
                }
                // ELIMINAR
                // INFORMAR SOBRE EL RESULTADO
                // HIDE THE BOTTOM SHEET - check if its updated
            }
        }
        // fullContent
        else if (fullContent.getVisibility() == View.VISIBLE) {
            if (v.getId() == productoPhotoMOD.getId()) {
                showInputImageDialog();
            } else if (v.getId() == imageButtonCantidadMinusMOD.getId()) {
                int currentNumber = Integer.parseInt(editTextCantidadMOD.getText().toString());
                if (currentNumber > 1) {
                    editTextCantidadMOD.setText(String.valueOf(currentNumber - 1));
                }
            } else if (v.getId() == imageButtonCantidadPlusMOD.getId()) {
                int currentNumber = Integer.parseInt(editTextCantidadMOD.getText().toString());
                editTextCantidadMOD.setText(String.valueOf(currentNumber + 1));
            } else if (v.getId() == btnFechaCaducidadMOD.getId()) {
                showCalendar(btnFechaCaducidadMOD);
            } else if (v.getId() == imageButtonCantidadMinMinusMOD.getId()) {
                int currentNumber = Integer.parseInt(editTextCantidadMinMOD.getText().toString());
                if (currentNumber > 1) {
                    editTextCantidadMinMOD.setText(String.valueOf(currentNumber - 1));
                }
            } else if (v.getId() == imageButtonCantidadMinPlusMOD.getId()) {
                int currentNumber = Integer.parseInt(editTextCantidadMinMOD.getText().toString());
                editTextCantidadMinMOD.setText(String.valueOf(currentNumber + 1));
            } else if (v.getId() == btnAceptarMOD.getId()) {
                if (comprobarDatos(editTextModNombreProductoMOD, spinnerCategoriasMOD)) {
                    int autoAnadirListaCompra = switchAnadirAutoMOD.isChecked() ? 1 : 0;
                    producto.setNombreProductoDespensa(editTextModNombreProductoMOD.getText().toString());
                    //producto.setImagenProductoDespensa(String.valueOf(Uri.parse(productoPhotoMOD.toString())));
                    producto.setIdCategoriaFK(spinnerCategoriasMOD.getSelectedItemPosition());
                    producto.setCantidadProductoDespensa(Integer.parseInt(editTextCantidadMOD.getText().toString()));
                    producto.setUnidadProductoDespensa(spinnerUnidadesMOD.getSelectedItem().toString());
                    String[] dateFromButton = (btnFechaCaducidadMOD.getText().toString()).split("/");
                    LocalDate fechaCad = LocalDate.of(Integer.parseInt(dateFromButton[2]), Integer.parseInt(dateFromButton[1]), Integer.parseInt(dateFromButton[0]));
                    producto.setFechaCaducidadProductoDespensa(fechaCad);
                    producto.setAutoanadirAListaCompraDespensa(autoAnadirListaCompra);
                    producto.setCantidadMinParaAnadirDespensa(Integer.parseInt(editTextCantidadMinMOD.getText().toString()));
                    producto.setTiendaProductoDespensa(editTextTiendaProcedenteMOD.getText().toString());
                    // MODIFICAR
                    BDConexion.modificacionProducto(producto, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Toast.makeText(getContext(), "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (response.code() == 200) {
                                    // modificación realizada correctamente
                                    Toast.makeText(getContext(), "La operación se ha realizado correctamente.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    getContext().startActivity(intent);

                                } else {
                                    Toast.makeText(getContext(), "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Introduce el nombre del producto y selecciona la categoría.", Toast.LENGTH_SHORT).show();
                }
            } else if (v.getId() == btnAnadirListaCompraMOD.getId()) {
                // COMPROBAR LOS DATOS
                // AÑADIR A LA LISTA DE COMPRA
                // PREGUNTAR SOBRE LA CANTIDAD????
                // HIDE THE BOTTOM SHEET
            } else if (v.getId() == btnCancelarMOD.getId()) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    }

    private boolean comprobarDatos(EditText et, Spinner sp) {
        if (!et.getText().toString().isBlank() && (sp.getSelectedItemPosition() != 0)) {
            return true;
        }
        return false;
    }

    private boolean comprobarDatos(EditText et) {
        if (!et.getText().toString().isBlank()) {
            return true;
        }
        return false;
    }

    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(getContext(), productoPhotoMOD);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Cámara");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Galeria");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1) {
                    if (checkCameraPermissions()) {
                        pickImageCamera();
                    } else {
                        requestCameraPermissions();
                    }
                } else if (item.getItemId() == 2) {
                    if (checkStoragePermission()) {
                        pickImageGallery();
                    } else {
                        requestStoragePermission();
                    }
                }
                return false;
            }
        });
    }

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
                    // receive the image, if picked
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // image picked
                        imageUri = result.getData().getData();
                        producto.setImagenProductoDespensa(String.valueOf(imageUri));
                        // change the imageButton to that image
                        Toast.makeText(getContext(), "image picked", Toast.LENGTH_SHORT).show();
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
                    // receive the image, if taken
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // change the imageButton to the captured image
                        Toast.makeText(getContext(), "imageTaken: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
                        producto.setImagenProductoDespensa(String.valueOf(imageUri));
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

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {
        boolean cameraResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraResult && storageResult;
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}