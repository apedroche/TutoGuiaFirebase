package tutoguia.firebase.android.interfaz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.datos.UploadedFile;
import tutoguia.firebase.android.funcionalidad.Authentication;
import tutoguia.firebase.android.funcionalidad.Database;
import tutoguia.firebase.android.funcionalidad.Storage;
import tutoguia.firebase.android.util.Functions;

public class CloudFunctionsActivity extends AppCompatActivity {

    // Código de retorno para seleccionar un archivo
    private static final int SELECT_FILE_REQUEST = 100;

    // Funcionalidad
    private Authentication authentication;
    private Storage storage;
    private Database database;

    // Elementos visuales de la pantalla
    private EditText emailText, passwordText;
    private ListView lineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_functions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authentication = new Authentication(this);
        storage = new Storage(this);
        database = new Database();

        initInterface();
    }

    /**
     * Este método se encarga de crear las pestañas e inicializar los elementos visuales
     */
    private void initInterface(){
        String items[] = getResources().getStringArray(R.array.cloud_functions_items);
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("authTab");
        spec.setContent(R.id.authTab);
        spec.setIndicator(items[0]);
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("storageTab");
        spec.setContent(R.id.storageTab);
        spec.setIndicator(items[1]);
        host.addTab(spec);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        lineList = (ListView) findViewById(R.id.lineList);

        loadFileContent();
    }

    /**
     * Este método se encarga de mostrar el contenido del archivo subido
     */
    private void loadFileContent(){
        final UploadedFileAdapter adapter = new UploadedFileAdapter(this);
        lineList.setAdapter(adapter);

        database.getUploadedFile(adapter);
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

    /**
     * Crea un usuario a partir de correo y contraseña
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void createUser(View view){
        if(emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty())
            Functions.showMessage(this, getString(R.string.login_empty), getString(R.string.ok));
        else
            authentication.createNewUser(emailText.getText().toString(), passwordText.getText().toString());
    }

    /* Abre la pantalla para elegir un archivo
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void uploadFile(View view){
        storage.selectTextFile(SELECT_FILE_REQUEST);
    }
}
