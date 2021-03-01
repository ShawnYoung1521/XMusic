package com.xy.media_lib.model;

import android.os.Handler;
import android.os.Message;

import com.xy.media_lib.utils.LoadMediaFile;
import com.xy.media_lib.view.VideoView;
import java.util.ArrayList;
import cn.xy.library.util.log.XLog;

public class VideoModel extends BaseModel {

    private static ArrayList<VideoView> mViews=new ArrayList<VideoView>();
    private static VideoModel mModel=null;

    public void setViews(VideoView mPV){
        mViews.add(mPV);
    }
    public static VideoModel getInstant(){
        if (mModel == null){
            mModel = new VideoModel();
        }
        return mModel;
    }

    public void onCreate(){
        handler = new mHandler();
        LoadMediaFile.getVideoList();
        XLog.i();
    }

    public void onRestart() {
        XLog.i();
    }

    public void onResume() {
        handler.sendEmptyMessage(0x01);
        XLog.i();
    }

    public void onPause() {
        XLog.i();
    }

    public void onStop() {
        XLog.i();
    }

    public void onDestroy(VideoView mPV) {
        mViews.remove(mPV);
        if (mViews.size()==0){
            clear();
        }
    }

    public final void clear() {
        handler.removeMessages(0x01);
        handler = null;
        mModel = null;
    }

    public void openTime(){

    }

    private mHandler handler;
    private int i = 0;
    private class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(0x01);
            handler.sendEmptyMessageDelayed(0x01,1000);
            ++i;
            for (VideoView mView : mViews) {
            }
        }
    }
}
