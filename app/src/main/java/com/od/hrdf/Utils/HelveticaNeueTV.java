package com.od.hrdf.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MuhammadMahmoor on 11/7/16.
 */

public class HelveticaNeueTV extends TextView {
    public HelveticaNeueTV(Context context) {
        super(context);
        init();
    }

    public HelveticaNeueTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaNeueTV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HelveticaNeueTV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeue.ttf");
            //setTypeface(Typeface.DEFAULT, sai);
        }
    }
}
