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

        // Inflate the view if it hasn't been already
        if (v == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
            v = layoutInflater.inflate(this.distribucion, null);
        }

        ProductoDespensa currentItem = items.get(position);

        TextView textView = v.findViewById(R.id.textView);
        textView.setText(currentItem.getNombreProductoDespensa());

        ImageView productPhoto = v.findViewById(R.id.imageView);
        String imageUrl = currentItem.getImagenProductoDespensa();

        Glide.with(contexto)
                .load(Uri.parse(imageUrl))
                .placeholder(R.drawable.no_photo)
                .error(R.drawable.no_photo)
                .into(productPhoto);



        LocalDate now = LocalDate.now();
        // expired
        if (currentItem.getFechaCaducidadProductoDespensa().isBefore(now)) {
            v.setBackgroundColor(contexto.getResources().getColor(R.color.expired));
        } else if (currentItem.getFechaCaducidadProductoDespensa().isAfter(now) && currentItem.getFechaCaducidadProductoDespensa().isBefore(now.plusDays(3))) {
            v.setBackgroundColor(contexto.getResources().getColor(R.color.about_to_expire));
        } else if (currentItem.getFechaCaducidadProductoDespensa().isAfter(now.plusDays(3))) {
            v.setBackgroundColor(contexto.getResources().getColor(R.color.good));
        }



        return v;
    }
}

