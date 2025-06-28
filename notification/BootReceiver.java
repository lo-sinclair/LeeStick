package xyz.losi.leestick.notification;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.concurrent.Executors;

import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.data.db.NoteDatabase;

public class BootReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Executors.newSingleThreadExecutor().execute(() -> {
                Application app = (Application) context.getApplicationContext();


                NoteDatabase db = NoteDatabase.getInstance(app);
                List<Note> notes = db.notesDao().getNotesList();

                if (!notes.isEmpty()) {
                    NotificationHelper.createNotificationChannel(context);
                    NotificationHelper.buildNotification(context, notes);
                }
            });
        }
    }
}
