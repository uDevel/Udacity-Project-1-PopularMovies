package com.udevel.popularmovies.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by benny on 7/21/2015.
 */
public class PosterImageView extends ImageView {
    private static final String TAG = PosterImageView.class.getSimpleName();
    private static final float HEIGHT_RATIO = 1.5f;

    public PosterImageView(Context context) {
        super(context);
    }

    public PosterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PosterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) ((width * HEIGHT_RATIO) + 0.5);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
