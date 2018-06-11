package tutoguia.firebase.android.interfaz;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.funcionalidad.RemoteConfig;

/**
 * Interfaz de la pantalla de Remote Config
 */
public class RemoteConfigActivity extends AppCompatActivity {

    // Funcionalidad
    private RemoteConfig remoteConfig;

    //private String selectedMode;

    // Elementos visuales de la pantalla
    private TextView devText, uatText, prodText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_config);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        remoteConfig = new RemoteConfig();

        initInterface();
    }

    /**
     * Este método se encarga de crear las pestañas e inicializar los elementos visuales
     */
    private void initInterface(){

        // Pestañas
        String items[] = getResources().getStringArray(R.array.remote_config_items);
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("devTab");
        spec.setContent(R.id.devTab);
        spec.setIndicator(items[0]);
        host.addTab(spec);

        spec = host.newTabSpec("uatTab");
        spec.setContent(R.id.uatTab);
        spec.setIndicator(items[1]);
        host.addTab(spec);

        spec = host.newTabSpec("prodTab");
        spec.setContent(R.id.prodTab);
        spec.setIndicator(items[2]);
        host.addTab(spec);

        devText = (TextView) findViewById(R.id.devText);
        uatText = (TextView) findViewById(R.id.uatText);
        prodText = (TextView) findViewById(R.id.prodText);

        /*host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s){
                    case "devTab":
                        selectedMode = RemoteConfig.DEV_MODE;
                        updateView();
                        break;
                    case "uatTab":
                        selectedMode = RemoteConfig.UAT_MODE;
                        updateView();
                        break;
                    case "prodTab":
                        selectedMode = RemoteConfig.PROD_MODE;
                        updateView();
                        break;
                }
            }
        });*/

        remoteConfig.fetch().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    remoteConfig.activate();
                }
                else {
                    // Error
                }
                updateView();
            }
        });
    }

    /**
     * Este método se encarga de cargar los valores de remote config y mostrarlo en pantalla
     */
    private void updateView(){
        devText.setText(remoteConfig.getMessage(RemoteConfig.DEV_MODE));
        devText.setAllCaps(remoteConfig.isMessageCaps(RemoteConfig.DEV_MODE));

        uatText.setText(remoteConfig.getMessage(RemoteConfig.UAT_MODE));
        uatText.setAllCaps(remoteConfig.isMessageCaps(RemoteConfig.UAT_MODE));

        prodText.setText(remoteConfig.getMessage(RemoteConfig.PROD_MODE));
        prodText.setAllCaps(remoteConfig.isMessageCaps(RemoteConfig.PROD_MODE));
    }
}
