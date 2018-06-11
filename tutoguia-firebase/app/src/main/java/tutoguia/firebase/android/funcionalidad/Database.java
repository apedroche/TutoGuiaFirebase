package tutoguia.firebase.android.funcionalidad;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tutoguia.firebase.android.datos.Alumno;
import tutoguia.firebase.android.datos.Asignatura;
import tutoguia.firebase.android.datos.MessagingToken;
import tutoguia.firebase.android.datos.UploadedFile;
import tutoguia.firebase.android.interfaz.AlumnoActivity;
import tutoguia.firebase.android.interfaz.AlumnosAdapter;
import tutoguia.firebase.android.interfaz.AsignaturaActivity;
import tutoguia.firebase.android.interfaz.AsignaturasAdapter;
import tutoguia.firebase.android.interfaz.CloudMessagingActivity;
import tutoguia.firebase.android.interfaz.StorageAdapter;
import tutoguia.firebase.android.interfaz.UploadedFileAdapter;

/**
 * Funcionalidad de base de datos
 */
public class Database {

    // Nombre de la tabla que se utiliza para guardar los ficheros subidos a Firebase Storage
    private static final String STORAGE_TABLE = "storage";

    // Tabla y columnas de la tabla asignaturas
    private static final String ASIGNATURAS_TABLE = "asignaturas";

    // Tabla y columnas de la tabla alumnos
    private static final String ALUMNOS_TABLE = "alumnos";
    private static final String NAME_COLUMN = "name";
    private static final String BIRTHCITY_COLUMN = "birthCity";

    // Tabla y columnas de la tabla asignaturas-alumnos
    private static final String ASIGNATURAS_ALUMNOS_TABLE = "asignaturas-alumnos";

    // Tabla donde se guarda el archivo de texto subido
    private static final String UPLOADED_FILE_TABLE = "File";

    // Tabla donde se guarda el token para enviar mensajes
    private static final String TOKENS_TABLE = "tokens";

    // Objeto de base de datos de Firebase
    private FirebaseDatabase mDatabase;

    /**
     * Inicialización
     */
    public Database(){
        mDatabase = FirebaseDatabase.getInstance();
    }

    /**
     * Registra un fichero que se ha subido a Firebase Storage
     *
     * @param fileId Identificador del fichero
     * @param file Información del archivo que se va a subir
     */
    public void storageUploadFile(String fileId, UploadedFile file){
        mDatabase.getReference().child(STORAGE_TABLE).child(fileId).setValue(file);
    }

    /**
     * Lee los ficheros que se subieron a Firebase Storage
     *
     * @param adapter Objeto que maneja la lista de ficheros
     */
    public void storageGetFiles(final StorageAdapter adapter){
        DatabaseReference reference = mDatabase.getReference().child(STORAGE_TABLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UploadedFile> items = new ArrayList<UploadedFile>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UploadedFile file = snapshot.getValue(UploadedFile.class);
                    items.add(file);
                }
                adapter.updateItems(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Elimina un fichero subido a Firebase Storage de la base de datos
     * El fichero debe eliminarse de forma independiente
     *
     * @param fileId Identificador del fichero
     */
    public void storageDeleteFile(String fileId){
        mDatabase.getReference().child(STORAGE_TABLE).child(fileId).removeValue();
    }

    /**
     * Crea o actualiza una asignatura
     *
     * @param asignatura Asignatura a crear
     */
    public void createAsignatura(Asignatura asignatura){
        DatabaseReference reference = mDatabase.getReference().child(ASIGNATURAS_TABLE).child(asignatura.getName());
        reference.setValue(asignatura);
    }

    /**
     * Obtiene la lista de asignaturas y las muestra
     *
     * @param adapter Objeto que maneja la lista de asignaturas
     */
    public void getAsignaturas(final AsignaturasAdapter adapter){
        DatabaseReference reference = mDatabase.getReference().child(ASIGNATURAS_TABLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Asignatura> items = new ArrayList<Asignatura>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Asignatura asignatura = snapshot.getValue(Asignatura.class);
                    items.add(asignatura);
                }
                adapter.updateItems(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Obtiene la lista de asignaturas y las muestra en un spinner
     *
     * @param context Contexto en el que se mostrará el spinner, normalmente un Activity
     * @param spinner Spinner en el que se mostrará la lista
     */
    public void getAsignaturas(final Context context, final Spinner spinner){
        DatabaseReference reference = mDatabase.getReference().child(ASIGNATURAS_TABLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> items = new ArrayList<String>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Asignatura asignatura = snapshot.getValue(Asignatura.class);
                    items.add(asignatura.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Obtiene una asignatura a partir de su nombre
     *
     * @param activity Actividad en la que se cargan los datos
     * @param asignaturaName Nombre de la asignatura
     */
    public void getAsignaturaByName(final AsignaturaActivity activity, String asignaturaName){
        mDatabase.getReference().child(ASIGNATURAS_TABLE).child(asignaturaName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activity.asignaturaReady(dataSnapshot.getValue(Asignatura.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Elimina una asignatura
     *
     * @param asignatura Asignatura a eliminar
     */
    public void deleteAsignaturas(Asignatura asignatura){
        mDatabase.getReference().child(ASIGNATURAS_TABLE).child(asignatura.getName()).removeValue();
    }

    /**
     * Añade la lista de asignaturas que tiene un alumno
     *
     * @param alumno Alumno que se va a actualizar
     */
    private void updateAsignaturasAlumno(Alumno alumno){
        for(String asignatura : alumno.getAsignaturas()){
            DatabaseReference ref = mDatabase.getReference().child(ASIGNATURAS_ALUMNOS_TABLE).child(alumno.getDni()+asignatura);
            ref.child(ASIGNATURAS_TABLE).setValue(asignatura);
            ref.child(ALUMNOS_TABLE).setValue(alumno.getDni());
        }
    }

    /**
     * Crea o actualiza un alumno.
     * Para mantener la lista de asignaturas del alumno, se borran todas a las que pertenece y después se añaden
     *
     * @param alumno
     */
    public void createAlumno(final Alumno alumno){
        DatabaseReference reference = mDatabase.getReference().child(ALUMNOS_TABLE).child(alumno.getDni());
        reference.setValue(alumno);

        // Obtenemos la lista de asignaturas del alumno
        mDatabase.getReference().child(ASIGNATURAS_ALUMNOS_TABLE).orderByChild(ALUMNOS_TABLE).equalTo(alumno.getDni()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Eliminamos todas las referencias
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();
                }
                // Añadimos las referencias nuevas
                updateAsignaturasAlumno(alumno);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                updateAsignaturasAlumno(alumno);
            }
        });
    }

    /**
     * Obtiene la lista de alumnos y las muestra
     *
     * @param adapter Objeto que maneja la lista de alumnos
     */
    public void getAlumnos(final AlumnosAdapter adapter){
        DatabaseReference reference = mDatabase.getReference().child(ALUMNOS_TABLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Alumno> items = new ArrayList<Alumno>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Alumno alumno = snapshot.getValue(Alumno.class);
                    items.add(alumno);
                }
                adapter.updateItems(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Obtiene un alumno a partir de su DNI
     *
     * @param activity Actividad en la que se cargan los datos
     * @param alumnoDni DNI del alumno
     */
    public void getAlumnoByDni(final AlumnoActivity activity, String alumnoDni){
        mDatabase.getReference().child(ALUMNOS_TABLE).child(alumnoDni).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activity.alumnoReady(dataSnapshot.getValue(Alumno.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Elimina un alumno
     *
     * @param alumno Alumno a eliminar
     */
    public void deleteAlumno(Alumno alumno){
        for(String asignatura : alumno.getAsignaturas()){
            mDatabase.getReference().child(ASIGNATURAS_ALUMNOS_TABLE).child(alumno.getDni()+asignatura).removeValue();
        }
        mDatabase.getReference().child(ALUMNOS_TABLE).child(alumno.getDni()).removeValue();
    }

    /**
     * Obtiene el número de alumnos que están apuntados a una asignatura
     *
     * @param asignaturaName Nombre de la asignatura
     * @param resultText TextView donde se mostrará el resultado
     */
    public void getNumAlumnosByAsignatura(final String asignaturaName, final TextView resultText){
        mDatabase.getReference().child(ASIGNATURAS_ALUMNOS_TABLE).orderByChild(ASIGNATURAS_TABLE).equalTo(asignaturaName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultText.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Obtiene la lista de ciudades y las muestra en un spinner
     *
     * @param context Contexto en el que se mostrará el spinner, normalmente un Activity
     * @param spinner Spinner en el que se mostrará la lista de ciudades
     */
    public void getCities(final Context context, final Spinner spinner){
        DatabaseReference reference = mDatabase.getReference().child(ALUMNOS_TABLE);
        reference.orderByChild(BIRTHCITY_COLUMN).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> items = new ArrayList<String>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Alumno alumno = snapshot.getValue(Alumno.class);
                    if(!items.contains(alumno.getBirthCity()))
                        items.add(alumno.getBirthCity());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Obtiene los alumnos que nacieron en una ciudad y los muestra en una lista
     *
     * @param adapter Objeto en el que se mostrará la lista
     * @param alumnoCity Ciudad por la que se buscará
     */
    public void getAlumnosByCity(final AlumnosAdapter adapter, String alumnoCity){
        mDatabase.getReference().child(ALUMNOS_TABLE).orderByChild(BIRTHCITY_COLUMN).equalTo(alumnoCity).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Alumno> items = new ArrayList<Alumno>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Alumno alumno = snapshot.getValue(Alumno.class);
                    items.add(alumno);
                }
                adapter.updateItems(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Obtiene los alumnos que nacieron en una ciudad mayores de una edad y los muestra en una lista
     *
     * @param adapter Objeto en el que se mostrará la lista
     * @param alumnoCity Ciudad por la que se buscará
     * @param alumnoAge Edad por la que se comparará
     */
    public void getAlumnosByCityAndAge(final AlumnosAdapter adapter, String alumnoCity, final int alumnoAge){
        mDatabase.getReference().child(ALUMNOS_TABLE).orderByChild(BIRTHCITY_COLUMN).equalTo(alumnoCity).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Alumno> items = new ArrayList<Alumno>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Alumno alumno = snapshot.getValue(Alumno.class);
                    if(alumno.getAge()>alumnoAge)
                        items.add(alumno);
                }
                adapter.updateItems(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Obtiene los alumnos cuyo nombre coincida
     *
     * @param adapter Objeto en el que se mostrará la lista
     * @param alumnoName Nombre por el que se buscará
     */
    public void getAlumnosByName(final AlumnosAdapter adapter, String alumnoName){
        mDatabase.getReference().child(ALUMNOS_TABLE).orderByChild(NAME_COLUMN).equalTo(alumnoName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Alumno> items = new ArrayList<Alumno>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Alumno alumno = snapshot.getValue(Alumno.class);
                    items.add(alumno);
                }
                adapter.updateItems(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Obtiene el archivo de texto subido y lo muestra en una lista
     *
     * @param adapter Objeto en el que se mostrará la lista
     */
    public void getUploadedFile(final UploadedFileAdapter adapter){
        mDatabase.getReference().child(UPLOADED_FILE_TABLE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> items = new ArrayList<String>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String s = snapshot.getValue(String.class);
                    items.add(s);
                }
                adapter.updateItems(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Crea o actualiza una token
     *
     * @param token Token a crear
     */
    public void createToken(MessagingToken token){
        DatabaseReference reference = mDatabase.getReference().child(TOKENS_TABLE).child(token.getId());
        reference.setValue(token);
    }


    /**
     * Obtiene la lista de tokens y los muestra en un spinner
     *
     * @param context Actividad en la que se mostrará el spinner
     */
    public void getTokens(final CloudMessagingActivity context){
        DatabaseReference reference = mDatabase.getReference().child(TOKENS_TABLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MessagingToken> tokens = new ArrayList<MessagingToken>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MessagingToken token = snapshot.getValue(MessagingToken.class);
                    tokens.add(token);
                }

                context.setTokens(tokens);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
