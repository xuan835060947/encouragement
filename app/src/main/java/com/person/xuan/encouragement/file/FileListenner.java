package com.person.xuan.encouragement.file;

import android.os.FileObserver;
import android.util.Log;

/**
 * Created by chenxiaoxuan1 on 15/12/21.
 */
public class FileListenner extends FileObserver {

    /**
     * SD卡中的目录创建监听器。
     *
     * @author mayingcai
     */
    private FileChangeListener mFileChangeListener;

    public FileListenner(String path, FileChangeListener fileChangeListener) {
        super(path);
        mFileChangeListener = fileChangeListener;
    }

    @Override
    public void onEvent(int event, String path) {
        switch (event) {
            case FileObserver.ALL_EVENTS:
                Log.e("all", "path:" + path);
                break;
            case FileObserver.CREATE:
                Log.e("Create", "path:" + path);
                break;
            case FileObserver.MODIFY:
                Log.e("MODIFY", "path:" + path);
                mFileChangeListener.onModify();
                break;
        }
    }

    public interface FileChangeListener {
        void onModify();
    }
}
