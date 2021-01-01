package com.shrewd.smart_gesture;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class SmartGestureListener implements View.OnTouchListener {

    private final Context mContext;
    private final List<GestureButton> buttonList;
    private boolean isStillDown = false, isGestureRunning = false;
    private SmartGestureDialog dialog;

    public SmartGestureListener(Context mContext, List<GestureButton> buttonList) {
        this.mContext = mContext;
        this.buttonList = buttonList;
        dialog = new SmartGestureDialog(mContext, buttonList);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        dialog.setOnSelectListener(onSelectListener);
    }

    public void setRadius(int radiusInDp) {
        dialog.setRadius(radiusInDp);
    }

    public void setBtnSize(int btnSizeInDp) {
        dialog.setBtnSize(btnSizeInDp);
    }

    public void setActionBarHeight(int actionBarHeight) {
        dialog.setActionBarHeight(actionBarHeight);
    }

    public void setBackgroundColor(int backgroundColor) {
        dialog.setBackgroundColor(backgroundColor);
    }

    public void setSelectedButtonTint(int selectedButtonTint) {
        dialog.setSelectedButtonTint(selectedButtonTint);
    }

    public void setNonSelectedButtonTint(int nonSelectedButtonTint) {
        dialog.setNonSelectedButtonTint(nonSelectedButtonTint);
    }

    public void setSelectedButtonTintResId(int resId) {
        dialog.setSelectedButtonTintResId(resId);
    }

    public void setNonSelectedButtonTintResId(int resId) {
        dialog.setNonSelectedButtonTintResId(resId);
    }

    public void setSelectedButtonDrawable(Drawable selectedButtonDrawable) {
        dialog.setSelectedButtonDrawable(selectedButtonDrawable);
    }

    public void setNonSelectedButtonDrawable(Drawable nonSelectedButtonDrawable) {
        dialog.setNonSelectedButtonDrawable(nonSelectedButtonDrawable);
    }

    public void setSelectedButtonDrawableResId(int resId) {
        dialog.setSelectedButtonDrawableResId(resId);
    }

    public void setNonSelectedButtonDrawableResId(int resId) {
        dialog.setNonSelectedButtonDrawableResId(resId);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isStillDown = true;
                isGestureRunning = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isStillDown) {
                            dialog.setTouchedView(view);
                            dialog.updateList(buttonList);
                            dialog.show();
                        }
                    }
                }, 500);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isGestureRunning && dialog != null) {
                    dialog.dismiss();
                }
                isStillDown = false;
                isGestureRunning = false;
                break;
            case MotionEvent.ACTION_MOVE:
                dialog.dispatchTouchEvent(event);
                break;
        }
        return true;
    }
}
