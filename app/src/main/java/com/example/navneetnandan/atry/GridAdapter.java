package com.example.navneetnandan.atry;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Navneet Nandan on 11-10-2016.
 */

public class GridAdapter extends BaseAdapter {
    private Context mContext;

    public Node[] getData() {
        return data;
    }

    public void setData(Node[] data) {
        this.data = data;
    }

    private Node[] data;
    private int screenWidth;
    public GridAdapter(Context c,Node[] a,int width) {
        screenWidth=width;
        mContext=c;
        data=a;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            int tileSize=screenWidth/8;
            textView.setLayoutParams(new GridView.LayoutParams(tileSize-5,tileSize-5));
            textView.setGravity(0x11);
        } else {
            textView = (TextView) convertView;
        }

        if (data[i].isObstacle){
            textView.setBackgroundColor(mContext.getResources().getColor( R.color.obstacle));
        }else
        if (data[i].isPath()){
            textView.setBackgroundColor(mContext.getResources().getColor( R.color.path));
            if(i==0)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.walk));
            if(i==63)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.ecologic));
            Log.e("h",i+"");
            if(data[i].getEnergy()!=-1){
                Log.e("km0","kimo");
                textView.setText(data[i].getEnergy()+"");
                textView.setBackgroundColor(mContext.getResources().getColor( R.color.energy));
                if(i==0)
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.walk));
                if(i==63)
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.ecologic));

            }
        }else
        if(data[i].isCurrent){
            textView.setText("");
            textView.setBackgroundColor(mContext.getResources().getColor( R.color.current));
            if(i==0)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.walk));
            if(i==63)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.ecologic));
        }else
        if(data[i].traversed){
            textView.setBackgroundColor(mContext.getResources().getColor( R.color.traversed));
            if(i==0)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.walk));
            if(i==63)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.ecologic));
        }else
        if(data[i].getEnergy()==-1){
            textView.setBackgroundColor(mContext.getResources().getColor( R.color.normal));
            if(i==0)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.walk));
            if(i==63)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.ecologic));
        }else{
            textView.setText(data[i].getEnergy()+"");
            textView.setBackgroundColor(mContext.getResources().getColor( R.color.energy));
            if(i==0)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.walk));
            if(i==63)
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.ecologic));
        }
        return textView;
    }
}
