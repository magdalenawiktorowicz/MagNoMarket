package es.studium.magnomarket.ui.micuenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.studium.magnomarket.databinding.FragmentMiCuentaBinding;

public class MiCuentaFragment extends Fragment {

    private FragmentMiCuentaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MiCuentaViewModel miCuentaViewModel =
                new ViewModelProvider(this).get(MiCuentaViewModel.class);

        binding = FragmentMiCuentaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMiCuenta;
        miCuentaViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}