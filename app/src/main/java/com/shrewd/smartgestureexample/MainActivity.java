package com.shrewd.smartgestureexample;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.shrewd.smart_gesture.GestureButton;
import com.shrewd.smart_gesture.Properties;
import com.shrewd.smart_gesture.SmartGestureCallBack;
import com.shrewd.smart_gesture.SmartGestureDialog;
import com.shrewd.smart_gesture.SmartGestureListener;
import com.shrewd.smart_gesture.SmartGestureUtils;
import com.shrewd.smartgestureexample.databinding.ActivityMainBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final ArrayList<GestureButton> buttonArrayList = new ArrayList<>();
    private Properties.Builder propertyBuilder;
    private SmartGestureListener smartGestureListener;
    private Properties properties;
//    private static boolean first = true;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (first) {
            first = false;
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }*/
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setButtonList("5");
        binding.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonList(binding.etNoOfButton.getText().toString());
            }
        });
        propertyBuilder = new Properties.Builder(this)
                .setBackgroundColor(getResources().getColor(R.color.teal_700))
                .setNonSelectedButtonDrawableResId(R.drawable.bg_unhovered)
                .setSelectedButtonDrawableResId(R.drawable.bg_hovered)
                .setSelectedButtonTintResId(R.color.white)
                .setTextVerticalOffset(50)
                .setBgAlpha(0)
                .setTypeface(Typeface.MONOSPACE)
                .setBtnSpacingOffset(20)
                .setTextGravity(Gravity.CENTER)
                .setDelay(500)
                .setNonSelectedButtonTintResId(R.color.white);
        properties = propertyBuilder.create();

        properties.getLiveMaxRadius().observe(this, integer -> {
            Log.d("TAG", "onCreate: Radius: " + properties.getRadius());
            binding.tvRadiusConstraints.setText("Radius (Min: " + properties.getMinRadius() + ", Max: " + properties.getMaxRadius() + ")");
        });
        properties.getLiveMinRadius().observe(this, integer -> {
            Log.d("TAG", "onCreate: " + integer);
            binding.tvRadiusConstraints.setText("Radius (Min: " + properties.getMinRadius() + ", Max: " + properties.getMaxRadius() + ")");
        });

        binding.tvSizeConstraints.setText("Size (Min: " + properties.getMinSize() + ", Max: " + properties.getMaxSize() + ")");

        smartGestureListener = new SmartGestureListener(this, buttonArrayList, properties);
        smartGestureListener.setCallback(new SmartGestureCallBack() {
            @Override
            public void onSelected(int id) {
                for (GestureButton imageButton : buttonArrayList) {
                    if (imageButton.getId() == id) {
                        Toast.makeText(MainActivity.this, "Selected item: " + imageButton.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected() {
                Toast.makeText(MainActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGestureStarted() {
            }

            @Override
            public void onGestureEnded() {
            }

            @Override
            public void onJustClicked() {
                Toast.makeText(MainActivity.this, "Hover and swipe to filter", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnTouch.setOnTouchListener(smartGestureListener);
        binding.sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int px = SmartGestureUtils.dpToPx(progress, MainActivity.this);
                if (px > 160) {
                    binding.tvSize.setTextColor(Color.RED);
                } else {
                    binding.tvSize.setTextColor(getResources().getColor(android.R.color.secondary_text_dark));
                }
                binding.tvSize.setText("Size (" + px + ")");
                propertyBuilder.setBtnSize(binding.sbSize.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.tvRadius.setText("Radius (" + SmartGestureUtils.dpToPx(progress, MainActivity.this) + ")");
                propertyBuilder.setRadius(binding.sbRadius.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setButtonList(String noOfButtons) {
        buttonArrayList.clear();
        buttonArrayList.add(new GestureButton(0, R.drawable.ic_share, "Share", "share wherever you want"));
        buttonArrayList.add(new GestureButton(1, R.drawable.ic_save, "Save", "Save locally"));
        buttonArrayList.add(new GestureButton(2, R.drawable.ic_facebook, "Facebook", "Post it to Facebook"));
        buttonArrayList.add(new GestureButton(3, R.drawable.ic_reaction, "React on it", "Add reaction"));
        buttonArrayList.add(new GestureButton(4, R.drawable.ic_check, "Done", "Save and mark as done"));
        buttonArrayList.add(new GestureButton(5, R.drawable.ic_baseline_add_to_drive, "Add to Drive", "Add file to google drive"));
        int no = Integer.parseInt(noOfButtons);
        if (no > 0 && no <= SmartGestureDialog.MAX_BUTTON) {
            for (int i = SmartGestureDialog.MAX_BUTTON; i > no; i--) {
                buttonArrayList.remove(i - 1);
            }
            binding.tvNoOfBtn.setText("Number of buttons: " + buttonArrayList.size());
        } else {
            Toast.makeText(this, "Number should be between 1 to 6", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, 0, Menu.NONE, "Save");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 0 && propertyBuilder != null) {
            if ("".equals(binding.etNoOfButton.getText().toString()) ||
                    "".equals(binding.etPadding.getText().toString()) ||
                    "".equals(binding.etHOffset.getText().toString()) ||
                    "".equals(binding.etVOffset.getText().toString()) ||
                    "".equals(binding.etTextHOffset.getText().toString()) ||
                    "".equals(binding.etTextVOffset.getText().toString()) ||
                    "".equals(binding.etHTouchOffset.getText().toString()) ||
                    "".equals(binding.etVTouchOffset.getText().toString())) {
                Toast.makeText(this, "Edit-text should not be empty", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            setButtonList(binding.etNoOfButton.getText().toString());
            propertyBuilder.setRadius(binding.sbRadius.getProgress());
            propertyBuilder.setBtnSize(binding.sbSize.getProgress());
            propertyBuilder.setButtonPaddingPercentage(Integer.parseInt(binding.etPadding.getText().toString()));
            propertyBuilder.setHorizontalOffset(Integer.parseInt(binding.etHOffset.getText().toString()));
            propertyBuilder.setVerticalOffset(Integer.parseInt(binding.etVOffset.getText().toString()));
            propertyBuilder.setTextHorizontalOffset(Integer.parseInt(binding.etTextHOffset.getText().toString()));
            propertyBuilder.setTextVerticalOffset(Integer.parseInt(binding.etTextVOffset.getText().toString()));
            propertyBuilder.setHorizontalTouchOffset(Integer.parseInt(binding.etHTouchOffset.getText().toString()));
            propertyBuilder.setVerticalTouchOffset(Integer.parseInt(binding.etVTouchOffset.getText().toString()));
            switch (binding.rgGravity.getCheckedRadioButtonId()) {
                case R.id.rbtnLeft:
                    propertyBuilder.setTextGravity(Gravity.START);
                    break;
                case R.id.rbtnCenter:
                    propertyBuilder.setTextGravity(Gravity.CENTER);
                    break;
                case R.id.rbtnRight:
                    propertyBuilder.setTextGravity(Gravity.END);
                    break;
            }
            properties = propertyBuilder.create();
            smartGestureListener.setProperties(properties);
            binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);

        }
        return super.onOptionsItemSelected(item);
    }
}