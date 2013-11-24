package fr.masciulli.drinks.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import fr.masciulli.drinks.R;

public class ListImageView extends ImageView {
    public ListImageView(Context context) {
        super(context);
    }

    public ListImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getResources().getDimensionPixelSize(R.dimen.drink_image_height));
    }
}
