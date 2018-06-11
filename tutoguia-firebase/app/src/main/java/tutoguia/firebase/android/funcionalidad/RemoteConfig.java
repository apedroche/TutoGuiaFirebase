package tutoguia.firebase.android.funcionalidad;

import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import tutoguia.firebase.android.R;

/**
 * Created by apedroche on 2/3/18.
 */

public class RemoteConfig {
    // Modos configurados
    public static final String DEV_MODE = "dev_";
    public static final String UAT_MODE = "uat_";
    public static final String PROD_MODE = "prod_";

    // Valores guardados en la configuración
    private static final String MESSAGE_TEXT = "message_text";
    private static final String MESSAGE_CAPS = "message_caps";

    // Objeto de configuración de Firebase
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    /**
     * Inicializa la configuración por defecto, utilizando el fichero remote_config_defaults.xml
     */
    public RemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    }

    /**
     * Obtiene los valores de los parámetros actualizados
     *
     * @return tarea que, cuando esté completada, tendrá los parámetros actualizados
     */
    public Task<Void> fetch(){
        return mFirebaseRemoteConfig.fetch(0);
    }

    /**
     * Copia los valores almacenados en Firebase a la configuración actual
     */
    public void activate(){
        mFirebaseRemoteConfig.activateFetched();
    }

    /**
     * @param mode modo del que se obtendrán los valores
     * @return mensaje del modo seleccionado
     */
    public String getMessage(String mode){
        return mFirebaseRemoteConfig.getString(mode + MESSAGE_TEXT);
    }

    /**
     * @param mode modo del que se obtendrán los valores
     * @return true si el mensaje tiene que mostrarse en mayúsculas
     */
    public boolean isMessageCaps(String mode){
        return mFirebaseRemoteConfig.getBoolean(mode + MESSAGE_CAPS);
    }
}
