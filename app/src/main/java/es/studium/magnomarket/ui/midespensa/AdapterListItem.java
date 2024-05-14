package es.studium.magnomarket.ui.midespensa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.studium.magnomarket.ProductoDespensa;
import es.studium.magnomarket.R;

public class AdapterListItem extends BaseAdapter {
    Context contexto;
    int distribucion;
    ArrayList<ProductoDespensa> items;

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

        // inflar la vista con la propia distribución
        LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
        v = layoutInflater.inflate(R.layout.list_item, null);

        // obtener el valor actual según la posición
        String currentName = String.valueOf(items.get(position));

        // referenciar el elemento a modificar y rellenarlo
        TextView textView = v.findViewById(R.id.textView);
        textView.setText(currentName);

        return v;
    }
}
