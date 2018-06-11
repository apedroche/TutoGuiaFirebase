package tutoguia.firebase.android.interfaz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tutoguia.firebase.android.R;

/**
 * Created by apedroche on 15/2/18.
 */

public class UploadedFileAdapter extends BaseAdapter {

    // Variable necesaria para interactuar con la interfaz
    private Activity context;

    // Elementos que se mostrarán
    private ArrayList<String> items;

    /**
     * Inicialización
     *
     * @param context Actividad desde la que se están gestionando los alumnos
     */
    public UploadedFileAdapter(Activity context){
        this.context = context;
        items = new ArrayList<>();
    }

    /**
     * Actualiza los datos de la lista
     *
     * @param items Datos actualizados
     */
    public void updateItems(ArrayList<String> items){
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Generamos una convertView por motivos de eficiencia
        View v = convertView;

        //Asociamos el layout de la lista que hemos creado
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.uploaded_file_item, null);
        }

        String item = items.get(position);

        TextView lineText = (TextView) v.findViewById(R.id.lineText);
        lineText.setText(item);

        return v;
    }
}
