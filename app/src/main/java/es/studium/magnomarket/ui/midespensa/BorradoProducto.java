package es.studium.magnomarket.ui.midespensa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import es.studium.magnomarket.ProductoDespensa;
import es.studium.magnomarket.R;

public class BorradoProducto extends DialogFragment implements View.OnClickListener {

    ProductoDespensa producto;
    private TextView mensajeConfirmacion;
    private Button btnSi;
    private Button btnNo;
    public BorradoProducto(ProductoDespensa pr) {
        this.producto = pr;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_borrado_producto, null);
        builder.setTitle("Borrado Producto: " + producto.getNombreProductoDespensa()).setView(dialogView);

        btnSi = dialogView.findViewById(R.id.btnSiBorradoProducto);
        btnSi.setOnClickListener(this);
        btnNo = dialogView.findViewById(R.id.btnNoBorradoProducto);
        btnNo.setOnClickListener(this);
        mensajeConfirmacion = dialogView.findViewById(R.id.textViewConfirmacionBorrado);
        mensajeConfirmacion.setText(mensajeConfirmacion.getText() + " " + producto.getNombreProductoDespensa() + "?");
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnNo.getId()) {
            dismiss();
        }

    }
}
