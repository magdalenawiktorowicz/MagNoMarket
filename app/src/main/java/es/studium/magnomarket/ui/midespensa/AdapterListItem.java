package es.studium.magnomarket.ui.midespensa;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.time.LocalDate;
import java.util.ArrayList;
import es.studium.magnomarket.ProductoDespensa;
import es.studium.magnomarket.R;

// adaptador para cada elemento en ListView de ProductoDespensa
public class AdapterListItem extends BaseAdapter {
    private Context contexto;
    private int distribucion;
    private ArrayList<ProductoDespensa> items;

    public AdapterListItem(Context context, int layout, ArrayList<ProductoDespensa> elementos) {
        this.contexto = context;
        this.distribucion = layout;
        this.items = elementos;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;

        if (v == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
            v = layoutInflater.inflate(this.distribucion, null);
        }

        // obtener un elemento
        ProductoDespensa currentItem = items.get(position);

        // asignarle el nombre del producto
        TextView textView = v.findViewById(R.id.textView);
        textView.setText(currentItem.getNombreProductoDespensa());

        // asignarle una imagen
        ImageView productPhoto = v.findViewById(R.id.imageView);
        String imageUrl = currentItem.getImagenProductoDespensa();

        if (imageUrl != null && !imageUrl.equals("null") && !imageUrl.isBlank()) {
            Glide.with(contexto)
                    .load(Uri.parse(imageUrl))
                    .placeholder(R.drawable.no_photo) // en caso de que no hay acceso a la imagen seleccionada
                    .error(R.drawable.no_photo) // en caso de error
                    .into(productPhoto);
        } else {
            productPhoto.setImageResource(R.drawable.no_photo);
        }

        // asignarle un símbolo informativo de la caducidad
        ImageView simbolo = v.findViewById(R.id.imageView3);

        LocalDate now = LocalDate.now();
        // rojo - cuando un producto está caducado
        if (currentItem.getFechaCaducidadProductoDespensa().isBefore(now)) {
            v.setBackgroundColor(contexto.getResources().getColor(R.color.expired));
            simbolo.setImageResource(R.drawable.expired);
        }
        // amarillo - cuando un producto está a punto de caducarse
        else if (currentItem.getFechaCaducidadProductoDespensa().isEqual(now) || (currentItem.getFechaCaducidadProductoDespensa().isAfter(now) && currentItem.getFechaCaducidadProductoDespensa().isBefore(now.plusDays(3))) || currentItem.getFechaCaducidadProductoDespensa().isEqual(now.plusDays(3))) {
            v.setBackgroundColor(contexto.getResources().getColor(R.color.about_to_expire));
            simbolo.setImageResource(R.drawable.about_to_expire);
        }
        // verde - cuando un producto está fresco
        else if (currentItem.getFechaCaducidadProductoDespensa().isAfter(now.plusDays(3))) {
            v.setBackgroundColor(contexto.getResources().getColor(R.color.good));
            simbolo.setImageResource(R.drawable.good);
        }

        return v;
    }
}

