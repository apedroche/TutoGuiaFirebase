package tutoguia.firebase.android.interfaz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.datos.UploadedFile;
import tutoguia.firebase.android.funcionalidad.Database;
import tutoguia.firebase.android.funcionalidad.Storage;

/**
 * Interfaz de la pantalla de almacenamiento
 */
public class StorageActivity extends AppCompatActivity {

    // Código de retorno para seleccionar un archivo
    private static final int SELECT_FILE_REQUEST = 100;

    // Funcionalidad
    private Storage storage;
    private Database database;

    // Elementos visuales de la pantalla
    private ListView storageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageList = (ListView) findViewById(R.id.storageList);

        storage = new Storage(this);
        database = new Database();

        loadStorageFiles();
    }

    /**
     * Carga los archivos desde Firebase Storage a la lista que se muestra en pantalla
     */
    private void loadStorageFiles(){
        final StorageAdapter adapter = new StorageAdapter(this, storage);
        storageList.setAdapter(adapter);

        database.storageGetFiles(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SELECT_FILE_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    //Log.d("ARCHIVO", "Uri: " + uri.toString());
                    storage.uploadFile(new UploadedFile(this, uri), uri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* Abre la pantalla para elegir un archivo
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void uploadFile(View view){
        storage.selectFile(SELECT_FILE_REQUEST);
    }
}
