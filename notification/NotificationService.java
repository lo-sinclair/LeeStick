package xyz.losi.leestick.notification;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationService extends Service {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationHelper.createNotificationChannel(this);
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        Notification notification = NotificationHelper.buildNotification(this, title, text);
        startForeground(NOTIFICATION_ID, notification);

        return START_STICKY; // 🧷 переживает завершение
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
