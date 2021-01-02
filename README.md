# Smart-gesture-android
A fully customizable smart gesture in simplest way to give a feel like 3D touch in Android.
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
    implementation 'com.github.nmghelani:smart-gesture-android:0.1.3'
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
});

//set gestureListener to a view as touchListener
view.setOnTouchListener(gestureListener);
```

That's it. You've added a smart gesture dialog with beautiful UI and feel of 3D touch.  

NOTE: 
- The view with this touch listener should be a direct child of rootLayout or its parent should cover whole screen or else the touch event will be cancelled and so the dialog will get dissmissed.
- You can add upto 5 buttons.


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


Voila, You've sucessfully customized gesture dialog.