package tutoguia.firebase.android.interfaz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.funcionalidad.Authentication;
import tutoguia.firebase.android.util.Functions;

/**
 * Interfaz de la primera pantalla de autenticación
 */
public class AuthenticationActivity extends AppCompatActivity {

    // Funcionalidad
    private Authentication authentication;

    // Facebook
    private CallbackManager callbackManager;

    // Elementos visuales de la pantalla
    private EditText emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authentication = new Authentication(this);

        initInterface();
    }

    /**
     * Este método se encarga de crear las pestañas e inicializar los elementos visuales
     */
    private void initInterface(){
        String items[] = getResources().getStringArray(R.array.authentication_items);
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("emailTab");
        spec.setContent(R.id.emailTab);
        spec.setIndicator(items[0]);
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("facebookTab");
        spec.setContent(R.id.facebookTab);
        spec.setIndicator(items[1]);
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("anonimousTab");
        spec.setContent(R.id.anonimousTab);
        spec.setIndicator(items[2]);
        host.addTab(spec);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                authentication.handleFacebookAccessToken(loginResult.getAccessToken(), true);
            }

            @Override
            public void onCancel() {
                Log.d("Facebook", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook", "facebook:onError", error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Cuando ha vuelto de Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Crea un usuario a partir de correo y contraseña
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void createUser(View view){
        if(emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty())
            Functions.showMessage(this, getString(R.string.login_empty), getString(R.string.ok));
        else
            authentication.createNewUser(emailText.getText().toString(), passwordText.getText().toString());
    }

    /**
     * Realiza el login a partir de correo y contraseña
     * Debe haberse creado el usuario previamente
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void loginEmail(View view){
        if(emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty())
            Functions.showMessage(this, getString(R.string.login_empty), getString(R.string.ok));
        else
            authentication.signExistingUser(emailText.getText().toString(), passwordText.getText().toString());
    }

    /**
     * Crea un usuario de forma anónima
     *
     * @param view botón sobre el que se ha pulsado
     */
    public void loginAnonimous(View view){
        authentication.loginAnonimous();
    }

}
