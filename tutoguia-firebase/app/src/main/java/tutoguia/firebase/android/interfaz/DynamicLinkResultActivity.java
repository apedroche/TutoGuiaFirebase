package tutoguia.firebase.android.interfaz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tutoguia.firebase.android.R;

/**
 * Interfaz de la pantalla a la que se accede cuando has recibido un Dynamic Link
 */
public class DynamicLinkResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_link_result);
    }
}
