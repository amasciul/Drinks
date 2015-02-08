package fr.masciulli.drinks.view;

import android.content.Context;
import android.graphics.Bitmap;
import com.squareup.picasso.Transformation;
import fr.masciulli.drinks.util.Blur;

public class BlurTransformation implements Transformation {
    private Context context;
    private int radius;

    public BlurTransformation(Context context, int radius) {
        this.context = context;
        this.radius = radius;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        Bitmap blurred = Blur.fastblur(context, bitmap, radius);
        bitmap.recycle();
        return blurred;
    }

    @Override
    public String key() {
        return "blur()";
    }
}
