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
    private static final String CHANNEL_ID = "leestick_notes_channel_id";
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

    public static void buildNotification(Context context, List<Note> notes) {
        RemoteViews collapsedView = new RemoteViews(context.getPackageName(), R.layout.notification_notes);
        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.notification_notes);

        String noteText = NoteFormatter.formatList(notes);
        collapsedView.setTextViewText(R.id.textNotes, noteText);
        expandedView.setTextViewText(R.id.textNotes, noteText);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_local_florist_24)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setSound(null)
                .setDefaults(0)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1, builder.build());
    }
}
