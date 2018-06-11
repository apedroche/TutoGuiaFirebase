package tutoguia.firebase.android.messaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.interfaz.MessagingResultActivity;

/**
 * Created by apedroche on 17/1/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    private static final String TAG = "MyFirebaseMsgService";

    public static final String NOTIFICATION_TITLE = "notificationTitle";
    public static final String NOTIFICATION_MESSAGE = "notificationMessage";
    public static final String NOTIFICATION_DATA = "notificationData";

    @Override
    public void handleIntent(Intent intent) {
        try
        {
            if (intent.getExtras() != null)
            {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet())
                {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            }
            else
            {
                super.handleIntent(intent);
            }
        }
        catch (Exception e)
        {
            super.handleIntent(intent);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if(notification!=null){ // Firebase push
            Log.d(TAG, "Notification Message Title: " + notification.getTitle());
            Log.d(TAG, "Notification Message Body: " + notification.getBody());
            Log.d(TAG, "Notification Message Data: " + remoteMessage.getData().toString());
            sendNotification(notification.getTitle(), notification.getBody(), remoteMessage.getData().toString());
        }
        else {                       // Own server push
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String message = data.get("message");
            sendNotification(title, message, remoteMessage.getData().toString());
        }
    }

    /**
     * Crea y muestra una notificación con los datos recibidos (sólo si la aplicación está en primer plano)
     *
     * @param title Titulo de la notificación
     * @param messageBody Mensaje de la notificación
     * @param messageData Datos de la aplicación en formato
     */
    private void sendNotification(String title, String messageBody, String messageData) {
        Intent intent = new Intent(this, MessagingResultActivity.class);
        intent.putExtra(NOTIFICATION_TITLE, title);
        intent.putExtra(NOTIFICATION_MESSAGE, messageBody);
        intent.putExtra(NOTIFICATION_DATA, messageData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String notifTitle = getString(R.string.app_name);
        if(title!=null && !title.isEmpty())
            notifTitle = title;

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "fcm_default_channel")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notifTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
