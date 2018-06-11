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
import tutoguia.firebase.android.datos.Asignatura;

/**
 * Created by apedroche on 18/12/17.
 */
public class AsignaturasAdapter extends BaseAdapter {

    // Variable necesaria para interactuar con la interfaz
    protected Activity context;

    // Elementos que se mostrarán
    private ArrayList<Asignatura> items;

    /**
     * Inicialización
     *
     * @param context Actividad desde la que se están gestionando las asignaturas
     */
    public AsignaturasAdapter(Activity context){
        this.context = context;
        items = new ArrayList<>();
    }

    /**
     * Actualiza los datos de la lista
     *
     * @param items Datos actualizados
     */
    public void updateItems(ArrayList<Asignatura> items){
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
            v = inf.inflate(R.layout.asignatura_item, null);
        }

        Asignatura item = items.get(position);

        TextView itemName = (TextView) v.findViewById(R.id.itemName);
        itemName.setText(item.getName());
        TextView itemTeacher = (TextView) v.findViewById(R.id.itemTeacher);
        itemTeacher.setText(item.getTeacher());

        return v;
    }
}
