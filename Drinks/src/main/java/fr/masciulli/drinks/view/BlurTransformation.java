package fr.masciulli.drinks.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

import fr.masciulli.drinks.util.Blur;

public class BlurTransformation implements Transformation {
    private Context mContext;

    public BlurTransformation(Context context) {
        mContext = context;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        Bitmap blurred = Blur.fastblur(mContext, bitmap, 12);
        bitmap.recycle();
        return blurred;
    }

    @Override
    public String key() {
        return "blur()";
    }
}
