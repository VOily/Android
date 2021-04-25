/**
组件的点击事件，使用Button来演示鼠标点击后组件跟随移动
**/
package com.example.events;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    //因为是内部类的原因不能用局部变量，使用全局或类的变量
    //按下和移动时的时间，用于判断是否是长按事件
    private long timeDown, timeMove;
    //是否是长按事件
    private boolean isLongClick;
    //移动相关
    private float downX, downY, moveX, moveY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //示例 按钮跟随移动
        MyButton mb = (MyButton)findViewById(R.id.button);
        //可将这部分代码改到MyButton类中
        mb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String TAG = "time: " + System.currentTimeMillis();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: ACTION_DOWN");
                        timeDown = System.currentTimeMillis();
                        isLongClick = false;
                        downX = event.getRawX();
                        downY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch: ACTION_MOVE");
                        timeMove = System.currentTimeMillis();
                        long durationMs = timeMove - timeDown;
                        //Log.d(TAG, "onTouch: durationMs="+durationMs);
                        //在move时检测按住按钮时长
                        if (durationMs > 500) {
                            vibrate();
                            Log.d(TAG, "longTouch: vibrate");
                            //长按事件，可以移动
                            moveX = event.getRawX();
                            moveY = event.getRawY();
                            //移动的距离
                            float dx = moveX - downX;
                            float dy = moveY - downY;
                            //重新设置控件的位置。移动
                            ViewGroup.MarginLayoutParams params = (FrameLayout.MarginLayoutParams) mb.getLayoutParams();
                            params.leftMargin += (int)dx;
                            params.topMargin += (int)dy;
                            //两种方式
                            //mb.setLayoutParams(params);
                            int l = mb.getLeft();
                            int r = mb.getRight();
                            int t = mb.getTop();
                            int b = mb.getBottom();
                            mb.layout(l+(int)dx, t+(int)dy, r+(int)dx, b+(int)dy);
                            //重置
                            downX = moveX;
                            downY = moveY;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: ACTION_UP");
                        mb.setText("结束");
                        break;
                }
                return true;
            }
        });

    }
    //长按震动   需要在配置文件中申请设备震动权限
    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }
}