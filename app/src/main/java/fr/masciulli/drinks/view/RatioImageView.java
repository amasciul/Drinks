package fr.masciulli.drinks.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import fr.masciulli.drinks.R;

public class RatioImageView extends AppCompatImageView {
    private static final float DEFAULT_RATIO = 1.0f;
    public static final int PRIORITY_WIDTH = 0;
    public static final int PRIORITY_HEIGHT = 1;

    private float ratio = DEFAULT_RATIO;
    private int priority = PRIORITY_WIDTH;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RatioImageView,
                0, 0);
        try {
            ratio = array.getFloat(R.styleable.RatioImageView_imageRatio, DEFAULT_RATIO);
            priority = array.getInteger(R.styleable.RatioImageView_priority, PRIORITY_WIDTH);
        } finally {
            array.recycle();
        }
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (priority == PRIORITY_WIDTH) {
            int height = (int) (getMeasuredWidth() / ratio);
            setMeasuredDimension(getMeasuredWidth(), height);
        } else if (priority == PRIORITY_HEIGHT) {
            int width = (int) (getMeasuredHeight() * ratio);
            setMeasuredDimension(width, getMeasuredHeight());
        }
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        invalidate();
        requestLayout();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        invalidate();
        requestLayout();
    }
}
