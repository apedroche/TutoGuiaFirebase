package tutoguia.firebase.android.funcionalidad;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.interfaz.AuthenticationResultActivity;
import tutoguia.firebase.android.interfaz.DynamicLinkActivity;
import tutoguia.firebase.android.util.Functions;

/**
 * Funcionalidad de autenticación
 */
public class Authentication {

    // Variable necesaria para interactuar con la interfaz
    private Activity activity;

    // Objetos de autenticación de Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    /**
     * Inicialización
     *
     * @param activity Actividad desde la que se está gestionando la autenticación
     */
    public Authentication(Activity activity){
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Avanzar a la pantalla de resultados, después de login correcto
     */
    private void goToResult(){
        Intent intent = new Intent(activity, AuthenticationResultActivity.class);
        activity.startActivity(intent);
    }

    /**
     * Crea un nuevo usuario que no estuviese ya creado en el sistema
     *
     * @param email Correo electrónico
     * @param password Contraseña
     */
    public void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Functions.showMessage(activity,
                                    activity.getString(R.string.create_user_ok), activity.getString(R.string.ok));
                        }
                        else {
                            Functions.showMessage(activity,
                                    activity.getString(R.string.create_user_wrong) + ": " + task.getException().getMessage(),
                                    activity.getString(R.string.ok));
                        }
                    }
                });
    }

    /**
     * Login con un usuario ya creado
     *
     * @param email Correo electrónico
     * @param password Contraseña
     */
    public void signExistingUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Functions.showMessage(activity, activity.getString(R.string.login_ok), activity.getString(R.string.ok));
                            goToResult();
                        }
                        else{
                            Functions.showMessage(activity,
                                    activity.getString(R.string.login_wrong), activity.getString(R.string.ok));
                        }
                    }
                });
    }

    /**
     * Login con Facebook
     *
     * @param token token de Facebook
     */
    public void handleFacebookAccessToken(AccessToken token, final boolean shouldContinue){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && shouldContinue){
                    goToResult();
                }
                else if(isFacebookSignedIn()){
                    ((DynamicLinkActivity) activity).checkAuthentication();
                }
            }
        });
    }

    /**
     * Login de forma anónima
     */
    public void loginAnonimous(){
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Functions.showMessage(activity, activity.getString(R.string.login_ok), activity.getString(R.string.ok));
                            goToResult();
                        }
                        else{
                            Functions.showMessage(activity, activity.getString(R.string.login_wrong), activity.getString(R.string.ok));
                        }
                    }
                });
    }

    /**
     * @return true si el usuario está logueado
     */
    public boolean isSignedIn(){
        if(mUser == null) mUser = mAuth.getCurrentUser();
        return mUser != null;
    }

    /**
     * @return true si el usuario está logueado en Facebook
     */
    public boolean isFacebookSignedIn(){
        if(!isSignedIn()) return false;
        if(AccessToken.getCurrentAccessToken() == null) return false;
        mAuth = FirebaseAuth.getInstance();

        if(getUserName()==null || getUserName().isEmpty()) return false;
        return true;
    }

    /**
     * @return ID del usuario
     */
    public String getUserID(){
        if(mUser == null) mUser = mAuth.getCurrentUser();
        return mUser.getUid();
    }

    /**
     * @return Correo electrónico del usuario
     */
    public String getUserEmail(){
        if(mUser == null) mUser = mAuth.getCurrentUser();
        return mUser.getEmail();
    }

    /**
     * @return Nombre del usuario
     */
    public String getUserName(){
        if(mUser == null) mUser = mAuth.getCurrentUser();
        return mUser==null?"":mUser.getDisplayName();
    }
}
