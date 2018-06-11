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
import tutoguia.firebase.android.datos.Alumno;

/**
 * Created by apedroche on 19/12/17.
 */

public class AlumnosAdapter extends BaseAdapter {

    // Variable necesaria para interactuar con la interfaz
    private Activity context;

    // Elementos que se mostrarán
    private ArrayList<Alumno> items;

    /**
     * Inicialización
     *
     * @param context Actividad desde la que se están gestionando los alumnos
     */
    public AlumnosAdapter(Activity context){
        this.context = context;
        items = new ArrayList<>();
    }

    /**
     * Actualiza los datos de la lista
     *
     * @param items Datos actualizados
     */
    public void updateItems(ArrayList<Alumno> items){
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
            v = inf.inflate(R.layout.alumno_item, null);
        }

        Alumno item = items.get(position);

        TextView itemDNI = (TextView) v.findViewById(R.id.itemDNI);
        itemDNI.setText(item.getDni());
        TextView itemName = (TextView) v.findViewById(R.id.itemName);
        itemName.setText(item.getName());
        TextView itemCity = (TextView) v.findViewById(R.id.itemCity);
        itemCity.setText(item.getBirthCity());
        TextView itemAge = (TextView) v.findViewById(R.id.itemAge);
        itemAge.setText(String.valueOf(item.getAge()));

        return v;
    }
}
