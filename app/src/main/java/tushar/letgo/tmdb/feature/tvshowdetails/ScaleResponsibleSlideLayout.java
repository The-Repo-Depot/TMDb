package tushar.letgo.tmdb.feature.tvshowdetails;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Tushar on 3/14/17.
 */

public class ScaleResponsibleSlideLayout extends LinearLayout {

    private float scale = ScaleResponsiblePagerAdapter.BIG_SLIDE;

    public ScaleResponsibleSlideLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public ScaleResponsibleSlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public ScaleResponsibleSlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = this.getWidth();
        int h = this.getHeight();
        canvas.scale(scale, scale, w / 2, h / 2);
    }
}
