package es.studium.magnomarket.ui.listadecompra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.studium.magnomarket.databinding.FragmentListaDeCompraBinding;

public class ListaDeCompraFragment extends Fragment {

    private FragmentListaDeCompraBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListaDeCompraViewModel listaDeCompraViewModel =
                new ViewModelProvider(this).get(ListaDeCompraViewModel.class);

        binding = FragmentListaDeCompraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textListaDeCompra;
        listaDeCompraViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}