package com.shrewd.smart_gesture;

public interface SmartGestureCallBack {
    void onSelected(int id);
    void onNothingSelected();
    void onGestureStarted();
    void onGestureEnded();
}
