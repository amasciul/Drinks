package fr.masciulli.drinks.ui.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import fr.masciulli.drinks.R;

public class Placeholders {
    private static int[] COLORS = new int[]{
            R.color.gray_light,
            R.color.gray,
            R.color.gray_dark
    };

    public Drawable get(Context context, int position) {
        return new ColorDrawable(context.getResources().getColor(COLORS[position % COLORS.length]));
    }
}
