package xyz.losi.leestick.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import xyz.losi.leestick.R;
import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.utils.NoteFormatter;

public class NotificationHelper {
    private static final String CHANNEL_ID = "notes_channel_id";
    private static  final String CHANNEL_NAME = "Заметки";
    private static  final String CHANNEL_DESCRIPTION = "Показывает ваши заметки как уведомления";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                  CHANNEL_ID,
                  CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.enableLights(false);
            channel.enableVibration(false);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static Notification buildNotification(Context context, String title, String text) {


        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_sticky_note_2_24)
                .setContentTitle("📝 " + title)
                .setContentText(text)
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.seacat2))
                        .bigLargeIcon(null))*/
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.leestick2)) // большая иконка
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true) // нельзя смахнуть
                .setAutoCancel(false)
                .setOnlyAlertOnce(false)
                .build();
    }
}
