package com.example.lz.myplayer.Adapter;

/**
 * Created by Administrator on 2019/6/29.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lz.myplayer.fragment.Fragment_0;
import com.example.lz.myplayer.fragment.Fragment_1;
import com.example.lz.myplayer.fragment.Fragment_2;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    //定义三个Fragment的索引
    public static final int Fragment_Index_0=0;
    public static final int Fragment_Index_1=1;
    public static final int Fragment_Index_2=2;

    private List<Fragment> fragmentList;
    public ViewPagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }
    public void  setFragmentList(List<Fragment> list){
        fragmentList=list;
    }

    @Override
    public Fragment getItem(int Index)
    {
      /*  Fragment mFragemnt=null;
        switch(Index)
        {
            case Fragment_Index_0:
                mFragemnt=new Fragment_0();
                break;
            case Fragment_Index_1:
                mFragemnt=new Fragment_1();
                break;
            case Fragment_Index_2:
                mFragemnt=new Fragment_2();
                break;
        }
        return mFragemnt;*/
        return fragmentList.get(Index);
    }

    @Override
    public int getCount()
    {
        return 3;
    }

}