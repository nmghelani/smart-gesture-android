package com.shrewd.smart_gesture;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class SmartGestureListener implements View.OnTouchListener {

    private final Context mContext;
    private final List<GestureButton> buttonList;
    private boolean isStillDown = false, isGestureRunning = false;
    private SmartGestureDialog dialog;
    private long delay;

    public SmartGestureListener(Context mContext, List<GestureButton> buttonList) {
        this.mContext = mContext;
        this.buttonList = buttonList;
        dialog = new SmartGestureDialog(mContext, buttonList);
    }

    public void setProperties(Properties properties) {
        delay = properties.getDelay();
        dialog.setProperties(properties);
    }

    public void setCallback(SmartGestureCallBack smartGestureCallBack) {
        dialog.setCallback(smartGestureCallBack);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dialog.setTouchedView(view);
                dialog.updateList(buttonList);
                break;
        }
        if (dialog != null) {
            dialog.dispatchTouchEvent(event);
        }
        return true;
    }
}
