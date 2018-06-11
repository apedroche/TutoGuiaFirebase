package tutoguia.firebase.android.interfaz;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.datos.MessagingToken;
import tutoguia.firebase.android.funcionalidad.Database;
import tutoguia.firebase.android.util.Functions;

public class CloudMessagingActivity extends AppCompatActivity {

    // Elementos visuales de la pantalla
    private LinearLayout registerTokenLayout, sendMessageLayout;

    private Spinner receiverSpinner;

    private EditText nameText, titleText, messageText;

    // Funcionalidad
    private Database database;

    private ArrayList<MessagingToken> tokens;

    private String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerTokenLayout = (LinearLayout) findViewById(R.id.registerTokenLayout);
        sendMessageLayout = (LinearLayout) findViewById(R.id.sendMessageLayout);

        receiverSpinner = (Spinner) findViewById(R.id.receiverSpinner);

        nameText = (EditText) findViewById(R.id.nameText);
        titleText = (EditText) findViewById(R.id.titleText);
        messageText = (EditText) findViewById(R.id.messageText);

        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        database = new Database();
        database.getTokens(this);
    }

    /**
     * Registra el token necesario para enviar una notificación
     * Utiliza el nombre que introduce el usuario para identificarlo posteriormente
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void registerToken(View view){
        if(nameText.getText().toString().length()==0){
            Functions.showMessage(this, getString(R.string.empty_name), getString(R.string.ok));
            return;
        }

        MessagingToken token = new MessagingToken();
        token.setId(androidID);
        token.setName(nameText.getText().toString());
        token.setToken(FirebaseInstanceId.getInstance().getToken());

        database.createToken(token);
        registerTokenLayout.setVisibility(View.GONE);
        sendMessageLayout.setVisibility(View.VISIBLE);
    }
    /**
     * Actualiza la lista de tokens
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void setTokens(ArrayList<MessagingToken> tokens){
        this.tokens = tokens;

        if(tokens.size()==0) return;

        ArrayList<String> items = new ArrayList<String>();
        for(MessagingToken token : tokens){
            items.add(token.getName());

            if(token.getId().equalsIgnoreCase(androidID)){  // Ya está registrado
                registerTokenLayout.setVisibility(View.GONE);
                sendMessageLayout.setVisibility(View.VISIBLE);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        receiverSpinner.setAdapter(adapter);
    }

    /**
     * Envía una notificación a sí mismo a través de un servidor externo
     * Utiliza el título y mensaje que introduce el usuario
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void sendMessage(View view){
        int position = receiverSpinner.getSelectedItemPosition();
        MessagingToken token = tokens.get(position);

        String url;
        try {
            url = String.format("http://spinlogic.es/test/testGCMPush.php?token=%s&title=%s&message=%s",
            //url = String.format("https://tutoguia-firebase.firebaseapp.com/testGCMPush.php?token=%s&title=%s&message=%s",
                    //FirebaseInstanceId.getInstance().getToken(),
                    token.getToken(),
                    URLEncoder.encode(titleText.getText().toString(), "utf-8"),
                    URLEncoder.encode(messageText.getText().toString(), "utf-8"));

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("response", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("response error", error.getMessage());
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
