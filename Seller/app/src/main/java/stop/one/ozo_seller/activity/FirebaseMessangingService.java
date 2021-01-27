package stop.one.ozo_seller.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import stop.one.ozo_seller.R;

public class FirebaseMessangingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title =remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();

        int importance = NotificationManager.IMPORTANCE_HIGH;

        Intent intent = new Intent(this,order_list.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(R.raw.notify);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),channelId)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(importance)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),getString(R.string.default_notification_channel_id))
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification.build());

    }
}
