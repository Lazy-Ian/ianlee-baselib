package com.ianlee.lazy.base.lib.view.background.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ianlee.lazy.base.lib.view.background.BackgroundFactory;
import com.ianlee.lazy.base.lib.view.background.BackgroundFactory;

public class BLLinearLayout extends LinearLayout {
    public BLLinearLayout(Context context) {
        super(context);
    }

    public BLLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BLLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        BackgroundFactory.setViewBackground(context, attrs, this);
    }
}
