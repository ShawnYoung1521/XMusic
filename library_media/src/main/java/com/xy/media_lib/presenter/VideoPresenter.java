package com.xy.media_lib.presenter;

import android.content.Context;
import com.xy.media_lib.model.VideoModel;
import com.xy.media_lib.view.VideoView;

public class VideoPresenter extends BasePresenter<VideoView, VideoModel> implements VideoView{

    public VideoPresenter(Context context) {
        super(context);
    }

    @Override
    public VideoModel getModel() {
        return VideoModel.getInstant();
    }

    public void onCreate(){
        mModel.onCreate();
        addView();
    }

    private void addView(){
        mModel.setViews(this);
    }

    public void onRestart() {
        mModel.onRestart();
    }

    public void onResume() {
        mModel.onResume();
    }

    public void onPause() {
        mModel.onPause();
    }

    public void onStop() {
        mModel.onStop();
    }

    public void onDestroy() {
        mModel.onDestroy(this);
    }

    public void openTime() {
        mModel.openTime();
    }

    @Override
    public void onTime(int position) {
        get().onTime(position);
    }
}
