package xyz.losi.leestick.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

import xyz.losi.leestick.R;
import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.data.db.NoteDatabase;
import xyz.losi.leestick.model.NoteIconType;
import xyz.losi.leestick.utils.Logger;
import xyz.losi.leestick.utils.NoteFormatter;
import xyz.losi.leestick.utils.SettingsManager;

public class NotificationHelper {
    private static final String CHANNEL_ID = "leestick_notes_channel_id";
    private static  final String CHANNEL_NAME = "Заметки";
    private static  final String CHANNEL_DESCRIPTION = "Показывает ваши заметки как уведомления";
    private static final int NOTIFICATION_ID = 1;


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

    public static void buildNotification(Context context, List<Note> notes, int visibility) {
        RemoteViews collapsedView = new RemoteViews(context.getPackageName(), R.layout.notification_notes);
        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.notification_notes);

        // Цвет фона из SharedPreferences
        int backgroundColor = SettingsManager.getNoteColor(context).getColor(context);
        NoteIconType.IconStyle iconStyle = SettingsManager.getIconStyle(context);

        // Установка фона
        collapsedView.setInt(R.id.notificationLayout, "setBackgroundColor", backgroundColor);
        expandedView.setInt(R.id.notificationLayout, "setBackgroundColor", backgroundColor);

        // Максимум 12 строк
        int maxLines = 12;
        for (int i = 0; i < maxLines; i++) {
            int lineId = context.getResources().getIdentifier("line" + (i + 1), "id", context.getPackageName());
            int iconId = context.getResources().getIdentifier("icon" + (i + 1), "id", context.getPackageName());
            int textId = context.getResources().getIdentifier("text" + (i + 1), "id", context.getPackageName());

            if (i < notes.size()) {
                Note note = notes.get(i);
                NoteIconType.IconColor iconColor = note.getIconColor();
                int iconResId = iconStyle.getIconResId(context, iconColor);
                collapsedView.setViewVisibility(lineId, visibility);
                collapsedView.setImageViewResource(iconId, iconResId);
                collapsedView.setTextViewText(textId, note.getText());

                expandedView.setViewVisibility(lineId, visibility);
                expandedView.setImageViewResource(iconId, iconResId);
                expandedView.setTextViewText(textId, note.getText());
            } else {
                collapsedView.setViewVisibility(lineId, View.GONE);
                expandedView.setViewVisibility(lineId, View.GONE);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.tulp)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setSound(null)
                .setDefaults(0)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);


        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void cancelNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
