package tutoguia.firebase.android.interfaz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.funcionalidad.Authentication;

/**
 * Interfaz de la segunda pantalla de autenticación
 * Muestra los datos del usuario que acaba de acceder
 */
public class AuthenticationResultActivity extends AppCompatActivity {

    // Elementos visuales de la pantalla
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_result);

        textView = (TextView) findViewById(R.id.textView);

        loadData();
    }

    /**
     * Muestra los datos del usuario que acaba de acceder
     */
    private void loadData(){
        Authentication authentication = new Authentication(this);

        String result = "ID: " + authentication.getUserID();
        result += "\nEmail: " + authentication.getUserEmail();
        result += "\nNombre: " + authentication.getUserName();
        textView.setText(result);
    }
}
