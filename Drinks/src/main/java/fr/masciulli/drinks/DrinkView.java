package fr.masciulli.drinks;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DrinkView extends ImageView {
    public DrinkView(Context context) {
        super(context);
    }

    public DrinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getResources().getInteger(R.integer.drink_image_height));
    }
}
