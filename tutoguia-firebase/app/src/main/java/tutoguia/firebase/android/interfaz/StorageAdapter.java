package tutoguia.firebase.android.interfaz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.datos.UploadedFile;
import tutoguia.firebase.android.funcionalidad.Storage;

/**
 * Gestiona la lista que aparece en la pantalla de almacenamiento
 */
public class StorageAdapter extends BaseAdapter {

    // Variable necesaria para interactuar con la interfaz
    private Activity context;

    // Funcionalidad
    private Storage storage;

    // Elementos que se mostrarán
    private ArrayList<UploadedFile> items;

    /**
     * Inicialización
     *
     * @param context Actividad desde la que se está gestionando el almacenamiento
     * @param storage Funcionalidad que se está utilizando
     */
    public StorageAdapter(Activity context, Storage storage){
        this.context = context;
        this.storage = storage;
        items = new ArrayList<>();
    }

    /**
     * Actualiza los datos de la lista
     *
     * @param items Datos actualizados
     */
    public void updateItems(ArrayList<UploadedFile> items){
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
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.storage_item, null);
        }

        final UploadedFile item = items.get(position);

        final TextView itemName = (TextView) v.findViewById(R.id.itemName);
        final TextView itemType = (TextView) v.findViewById(R.id.itemType);
        TextView itemSize = (TextView) v.findViewById(R.id.itemSize);

        // Leemos la información de Firebase Storage
        storage.getMetadata(item.getDownloadUri(), itemName, itemType, itemSize);

        // Botón de descarga
        ((Button) v.findViewById(R.id.downloadButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storage.saveFile(context, item, itemType.getText().toString());
            }
        });

        // Botón de eliminar
        ((Button) v.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setMessage(context.getString(R.string.sure_to_delete_file));
                alert.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        storage.deleteFile(itemName.getText().toString(), item.getDownloadUri());
                    }
                });
                alert.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                alert.show();
            }
        });

        return v;
    }
}
