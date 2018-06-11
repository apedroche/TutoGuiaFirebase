package tutoguia.firebase.android.interfaz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.funcionalidad.Analytics;
import tutoguia.firebase.android.util.Functions;

public class AnalyticsActivity extends AppCompatActivity {

    // Funcionalidad
    private Analytics analytics;

    // Elementos visuales de la pantalla
    private Button eventEnterButton;
    private LinearLayout eventLayout;
    private RadioGroup eventRadioGroup;
    private Spinner smokerSpinner, occupationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initInterface();
    }

    /**
     * Este método se encarga de crear las pestañas e inicializar los elementos visuales
     */
    private void initInterface(){
        analytics = new Analytics(this);

        // Pestañas
        String items[] = getResources().getStringArray(R.array.analytic_items);
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("eventsTab");
        spec.setContent(R.id.eventsTab);
        spec.setIndicator(items[0]);
        host.addTab(spec);

        spec = host.newTabSpec("propertiesTab");
        spec.setContent(R.id.propertiesTab);
        spec.setIndicator(items[1]);
        host.addTab(spec);

        eventEnterButton = (Button) findViewById(R.id.eventEnterButton);
        eventLayout = (LinearLayout) findViewById(R.id.eventLayout);
        eventRadioGroup = (RadioGroup) findViewById(R.id.eventRadioGroup);

        smokerSpinner = (Spinner) findViewById(R.id.smokerSpinner);
        String smokerItems[] = getResources().getStringArray(R.array.smoker_items);
        ArrayAdapter<String> smokerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, smokerItems);
        smokerSpinner.setAdapter(smokerAdapter);

        occupationSpinner = (Spinner) findViewById(R.id.occupationSpinner);
        String occupationItems[] = getResources().getStringArray(R.array.occupation_items);
        ArrayAdapter<String> occupationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, occupationItems);
        occupationSpinner.setAdapter(occupationAdapter);
    }

    /**
     * Abre el contenido de eventos y registra el evento en firebase
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void eventEnter(View view){
        eventEnterButton.setVisibility(View.GONE);
        eventLayout.setVisibility(View.VISIBLE);

        analytics.registerEnterEvent("Evento");
    }

    /**
     * Envía la selección obtenida (valores del 1 al 3) del evento
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void sendSelectionEvent(View view){
        int radioButtonID = eventRadioGroup.getCheckedRadioButtonId();
        if(radioButtonID == -1){
            Functions.showMessage(this, getString(R.string.no_option_selected), getString(R.string.ok));
            return;
        }

        View radioButton = eventRadioGroup.findViewById(radioButtonID);
        int selected = eventRadioGroup.indexOfChild(radioButton);

        analytics.sendSelectionEvent("Evento", selected + 1);

        eventEnterButton.setVisibility(View.VISIBLE);
        eventLayout.setVisibility(View.GONE);

        Functions.showMessage(this, getString(R.string.event_selection_sent), getString(R.string.ok));
    }

    /**
     * Envía las propiedades seleccionadas por el usuario
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void sendProperties(View view){
        String smoker = (String) smokerSpinner.getSelectedItem();
        String occupation = (String) occupationSpinner.getSelectedItem();

        analytics.setProperties(smoker, occupation);

        Functions.showMessage(this, getString(R.string.properties_sent), getString(R.string.ok));
    }
}
