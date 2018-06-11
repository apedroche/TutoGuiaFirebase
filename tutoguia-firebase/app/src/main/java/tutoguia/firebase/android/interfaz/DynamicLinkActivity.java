package tutoguia.firebase.android.interfaz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appinvite.AppInviteInvitation;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.funcionalidad.Authentication;

/**
 * Interfaz de la pantalla de Dynamic Link
 */
public class DynamicLinkActivity extends AppCompatActivity {
    private final int REQUEST_INVITE = 100;

    // Funcionalidad
    private Authentication authentication;

    // Facebook
    private CallbackManager callbackManager;

    // Elementos visuales de la pantalla
    private LoginButton fb_login_button;
    private LinearLayout dynamicLayout;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_link);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fb_login_button = (LoginButton) findViewById(R.id.fb_login_button);
        dynamicLayout = (LinearLayout) findViewById(R.id.dynamicLayout);
        titleText = (TextView) findViewById(R.id.titleText);

        authentication = new Authentication(this);
        checkAuthentication();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d("AppInviteInvitation", "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
        else {
            // Cuando ha vuelto de Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);

            if(authentication.isFacebookSignedIn()) {
                fb_login_button.setVisibility(View.GONE);
                dynamicLayout.setVisibility(View.VISIBLE);
                titleText.setText(String.format(getString(R.string.send_dynamic_title), authentication.getUserName()));
            }
        }
    }

    /**
     * Comprueba si el usuario está logueado en Facebook.
     * Si no lo está, muestra el botón de Facebook
     */
    public void checkAuthentication(){
        if(!authentication.isFacebookSignedIn()){
            callbackManager = CallbackManager.Factory.create();
            fb_login_button.setReadPermissions("email", "public_profile");
            fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                    authentication.handleFacebookAccessToken(loginResult.getAccessToken(), false);
                    checkAuthentication();
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
            return;
        }

        fb_login_button.setVisibility(View.GONE);
        dynamicLayout.setVisibility(View.VISIBLE);
        titleText.setText(String.format(getString(R.string.send_dynamic_title), authentication.getUserName()));
    }

    /**
     * Crea una invitación a la aplicación, que puede enviarse a los contactos por email o sms
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void createDynamicLink(View view){
        String message = String.format(getString(R.string.invitation_message), authentication.getUserName());

        String url = "https://https://sea28.app.goo.gl/?link=http%3A%2F%2Fwww.spinlogic.es%2Fes%2Fsobre-nosotros.html&apn=tutoguia.firebase.android&afl=http%3A%2F%2Fwww.spinlogic.es%2Fes%2Fsobre-nosotros.html";
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(message)
                .setDeepLink(Uri.parse(url))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
}
