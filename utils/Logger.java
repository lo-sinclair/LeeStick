package xyz.losi.leestick.utils;

import android.util.Log;

public class Logger {

    private static final String DEFAULT_TAG = "🍄 L-DEBUG";

    public static void dd(Object msg) {
        Log.d(DEFAULT_TAG, String.valueOf(msg));
    }

    public static void ee(Object msg) {
        Log.e(DEFAULT_TAG, String.valueOf(msg));
    }

    public static void dd(String tag, Object msg) {
        Log.d(tag, String.valueOf(msg));
    }

    /* public static void json(String tag, Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Log.d(tag, gson.toJson(obj));
    }*/
}
