package tutoguia.firebase.android.interfaz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Set;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.messaging.MyFirebaseMessagingService;

/**
 * Interfaz de la pantalla principal de la aplicación
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener{

    // Elementos visuales de la pantalla
    private ListView menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkNotificationClicked();

        String items[] = getResources().getStringArray(R.array.main_menu_items);
        menuList = (ListView) findViewById(R.id.menuList);
        menuList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
        menuList.setOnItemClickListener(this);

        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        //Log.v("TOKEN", FirebaseInstanceId.getInstance().getToken());
    }

    /*
     * Comprueba si la aplicación se ha abierto a causa de una notificación recibida en background
     */
    private void checkNotificationClicked(){
        if(getIntent()==null) return;

        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;

        Set<String> keyset = bundle.keySet();
        if(keyset.contains("com.google.firebase.dynamiclinks.DYNAMIC_LINK_DATA")){
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(AppInvite.API).build();
            AppInvite.AppInviteApi.getInvitation(googleApiClient, this, false).setResultCallback(new ResultCallback<AppInviteInvitationResult>() {
                @Override
                public void onResult(@NonNull AppInviteInvitationResult result) {
                    if (result.getStatus().isSuccess()) {
                        Intent intent = result.getInvitationIntent();
                        String deepLink = AppInviteReferral.getDeepLink(intent);
                        openDynamicLinkResult(deepLink);
                    }
                }
            });
            /*String s = bundle.getString("com.google.firebase.dynamiclinks.DYNAMIC_LINK_DATA");
            String s1 = bundle.getString("com.google.android.gms.appinvite.REFERRAL_BUNDLE");
            Log.v("DYNAMIC", s);*/
            return;
        }

        String result = "{";
        for(String key : keyset){
            // Ignoramos todas las claves que sabemos que pueden llegar y no son datos
            if(key.equalsIgnoreCase("google.sent_time")) continue;
            if(key.equalsIgnoreCase("_fbSourceApplicationHasBeenSet")) continue;
            if(key.equalsIgnoreCase("from")) continue;
            if(key.equalsIgnoreCase("google.message_id")) continue;
            if(key.equalsIgnoreCase("collapse_key")) continue;
            if(key.equalsIgnoreCase("profile")) continue;

            result += key + "=" + bundle.getString(key) + ",";
        }
        result = result.substring(0, result.length()-1) + "}";

        if(result.length() > 1){
            Intent intent = new Intent(this, MessagingResultActivity.class);
            intent.putExtra(MyFirebaseMessagingService.NOTIFICATION_DATA, result);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = null;
        switch (position){
            case 0:
                intent = new Intent(this, DatabaseActivity.class);
                break;

            case 1:
                intent = new Intent(this, StorageActivity.class);
                break;

            case 2:
                intent = new Intent(this, AuthenticationActivity.class);
                break;

            case 3:
                intent = new Intent(this, CrashReportingActivity.class);
                break;

            case 4:
                intent = new Intent(this, CloudFunctionsActivity.class);
                break;

            case 5:
                intent = new Intent(this, CloudMessagingActivity.class);
                break;

            case 6:
                intent = new Intent(this, RemoteConfigActivity.class);
                break;

            case 7:
                intent = new Intent(this, DynamicLinkActivity.class);
                break;

            case 8:
                intent = new Intent(this, AdMobActivity.class);
                break;

            case 9:
                intent = new Intent(this, AnalyticsActivity.class);
                break;
        }

        if(intent!=null){
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w("DynamicLink", "onConnectionFailed:" + connectionResult);
    }

    /**
     * Abre la pantalla de resultados porque el usuario ha entrado a través de un Dynamic Link
     *
     * @param link Enlace por el que ha entrado
     */
    private void openDynamicLinkResult(String link){
        Intent intent = new Intent(this, DynamicLinkResultActivity.class);
        intent.putExtra("LINK", link);
        startActivity(intent);
    }
}
