package com.example.se;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityAlter extends Activity {

    // protect from xss
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activityalter);

        //接受mainActivity传过来的intent&值
        Intent intent=getIntent();
        String message=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //此处代码与注释代码作用类似
        TextView textView=new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        setContentView(textView);
        //TextView textView=(TextView) findViewById(R.id.two_tV);
        //textView.setText(message);
    }
}