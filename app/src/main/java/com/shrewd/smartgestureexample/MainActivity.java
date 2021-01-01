package com.shrewd.smartgestureexample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.shrewd.smart_gesture.GestureButton;
import com.shrewd.smart_gesture.OnSelectListener;
import com.shrewd.smart_gesture.SmartGestureListener;
import com.shrewd.smartgestureexample.databinding.ActivityMainBinding;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final ArrayList<GestureButton> buttonArrayList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setButtonList("5");
        binding.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonList(binding.etNoOfButton.getText().toString());
            }
        });
        OnSelectListener onSelectListener = id -> {
            for (GestureButton imageButton : buttonArrayList) {
                if (imageButton.getId() == id) {
                    Toast.makeText(MainActivity.this, "Selected item: " + imageButton.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        };
        SmartGestureListener smartGestureListener = new SmartGestureListener(this, buttonArrayList, onSelectListener);
        binding.btnTouch.setOnTouchListener(smartGestureListener);
        binding.sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.tvSize.setText("Size (" + progress + ")");
                smartGestureListener.setBtnSize(progress);
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
                binding.tvRadius.setText("Radius (" + progress + ")");
                smartGestureListener.setRadius(progress);
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
        buttonArrayList.add(new GestureButton(0, R.drawable.ic_launcher_background, "Share", "share wherever you want"));
        buttonArrayList.add(new GestureButton(1, R.drawable.ic_launcher_background, "Save", "Save locally"));
        buttonArrayList.add(new GestureButton(2, R.drawable.ic_launcher_background, "Facebook", "Post it to Facebook"));
        buttonArrayList.add(new GestureButton(3, R.drawable.ic_launcher_background, "Instagram", "Post it to Instagram"));
        buttonArrayList.add(new GestureButton(4, R.drawable.ic_launcher_background, "Twitter", "Post it to Twitter"));
        int no = Integer.parseInt(noOfButtons);
        if (no > 0 && no <= 5) {
            for (int i = 5; i > no; i--) {
                buttonArrayList.remove(i - 1);
            }
            binding.tvNoOfBtn.setText("Number of buttons: " + buttonArrayList.size());
        } else {
            Toast.makeText(this, "Number should be between 1 to 5", Toast.LENGTH_SHORT).show();
        }
    }
}