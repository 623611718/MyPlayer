package com.example.lz.myplayer.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;


import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.TimedText;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;


import com.example.lz.myplayer.R;
import com.example.lz.myplayer.Utils.Utils;

import java.io.IOException;

public class PlayerActivity extends Activity {
    protected static final int PROGRESS = 1;
    private String path;
    private SurfaceView surfaceview;
    private TextView tv_begin;
    private SeekBar sb_main;
    private TextView tv_end;
    private Button btn_play;
    private ProgressBar pb_main;
    private MediaPlayer mediaPlayer;
    private android.view.SurfaceHolder mSurfaceHolder;
    private int currenposition;
    private int duration;
    private Utils utils;
    private int max;

    private int pro =0;
    private int errorcode = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    if(mediaPlayer!=null){
                        // 1.得到当前的视频播放进度
                        currenposition = mediaPlayer.getCurrentPosition();
                        // 2.Seekbar.setprogress(当前进度);
                        sb_main.setProgress(currenposition);

                        tv_begin.setText(utils.formatTime(currenposition));

                        // 3.每秒更新一次
                        removeMessages(PROGRESS);
                        sendEmptyMessageDelayed(PROGRESS, 1000);
                        Log.i("url","PROGRESS  "+currenposition);
                        break;
                    }
            }
        }
    };
    //对快进 和快退进行异步处理
    class MyAsyncTask extends AsyncTask<String,Void,String> {

        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //此处将progressBar设置为可见.

        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected String doInBackground(String... params) {
            Log.i("dsa","doInBackground");
            mediaPlayer.seekTo(pro);               //改变播放进度
            Log.i("dsa","doInBackground after");
            return null;
        }

        //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("dsa","onPostExecute");
            //   mediaPlayer.seekTo(pro);
            // 1.得到当前的视频播放进度
            currenposition = mediaPlayer.getCurrentPosition();
            // 2.Seekbar.setprogress(当前进度);
            sb_main.setProgress(currenposition);
            // mediaPlayer.start();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_player);
        Intent myintent = getIntent();
        path = myintent.getStringExtra("url");
        utils = new Utils();
        initdata();
        // 初始化Mediaplayer

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

    }

    private void initdata() {
        surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        tv_begin = (TextView) findViewById(R.id.tv_begin);
        tv_end = (TextView) findViewById(R.id.tv_end);
        sb_main = (SeekBar) findViewById(R.id.sb_main);
        btn_play = (Button) findViewById(R.id.btn_play);
        pb_main = (ProgressBar) findViewById(R.id.pb_main);
        setSurfaceview();
    }

    private void setSurfaceview() {
        // 设置surfaceHolder
        mSurfaceHolder =  surfaceview.getHolder();
        // 设置Holder类型,该类型表示surfaceView自己不管理缓存区
        //mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置surface回调
        (mSurfaceHolder).addCallback(new SurfaceCallback());
    }

    class SurfaceCallback implements  Callback {

        @Override
        public void surfaceCreated(android.view.SurfaceHolder holder) {
            // 当surfaceview被创建的时候播放
            Log.i("tag","surfaceCreated  ");
            play();
        }

        @Override
        public void surfaceChanged(android.view.SurfaceHolder holder, int format, int width,
                                   int height) {
            Log.i("tag","surfaceChanged  ");

        }

        @Override
        public void surfaceDestroyed(android.view.SurfaceHolder holder) {
            Log.i("tag","surfaceDestroyed  ");

        }

    }


    // 初次播放视屏的时候调用
    public void play() {
        // 重置mediaPaly,建议在初始化mediaplay立即调用
        mediaPlayer.reset();
        // 设置声音效果
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置播放完成的监听
        mediaPlayer.setOnCompletionListener(new CompletionListener());
        // 设置媒体加载完成后的监听
        mediaPlayer.setOnPreparedListener(new PreparedListener());
        // 设置错误监听回调函数
        mediaPlayer.setOnErrorListener(new ErrorListener());
        // 设置缓存变化监听
        //mediaPlayer.setOnBufferingUpdateListener(new BufferingUpdateListener());
        // 设置拖动监听事件
        sb_main.setOnSeekBarChangeListener(new SeekBarChangeListener());

        Log.i("tag","path  "+path);
        try {

            mediaPlayer.setDataSource(path);
            Log.i("tag","url  "+path);
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "加载视频错误！", 0).show();
        }

    }

    // 设置播放完成的监听
    class CompletionListener implements OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            //PlayerActivity.this.registerReceiver(receiver,filter);
            if (mediaPlayer != null) {
                //mediaPlayer.release();
                //mediaPlayer=null;
                mediaPlayer.start();
                mediaPlayer.pause();
                btn_play.setBackgroundResource(R.drawable.btn_start_selector);
                btn_play.setVisibility(View.VISIBLE);
                Log.i("url", "CompletionListener  ");
            }
        }
    }
    class PreparedListener implements OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            {
                // TODO Auto-generated method stub
                // 当视频加载完毕以后，隐藏加载进度条
                pb_main.setVisibility(View.GONE);
                btn_play.setVisibility(View.GONE);
                // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
                sb_main.setMax(mediaPlayer.getDuration());
                // 设置播放时间
                max = mediaPlayer.getDuration();
                tv_end.setText(utils.formatTime(max));

                // 播放视频
                mediaPlayer.start();

                //设置循环播放
                //mediaPlayer.setLooping(true);
                // 设置显示到屏幕
                mediaPlayer.setDisplay(mSurfaceHolder);
                // 设置surfaceView保持在屏幕上
                mediaPlayer.setScreenOnWhilePlaying(true);
                mSurfaceHolder.setKeepScreenOn(true);
                // 发消息
                handler.sendEmptyMessage(PROGRESS);
                Log.i("url","PreparedListener  ");
            }
        }
    }
    class ErrorListener implements OnErrorListener {
        @Override
        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            Log.i("url", "ErrorListener  " + arg1 + "  " + arg2);
            errorcode = arg1;
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            return false;
        }
    }





    // 给seekbar设置监听
    class SeekBarChangeListener implements OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (progress > 0) {
                if (fromUser) {
                    Log.i("dsa","onProgressChanged");
                    pro = progress;
                    PlayerActivity.MyAsyncTask asynctask= null;
                    asynctask = new MyAsyncTask();
                    asynctask.execute();

                }
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.i("dsa","onProgressChanged");
            int  progress = seekBar.getProgress();
            pro = progress;
            MyAsyncTask asynctask= null;
            asynctask = new MyAsyncTask();
            asynctask.execute();

        }

    }

    public void playLocalMedia(View v) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {
        Log.i("url","playLocalMedia(View v)  ");

        isPlayingVideo();
    }

    public void isPlayingVideo() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.i("localplayer", "暂停");
            btn_play.setBackgroundResource(R.drawable.btn_start_selector);
            btn_play.setVisibility(View.VISIBLE);
            Log.i("TAG", "STOP");

        } else {
            Log.i("TAG", "PLAY");
            mediaPlayer.start();
            btn_play.setBackgroundResource(R.drawable.btn_pause_selector);
            // 1秒过后自动消失
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_play.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

    /**
     * 给back键添加监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.i("dsa","keyCode  "+keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mediaPlayer !=null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
            finish();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mediaPlayer.isPlaying()) {
                if (mediaPlayer.getCurrentPosition() > 0) {
                    int pos = mediaPlayer.getCurrentPosition();
                    if (pos < 5000) {
                        mediaPlayer.seekTo(pos);
                    }else {
                        // 毫秒 5秒
                        pos -= 5000;
                        mediaPlayer.seekTo(pos);
                        mediaPlayer.start();
                        btn_play.setBackgroundResource(R.drawable.btn_pause_selector);
                        // 1秒过后自动消失
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn_play.setVisibility(View.GONE);
                            }
                        }, 1000);
                        Log.i("TAG", "left！");
                    }
                }
            } else {
                Log.i("TAG", "按左键了！");
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (mediaPlayer.isPlaying()) {
                if (mediaPlayer.getCurrentPosition() > 0) {
                    int pos = mediaPlayer.getCurrentPosition();
                    int druation = mediaPlayer.getDuration();
                    Log.i("TAG","pos"+pos);
                    Log.i("TAG","druation"+druation);
                    if (pos < druation) {
                        // 5秒
                        pos += 5000;
                        mediaPlayer.seekTo(pos);
                        Log.i("TAG","pos"+pos);
                        Log.i("TAG","druation"+druation);
                    } else {
                        mediaPlayer.seekTo(druation);
                    }
                }
            } else {
                Log.i("TAG", "按右键了！");
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if(errorcode !=100){
                isPlayingVideo();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void display(View v) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {
        playLocalMedia(v);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer=null;
        }
        super.onDestroy();
    }
}

