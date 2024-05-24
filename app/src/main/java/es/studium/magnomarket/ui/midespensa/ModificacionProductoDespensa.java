package es.studium.magnomarket.ui.midespensa;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import es.studium.magnomarket.BDConexion;
import es.studium.magnomarket.Categoria;
import es.studium.magnomarket.MainActivity;
import es.studium.magnomarket.ProductoDespensa;
import es.studium.magnomarket.R;

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
    Button btnModFechaCaducidad, btnModEditar, btnModAnadirListaCompra, btnModEliminar;

    // componentes de fullContent:
    ImageView productoPhotoMOD;
    EditText editTextModNombreProductoMOD, editTextCantidadMinMOD, editTextTiendaProcedenteMOD, editTextCantidadMOD;
    Spinner spinnerCategoriasMOD, spinnerUnidadesMOD;
    ImageButton imageButtonCantidadMinusMOD, imageButtonCantidadPlusMOD, imageButtonCantidadMinPlusMOD,imageButtonCantidadMinMinusMOD;
    Button btnFechaCaducidadMOD, btnAceptarMOD, btnAnadirListaCompraMOD, btnCancelarMOD;
    SwitchCompat switchAnadirAutoMOD;


    public ModificacionProductoDespensa(ProductoDespensa productoDespensa) {
        this.producto = productoDespensa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCount++;
        categorias = BDConexion.consultarCategorias();

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
        editTextModCantidad = view.findViewById(R.id.editTextModCantidad);
        imageButtonModCantidadMinus = view.findViewById(R.id.imageButtonModCantidadMinus);
        imageButtonModCantidadMinus.setOnClickListener(this);
        imageButtonModCantidadPlus = view.findViewById(R.id.imageButtonModCantidadPlus);
        imageButtonModCantidadPlus.setOnClickListener(this);
        spinnerModUnidades = view.findViewById(R.id.spinnerModUnidades);
        btnModFechaCaducidad = view.findViewById(R.id.btnModFechaCaducidad);
        btnModFechaCaducidad.setOnClickListener(this);
        btnModEditar = view.findViewById(R.id.btnModEditar);
        btnModEditar.setOnClickListener(this);
        btnModAnadirListaCompra = view.findViewById(R.id.btnModAnadirListaCompra);
        btnModAnadirListaCompra.setOnClickListener(this);
        btnModEliminar = view.findViewById(R.id.btnModEliminar);
        btnModEliminar.setOnClickListener(this);

        // componentes de fullContent:
        productoPhotoMOD = view.findViewById(R.id.productoPhotoMOD);
        productoPhotoMOD.setOnClickListener(this);
        editTextModNombreProductoMOD = view.findViewById(R.id.editTextModNombreProductoMOD);
        editTextCantidadMinMOD = view.findViewById(R.id.editTextCantidadMinMOD);
        editTextTiendaProcedenteMOD = view.findViewById(R.id.editTextTiendaProcedenteMOD);
        editTextCantidadMOD = view.findViewById(R.id.editTextCantidadMOD);

        // crear un arrayList para guardar todas las Categorias y mostrarlas en el Spinner
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(getResources().getString(R.string.spinnerPrompt));
        // añadir cada categoria al Spinner
        for (Categoria c : categorias) {
            spinnerArray.add(c.toString());
        }
        spinnerCategoriasMOD = view.findViewById(R.id.spinnerCategoriasMOD);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerCategoriasMOD.setAdapter(spinnerArrayAdapter);
        spinnerCategoriasMOD.setOnItemSelectedListener(this);

        spinnerUnidadesMOD = view.findViewById(R.id.spinnerUnidadesMOD);
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

        if (partialContent.getVisibility() == View.VISIBLE) {
        }
        else if (fullContent.getVisibility() == View.VISIBLE) {
        }


        return view;
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