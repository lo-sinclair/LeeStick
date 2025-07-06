package xyz.losi.leestick.model;

import android.content.Context;

import androidx.core.content.ContextCompat;

import xyz.losi.leestick.R;

public class NoteIconType {

    public enum IconColor {
        RED(R.color.icon_red),
        ORANGE(R.color.icon_orange),
        GREEN(R.color.icon_green),
        BLUE(R.color.icon_blue),
        YELLOW(R.color.icon_yellow),
        PURPLE(R.color.icon_purple),
        BLACK(R.color.icon_black),
        WHITE(R.color.icon_white),
        BROWN(R.color.icon_brown);

        private final int colorResId;

        IconColor(int colorResId) {
            this.colorResId = colorResId;
        }

        public int getColor(Context context) {
            return ContextCompat.getColor(context, colorResId);
        }
    }

    public enum IconStyle {
        SQUARE("icon_square_", "Квадратные"),
        CIRCLE("icon_circle_", "Круглые"),
        CHECK("icon_check_", "Галочки");

        private final String prefix;
        private final String name;

        public String getName() {
            return name;
        }

        IconStyle(String prefix, String name) {
            this.prefix = prefix;
            this.name = name;
        }

        public int getIconResId(Context context, IconColor color) {
            String name = prefix + color.name().toLowerCase();
            return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        }
    }

}
