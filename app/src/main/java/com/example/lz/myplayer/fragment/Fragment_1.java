package com.example.lz.myplayer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lz.myplayer.Activity.HttpActivity;
import com.example.lz.myplayer.Activity.PlayerActivity;
import com.example.lz.myplayer.R;

/**
 * Created by Administrator on 2019/6/29.
 * 网络播放的fragment
 */

public class Fragment_1 extends Fragment {
    private EditText et_url;
    private Button bt_play;
    private String url,name;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_1,container,false);
        et_url =(EditText) view.findViewById(R.id.et_url);
        bt_play =(Button) view.findViewById(R.id.bt_play);
        bt_play.setOnClickListener(new Listener());
        return view;
    }

    private void getUrl() {
        url = et_url.getText().toString();
        name = url.substring(url.indexOf("/")+1);
    }
    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            getUrl();
            Intent intent = new Intent(getActivity(), HttpActivity.class);
            intent.putExtra("url",url);
            intent.putExtra("name",name);
            startActivity(intent);
        }
    }
}
