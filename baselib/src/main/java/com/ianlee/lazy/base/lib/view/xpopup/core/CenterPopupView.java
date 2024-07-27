package com.ianlee.lazy.base.lib.view.xpopup.core;

import static com.ianlee.lazy.base.lib.view.xpopup.enums.PopupAnimation.ScaleAlphaFromCenter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.ianlee.lazy.base.lib.R;
import com.ianlee.lazy.base.lib.view.xpopup.animator.PopupAnimator;
import com.ianlee.lazy.base.lib.view.xpopup.animator.ScaleAlphaAnimator;
import com.ianlee.lazy.base.lib.view.xpopup.util.XPopupUtils;

/**
 * Description: 在中间显示的Popup
 * Create by dance, at 2018/12/8
 */
public class CenterPopupView extends BasePopupView {
    protected FrameLayout centerPopupContainer;
    protected int bindLayoutId;
    protected int bindItemLayoutId;
    protected View contentView;

    public CenterPopupView(@NonNull Context context) {
        super(context);
        centerPopupContainer = findViewById(R.id.centerPopupContainer);
    }

    protected void addInnerContent() {
        if (getImplLayoutId() instanceof View) {
            contentView = (View) getImplLayoutId();
        } else if ((getImplLayoutId() instanceof Integer)) {
            contentView = LayoutInflater.from(getContext()).inflate((Integer) getImplLayoutId(), centerPopupContainer, false);
        }
        LayoutParams params = (LayoutParams) contentView.getLayoutParams();
        if (params != null) {
            params.gravity = Gravity.CENTER;
            centerPopupContainer.addView(contentView, params);
        } else {
            centerPopupContainer.addView(contentView);
        }
    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._xpopup_center_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (centerPopupContainer.getChildCount() == 0) addInnerContent();
        getPopupContentView().setTranslationX(popupInfo.offsetX);
        getPopupContentView().setTranslationY(popupInfo.offsetY);
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(),
                getPopupWidth(), getPopupHeight(), null);
    }

    @Override
    protected void doMeasure() {
        super.doMeasure();
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(),
                getPopupWidth(), getPopupHeight(), null);
    }

    protected void applyTheme() {
        if (bindLayoutId == 0) {
            if (popupInfo.isDarkTheme) {
                applyDarkTheme();
            } else {
                applyLightTheme();
            }
        }
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        centerPopupContainer.setBackground(XPopupUtils.createDrawable(getResources().getColor(R.color._xpopup_dark_color),
                popupInfo.borderRadius));
    }

    @Override
    protected void applyLightTheme() {
        super.applyLightTheme();
        centerPopupContainer.setBackground(XPopupUtils.createDrawable(getResources().getColor(R.color._xpopup_light_color),
                popupInfo.borderRadius));
    }

    /**
     * 具体实现的类的布局
     *
     * @return
     */
    protected Object getImplLayoutId() {
        return null;
    }

    protected int getMaxWidth() {
        if (popupInfo == null) return 0;
        return popupInfo.maxWidth == 0 ? (int) (XPopupUtils.getAppWidth(getContext()) * 0.85f)
                : popupInfo.maxWidth;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), ScaleAlphaFromCenter);
    }
}
