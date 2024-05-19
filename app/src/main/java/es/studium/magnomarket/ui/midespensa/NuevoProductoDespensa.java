package es.studium.magnomarket.ui.midespensa;

import android.app.Activity;
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
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.studium.magnomarket.BDConexion;
import es.studium.magnomarket.Categoria;
import es.studium.magnomarket.R;

public class NuevoProductoDespensa extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageView nuevoProductoPhoto;
    EditText editTextNombreProducto;
    Spinner spinnerCategorias;
    ArrayList<Categoria> categorias;
    ImageButton imageButtonCantidadMinus, imageButtonCantidadPlus;
    EditText editTextCantidad;
    Spinner spinnerUnidades;
    EditText editTextFechaCaducidad;
    SwitchCompat switchAnadirAuto;
    ImageButton imageButtonCantidadMinMinus, imageButtonCantidadMinPlus;
    EditText editTextCantidadMin;
    EditText editTextTiendaProcedente;
    private Uri imageUri = null;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] cameraPermissions;
    private String[] storagePermissions;


    public NuevoProductoDespensa() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categorias = BDConexion.consultarCategorias();
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevo_producto_despensa, container, false);

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
        editTextFechaCaducidad = view.findViewById(R.id.editTextFechaCaducidad);
        switchAnadirAuto = view.findViewById(R.id.switchAnadirAuto);
        imageButtonCantidadMinMinus = view.findViewById(R.id.imageButtonCantidadMinMinus);
        imageButtonCantidadMinMinus.setOnClickListener(this);
        imageButtonCantidadMinPlus = view.findViewById(R.id.imageButtonCantidadMinPlus);
        imageButtonCantidadMinPlus.setOnClickListener(this);
        editTextCantidadMin = view.findViewById(R.id.editTextCantidadMin);
        editTextTiendaProcedente = view.findViewById(R.id.editTextTiendaProcedente);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == nuevoProductoPhoto.getId()) {
            showInputImageDialog();
        } else if (v.getId() == imageButtonCantidadMinus.getId()) {
            int currentNumber = Integer.parseInt(editTextCantidad.getText().toString());
            if (currentNumber > 1) {
                editTextCantidad.setText(String.valueOf(currentNumber - 1));
            }
        } else if (v.getId() == imageButtonCantidadPlus.getId()) {
            int currentNumber = Integer.parseInt(editTextCantidad.getText().toString());
            editTextCantidad.setText(String.valueOf(currentNumber + 1));
        }
    }





    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(getContext(), nuevoProductoPhoto);

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
                        // change the imageButton to that image
                        Toast.makeText(getContext(), "image picked", Toast.LENGTH_SHORT).show();
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