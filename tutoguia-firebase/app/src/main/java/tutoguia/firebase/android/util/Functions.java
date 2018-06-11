package tutoguia.firebase.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Funciones genéricas que pueden ser útiles
 */
public class Functions {

    /**
     * Muestra un mensaje con un botón
     *
     * @param context Contexto en el que se mostrará el mensaje, normalmente un Activity
     * @param message Mensaje
     * @param button Texto del botón
     */
    public static void showMessage(Context context, String message, String button){
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        alert.show();
    }

    /**
     * Obtiene el nombre del archivo a partir de una Uri
     *
     * @param context Contexto, normalmente un Activity
     * @param uri Uri del archivo local
     * @return Nombre del archivo (nombre y extensión)
     */
    public static String getFilenameFromUri(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if(cursor==null){
            String str = uri.toString();
            return str.substring(str.lastIndexOf('/')+1);
        }
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(nameIndex);
    }

    /**
     * Obtiene un entero a partir de una cadena.
     * Si no se puede obtener, devuelve el valor por defecto
     *
     * @param intString string que contiene el int
     * @param defaultValue valor por defecto en caso de no poder parsear
     * @return
     */
    public static int parseSecureInt(String intString, int defaultValue){
        try{
            int intValue = Integer.parseInt(intString);
            return intValue;
        }
        catch (NumberFormatException exception){
            return defaultValue;
        }
    }

    /**
     * Obtiene un identificador único de 64-bit
     *
     * @param context Contexto, normalmente un Activity
     * @return identificadar único
     */
    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Obtiene la fecha actual en formato: día-mes-año hora:minutos:segundos
     *
     * @return fecha actual
     */
    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
