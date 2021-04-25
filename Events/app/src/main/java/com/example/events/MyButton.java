package com.example.events;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

//此处 extend android.Button报错
public class MyButton extends androidx.appcompat.widget.AppCompatButton {
    private static String TAG = "ABC";
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写键盘按下触发的事件
    //event中记录按下按钮后到放开的整个运动过程
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        Log.i(TAG,event.toString());
        return true;
    }
}