package xyz.losi.leestick.utils;

import android.content.Context;
import android.util.TypedValue;

public class ViewUtils {
    // dp → px
    public static int dpToPx(Context context, float dp) {
        return Math.round(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dp,
                        context.getResources().getDisplayMetrics()
                )
        );
    }

    // sp → px
    public static int spToPx(Context context, float sp) {
        return Math.round(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        sp,
                        context.getResources().getDisplayMetrics()
                )
        );
    }

    // px → dp
    public static float pxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    // px → sp
    public static float pxToSp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }
}
