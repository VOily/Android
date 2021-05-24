package com.example.se;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE="com.example.se.MESSAGE";
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        //外部存储路径
        File[] files;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            files = getExternalFilesDirs(Environment.MEDIA_MOUNTED);
            for(File file:files){
                Log.e("main", String.valueOf(file));
            }
        }
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