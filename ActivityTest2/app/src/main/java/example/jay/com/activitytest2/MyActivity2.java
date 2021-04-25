package example.jay.com.activitytest2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.reflect.GenericArrayType;

import example.jay.com.activitytest2.R;

public class MyActivity2 extends ActionBarActivity {

    public int[] imgs = new int[]
            {
                R.drawable.icon1,R.drawable.icon2,R.drawable.icon3,R.drawable.icon4,
                R.drawable.icon5,R.drawable.icon6,R.drawable.icon7,R.drawable.icon8,
                R.drawable.icon9,R.drawable.icon10
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity2);

        GridView gd = (GridView)findViewById(R.id.gridicon);
        BaseAdapter baseAdapter = new BaseAdapter() {

            //获得数量
            @Override
            public int getCount() {
                return imgs.length;
            }
            //获得当前选项
            @Override
            public Object getItem(int position) {
                return position;
            }
            //获得当前选项对应的id
            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if(convertView == null)
                {
                    imageView = new ImageView(MyActivity2.this);
                    //设置图片宽高
                    imageView.setAdjustViewBounds(true);
                    imageView.setMaxHeight(111);
                    imageView.setMaxWidth(111);
                    imageView.setPadding(5,5,5,5);
                }
                else  imageView = (ImageView)convertView;

                imageView.setImageResource(imgs[position]);
                return imageView;

            }
        };

        gd.setAdapter(baseAdapter);


        gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = getIntent();
                Bundle bd = new Bundle();
                bd.putInt("imgid",imgs[position]);
                it.putExtras(bd);
                setResult(0x123,it);
                finish();
            }
        });
    }
}
