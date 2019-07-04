package com.example.lz.myplayer.Activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TtsSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.TabLayout;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.lz.myplayer.Adapter.MyFragmentPagerAdapter;
import com.example.lz.myplayer.Adapter.TabPagerAdapter;
import com.example.lz.myplayer.Adapter.ViewPagerAdapter;
import com.example.lz.myplayer.R;
import com.example.lz.myplayer.fragment.Fragment_0;
import com.example.lz.myplayer.fragment.Fragment_1;
import com.example.lz.myplayer.fragment.Fragment_2;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {

    private TabLayout mTab;  //底部栏
    private DrawerLayout mDrawerLayout; //
    private ViewPager mViewPager;   //滑动的页面
    private ActionBarDrawerToggle mToggle;  //左上角按键


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initActionBar();
        initViewPager();
     //   initDp();
    }

    //定义Viewpager和底部栏的大小
    private void initDp() {
      /*  WindowManager wm = this.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)*/


        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);///取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
        int top = rect.top;////状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度
        View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);///获得根视图
        int top2 = v.getTop();///状态栏标题栏的总高度,所以标题栏的高度为top2-top
        int width = v.getWidth();///视图的宽度,这个宽度好像总是最大的那个
        int height = v.getHeight();////视图的高度，不包括状态栏和标题栏
        Display display = getWindowManager().getDefaultDisplay() ;
        display.getWidth();
        display.getHeight();

        Log.i("test","actionbarheight:  "+actionBar.getHeight());
        Log.i("test","screenWidth:  "+width+"  "+"screenHeight:  "+height);
        LinearLayout.LayoutParams lp_vp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        mViewPager.setLayoutParams(lp_vp);
        LinearLayout.LayoutParams lp_tab = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        mTab.setLayoutParams(lp_tab);
    }

    //设置ViewPager
    private void initViewPager() {
        List<Fragment> list = new ArrayList<>();
        list.add(new Fragment_0());
        list.add(new Fragment_1());
        list.add(new Fragment_2());
        list.add(new Fragment_0());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.setFragmentList(list);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mViewPager.setAdapter(adapter);
        mTab.getTabAt(0).setText("本地视频").setIcon(R.drawable.tab_i8);
        mTab.getTabAt(1).setText("网络视频").setIcon(R.drawable.radio_network);
        mTab.getTabAt(2).setText("更多...").setIcon(R.drawable.tab_i4);
        mTab.getTabAt(3).setText("用户").setIcon(R.drawable.tab_i6);
    }

    private ActionBar actionBar;
    //设置ActionBar和ActionBarDrawerToggle
    private void initActionBar() {
      /*  View view = LayoutInflater.from(this).inflate(R.layout.layout_title,null);
        TextView tv = (TextView) view.findViewById(R.id.title1);
        tv.setText("本地播放");*/
        actionBar = getSupportActionBar();
        actionBar.setTitle("本地播放");
    /*    actionBar.setCustomView(view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);*/
        actionBar.setDisplayHomeAsUpEnabled(true);// 左上角添加一个返回图标
        //ActionBar和DrawerLayout联动
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mToggle.syncState();//同步状态
        mDrawerLayout.addDrawerListener(mToggle);
    }

    //初始化控件
    private void initView() {
        mTab = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTab.setupWithViewPager(mViewPager);
        Log.i("test","width:  "+mTab.getLayoutParams().width+"  "+"height:  "+mTab.getLayoutParams().height );
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选择时触发
                actionBar.setTitle(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选择时触发
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //选中之后再次点击触发
            }
        });
    }

    /*
    左上角按钮点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (mToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }


    /*
    DrawerLayout 监听事件
     */
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}