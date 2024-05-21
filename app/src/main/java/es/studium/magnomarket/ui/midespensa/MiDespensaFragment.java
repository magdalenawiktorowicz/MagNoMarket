package es.studium.magnomarket.ui.midespensa;

import static es.studium.magnomarket.Login.LoginCredenciales;

import android.Manifest;
import android.content.Context;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.studium.magnomarket.BDConexion;
import es.studium.magnomarket.MainActivity;
import es.studium.magnomarket.ProductoDespensa;
import es.studium.magnomarket.R;
import es.studium.magnomarket.databinding.FragmentMiDespensaBinding;

public class MiDespensaFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private FragmentMiDespensaBinding binding;

    // componentes de GUI
    ListView listView;
    ArrayList<ProductoDespensa> productoDespensas;
    Spinner spinnerOrdenar;
    ImageButton buttonVistaLista, buttonVistaCategorias;
    FloatingActionButton floatingButtonAdd;
    SharedPreferences sharedpreferences;
    FragmentManager fm;
    FragmentTransaction ft;
    Fragment fragmentNuevoProductoDespensa;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] storagePermissions;

    AdapterListItem adaptador;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(LoginCredenciales, Context.MODE_PRIVATE);
        // si las preferencias compartidas existen
        if (sharedpreferences != null) {
            // obtener el valor de idUsuario, si no - por defecto el valor de la variable estática idUsuario
            MainActivity.idUsuario = sharedpreferences.getInt("usuarioID", MainActivity.idUsuario);
        }
        storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MiDespensaViewModel miDespensaViewModel =
                new ViewModelProvider(this).get(MiDespensaViewModel.class);

        productoDespensas = BDConexion.consultarProductosDespensa(MainActivity.idUsuario);

        binding = FragmentMiDespensaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = root.findViewById(R.id.listView);

        // asignar un listener a cada elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Has seleccionado " + productoDespensas.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Crear un Adaptador
        adaptador = new AdapterListItem(getContext(), R.layout.list_item, productoDespensas);
        if (checkStoragePermission()) {
            adaptador.notifyDataSetChanged();
        } else {
            requestStoragePermission();
        }
        // Asignar el adaptador a nuestro ListView
        listView.setAdapter(adaptador);

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("alfabéticamente");
        spinnerArray.add("por fecha de caducidad");
        spinnerOrdenar = root.findViewById(R.id.miDespensaSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerOrdenar.setAdapter(spinnerArrayAdapter);
        spinnerOrdenar.setOnItemSelectedListener(this);

        buttonVistaLista = root.findViewById(R.id.imageButton);
        buttonVistaLista.setOnClickListener(this);
        buttonVistaLista.setEnabled(false);

        buttonVistaCategorias = root.findViewById(R.id.imageButton2);
        buttonVistaCategorias.setOnClickListener(this);

        floatingButtonAdd = root.findViewById(R.id.miDespensaFloatingActionBotton);
        floatingButtonAdd.setOnClickListener(this);

        return root;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerOrdenar.getSelectedItemPosition() == 0) {
            // ordenar los productos alfabéticamente
            productoDespensas.sort(Comparator.comparing((ProductoDespensa p) -> p.getNombreProductoDespensa().toLowerCase()));
            adaptador.notifyDataSetChanged();
        } else if (spinnerOrdenar.getSelectedItemPosition() == 1) {
            // ordenar los productos por la fecha de caducidad
            productoDespensas.sort(Comparator.comparing((ProductoDespensa p) -> p.getFechaCaducidadProductoDespensa()));
            adaptador.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonVistaLista.getId()) {
            buttonVistaCategorias.setEnabled(true);
            buttonVistaCategorias.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.azul_claro)));
            buttonVistaLista.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.naranja)));
            buttonVistaLista.setEnabled(false);
        }
        else if (v.getId() == buttonVistaCategorias.getId()) {
            buttonVistaLista.setEnabled(true);
            buttonVistaLista.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.azul_claro)));
            buttonVistaCategorias.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.naranja)));
            buttonVistaCategorias.setEnabled(false);
        }
        else if (v.getId() == floatingButtonAdd.getId()) {
            fragmentNuevoProductoDespensa = new NuevoProductoDespensa();
            fm = getActivity().getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.container, fragmentNuevoProductoDespensa, "nuevoProductoDespensa")
                    .addToBackStack(null)
                    .commit();
        }
    }
}