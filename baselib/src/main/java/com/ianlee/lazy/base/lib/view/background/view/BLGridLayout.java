package com.ianlee.lazy.base.lib.view.background.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

import com.ianlee.lazy.base.lib.view.background.BackgroundFactory;
import com.ianlee.lazy.base.lib.view.background.BackgroundFactory;

public class BLGridLayout extends GridLayout {
    public BLGridLayout(Context context) {
        super(context);
    }

    public BLGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BLGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        BackgroundFactory.setViewBackground(context, attrs, this);
    }
}
