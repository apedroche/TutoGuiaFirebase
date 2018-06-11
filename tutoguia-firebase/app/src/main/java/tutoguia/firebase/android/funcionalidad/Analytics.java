package tutoguia.firebase.android.funcionalidad;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import tutoguia.firebase.android.util.Functions;

/**
 * Created by apedroche on 2/4/18.
 */

public class Analytics {

    // Contexto de la actividad
    private Context context;

    // Objetos de analytics de Firebase
    private FirebaseAnalytics mFirebaseAnalytics;

    /**
     * Inicialización
     *
     * @param context Contexto de la actividad
     */
    public Analytics(Context context){
        this.context = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    /**
     * Registra que se ha entrado en un evento.
     * Coge el id del dispositivo y la fecha actual
     *
     * @param eventName nombre del evento
     */
    public void registerEnterEvent(String eventName){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Functions.getDeviceId(context));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName);
        bundle.putString(FirebaseAnalytics.Param.START_DATE, Functions.getCurrentDate());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    /**
     * Envía el valor elegido en el evento
     *
     * @param eventName nombre del evento
     * @param selection valor elegido
     */
    public void sendSelectionEvent(String eventName, int selection){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Functions.getDeviceId(context));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName);
        bundle.putInt(FirebaseAnalytics.Param.VALUE, selection);
        bundle.putString(FirebaseAnalytics.Param.END_DATE, Functions.getCurrentDate());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * Establece las propiedades del usuario
     *
     * @param smoker si es fumador, ocasional o no fumador
     * @param occupation trabajador, estudiante o empleado
     */
    public void setProperties(String smoker, String occupation){
        mFirebaseAnalytics.setUserProperty("smoker", smoker);
        mFirebaseAnalytics.setUserProperty("occupation", occupation);
        mFirebaseAnalytics.setUserProperty("properties_change", Functions.getCurrentDate());
    }
}
