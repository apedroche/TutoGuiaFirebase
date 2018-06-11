package tutoguia.firebase.android.interfaz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import tutoguia.firebase.android.R;
import tutoguia.firebase.android.messaging.MyFirebaseMessagingService;

public class MessagingResultActivity extends AppCompatActivity {

    // Elementos visuales de la pantalla
    private TextView titleText, messageText, dataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_result);

        titleText = (TextView) findViewById(R.id.titleText);
        messageText = (TextView) findViewById(R.id.messageText);
        dataText = (TextView) findViewById(R.id.dataText);

        if(getIntent().hasExtra(MyFirebaseMessagingService.NOTIFICATION_TITLE))
            titleText.setText(getIntent().getStringExtra(MyFirebaseMessagingService.NOTIFICATION_TITLE));

        if(getIntent().hasExtra(MyFirebaseMessagingService.NOTIFICATION_MESSAGE))
            messageText.setText(getIntent().getStringExtra(MyFirebaseMessagingService.NOTIFICATION_MESSAGE));

        if(getIntent().hasExtra(MyFirebaseMessagingService.NOTIFICATION_DATA))
            dataText.setText(getIntent().getStringExtra(MyFirebaseMessagingService.NOTIFICATION_DATA));
    }
}
