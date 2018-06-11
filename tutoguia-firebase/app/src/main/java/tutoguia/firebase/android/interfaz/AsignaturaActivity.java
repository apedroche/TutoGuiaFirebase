package tutoguia.firebase.android.interfaz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.datos.Asignatura;
import tutoguia.firebase.android.funcionalidad.Database;
import tutoguia.firebase.android.util.Functions;

public class AsignaturaActivity extends AppCompatActivity {
    // Parámetro que se recibe para identificar la asignatura
    public static final String PARAM_ASIGNATURA_NAME = "name";

    // Funcionalidad
    private Database database;
    private boolean asignaturaExists;

    // Elementos visuales de la pantalla
    private TextView nameText, teacherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignatura);

        nameText = (TextView) findViewById(R.id.nameText);
        teacherText = (TextView) findViewById(R.id.teacherText);

        database = new Database();

        asignaturaExists = getIntent().hasExtra(PARAM_ASIGNATURA_NAME);
        if(asignaturaExists) {
            database.getAsignaturaByName(this, getIntent().getStringExtra(PARAM_ASIGNATURA_NAME));
        }
    }

    /**
     * Carga los datos de la asignatura en el formulario
     *
     * @param asignatura Asignatura recibida
     */
    public void asignaturaReady(Asignatura asignatura){
        nameText.setText(asignatura.getName());
        teacherText.setText(asignatura.getTeacher());
        findViewById(R.id.deleteButton).setVisibility(View.VISIBLE);
    }

    /**
     * Guarda cualquier dato modificado
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void save(View view){
        if(nameText.getText().toString().length()==0){
            Functions.showMessage(this, getString(R.string.empty_subject), getString(R.string.ok));
            return;
        }

        Asignatura asignatura = new Asignatura();
        asignatura.setName(nameText.getText().toString());
        asignatura.setTeacher(teacherText.getText().toString());

        database.createAsignatura(asignatura);
        finish();
    }

    /**
     * Elimina toda la información
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void delete(View view){
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setMessage(getString(R.string.sure_to_delete_subject));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Asignatura asignatura = new Asignatura();
                asignatura.setName(nameText.getText().toString());
                database.deleteAsignaturas(asignatura);
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
}
