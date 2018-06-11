package tutoguia.firebase.android.funcionalidad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import tutoguia.firebase.android.BuildConfig;
import tutoguia.firebase.android.R;
import tutoguia.firebase.android.datos.UploadedFile;
import tutoguia.firebase.android.util.Functions;

/**
 * Funcionalidad de almacenamiento
 */
public class Storage {

    // Variable necesaria para interactuar con la interfaz
    private Activity mActivity;

    // Objetos de almacenamiento de Firebase
    private FirebaseStorage mStorage;
    private StorageReference mReference;

    /**
     * Inicialización
     *
     * @param activity Actividad desde la que se está gestionando el almacenamiento
     */
    public Storage(Activity activity){
        mActivity = activity;
        mStorage = FirebaseStorage.getInstance();
        mReference = mStorage.getReference();
    }

    /**
     * Abre un selector para elegir el archivo que se quiere subir
     *
     * @param requestCode Código de retorno
     */
    public void selectFile(int requestCode){
        selectFile(requestCode, "*/*");
    }

    /**
     * Abre un selector para elegir el archivo de texto que se quiere subir
     *
     * @param requestCode Código de retorno
     */
    public void selectTextFile(int requestCode){
        selectFile(requestCode, "text/*");
    }

    /**
     * Función privada para elegir el archivo a subir dependiento del tipo
     *
     * @param requestCode Código de retorno
     * @param fileType Tipo de archivo permitido
     */
    private void selectFile(int requestCode, String fileType){
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(fileType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            mActivity.startActivityForResult(Intent.createChooser(intent, mActivity.getString(R.string.upload_file)), requestCode);
        }
        catch (ActivityNotFoundException ex){
            Functions.showMessage(mActivity, mActivity.getString(R.string.select_file_error), mActivity.getString(R.string.ok));
        }
    }

    /**
     * Subir un archivo local a Firebase Storage
     * Para poder gestionarlo posteriormente, se añade una referencia en la base de datos
     *
     * @param file Archivo que se utiliza para gestionar referencias en la base de datos
     * @param uri Uri del archivo local
     */
    public void uploadFile(final UploadedFile file, final Uri uri){
        final AlertDialog alert = new AlertDialog.Builder(mActivity).create();
        alert.setMessage(mActivity.getString(R.string.uploading_file));
        alert.show();

        StorageReference reference = mReference.child(file.getFilename());
        UploadTask uploadTask = reference.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alert.dismiss();
                //Log.e("UPLOAD FILE", e.getMessage());
                e.printStackTrace();
                Functions.showMessage(mActivity, mActivity.getString(R.string.upload_file_error) + " " + e.getMessage(), mActivity.getString(R.string.ok));
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                alert.dismiss();
                String msg = String.format(mActivity.getString(R.string.upload_file_ok), taskSnapshot.getBytesTransferred());
                Functions.showMessage(mActivity, msg, mActivity.getString(R.string.ok));

                Database database = new Database();
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                file.setDownloadUri(downloadUri.toString());
                database.storageUploadFile(file.getFilename(), file);
            }
        });
    }

    /**
     * Obtiene algunos valores del fichero subido a Firebase Storage
     *
     * @param url   Url del fichero
     * @param nameText TextView donde se mostrará el nombre del archivo
     * @param typeText TextView donde se mostrará el tipo del archivo
     * @param sizeText TextView donde se mostrará el tamaño del archivo
     */
    public void getMetadata(String url, final TextView nameText, final TextView typeText, final TextView sizeText){
        StorageReference reference = mStorage.getReferenceFromUrl(url);
        reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                nameText.setText(storageMetadata.getName());
                typeText.setText(storageMetadata.getContentType());
                sizeText.setText(storageMetadata.getSizeBytes() + " bytes");

                //Log.v("Metadata", storageMetadata.getName());
            }
        });
    }

    /**
     * @param type Tipo del archivo (ej: image/png)
     * @return Extensión del archivo (ej: png)
     */
    private String getExtensionFromType(String type){
        return "." + type.substring(type.lastIndexOf('/') + 1);
    }

    /**
     * Descarga un archivo de Firebase Storage a la carpeta "Descargas" de dispositivo
     *
     * @param activity Actividad desde la que se está gestionando el almacenamiento
     * @param file Archivo que gestiona la referecia a la base de datos. Contiene nombre y extensión del archivo
     * @param type Tipo del archivo, necesario para guardarlo localmente
     */
    public void saveFile(final Activity activity, UploadedFile file, final String type){
        StorageReference reference = mStorage.getReferenceFromUrl(file.getDownloadUri());
        try {
            File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            final File localFile = File.createTempFile(file.getFilename(), "." + file.getExtension(), storageDir);
            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Log.v("Storage", "Archivo guardado: " + localFile.getPath());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setDataAndType(FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", localFile), type);
                    }
                    else {
                        //Uri uri = Uri.fromFile(localFile);
                        intent.setDataAndType(Uri.fromFile(localFile), type);
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(intent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un archivo de Firebase Storage
     *
     * @param name Nombre del archivo, necesario para eliminar la referencia en la base de datos
     * @param url Url del archivo en Firebase Storage
     */
    public void deleteFile(String name, String url){
        StorageReference reference = mStorage.getReferenceFromUrl(url);
        reference.delete();
        Database database = new Database();
        database.storageDeleteFile(name);
    }
}
