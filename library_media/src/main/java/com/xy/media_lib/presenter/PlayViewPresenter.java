package com.xy.media_lib.presenter;

import android.content.Context;
import android.media.MediaPlayer;

import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.model.MusicModel;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;

public class PlayViewPresenter extends BasePresenter<MusicView.PlayView, MusicModel> implements MusicView.PlayView{

    public PlayViewPresenter(Context context) {
        super(context);
    }
    @Override
    public MusicModel getModel() {
        return MusicModel.getInstant();
    }

    public void onCreate(MusicView.PlayView context){
        mModel.BindPlayView(context);
    }

    public void onResume() {
        mModel.getPlayViewDate();
    }

    public void onDestroy() {
        mModel.unBindPlayView();
    }

    @Override
    public void onPlayMode(int mode) {
        get().onPlayMode(mode);
    }

    @Override
    public void onPlayState(boolean isPlaying, int CurrentPosition, int Duration) {
        get().onPlayState(isPlaying,CurrentPosition,Duration);
    }

    @Override
    public void onCurrPlayMedia(LMedia media) {
        get().onCurrPlayMedia(media);
    }

    @Override
    public void onPlayList(ArrayList<LMedia> MusicList) {
        get().onPlayList(MusicList);
    }

    @Override
    public void onMediaPlay(MediaPlayer mediaPlayer) {
        get().onMediaPlay(mediaPlayer);
    }

    public void setClickInPlayViewList(int tag,int pos){
        mModel.setClickInPlayViewList(tag,pos);
    }

    public void setClearCurrPlayMediaList(){
        mModel.setClearCurrPlayMediaList();
    }
    public void next() {
        mModel.next();
    }
    public void prev() {
        mModel.prev();
    }
    public void stop() {
        mModel.stop();
    }
    public void playPause() {
        mModel.playPause();
    }
    public void seekTo(int position) {
        mModel.seekTo(position);
    }
    public void checkPlayMode(){
        mModel.checkPlayMode();
    }
}
