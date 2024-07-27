package com.ianlee.lazy.base.lib.view;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;

/**
 * Created by dzh on 02.23.023.
 */
public class MyFlexBoxLayoutManager extends FlexboxLayoutManager {
    public MyFlexBoxLayoutManager(Context context) {
        super(context);
    }

    public MyFlexBoxLayoutManager(Context context, int flexDirection) {
        super(context, flexDirection);
    }

    public MyFlexBoxLayoutManager(Context context, int flexDirection, int flexWrap) {
        super(context, flexDirection, flexWrap);
    }


    /**
     * 将LayoutParams转换成新的FlexboxLayoutManager.LayoutParams
     */
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof RecyclerView.LayoutParams) {
            return new LayoutParams((RecyclerView.LayoutParams) lp);
        } else if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        } else {
            return new LayoutParams(lp);
        }
    }
}