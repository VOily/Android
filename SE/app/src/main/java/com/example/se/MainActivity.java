package com.example.se;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE="com.example.se.MESSAGE";
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void sendMessage(View v){
        //初始化Intent 从一个Activity到另一个Activity
        Intent intent=new Intent(this , ActivityAlter.class);
        //第二、三种显式启动方式  隐式启动略
        /*                                  此处填写 全限定类名
        ComponentName cn = new ComponentName(this , ActivityAlter.class);
        intent.setComponent(cn);
        *****
        初始化Intent指向包名
        Intent i = new Intent("android.intent.action.Main");
        intent.setClassName(同上);
        */
        EditText editText=(EditText) findViewById(R.id.editText);
        String message=editText.getText().toString();
        //携带额外的信息
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }
}