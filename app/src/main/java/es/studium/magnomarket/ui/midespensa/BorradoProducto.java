package es.studium.magnomarket.ui.midespensa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.io.IOException;
import es.studium.magnomarket.BDConexion;
import es.studium.magnomarket.MainActivity;
import es.studium.magnomarket.ProductoDespensa;
import es.studium.magnomarket.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BorradoProducto extends DialogFragment implements View.OnClickListener {

    ProductoDespensa producto;
    private TextView mensajeConfirmacion;
    private Button btnSi;
    private Button btnNo;
    private MiDespensaCallback callback;

    public BorradoProducto(ProductoDespensa pr, MiDespensaCallback callback) {
        this.producto = pr;
        this.callback = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomDialog);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_borrado_producto, null);
        builder.setTitle("Borrado Producto: " + producto.getNombreProductoDespensa()).setView(dialogView);
        btnSi = dialogView.findViewById(R.id.btnSiBorradoProducto);
        btnSi.setOnClickListener(this);
        btnNo = dialogView.findViewById(R.id.btnNoBorradoProducto);
        btnNo.setOnClickListener(this);
        mensajeConfirmacion = dialogView.findViewById(R.id.textViewConfirmacionBorrado);
        mensajeConfirmacion.setText((Html.fromHtml(mensajeConfirmacion.getText() + " <b>" + producto.getNombreProductoDespensa() + "</b>?")));
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnNo.getId()) {
            dismiss();
        } else if (v.getId() == btnSi.getId()) {
            BDConexion.borrarProductoDespensa(producto, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (response.code() == 200) {
                            // borrado realizado correctamente
                            Toast.makeText(getContext(), "La operación se ha realizado correctamente.", Toast.LENGTH_SHORT).show();
                            dismiss();
                            callback.onOperacionCorrectaUpdated(true);
                        } else {
                            Toast.makeText(getContext(), "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });
                }
            });
        }

    }
}
