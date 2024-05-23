package es.studium.magnomarket.ui.midespensa;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import es.studium.magnomarket.MainActivity;
import es.studium.magnomarket.ProductoDespensa;
import es.studium.magnomarket.R;

public class ModificacionProductoDespensa extends Fragment {
    ProductoDespensa producto;

    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private ConstraintLayout partialContent, fullContent;
    EditText editTextNombreProducto;

    public ModificacionProductoDespensa(ProductoDespensa productoDespensa) {
        this.producto = productoDespensa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificacion_producto_despensa, container, false);
        // ocultar la barra de navegación
        ((MainActivity) getActivity()).hideBottomNavigationView();

        LinearLayout bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        partialContent = view.findViewById(R.id.partial_content);
        fullContent = view.findViewById(R.id.full_content);

        // Set up BottomSheetBehavior callbacks
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // When bottom sheet is fully expanded
                    fullContent.setVisibility(View.VISIBLE);
                    partialContent.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // When bottom sheet is collapsed or hidden
                    fullContent.setVisibility(View.GONE);
                    partialContent.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.getPrimaryNavigationFragment();
                    ((MainActivity) getActivity()).showBottomNavigationView();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        editTextNombreProducto = view.findViewById(R.id.editTextModNombre);
        editTextNombreProducto.setText(producto.getNombreProductoDespensa());

        return view;
    }

    public void showBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}