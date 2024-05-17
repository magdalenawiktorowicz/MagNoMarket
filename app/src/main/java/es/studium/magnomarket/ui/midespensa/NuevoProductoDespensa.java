package es.studium.magnomarket.ui.midespensa;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.studium.magnomarket.BDConexion;
import es.studium.magnomarket.Categoria;
import es.studium.magnomarket.R;

public class NuevoProductoDespensa extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageView nuevoProductoPhoto;
    EditText editTextNombreProducto;
    ImageButton imageButtonMic;
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

    public NuevoProductoDespensa() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categorias = BDConexion.consultarCategorias();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevo_producto_despensa, container, false);

        nuevoProductoPhoto = view.findViewById(R.id.nuevoProductoPhoto);
        nuevoProductoPhoto.setOnClickListener(this);
        editTextNombreProducto = view.findViewById(R.id.editTextNombreProducto);
        imageButtonMic = view.findViewById(R.id.imageButtonMic);
        imageButtonMic.setOnClickListener(this);

        // crear un arrayList para guardar todas las Categorias y mostrarlas en el Spinner
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(getResources().getString(R.string.spinnerPrompt));
        // añadir cada categoria al Spinner
        Toast.makeText(getContext(), categorias.toString(), Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}