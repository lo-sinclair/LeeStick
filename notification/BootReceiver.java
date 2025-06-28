package xyz.losi.leestick.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class BootReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Можно восстановить из БД/Preferences
            Intent serviceIntent = new Intent(context, NotificationService.class);
            serviceIntent.putExtra("title", "Заметки");
            serviceIntent.putExtra("text", "Вы не забыли ничего важного?");
            context.startForegroundService(serviceIntent);
        }
    }
}
