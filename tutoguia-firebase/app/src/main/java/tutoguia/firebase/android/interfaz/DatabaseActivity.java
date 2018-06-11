package tutoguia.firebase.android.interfaz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.datos.Alumno;
import tutoguia.firebase.android.datos.Asignatura;
import tutoguia.firebase.android.funcionalidad.Database;
import tutoguia.firebase.android.util.Functions;

/**
 * Interfaz de la primera pantalla de base de datos
 */
public class DatabaseActivity extends AppCompatActivity {

    // Funcionalidad
    private Database database;

    // Elementos visuales de la pantalla
    private ListView asignaturasList, alumnosList, queryList;
    private TextView numStudentsText;
    private Spinner numStudentsSpinner, citySpinner, citySpinner2;
    private EditText ageText, nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initInterface();
    }

    /**
     * Este método se encarga de crear las pestañas e inicializar los elementos visuales
     */
    private void initInterface(){
        database = new Database();

        // Pestañas
        String items[] = getResources().getStringArray(R.array.database_items);
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("asignaturasTab");
        spec.setContent(R.id.asignaturasTab);
        spec.setIndicator(items[0]);
        host.addTab(spec);

        spec = host.newTabSpec("alumnosTab");
        spec.setContent(R.id.alumnosTab);
        spec.setIndicator(items[1]);
        host.addTab(spec);

        spec = host.newTabSpec("consultasTab");
        spec.setContent(R.id.consultasTab);
        spec.setIndicator(items[2]);
        host.addTab(spec);

        // Lista de pestaña de asignaturas
        asignaturasList = (ListView) findViewById(R.id.asignaturasList);
        final AsignaturasAdapter asignaturasAdapter = new AsignaturasAdapter(this);
        asignaturasList.setAdapter(asignaturasAdapter);
        database.getAsignaturas(asignaturasAdapter);
        asignaturasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openAsignatura((Asignatura) asignaturasAdapter.getItem(i));
            }
        });

        // Lista de pestaña de alumnos
        alumnosList = (ListView) findViewById(R.id.alumnosList);
        final AlumnosAdapter alumnosAdapter = new AlumnosAdapter(this);
        alumnosList.setAdapter(alumnosAdapter);
        database.getAlumnos(alumnosAdapter);
        alumnosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openAlumno((Alumno) alumnosAdapter.getItem(i));
            }
        });

        // Consulta de número de estudiantes por asignatura
        numStudentsText = (TextView) findViewById(R.id.numStudentsText);
        numStudentsSpinner = (Spinner) findViewById(R.id.numStudentsSpinner);
        database.getAsignaturas(this, numStudentsSpinner);
        numStudentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                database.getNumAlumnosByAsignatura(numStudentsSpinner.getSelectedItem().toString(), numStudentsText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Lista de resultados en la pestaña de consultas
        queryList = (ListView) findViewById(R.id.queryList);
        final AlumnosAdapter resultAdapter = new AlumnosAdapter(this);
        queryList.setAdapter(resultAdapter);

        // Desplegable de ciudades
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        database.getCities(this, citySpinner);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                database.getAlumnosByCity(resultAdapter, citySpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ageText = (EditText) findViewById(R.id.ageText);
        citySpinner2 = (Spinner) findViewById(R.id.citySpinner2);
        database.getCities(this, citySpinner2);
        citySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int age = Functions.parseSecureInt(ageText.getText().toString(), 0);
                database.getAlumnosByCityAndAge(resultAdapter, citySpinner2.getSelectedItem().toString(), age);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ageText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    int age = Functions.parseSecureInt(ageText.getText().toString(), 0);
                    database.getAlumnosByCityAndAge(resultAdapter, citySpinner2.getSelectedItem().toString(), age);
                }
                return false;
            }
        });

        nameText = (EditText) findViewById(R.id.nameText);
        nameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    database.getAlumnosByName(resultAdapter, nameText.getText().toString());
                }
                return false;
            }
        });
    }

    /**
     * Abre una nueva pantalla para gestionar una asignatura
     *
     * @param asignatura Asignatura a gestionar
     */
    private void openAsignatura(Asignatura asignatura){
        Intent intent = new Intent(this, AsignaturaActivity.class);
        if(asignatura!=null){
            intent.putExtra(AsignaturaActivity.PARAM_ASIGNATURA_NAME, asignatura.getName());
        }
        startActivity(intent);
    }

    /**
     * Abre una nueva pantalla para crear una asignatura
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void createAsignatura(View view){
        openAsignatura(null);
    }

    private void openAlumno(Alumno alumno){
        Intent intent = new Intent(this, AlumnoActivity.class);
        if(alumno!=null){
            intent.putExtra(AlumnoActivity.PARAM_ALUMNO_DNI, alumno.getDni());
        }
        startActivity(intent);
    }

    /**
     * Abre una nueva pantalla para crear una alumno
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void createAlumno(View view){
        openAlumno(null);
    }
}
