package com.xy.media_lib.presenter;

import android.content.Context;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.model.MusicModel;
import com.xy.media_lib.view.MusicView;

import java.util.ArrayList;

public class MainPresenter extends BasePresenter<MusicView.MainMusicList, MusicModel> implements MusicView.MainMusicList {

    public MainPresenter(Context context) {
        super(context);
    }

    @Override
    public MusicModel getModel() {
        return MusicModel.getInstant();
    }

    public void onCreate(){
        mModel.onCreate();
        mModel.BindMainMusicListView(this);
        mModel.getMainData();
    }

    public void onLoadData(){
        mModel.onLoadFastData();
    }

    public void onGrantedPermissions(){
        mModel.setGrantedPermissions();
    }

    public void setPermissions(boolean has){
        mModel.setPermissions(has);
    }

    public void onResume() {
        mModel.getFristData();
    }

    public void onDestroy() {
        mModel.unBindMainMusicListView();
    }

    @Override
    public void onCurrPlayMedia(LMedia media) {
        get().onCurrPlayMedia(media);
    }

    @Override
    public void onPlayState(boolean isPlaying) {
        get().onPlayState(isPlaying);
    }

    @Override
    public void onMediaList(ArrayList<LMedia> ALLMusicList) {
        get().onMediaList(ALLMusicList);
    }

    public void next() {
        mModel.next();
    }
    public void prev(){
        mModel.prev();
    }
    public void playPause() {
        mModel.playPause();
    }

}
