package com.example.adapter;

import android.annotation.SuppressLint;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class AnimalAdapter extends BaseAdapter {

    private LinkedList<Animal> mData;
    private Context mContext;

    public AnimalAdapter(LinkedList<Animal> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter,parent,false);
        ImageView img_icon = (ImageView) convertView.findViewById(R.id.imgtou);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.name);
        TextView txt_aSpeak = (TextView) convertView.findViewById(R.id.says);
        Animal animal = mData.get(position);
        img_icon.setBackgroundResource(animal.getaIcon());
        txt_aName.setText(animal.getaName());
        txt_aSpeak.setText(animal.getaSpeak());
        return convertView;
    }
}
