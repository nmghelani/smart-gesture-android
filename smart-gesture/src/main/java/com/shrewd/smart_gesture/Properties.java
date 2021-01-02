package com.shrewd.smart_gesture;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.IntRange;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Properties {
    private static final String TAG = Properties.class.getName();
    private Context mContext;
    private int radius, btnSize;
    private Integer buttonPadding;
    private int backgroundColor, selectedButtonTint, nonSelectedButtonTint;
    private Drawable selectedButtonDrawable, nonSelectedButtonDrawable;
    private float horizontalOffset, verticalOffset, textVerticalOffset, textHorizontalOffset, verticalTouchAdjust, horizontalTouchAdjust;
    private boolean textEnabled, animationEnabled;
    private int textGravity;
    private Typeface typeface;
    private long delay, animationTime;

    private final static MutableLiveData<Integer> MIN_RADIUS = new MutableLiveData<>();
    private final static MutableLiveData<Integer> MAX_RADIUS = new MutableLiveData<>();

    public Properties(Context mContext) {
        this(new Builder(mContext));
    }

    public Properties(Builder builder) {
        this.mContext = builder.mContext;
        radius = builder.radius;
        btnSize = builder.btnSize.getValue();
        buttonPadding = btnSize * builder.btnPaddingPerc / 100;
        backgroundColor = builder.bgColor;
        selectedButtonTint = builder.selectedButtonTint;
        nonSelectedButtonTint = builder.nonSelectedButtonTint;
        selectedButtonDrawable = builder.selectedButtonDrawable;
        nonSelectedButtonDrawable = builder.nonSelectedButtonDrawable;
        horizontalOffset = builder.horizontalOffset;
        verticalOffset = builder.verticalOffset;
        textVerticalOffset = builder.textVerticalOffset;
        textHorizontalOffset = builder.textHorizontalOffset;
        horizontalTouchAdjust = builder.horizontalTouchOffset;
        verticalTouchAdjust = builder.verticalTouchOffset;
        textEnabled = builder.textEnabled;
        animationEnabled = builder.animationEnabled;
        textGravity = builder.textGravity;
        typeface = builder.typeface;
        delay = builder.delay;
        animationTime = builder.animationTime;
        builder.MIN_RADIUS.observe((LifecycleOwner) mContext, MIN_RADIUS::setValue);
        builder.MAX_RADIUS.observe((LifecycleOwner) mContext, MAX_RADIUS::setValue);

        MIN_RADIUS.observe((LifecycleOwner) mContext, integer -> Log.d(TAG, "onChanged: Min radius " + integer));
        MAX_RADIUS.observe((LifecycleOwner) mContext, integer -> Log.d(TAG, "onChanged: Max radius " + integer));
    }

    public static LiveData<Integer> getLiveMinRadius() {
        return MIN_RADIUS;
    }

    public static LiveData<Integer> getLiveMaxRadius() {
        return MAX_RADIUS;
    }

    public static int getMinRadius() {
        return MIN_RADIUS.getValue();
    }

    public static int getMaxRadius() {
        return MAX_RADIUS.getValue();
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public long getAnimationTime() {
        return animationTime;
    }

    public long getDelay() {
        return delay;
    }

    public int getMinSize() {
        return mContext.getResources().getDimensionPixelSize(R.dimen.min_size);
    }

    public int getMaxSize() {
        return mContext.getResources().getDimensionPixelSize(R.dimen.max_size);
    }

    public int getRadius() {
        if (radius < getMinRadius()) {
            Log.e("RadiusConstraint", "Radius is less than minimum radius. (New radius is: " + getMinRadius() + ")");
            return getMinRadius();
        } else if (radius > getMaxRadius()) {
            Log.e("RadiusConstraint", "Radius is greater than maximum radius. (New radius is: " + getMaxRadius() + ")");
            return getMaxRadius();
        } else {
            return radius;
        }
    }

    public int getBtnSize() {
        int minSize = mContext.getResources().getDimensionPixelSize(R.dimen.min_size);
        int maxSize = mContext.getResources().getDimensionPixelSize(R.dimen.max_size);
        if (btnSize < minSize) {
            Log.e("SizeConstraint", "Size is less than minimum size. (New size is: " + minSize + ")");
            return minSize;
        } else if (btnSize > maxSize) {
            Log.e("SizeConstraint", "Size is greater than maximum size. (New size is: " + maxSize + ")");
            return maxSize;
        } else {
            return btnSize;
        }
    }

    public Integer getButtonPadding() {
        return buttonPadding;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getSelectedButtonTint() {
        return selectedButtonTint;
    }

    public int getNonSelectedButtonTint() {
        return nonSelectedButtonTint;
    }

    public Drawable getSelectedButtonDrawable() {
        return selectedButtonDrawable;
    }

    public Drawable getNonSelectedButtonDrawable() {
        return nonSelectedButtonDrawable;
    }

    public float getHorizontalOffset() {
        return horizontalOffset;
    }

    public float getVerticalOffset() {
        return verticalOffset;
    }

    public float getTextVerticalOffset() {
        return textVerticalOffset;
    }

    public float getTextHorizontalOffset() {
        return textHorizontalOffset;
    }

    public float getVerticalTouchAdjust() {
        return verticalTouchAdjust;
    }

    public float getHorizontalTouchAdjust() {
        return horizontalTouchAdjust;
    }

    public boolean isTextEnabled() {
        return textEnabled;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public static class Builder {
        private static final String TAG = Builder.class.getName();
        private Context mContext;
        private int radius;
        private MutableLiveData<Integer> btnSize = new MutableLiveData<>();
        private int btnPaddingPerc;
        private int bgColor, selectedButtonTint, nonSelectedButtonTint;
        private Drawable selectedButtonDrawable, nonSelectedButtonDrawable;
        private float horizontalOffset, verticalOffset, textVerticalOffset, textHorizontalOffset, verticalTouchOffset, horizontalTouchOffset;
        private boolean textEnabled, animationEnabled;
        private int textGravity;
        private Typeface typeface;
        private long delay, animationTime;

        private final MutableLiveData<Integer> MIN_RADIUS = new MutableLiveData<>();
        private final MutableLiveData<Integer> MAX_RADIUS = new MutableLiveData<>();

        public Builder(Context mContext) {
            this.mContext = mContext;

            btnSize.observe((LifecycleOwner) mContext, newSize -> {
                if (newSize == null)
                    return;
                int minSize = mContext.getResources().getDimensionPixelSize(R.dimen.min_size);
                int maxSize = mContext.getResources().getDimensionPixelSize(R.dimen.max_size);
                if (newSize < minSize) {
                    newSize = minSize;
                    Log.e("SizeConstraint", "Size is less than minimum size. (New size is: " + newSize + ")");
                } else if (newSize > maxSize) {
                    newSize = maxSize;
                    Log.e("SizeConstraint", "Size is greater than maximum size. (New size is: " + newSize + ")");
                }
                int min = (int) (newSize * 2.5f);
                int max = (int) (SmartGestureUtils.getScreenWidthPixels(mContext) / 2f - newSize);
                if (min > max) {
                    min = max;
                }
                MIN_RADIUS.setValue(min);
                MAX_RADIUS.setValue(max);
                Log.d(TAG, "Builder: btnSize changed: ");
            });

            btnSize.setValue(mContext.getResources().getDimensionPixelSize(R.dimen.btn_default_size));
            radius = mContext.getResources().getDimensionPixelSize(R.dimen.default_radius);
            this.btnPaddingPerc = 20;
            bgColor = mContext.getResources().getColor(R.color.transparent_black);
            selectedButtonTint = ContextCompat.getColor(mContext, R.color.white);
            nonSelectedButtonTint = ContextCompat.getColor(mContext, R.color.black);
            selectedButtonDrawable = ContextCompat.getDrawable(mContext, R.drawable.bg_checked);
            nonSelectedButtonDrawable = ContextCompat.getDrawable(mContext, R.drawable.bg_unchecked);
            horizontalOffset = 0;
            verticalOffset = 0;
            textVerticalOffset = 0;
            textHorizontalOffset = 0;
            horizontalTouchOffset = 0;
            verticalTouchOffset = 0;
            textEnabled = true;
            animationEnabled = true;
            delay = 500;
            animationTime = 100;
            textGravity = Gravity.START;
            typeface = Typeface.DEFAULT;
        }

        public Builder setRadius(int dp) {
            this.radius = SmartGestureUtils.dpToPx(dp, mContext);
            return this;
        }

        public Builder setBtnSize(int dp) {
            this.btnSize.setValue(SmartGestureUtils.dpToPx(dp, mContext));
            return this;
        }

        public Builder setDelay(long millis) {
            this.delay = millis;
            return this;
        }

        public Builder setAnimationEnabled(boolean animationEnabled) {
            this.animationEnabled = animationEnabled;
            return this;
        }

        public Builder setAnimationTime(long millis) {
            this.animationTime = millis;
            return this;
        }

        public Builder setButtonPaddingPercentage(@IntRange(from = 0, to = 100) int perc) {
            if (perc < 0) {
                perc = 0;
            } else if (perc > 100) {
                perc = 100;
            }
            this.btnPaddingPerc = perc;
            return this;
        }

        public Builder setBackgroundColor(int color) {
            this.bgColor = color;
            return this;
        }

        public Builder setBackgroundColorResId(int resId) {
            this.bgColor = ContextCompat.getColor(mContext, resId);
            return this;
        }

        public Builder setSelectedButtonTint(int color) {
            this.selectedButtonTint = color;
            return this;
        }

        public Builder setSelectedButtonTintResId(int resId) {
            this.selectedButtonTint = ContextCompat.getColor(mContext, resId);
            return this;
        }

        public Builder setNonSelectedButtonTint(int color) {
            this.nonSelectedButtonTint = color;
            return this;
        }

        public Builder setNonSelectedButtonTintResId(int resId) {
            this.nonSelectedButtonTint = ContextCompat.getColor(mContext, resId);
            return this;
        }

        public Builder setSelectedButtonDrawable(Drawable drawable) {
            this.selectedButtonDrawable = drawable;
            return this;
        }

        public Builder setSelectedButtonDrawableResId(int resId) {
            this.selectedButtonDrawable = ContextCompat.getDrawable(mContext, resId);
            return this;
        }

        public Builder setNonSelectedButtonDrawable(Drawable drawable) {
            this.nonSelectedButtonDrawable = drawable;
            return this;
        }

        public Builder setNonSelectedButtonDrawableResId(int resId) {
            this.nonSelectedButtonDrawable = ContextCompat.getDrawable(mContext, resId);
            return this;
        }

        public Builder setHorizontalOffset(float dp) {
            this.horizontalOffset = SmartGestureUtils.dpToPx(dp, mContext);
            return this;
        }

        public Builder setVerticalOffset(float dp) {
            this.verticalOffset = SmartGestureUtils.dpToPx(dp, mContext);
            return this;
        }

        public Builder setTextVerticalOffset(float dp) {
            this.textVerticalOffset = SmartGestureUtils.dpToPx(dp, mContext);
            return this;
        }

        public Builder setTextHorizontalOffset(float dp) {
            this.textHorizontalOffset = SmartGestureUtils.dpToPx(dp, mContext);
            return this;
        }

        public Builder setTextEnabled(boolean textEnabled) {
            this.textEnabled = textEnabled;
            return this;
        }

        public Builder setTextGravity(int gravity) {
            textGravity = gravity;
            return this;
        }

        public Builder setHorizontalTouchOffset(float dp) {
            this.horizontalTouchOffset = SmartGestureUtils.dpToPx(dp, mContext);
            return this;
        }

        public Builder setVerticalTouchOffset(float dp) {
            this.verticalTouchOffset = SmartGestureUtils.dpToPx(dp, mContext);
            return this;
        }

        public Builder setTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        public Properties create() {
            return new Properties(this);
        }
    }

}
