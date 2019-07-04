package com.example.lz.myplayer.AppLication;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;

import java.io.File;

/**
 * Created by Administrator on 2019/7/4.
 * 视频缓存
 */

public class App extends Application {
    private HttpProxyCacheServer proxy;

    //配置缓存目录
    public HttpProxyCacheServer.Builder cacheDirectory(File file) {
        return null;
    }

    //配置缓存文件命名规则
    public HttpProxyCacheServer.Builder fileNameGenerator(FileNameGenerator fileNameGenerator) {
        return null;
    }

    //配置缓存文件大小
    public HttpProxyCacheServer.Builder maxCacheSize(long maxSize) {
        return null;
    }

    //配置缓存文件数量
    public HttpProxyCacheServer.Builder maxCacheFilesCount(int count) {
        return null;
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

}
