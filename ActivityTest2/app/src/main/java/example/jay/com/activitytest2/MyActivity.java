package example.jay.com.activitytest2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MyActivity extends ActionBarActivity {

    private Button btnchoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        btnchoose = (Button)findViewById(R.id.btnchoose);
        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MyActivity.this,MyActivity2.class);
                startActivityForResult(it,0x123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x123 && resultCode == 0x123)
        {
            Bundle bd = data.getExtras();
            int imgid = bd.getInt("imgid");
            //获取布局文件中的ImageView组件
            ImageView img = (ImageView)findViewById(R.id.imgicon);
            img.setImageResource(imgid);
        }
    }
}
