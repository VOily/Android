package com.example.se;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class AdapterDemo extends AppCompatActivity {

    final private String[] names = new String[]{"A", "B", "C"};
    final private String[] says = new String[]{"1234", "5678", "9101"};
    final private int[] imgIds = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置顶层activity的layout
        setContentView(R.layout.list_view);

        //使用字典存储数据（图片文字ect）
        List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("touxiang", imgIds[i]);
            showitem.put("name", names[i]);
            showitem.put("says", says[i]);
            listitem.add(showitem);
        }

        //创建一个simpleAdapter  数据与layout中相对应
        SimpleAdapter myAdapter = new SimpleAdapter(
                getApplicationContext(), listitem, R.layout.adapter,
                new String[]{"touxiang", "name", "says"}, new int[]{R.id.imgtou, R.id.name, R.id.says});
        //将顶层activity中layout的list_view实例化并设置adapter
        ListView listView = (ListView) findViewById(R.id.list_test);
        listView.setAdapter(myAdapter);
    }
}