package com.shrewd.smart_gesture;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
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

public class SmartGestureDialog extends Dialog {

    private static final String TAG = SmartGestureDialog.class.getName();
    private Properties properties;
    private List<GestureButton> buttonList;
    private SmartGestureCallBack smartGestureCallBack;
    private DgSmartGestureBinding binding;
    private final Context mContext;
    private View touchedView;
    private View lastSelected;

    public SmartGestureDialog(@NonNull Context mContext, List<GestureButton> buttonList) {
        super(mContext);
        this.mContext = mContext;
        this.buttonList = buttonList;
        properties = new Properties(mContext);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void updateList(List<GestureButton> buttonList) {
        if (buttonList.size() > 5) {
            for (int i = 5; i < buttonList.size(); i++) {
                buttonList.remove(i);
            }
        }
        this.buttonList = buttonList;
    }

    public void setCallback(SmartGestureCallBack smartGestureCallBack) {
        this.smartGestureCallBack = smartGestureCallBack;
    }

    public void setTouchedView(View touchedView) {
        this.touchedView = touchedView;
    }

    private void initDialog() {
        binding = DgSmartGestureBinding.inflate(LayoutInflater.from(mContext));
        setContentView(binding.getRoot());
        setFocusView();
        repositionDescriptionView();
        Rect frame = new Rect();
        ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int focusId = binding.ivFocus.getId();

        boolean isEvenNoOfBtn = isEven(buttonList.size());
        for (int i = isEvenNoOfBtn ? 1 : 0; i < (isEvenNoOfBtn ? buttonList.size() + 1 : buttonList.size()); i++) {
            GestureButton gestureButton = buttonList.get(isEvenNoOfBtn ? i - 1 : i);
            ImageView imageView = new ImageView(mContext);
            imageView.setId(gestureButton.getId());
            imageView.setPadding(
                    properties.getButtonPadding(),
                    properties.getButtonPadding(),
                    properties.getButtonPadding(),
                    properties.getButtonPadding()
            );
            imageView.setImageResource(gestureButton.getIconResId());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setElevation(10);
                imageView.setImageTintList(ColorStateList.valueOf(properties.getNonSelectedButtonTint()));
            }
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(properties.getBtnSize(), properties.getBtnSize());
            if (i == 0) {
                params.startToStart = focusId;
                params.endToEnd = focusId;
                params.bottomToTop = focusId;
                params.bottomMargin = properties.getRadius();
            } else if (i == 1) {
                params.endToStart = focusId;
                params.bottomToTop = focusId;
                if (isEvenNoOfBtn) {
                    params.rightMargin = (int) ((properties.getRadius() * Math.sin(Math.PI / 8)) - properties.getBtnSize());
                    params.bottomMargin = (int) ((properties.getRadius() * Math.cos(Math.PI / 8)) - properties.getBtnSize());
                } else {
                    params.rightMargin = (int) ((properties.getRadius() * Math.sin(Math.PI / 4)) - properties.getBtnSize());
                    params.bottomMargin = (int) ((properties.getRadius() * Math.cos(Math.PI / 4)));
                }
            } else if (i == 2) {
                params.startToEnd = focusId;
                params.bottomToTop = focusId;
                if (isEvenNoOfBtn) {
                    params.leftMargin = (int) ((properties.getRadius() * Math.sin(Math.PI / 8)) - properties.getBtnSize());
                    params.bottomMargin = (int) ((properties.getRadius() * Math.cos(Math.PI / 8)) - properties.getBtnSize());
                } else {
                    params.leftMargin = (int) ((properties.getRadius() * Math.sin(Math.PI / 4)) - properties.getBtnSize());
                    params.bottomMargin = (int) ((properties.getRadius() * Math.cos(Math.PI / 4)));
                }
            } else if (i == 3) {
                params.endToStart = focusId;
                params.bottomToTop = focusId;
                if (isEvenNoOfBtn) {
                    params.rightMargin = (int) ((properties.getRadius() * Math.cos(Math.PI / 8)) - properties.getBtnSize());
                    params.bottomMargin = (int) ((properties.getRadius() * Math.sin(Math.PI / 8)) - properties.getBtnSize());
                } else {
                    params.rightMargin = (int) ((properties.getRadius() * Math.sin(Math.PI / 2)) - properties.getBtnSize());
                }
            } else if (i == 4) {
                params.startToEnd = focusId;
                params.bottomToTop = focusId;
                if (isEvenNoOfBtn) {
                    params.leftMargin = (int) ((properties.getRadius() * Math.cos(Math.PI / 8)) - properties.getBtnSize());
                    params.bottomMargin = (int) ((properties.getRadius() * Math.sin(Math.PI / 8)) - properties.getBtnSize());
                } else {
                    params.leftMargin = (int) ((properties.getRadius() * Math.sin(Math.PI / 2)) - properties.getBtnSize());
                }
            }
            imageView.setBackground(properties.getNonSelectedButtonDrawable());
            imageView.setLayoutParams(params);
            binding.rootLayout.addView(imageView);
        }

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(properties.getBackgroundColor()));
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    @Override
    public void show() {
        initDialog();
        super.show();
        binding.rootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < binding.rootLayout.getChildCount(); i++) {
                    View view = binding.rootLayout.getChildAt(i);
                    if (view instanceof ImageView && view != binding.ivFocus) {
                        Rect rect = new Rect();
                        view.getHitRect(rect);
                        Log.d(TAG, "show: " + view.getX() + " " + view.getY() + " " + rect.left + " " + rect.top);
                    }
                }
            }
        }, 1000);
    }

    private void repositionDescriptionView() {
        if (properties.isTextEnabled()) {
            binding.tvTitle.setVisibility(View.VISIBLE);
            binding.tvDescription.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams descriptionParams = (ConstraintLayout.LayoutParams) binding.tvDescription.getLayoutParams();
            descriptionParams.startToStart = binding.rootLayout.getId();
            descriptionParams.endToEnd = binding.rootLayout.getId();
            descriptionParams.bottomToTop = binding.ivFocus.getId();
            descriptionParams.bottomMargin = (int) (properties.getRadius() + (properties.getBtnSize()) + properties.getTextVerticalOffset());
            descriptionParams.leftMargin = (int) properties.getTextHorizontalOffset();
            binding.tvDescription.requestLayout();

            binding.tvTitle.setTypeface(properties.getTypeface());
            binding.tvDescription.setTypeface(properties.getTypeface());

            binding.tvTitle.setGravity(properties.getTextGravity());
            binding.tvDescription.setGravity(properties.getTextGravity());

            ConstraintLayout.LayoutParams titleParams = (ConstraintLayout.LayoutParams) binding.tvTitle.getLayoutParams();
            titleParams.leftMargin = (int) properties.getTextHorizontalOffset();
            binding.tvTitle.requestLayout();
        } else {
            binding.tvTitle.setVisibility(View.GONE);
            binding.tvDescription.setVisibility(View.GONE);
        }
    }

    private void setFocusView() {
        ConstraintLayout.LayoutParams focusParams = (ConstraintLayout.LayoutParams) binding.ivFocus.getLayoutParams();
        focusParams.height = properties.getBtnSize();
        focusParams.width = properties.getBtnSize();
        focusParams.startToStart = binding.rootLayout.getId();
        focusParams.endToEnd = binding.rootLayout.getId();
        focusParams.bottomToBottom = binding.rootLayout.getId();
        int[] coords = {0, 0};
        touchedView.getLocationInWindow(coords);
        int bottomMargin = (int) (SmartGestureUtils.getScreenHeightPixels(mContext)
                - coords[1]
                - SmartGestureUtils.getActionBarHeight(mContext)
                - SmartGestureUtils.getStatusBarHeight(mContext)
                - properties.getVerticalOffset());
        int minHeight = (SmartGestureUtils.dpToPx(250, mContext) + properties.getRadius());
        if (minHeight < coords[1]) {
            if (bottomMargin > 0) {
                focusParams.bottomMargin = bottomMargin;
            } else {
                focusParams.topMargin = bottomMargin;
            }
        } else {
            focusParams.bottomMargin = SmartGestureUtils.dpToPx(20, mContext);
        }
        int leftMargin = (int) properties.getHorizontalOffset();
        int minWidth = (SmartGestureUtils.dpToPx(50, mContext) + (properties.getRadius() * 2));
        if (minWidth < SmartGestureUtils.getScreenWidthPixels(mContext)
                - leftMargin
                - binding.rootLayout.getPaddingLeft()
                - binding.rootLayout.getPaddingRight()) {
            if (leftMargin > 0) {
                focusParams.leftMargin = leftMargin;
            } else {
                focusParams.rightMargin = leftMargin;
            }
        } else {
            focusParams.leftMargin = 0;
            focusParams.rightMargin = leftMargin;
        }
        binding.ivFocus.requestLayout();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                smartGestureCallBack.onGestureStarted();
                break;
            case MotionEvent.ACTION_MOVE:
                int rectX = (int) (event.getRawX() + properties.getHorizontalTouchAdjust());
                int rectY = (int) (event.getRawY()
                        + properties.getVerticalTouchAdjust());

                for (int i = 0; i < binding.rootLayout.getChildCount(); i++) {
                    View view = binding.rootLayout.getChildAt(i);
                    if (view instanceof ImageView && view != binding.ivFocus) {
                        int[] co = {0, 0};
                        view.getLocationOnScreen(co);
                        Rect rect = new Rect(co[0], co[1], co[0] + view.getWidth(), co[1] + view.getHeight());
                        if (rect.contains(rectX, rectY)) {
                            onSelected((ImageView) view);
                            return true;
                        }
                    }
                }
                onDeselected();
                break;
        }
        return true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        smartGestureCallBack.onGestureEnded();
        if (smartGestureCallBack == null)
            return;
        if (lastSelected != null) {
            smartGestureCallBack.onSelected(lastSelected.getId());
            lastSelected = null;
        } else {
            smartGestureCallBack.onNothingSelected();
        }
    }

    private void onSelected(ImageView view) {
        if (lastSelected != view) {
            onDeselected();
            lastSelected = view;
            view.setBackground(properties.getSelectedButtonDrawable());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setImageTintList(ColorStateList.valueOf(properties.getSelectedButtonTint()));
            }
            if (properties.isAnimationEnabled()) {
                view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(properties.getAnimationTime()).start();
            } else {
                view.setScaleX(1.2f);
                view.setScaleY(1.2f);
            }
            GestureButton selectedGestureButton = getGestureButtonById(view.getId());
            if (selectedGestureButton != null) {
                binding.tvTitle.setText(selectedGestureButton.getTitle());
                binding.tvDescription.setText(selectedGestureButton.getDescription());
            }
        }
    }

    private void onDeselected() {
        if (lastSelected == null)
            return;
        lastSelected.setBackground(properties.getNonSelectedButtonDrawable());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ImageView) lastSelected).setImageTintList(ColorStateList.valueOf(properties.getNonSelectedButtonTint()));
        }
        if (properties.isAnimationEnabled()) {
            lastSelected.clearAnimation();
            lastSelected.animate().scaleX(1).scaleY(1).setDuration(properties.getAnimationTime()).start();
        } else {
            lastSelected.setScaleX(1);
            lastSelected.setScaleY(1);
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
}
