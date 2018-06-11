package tutoguia.firebase.android.datos;

import android.content.Context;
import android.net.Uri;

import tutoguia.firebase.android.util.Functions;

/**
 * Definición de un tipo de datos necesario para guardar una referencia de los archivos subidos a Storage
 * Los archivos se suben a Storage pero se añaden a Database para poder listarlos
 */
public class UploadedFile {
    // Datos
    String filename, extension, downloadUri;

    /**
     * Constructor por defecto
     */
    public UploadedFile(){

    }

    /**
     * Constructor específico
     *
     * @param context Contexto, normalmente un Activity
     * @param uri Uri del archivo local que se va a subir. A partir de ella se extrae el nombre del archivo y la extensión
     */
    public UploadedFile(Context context, Uri uri){
        String temp = Functions.getFilenameFromUri(context, uri);
        filename = temp.substring(0, temp.indexOf('.'));
        extension = temp.substring(temp.lastIndexOf('.')+1);
    }

    /**
     *
     * @return nombre del archivo
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename nombre del archivo
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return extensión del archivo (ej: png)
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension extensión del archivo (ej: png)
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return String representando la Uri necesaria para descargar el archivo de Storage
     */
    public String getDownloadUri() {
        return downloadUri;
    }

    /**
     * @param downloadUri String representando la Uri necesaria para descargar el archivo de Storage
     */
    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }
}
