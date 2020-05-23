package es.upm.etsiinf.pmd.pmdproject1920;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


public class NotificationHandler extends ContextWrapper {
    private NotificationManager manager;

    public static final String MY_CHANNEL_ID ="1";
    private final String MY_CHANNEL_NAME = "LOW CHANNEL";

    public static final int GROUP_ID = 1;
    private final String GROUP_NAME = "GROUP";

    public NotificationHandler(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            // Create channel

            NotificationChannel lowChannel = new NotificationChannel(MY_CHANNEL_ID, MY_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            lowChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(lowChannel);
        }
    }


    public Notification.Builder createNotification (String title, String msg) {
        if (Build.VERSION.SDK_INT >= 26)
            return createNotificationWithChannels(title, msg, MY_CHANNEL_ID);
        return createNotificationWithChannels(title, msg, MY_CHANNEL_ID);
    }


    private Notification.Builder createNotificationWithChannels (String title, String msg, String channelID) {
        /*Intent intent = new Intent(this, notificationActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("msg", msg);*/
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Notification.Action action = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.ic_notifications), "VIEW", pendingIntent).build();

        //Convert image to bitmap
        //Bitmap my_image = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        return new Notification.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(msg)
                .setStyle(new Notification.BigTextStyle().bigText(msg))
                .setGroup(GROUP_NAME)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                ;
    }


    public void publishNotificationGroup () {
        String channelID = MY_CHANNEL_ID;

        Notification groupNotification = new Notification.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setGroup(GROUP_NAME)
                .setGroupSummary(true)
                .setGroupAlertBehavior(Notification.GROUP_ALERT_CHILDREN)
                .build();
        getManager().notify(GROUP_ID, groupNotification);
    }


    public NotificationManager getManager() {
        if (manager==null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
