package com.shrewd.smart_gesture;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.shrewd.smart_gesture.databinding.DgSmartGestureBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

public class SmartGestureDialog extends Dialog {

    private static final String TAG = SmartGestureDialog.class.getName();
    private List<GestureButton> buttonList;
    private final OnSelectListener onSelectListener;
    private DgSmartGestureBinding binding;
    private final Context mContext;
    private final View touchedView;
    private View lastSelected;
    private int statusBarHeight, actionBarHeight;
    private int radiusPx, btnSizePx;
    private final MutableLiveData<Integer> MIN_RADIUS = new MutableLiveData<>();
    private final MutableLiveData<Integer> MAX_RADIUS = new MutableLiveData<>();
    private final MutableLiveData<Integer> MIN_SIZE = new MutableLiveData<>();
    private final MutableLiveData<Integer> MAX_SIZE = new MutableLiveData<>();

    public SmartGestureDialog(@NonNull Context mContext, List<GestureButton> buttonList, View touchedView, OnSelectListener onSelectListener) {
        super(mContext);
        this.mContext = mContext;
        this.touchedView = touchedView;
        this.buttonList = buttonList;
        this.onSelectListener = onSelectListener;
        initConstraints();
        MAX_RADIUS.postValue(pxToDp((getScreenWidthPixels(mContext) + btnSizePx) / 3f, mContext));
        btnSizePx = Math.min(dpToPx(MAX_SIZE.getValue(), mContext), Math.max(dpToPx(MIN_SIZE.getValue(), mContext), mContext.getResources().getDimensionPixelSize(R.dimen.btn_default_size)));
        MIN_RADIUS.postValue(pxToDp(Math.max(dpToPx(MIN_RADIUS.getValue(), mContext), btnSizePx * 2.5f), mContext));
        Log.d(TAG, "SmartGestureDialog: minimum radius: " + MIN_RADIUS);
        this.radiusPx = Math.min(dpToPx(MAX_RADIUS.getValue(), mContext), Math.max(dpToPx(MIN_RADIUS.getValue(), mContext), mContext.getResources().getDimensionPixelSize(R.dimen.default_radius)));
        MIN_RADIUS.observe((LifecycleOwner) mContext, integer -> Log.d(TAG, "onConstraintsChanged: MIN_RADIUS: " + integer));
        MAX_RADIUS.observe((LifecycleOwner) mContext, integer -> Log.d(TAG, "onConstraintsChanged: MAX_RADIUS: " + integer));
        MIN_SIZE.observe((LifecycleOwner) mContext, integer -> Log.d(TAG, "onConstraintsChanged: MIN_SIZE: " + integer));
        MAX_SIZE.observe((LifecycleOwner) mContext, integer -> Log.d(TAG, "onConstraintsChanged: MAX_SIZE: " + integer));
    }

    private void initConstraints() {
        MIN_RADIUS.setValue(100);
        MAX_RADIUS.setValue(250);
        MIN_SIZE.setValue(50);
        MAX_SIZE.setValue(80);
    }

    public void updateList(List<GestureButton> buttonList) {
        this.buttonList = buttonList;
    }

    public void setRadius(int radiusInDp) {
        Log.d(TAG, "setRadius: " + radiusInDp);
        this.radiusPx = dpToPx(Math.min(MAX_RADIUS.getValue(), Math.max(MIN_RADIUS.getValue(), radiusInDp)), mContext);
    }

    public void setBtnSize(int btnSizeInDp) {
        this.btnSizePx = dpToPx(Math.min(MAX_SIZE.getValue(), Math.max(MIN_SIZE.getValue(), btnSizeInDp)), mContext);
        MIN_RADIUS.postValue(pxToDp(Math.max(mContext.getResources().getDimensionPixelSize(R.dimen.min_radius), btnSizePx * 2.5f), mContext));
        Log.d(TAG, "setBtnSize: " + MIN_RADIUS + " " + btnSizePx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MAX_RADIUS.postValue(pxToDp((getScreenWidthPixels(mContext) + btnSizePx) / 3f, mContext));
        Log.d(TAG, "onCreate: " + MAX_RADIUS);

        binding = DgSmartGestureBinding.inflate(LayoutInflater.from(mContext));
        setContentView(binding.getRoot());
        resizeFocusView();
        repositionDescriptionView();
        Rect frame = new Rect();
        ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        try {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, mContext.getResources().getDisplayMetrics());
        } catch (Exception ex) {
            actionBarHeight = 0;
        }

        int focusId = binding.ivFocus.getId();

        boolean isEvenNoOfBtn = isEven(buttonList.size());
        for (int i = isEvenNoOfBtn ? 1 : 0; i < (isEvenNoOfBtn ? buttonList.size() + 1 : buttonList.size()); i++) {
            GestureButton gestureButton = buttonList.get(isEvenNoOfBtn ? i - 1 : i);
            ImageView imageView = new ImageView(mContext);
            imageView.setId(gestureButton.getId());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(btnSizePx, btnSizePx);
            if (i == 0) {
                params.startToStart = binding.rootLayout.getId();
                params.endToEnd = binding.rootLayout.getId();
                params.bottomToTop = focusId;
                params.bottomMargin = radiusPx;
            } else if (i == 1) {
                params.endToStart = focusId;
                params.bottomToTop = focusId;
                if (isEvenNoOfBtn) {
                    params.rightMargin = (int) ((radiusPx * Math.sin(Math.PI / 8)) - btnSizePx);
                    params.bottomMargin = (int) ((radiusPx * Math.cos(Math.PI / 8)) - btnSizePx);
                } else {
                    params.rightMargin = (int) ((radiusPx * Math.sin(Math.PI / 4)) - btnSizePx);
                    params.bottomMargin = (int) ((radiusPx * Math.cos(Math.PI / 4)));
                }
            } else if (i == 2) {
                params.startToEnd = focusId;
                params.bottomToTop = focusId;
                if (isEvenNoOfBtn) {
                    params.leftMargin = (int) ((radiusPx * Math.sin(Math.PI / 8)) - btnSizePx);
                    params.bottomMargin = (int) ((radiusPx * Math.cos(Math.PI / 8)) - btnSizePx);
                } else {
                    params.leftMargin = (int) ((radiusPx * Math.sin(Math.PI / 4)) - btnSizePx);
                    params.bottomMargin = (int) ((radiusPx * Math.cos(Math.PI / 4)));
                }
            } else if (i == 3) {
                params.endToStart = focusId;
                params.bottomToTop = focusId;
                if (isEvenNoOfBtn) {
                    params.rightMargin = (int) ((radiusPx * Math.cos(Math.PI / 8)) - btnSizePx);
                    params.bottomMargin = (int) ((radiusPx * Math.sin(Math.PI / 8)) - btnSizePx);
                } else {
                    params.rightMargin = (int) ((radiusPx * Math.sin(Math.PI / 2)) - btnSizePx);
                }
            } else if (i == 4) {
                params.startToEnd = focusId;
                params.bottomToTop = focusId;
                if (isEvenNoOfBtn) {
                    params.leftMargin = (int) ((radiusPx * Math.cos(Math.PI / 8)) - btnSizePx);
                    params.bottomMargin = (int) ((radiusPx * Math.sin(Math.PI / 8)) - btnSizePx);
                } else {
                    params.leftMargin = (int) ((radiusPx * Math.sin(Math.PI / 2)) - btnSizePx);
                }
            }
            imageView.setBackgroundResource(R.drawable.bg_unchecked);
            imageView.setLayoutParams(params);
            binding.rootLayout.addView(imageView);
        }

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.transparent_black);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    @Override
    public void show() {
        onCreate(null);
        super.show();
    }

    private void repositionDescriptionView() {
        ConstraintLayout.LayoutParams focusParams = (ConstraintLayout.LayoutParams) binding.tvDescription.getLayoutParams();
        focusParams.startToStart = binding.rootLayout.getId();
        focusParams.endToEnd = binding.rootLayout.getId();
        focusParams.bottomToTop = binding.ivFocus.getId();
        focusParams.bottomMargin = radiusPx + (btnSizePx);
        binding.tvDescription.requestLayout();
    }

    private void resizeFocusView() {
        ConstraintLayout.LayoutParams focusParams = (ConstraintLayout.LayoutParams) binding.ivFocus.getLayoutParams();
        focusParams.height = btnSizePx;
        focusParams.width = btnSizePx;
        focusParams.startToStart = binding.rootLayout.getId();
        focusParams.endToEnd = binding.rootLayout.getId();
        focusParams.bottomToBottom = binding.rootLayout.getId();
        int[] coords = {0, 0};
        touchedView.getLocationInWindow(coords);
        int bottomMargin = getScreenHeightPixels(mContext) - coords[1] - getActionBarHeight(mContext) - getStatusBarHeight(mContext) - (touchedView.getHeight() / 2);
        int minHeight = (dpToPx(250, mContext) + radiusPx);
        if (minHeight < coords[1]) {
            focusParams.bottomMargin = bottomMargin;
        } else {
            focusParams.bottomMargin = dpToPx(20, mContext);
        }
        binding.ivFocus.requestLayout();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
//    Log.d(TAG, "dispatchTouchEvent: " + event.getX() + " " + event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                Rect rectFilter = new Rect();
                touchedView.getHitRect(rectFilter);
                int rectX = (int) (event.getX() + rectFilter.left);
                int rectY = (int) (event.getY() + rectFilter.top + actionBarHeight);

                for (int i = 0; i < binding.rootLayout.getChildCount(); i++) {
                    View view = binding.rootLayout.getChildAt(i);
                    if (view instanceof ImageView && view != binding.ivFocus) {
                        Rect rect = new Rect();
                        view.getHitRect(rect);
                        if (rect.contains(rectX, rectY)) {
                            onSelected(view);
                            return true;
                        }
                    }
                }
                deselectAll();

                break;
        }
        return true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (lastSelected != null) {
            onSelectListener.onSelected(lastSelected.getId());
            lastSelected = null;
        }
    }

    private void onSelected(View view) {
        deselectAll();
        lastSelected = view;
        view.setBackgroundResource(R.drawable.bg_checked);
        view.setScaleX(1.2f);
        view.setScaleY(1.2f);
        GestureButton selectedGestureButton = getGestureButtonById(view.getId());
        if (selectedGestureButton != null) {
            binding.tvTitle.setText(selectedGestureButton.getTitle());
            binding.tvDescription.setText(selectedGestureButton.getDescription());
        }
    }

    private void deselectAll() {
        for (int i = 0; i < binding.rootLayout.getChildCount(); i++) {
            View view = binding.rootLayout.getChildAt(i);
            if (view instanceof ImageView && view != binding.ivFocus) {
                view.setBackgroundResource(R.drawable.bg_unchecked);
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        }
        binding.tvTitle.setText("");
        binding.tvDescription.setText("");
        lastSelected = null;
    }

    private GestureButton getGestureButtonById(int id) {
        for (int i = 0; i < buttonList.size(); i++) {
            GestureButton gestureButton = buttonList.get(i);
            if (gestureButton.getId() == id) {
                return gestureButton;
            }
        }
        return null;
    }

    private static boolean isEven(int no) {
        return no % 2 == 0;
    }

    public static int pxToDp(float px, Context context) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int dpToPx(float dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
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
}
