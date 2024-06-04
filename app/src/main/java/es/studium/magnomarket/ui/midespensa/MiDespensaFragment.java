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

    public interface ProductosDespensaCallback {
        void onProductosDespensaLoaded(ArrayList<ProductoDespensa> productosDespensa);
    }
    private FragmentMiDespensaBinding binding;
    ListView listView;
    ArrayList<ProductoDespensa> productosDespensa;
    Spinner spinnerOrdenar;
    ImageButton buttonVistaLista, buttonVistaCategorias;
    FloatingActionButton floatingButtonAdd;
    SharedPreferences sharedpreferences;
    FragmentManager fm;
    FragmentTransaction ft;
    NuevoProductoDespensa fragmentNuevoProductoDespensa;
    ModificacionProductoDespensa fragmentModificacionProductoDespensa;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] storagePermissions;
    AdapterListItem adaptador;
    BorradoProducto borradoProducto;
    MiDespensaCallback despensaCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtener el acceso a SharedPreferences
        sharedpreferences = getActivity().getSharedPreferences(LoginCredenciales, Context.MODE_PRIVATE);
        if (sharedpreferences != null) {
            // obtener el idUsuario de los SharedPreferences guardadas anteriormente
            // y asignar el valor a la variable estática compartida por toda la aplicación
            MainActivity.idUsuario = sharedpreferences.getInt("usuarioID", MainActivity.idUsuario);
        }
        storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        productosDespensa = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MiDespensaViewModel miDespensaViewModel =
                new ViewModelProvider(this).get(MiDespensaViewModel.class);

        binding = FragmentMiDespensaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = root.findViewById(R.id.listView);

        // Crear un adaptador para el listView
        adaptador = new AdapterListItem(getContext(), R.layout.list_item, productosDespensa);
        listView.setAdapter(adaptador);

        // método para realizar la consulta de los productos de la tabla productosDespensa para el usuario establecido por su id
        BDConexion.consultarProductosDespensa(MainActivity.idUsuario, new ProductosDespensaCallback() {
            @Override
            public void onProductosDespensaLoaded(ArrayList<ProductoDespensa> loadedProductosDespensa) {
                getActivity().runOnUiThread(() -> {
                    // vaciar la lista de los productos
                    productosDespensa.clear();
                    // rellenar la lista con los productos de la tabla productosDespensa pasados por el callback
                    productosDespensa.addAll(loadedProductosDespensa);
                    // actualizar el listView
                    adaptador.notifyDataSetChanged();
                });
            }
        });

        despensaCallback = new MiDespensaCallback() {
            @Override
            public void onOperacionCorrectaUpdated(boolean operacionCorrecta) {
                if (operacionCorrecta) {
                    // en caso de realizar alta, modificacion o baja de un producto, se llamará a un método que actualiza la lista de los productos
                    refreshProductList();
                }
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // click corto para realizar modificación de el producto seleccionado
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentModificacionProductoDespensa = new ModificacionProductoDespensa(productosDespensa.get(position), despensaCallback);
                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.add(R.id.container, fragmentModificacionProductoDespensa, "modificacionProductoDespensa")
                        .addToBackStack(null)
                        .commit();
                // abrir inicialmente la vista parcial del fragmento
                fragmentModificacionProductoDespensa.showBottomSheet();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // click largo para eliminar el producto seleccionado
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                borradoProducto = new BorradoProducto(productosDespensa.get(position), despensaCallback);
                borradoProducto.setCancelable(false);
                borradoProducto.show(getActivity().getSupportFragmentManager(), "Borrado Producto");
                // devolver true para que el programa no lo interprete como un click corto
                return true;
            }
        });

        // comprobar que la aplicación tiene permisos para acceder a los archivos del móvil
        if (checkStoragePermission()) {
            adaptador.notifyDataSetChanged();
        } else {
            requestStoragePermission();
        }

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

    // método para actualizar la lista de los productos
    private void refreshProductList() {
        BDConexion.consultarProductosDespensa(MainActivity.idUsuario, new ProductosDespensaCallback() {
            @Override
            public void onProductosDespensaLoaded(ArrayList<ProductoDespensa> loadedProductosDespensa) {
                getActivity().runOnUiThread(() -> {
                    productosDespensa.clear();
                    productosDespensa.addAll(loadedProductosDespensa);
                    // comprobar la selección del spinner de ordenación de la lista
                    if (spinnerOrdenar.getSelectedItemPosition() == 1) {
                        // ordenar los productos por la fecha de caducidad
                        productosDespensa.sort(Comparator.comparing((ProductoDespensa p) -> p.getFechaCaducidadProductoDespensa()));
                    }
                    adaptador.notifyDataSetChanged();
                });
            }
        });
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
            productosDespensa.sort(Comparator.comparing((ProductoDespensa p) -> p.getNombreProductoDespensa().toLowerCase()));
            adaptador.notifyDataSetChanged();
        } else if (spinnerOrdenar.getSelectedItemPosition() == 1) {
            // ordenar los productos por la fecha de caducidad
            productosDespensa.sort(Comparator.comparing((ProductoDespensa p) -> p.getFechaCaducidadProductoDespensa()));
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
            // to do: presentar la lista de forma de lista (por defecto)
        } else if (v.getId() == buttonVistaCategorias.getId()) {
            buttonVistaLista.setEnabled(true);
            buttonVistaLista.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.azul_claro)));
            buttonVistaCategorias.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.naranja)));
            buttonVistaCategorias.setEnabled(false);
            // to do: presentar la lista dividida por categorías
        }
        // floating button para realizar el alta de un producto
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
