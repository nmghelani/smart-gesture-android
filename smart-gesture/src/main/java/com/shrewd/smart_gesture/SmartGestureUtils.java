package com.shrewd.smart_gesture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class SmartGestureUtils {

    public static int pxToDp(float px, Context context) {
        return (int) (px / (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(float dp, Context context) {
        return (int) (dp * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getScreenWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static Animation scaleView(View v, float startScale, float endScale, boolean withAnimation) {
        if (v.getScaleX() == endScale && v.getScaleY() == endScale) {
            return null;
        }
        Animation anim = new ScaleAnimation(
                startScale, endScale,
                startScale, endScale,1,1);
        anim.setFillAfter(true);
        anim.setDuration(withAnimation ? 100 : 0);
        v.startAnimation(anim);
        return anim;
    }
}
