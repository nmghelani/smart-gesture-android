package com.shrewd.smart_gesture;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class SmartGestureListener implements View.OnTouchListener {

    private final Context mContext;
    private final List<GestureButton> buttonList;
    private final OnSelectListener onSelectListener;
    private boolean isStillDown = false, isGestureRunning = false;
    private SmartGestureDialog dialog;
    private int radius, btnSize;

    public SmartGestureListener(Context mContext, List<GestureButton> buttonList, OnSelectListener onSelectListener) {
        this.mContext = mContext;
        this.buttonList = buttonList;
        this.onSelectListener = onSelectListener;
    }

    public void setRadius(int radiusInDp) {
        if (dialog != null) {
            dialog.setRadius(radiusInDp);
        }
    }

    public void setBtnSize(int btnSizeInDp) {
        if (dialog != null) {
            dialog.setBtnSize(btnSizeInDp);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isStillDown = true;
                isGestureRunning = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isStillDown) {
                            if (dialog == null) {
                                dialog = new SmartGestureDialog(mContext, buttonList, v, onSelectListener);
                            } else {
                                dialog.updateList(buttonList);
                            }
                            if (radius != 0) {
                                dialog.setRadius(radius);
                            }
                            if (btnSize != 0) {
                                dialog.setBtnSize(btnSize);
                            }
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
