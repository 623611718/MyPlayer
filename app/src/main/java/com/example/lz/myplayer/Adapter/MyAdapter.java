package com.example.lz.myplayer.Adapter;

/**
 * Created by LZ on 2019/6/10.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lz.myplayer.R;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends BaseAdapter{
    private LayoutInflater inflater;
   // private Bitmap directory,file;

    private ArrayList<String> names = null;

    private ArrayList<String> paths = null;

    public MyAdapter(Context context,ArrayList<String> na,ArrayList<String> pa){
        names = na;
        paths = pa;
       // directory = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);
       // file = BitmapFactory.decodeResource(context.getResources(),R.drawable.video_default_icon);
        //directory = small(directory,0.16f);
       // file = small(file,0.1f);
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return names.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (null == convertView){
            convertView = inflater.inflate(R.layout.file, null);
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.tv_name);
            holder.image = (ImageView)convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        File f = new File(paths.get(position).toString());
        if (names.get(position).equals("@1")){
            holder.text.setText("返回根目录");
            // holder.image.setImageBitmap(directory);
            holder.image.setImageResource(R.drawable.folder5);
        }
        else if (names.get(position).equals("@2")){
            holder.text.setText("返回上一级目录");
            // holder.image.setImageBitmap(directory);
            holder.image.setImageResource(R.drawable.folder5);
        }
        else{
            holder.text.setText(f.getName());
            if (f.isDirectory()){
                //  holder.image.setImageBitmap(directory);
                holder.image.setImageResource(R.drawable.folder5);
            }
            else if (f.isFile()){
                //  holder.image.setImageBitmap(file);
                holder.image.setImageResource(R.drawable.video_default_icon);
            }
            else{
                System.out.println(f.getName());
            }
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView text;
        private ImageView image;
    }
    /*private Bitmap small(Bitmap map,float num){
        Matrix matrix = new Matrix();
        matrix.postScale(num, num);
        return Bitmap.createBitmap(map,0,0,map.getWidth(),map.getHeight(),matrix,true);
    }*/
}




