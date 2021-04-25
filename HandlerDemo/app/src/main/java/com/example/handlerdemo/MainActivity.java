package com.example.handlerdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    int[] imgs = {R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5};
    int img = 0;
    //此处初始化会出错
    //ImageView imgchange = (ImageView)findViewById(R.id.imgchange);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imgchange = (ImageView)findViewById(R.id.imgchange);

        //handler和timer写在主线程  与主线程looper绑定
        Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1)
                {
                    imgchange.setImageResource(imgs[img++ % 5]);
                }
            }
        };

        //使用timer提示handler更新UI
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(1);

            }
        };
        timer.schedule(task, 0, 200);

        //使用Thread
        ThreadDemo td = new ThreadDemo(myHandler);
        td.start();
    }

    //使用Thread来提示handler更新UI
    class ThreadDemo extends Thread{
        Handler myhandler;
        public ThreadDemo(Handler myhandler) {
            this.myhandler = myhandler;
        }
        @Override
        public void run() {
            try{
                //message用法
                Message msg = new Message();
                //虽然Message的构造函数式public的，我们也可以通过以下两种方式通过循环对象获取Message
                //msg = Message.obtain(uiHandler);
                //msg = uiHandler.obtainMessage();
                //what是我们自定义的一个Message的识别码，以便于在Handler的handleMessage方法中根据what识别
                //出不同的Message，以便我们做出不同的处理操作
                msg.what = 1;
                //我们可以通过arg1和arg2给Message传入简单的数据
                msg.arg1 = 123;
                msg.arg2 = 321;
                //我们也可以通过给obj赋值Object类型传递向Message传入任意数据
                //msg.obj = null;
                //我们还可以通过setData方法和getData方法向Message中写入和读取Bundle类型的数据
                //msg.setData(null);
                //Bundle data = msg.getData();
                while(true) {
                    //此处让线程间断0.05秒发送变换图片的信息
                    Thread.sleep(200);
                    //将该Message发送给对应的Handler
                    //一个massage对象只能用一次
                    //myhandler.sendMessage(msg);
                    myhandler.sendEmptyMessage(1);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}