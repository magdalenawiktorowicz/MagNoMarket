package es.studium.magnomarket.ui.midespensa;

import static es.studium.magnomarket.Login.LoginCredenciales;

import android.content.Context;

import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(LoginCredenciales, Context.MODE_PRIVATE);
        // si las preferencias compartidas existen
        if (sharedpreferences != null) {
            // obtener el valor de idUsuario, si no - por defecto el valor de la variable estática idUsuario
            MainActivity.idUsuario = sharedpreferences.getInt("usuarioID", MainActivity.idUsuario);
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MiDespensaViewModel miDespensaViewModel =
                new ViewModelProvider(this).get(MiDespensaViewModel.class);

        binding = FragmentMiDespensaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.listView);
        Toast.makeText(getContext(), MainActivity.idUsuario + "", Toast.LENGTH_SHORT).show();
        productoDespensas = BDConexion.consultarProductosDespensa(MainActivity.idUsuario);

        // asignar un listener a cada elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Has seleccionado " + productoDespensas.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Crear un Adaptador
        AdapterListItem adaptador = new AdapterListItem(getContext(), R.layout.list_item, productoDespensas);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerOrdenar.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "First", Toast.LENGTH_SHORT).show();
        } else if (spinnerOrdenar.getSelectedItemPosition() == 1) {
            Toast.makeText(getContext(), "Second", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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