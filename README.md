> This project was created during my internship <a href="https://dcoder.tech/" target="_blank">@dcodermobile</a>
<kbd><img src="https://play-lh.googleusercontent.com/PWUsRLZ8fawBOdTjMrSWwa6-EBpzOguNIlvqxepMRFmBD8eTq8UPGj2241I2qFF1Eg=s360-rw" width="28" height="28" /></kbd>.
I am open sourcing it to give back to community. 
> <br/><a href="https://play.google.com/store/apps/details?id=com.paprbit.dcoder" target="_blank">Download Dcoder app for Android</a>

# Smart-gesture-android
A fully customizable smart gesture in simplest way to give a feel like 3D touch in Android.

## A quick demo

[![Video demo](https://github.com/nmghelani/smart-gesture-android/blob/master/smart-gesture-library.gif)](https://www.youtube.com/watch?v=NTqDCo0q2ys)

Video demo: https://www.youtube.com/watch?v=NTqDCo0q2ys

## How to implement in your project
### Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}
```
### Add the dependency in your app build.gradle
```
dependencies {
    ...
    implementation 'com.github.nmghelani:smart-gesture-android:0.1.6'
}
```
### Now in your Java code,
```JAVA
//List of buttons to be shown on gesture dialog
ArrayList<GestureButton> buttons = new ArrayList<>();
buttons.add(new GestureButton(0, R.drawable.ic_launcher, "Write title here", "Write description here"));
buttons.add(new GestureButton(1, R.drawable.ic_launcher, "Write title here", "Write description here"));
buttons.add(new GestureButton(2, R.drawable.ic_launcher, "Write title here", "Write description here"));
buttons.add(new GestureButton(3, R.drawable.ic_launcher, "Write title here", "Write description here"));
buttons.add(new GestureButton(4, R.drawable.ic_launcher, "Write title here", "Write description here"));
buttons.add(new GestureButton(4, R.drawable.ic_launcher, "Write title here", "Write description here"));

SmartGestureListener gestureListener = new SmartGestureListener(mContext, buttons);

gestureListener.setProperties(new Properties.Builder(mContext).create());

gestureListener.setCallback(new SmartGestureCallBack() {
    @Override
    public void onSelected(int id) {
        //Selected item id that we've passed in buttons arrayList
    }

    @Override
    public void onNothingSelected() {
        //Nothing was selected
    }

    @Override
    public void onGestureStarted() {
        //Gesture dialog is opened
    }

    @Override
    public void onGestureEnded() {
        //Gesture dialog is closed
    }
    
    @Override
    public void onJustClicked() {
      Toast.makeText(MainActivity.this, "Hold the button", Toast.LENGTH_SHORT).show();
    }
});

//set gestureListener to a view as touchListener
view.setOnTouchListener(gestureListener);
```

That's it. You've added a smart gesture dialog with beautiful UI and feel of 3D touch.  

**NOTE:**
- The view with this touch listener should be a direct child of rootLayout or its parent should cover whole screen or else the touch event will be cancelled and so the dialog will get dissmissed.
- You can add upto 6 buttons.


### To customize it,
```JAVA
new Properties.Builder(this)
        .setRadius(100) //Radius in dp
        .setBtnSize(50) //Button size in dp
        .setButtonPaddingPercentage(20) //Icon padding in percentage
        .setBackgroundColor(getResources().getColor(R.color.transparent_black)) //Background color of gesture dialog
        .setSelectedButtonTint(getResources().getColor(R.color.white)) //Selected icon tint
        .setNonSelectedButtonTint(getResources().getColor(R.color.black)) //Non-selected icon tint
        .setSelectedButtonDrawable(ContextCompat.getDrawable(mContext,R.drawable.bg_checked)) //Selected button background
        .setNonSelectedButtonDrawable(ContextCompat.getDrawable(mContext,R.drawable.bg_unchecked)) //Non-selected button background
        .setBtnSpacingOffset(20) //horizontal spacing between buttons
        .setTextHorizontalOffset(50) //To adjust text (Left-right) in dp
        .setTextVerticalOffset(50) //To adjust text (Top-Bottom) in dp
        .setVerticalTouchOffset(10) //To adjust hover (Top-Bottom) in dp
        .setTextEnabled(false) //Show/hide title and description (Enabled by-default)
        .setTextGravity(Gravity.CENTER) //Set gravity to title and description
        .setTypeface(Typeface.MONOSPACE) //Typeface for title and description
        .setAnimationEnabled(false) //To animate view on hover (Enabled by-default)
        .setDelay(100) //Delay of showing dialog on touch in millis
        .setAnimationTime(100) //Animation time on hover
        .create();
```


Voila, You've sucessfully customized the smart-gesture-dialog for android.
