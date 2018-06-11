package tutoguia.firebase.android.interfaz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.datos.Alumno;
import tutoguia.firebase.android.datos.Asignatura;
import tutoguia.firebase.android.funcionalidad.Database;
import tutoguia.firebase.android.util.Functions;

public class AlumnoActivity extends AppCompatActivity {
    // Parámetro que se recibe para identificar al alumno
    public static final String PARAM_ALUMNO_DNI = "dni";

    // Información del alumno
    private Alumno alumno;

    // Funcionalidad
    private Database database;
    private boolean alumnoExists;

    // Elementos visuales de la pantalla
    private TextView dniText, nameText, cityText, ageText;
    private ListView asignaturasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        dniText = (TextView) findViewById(R.id.dniText);
        nameText = (TextView) findViewById(R.id.nameText);
        cityText = (TextView) findViewById(R.id.cityText);
        ageText = (TextView) findViewById(R.id.ageText);
        asignaturasList = (ListView) findViewById(R.id.asignaturasList);

        database = new Database();

        alumnoExists = getIntent().hasExtra(PARAM_ALUMNO_DNI);
        if(alumnoExists){
            database.getAlumnoByDni(this, getIntent().getStringExtra(PARAM_ALUMNO_DNI));
        }
        else {
            alumno = new Alumno();
            loadAsignaturas();
        }
    }

    /**
     * Carga la lista de asignaturas. Las que tenga el alumno aparecerán marcadas
     */
    private void loadAsignaturas(){
        final AsignaturasCheckAdapter asignaturasAdapter = new AsignaturasCheckAdapter(this, alumno);
        asignaturasList.setAdapter(asignaturasAdapter);
        database.getAsignaturas(asignaturasAdapter);
    }

    /**
     * Carga los datos del alumno en el formulario
     *
     * @param alumno Alumno recibido
     */
    public void alumnoReady(Alumno alumno){
        this.alumno = alumno;
        dniText.setText(alumno.getDni());
        nameText.setText(alumno.getName());
        cityText.setText(alumno.getBirthCity());
        ageText.setText(String.valueOf(alumno.getAge()));
        findViewById(R.id.deleteButton).setVisibility(View.VISIBLE);
        loadAsignaturas();
    }

    /**
     * Guarda cualquier dato modificado
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void save(View view) {
        if (dniText.getText().toString().length() == 0) {
            Functions.showMessage(this, getString(R.string.empty_dni), getString(R.string.ok));
            return;
        }

        if(alumno == null)
            alumno = new Alumno();
        alumno.setDni(dniText.getText().toString());
        alumno.setName(nameText.getText().toString());
        alumno.setBirthCity(cityText.getText().toString());
        int age = Functions.parseSecureInt(ageText.getText().toString(), alumno.getAge());
        alumno.setAge(age);
        database.createAlumno(alumno);
        finish();
    }

    /**
     * Elimina toda la información
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void delete(View view){
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setMessage(getString(R.string.sure_to_delete_student));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Alumno alumno = new Alumno();
                alumno.setDni(dniText.getText().toString());
                database.deleteAlumno(alumno);
                finish();
            }
        });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        alert.show();
    }

    /**
     * Clase para gestionar la lista de asignaturas, mostrando checks si el alumno tiene una asignatura
     */
    private class AsignaturasCheckAdapter extends AsignaturasAdapter{

        private Alumno alumno;

        /**
         * Inicialización
         *
         * @param context Actividad desde la que se están gestionando las asignaturas
         * @param alumno Alumno, necesario para saber las asignaturas que tiene
         */
        public AsignaturasCheckAdapter(Activity context, Alumno alumno){
            super(context);
            this.alumno = alumno;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Generamos una convertView por motivos de eficiencia
            View v = convertView;

            //Asociamos el layout de la lista que hemos creado
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.asignatura_check_item, null);
            }

            final Asignatura item = (Asignatura) getItem(position);

            CheckBox asignaturaCheck = (CheckBox) v.findViewById(R.id.asignaturaCheck);
            asignaturaCheck.setText(item.getName());
            asignaturaCheck.setChecked(alumno.hasAsignatura(item.getName()));
            asignaturaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if(checked)
                        alumno.insertAsignatura(item.getName());
                    else
                        alumno.deleteAsignatura(item.getName());
                }
            });

            return v;
        }
    }
}
