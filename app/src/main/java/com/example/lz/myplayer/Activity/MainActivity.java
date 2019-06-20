package com.example.lz.myplayer.Activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lz.myplayer.Adapter.MyAdapter;
import com.example.lz.myplayer.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    private static final String ROOT_PATH = "/storage/emulated/0";

    private ArrayList<String> names = null;

    private ArrayList<String> paths = null;
    private String path1 = "/storage/emulated/0"; // 当前显示的路径
    private View view;
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initChmod("chmod 666 /storage/emulated/0");
        // 显示文件列表
        showFileDir(ROOT_PATH);
        Log.i("dsa", "onCreate activity");
    }

    private void initChmod(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (Exception e) {
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }

    private void showFileDir(String path) {
        Log.i("dsa", "path gen " + path);

        names = new ArrayList<String>();
        paths = new ArrayList<String>();
        File file = new File(path);
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("chmod 777 " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }

      /*  try {
            String command = "chmod 777 " + file.getAbsolutePath();
            Log.i("dsa", "command = " + command);
            Runtime runtime = Runtime.getRuntime();

            Process proc = runtime.exec(command);
        } catch (IOException e) {
            Log.i("dsa", "chmod fail!!!!");
            e.printStackTrace();
        }*/
        path1 = file.getPath();
        Log.i("dsa", "showFileDir  path1  " + path1);
        File[] files = file.listFiles();
        Log.i("dsa", "files长度: " + file.getPath());
        // 如果当前目录不是根目录
        if (!ROOT_PATH.equals(path)) {
            names.add("@1");
            paths.add(ROOT_PATH);
            names.add("@2");
            paths.add(file.getParent());
        }
        // 添加所有文件
        for (File f : files) {
            // Log.i("dsa","f.getName()  "+f.getName());
            // Log.i("dsa","f.getPath()  "+f.getPath());

            int start = f.getName().lastIndexOf(".") + 1;
            int end = f.getName().length();
            String name = f.getName().substring(start, end);
           /* if (f.isDirectory()
                    || name.equalsIgnoreCase("3g2") || name.equalsIgnoreCase("3gp")
                    || name.equalsIgnoreCase("aac") || name.equalsIgnoreCase("ape")
                    || name.equalsIgnoreCase("asf") || name.equalsIgnoreCase("avi")
                    || name.equalsIgnoreCase("avs") || name.equalsIgnoreCase("f4v")
                    || name.equalsIgnoreCase("flac") || name.equalsIgnoreCase("flv")
                    || name.equalsIgnoreCase("m3u8") || name.equalsIgnoreCase("m1v")
                    || name.equalsIgnoreCase("m2t") || name.equalsIgnoreCase("m2ts")
                    || name.equalsIgnoreCase("m2v") || name.equalsIgnoreCase("m4a")
                    || name.equalsIgnoreCase("m4v") || name.equalsIgnoreCase("mkv")
                    || name.equalsIgnoreCase("mov") || name.equalsIgnoreCase("mp3")
                    || name.equalsIgnoreCase("mp4") || name.equalsIgnoreCase("mp4g")
                    || name.equalsIgnoreCase("ts") || name.equalsIgnoreCase("mpeg")) {*/
                names.add(f.getName());
                paths.add(f.getPath());
         //   }
            /*
             * names.add(f.getName()); paths.add(f.getPath());
			 */
        }
        this.setListAdapter(new MyAdapter(this, names, paths));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        path1 = paths.get(position);
        File file = new File(path1);
        // 文件存在并可读
        if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                // 显示子目录及文件
                showFileDir(path1);
                Log.i("dsa", "path1  " + path1);
            } else {
                // Log.i("dsa","file "+file);
                // Log.i("dsa","reader "+file.getPath());
                file.getName();
                Log.i("play","name:  "+file.getName());
                openFile(file.getPath(),file.getName());
            }
        }
        // 没有权限
        else {
            Resources res = getResources();
            new AlertDialog.Builder(this).setTitle("Message")
                    .setMessage("没有权限")
                    .setPositiveButton("OK", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
        super.onListItemClick(l, v, position, id);
    }

    // 打开文件
    private void openFile(String path,String name) {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.putExtra("url", path);
        intent.putExtra("name",name);
        SharedPreferences.Editor editor = getSharedPreferences("PlayName",MODE_PRIVATE).edit();
        editor.putString("name",name);
        editor.apply();


        Log.i("dsa", "path111  " + path);
        startActivity(intent);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.setAction(android.content.Intent.ACTION_VIEW);
        // String type = getMIMEType(file);
        // intent.setDataAndType(Uri.fromFile(file), type);
        // startActivity(intent);
    }

    // 获取文件mimetype
    private String getMIMEType(File file) {
        String type = "";
        String name = file.getName();
        // 文件扩展名
        String end = name.substring(name.lastIndexOf(".") + 1, name.length())
                .toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("mp4") || end.equals("3gp")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")
                || end.equals("bmp") || end.equals("gif")) {
            type = "image";
        } else {
            // 如果无法直接打开，跳出列表由用户选择
            type = "*";
        }
        type += "/*";
        return type;
    }

    private void displayToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.i("dsa", "keyCode  " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (path1.equals("/")) {   //path1  当前路径
                finish(); // 如果是根目录 则退出
            } else { // 如果不是根目录,返回上一级
                File f = new File(path1);
                boolean d = f.isDirectory();
                Log.i("dsa", "  d  " + d);
                String path2 = path1.substring(0, path1.lastIndexOf("/") + 1);   //path2  返回上一级的路径
                // 播放文件之后path1 路径 xxx/xxx.mp4 继续分割两次
                if (d == false) {
                    path2 = path2.substring(0, path2.lastIndexOf("/"));
                    path2 = path2.substring(0, path2.lastIndexOf("/"));
                }
                if (path2.equals(" ")) {   //如果播放了根目录 / 下的文件  path2重新赋值
                    path2 = "/";
                }
                Log.i("dsa", "back path2  " + path2);
                File file = new File(path2);
                // 文件存在并可读
                if (file.exists() && file.canRead()) {
                    if (file.isDirectory()) {
                        // 显示子目录及文件
                        showFileDir(path2);
                    } else {

                    }
                }
                // 没有权限
                else {
                    Resources res = getResources();
                    new AlertDialog.Builder(this).setTitle("Message")
                            .setMessage("没有权限")
                            .setPositiveButton("OK", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }).show();
                }
            }
            Log.i("dsa", "BACK path " + path1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


