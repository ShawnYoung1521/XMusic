package com.xy.media_lib.presenter;

import android.content.Context;
import android.media.MediaPlayer;

import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.model.MusicModel;
import com.xy.media_lib.view.MusicView;

public class LrcPresenter extends BasePresenter<MusicView.LrcAblum, MusicModel> implements MusicView.LrcAblum {

    public LrcPresenter(Context context) {
        super(context);
    }

    @Override
    public MusicModel getModel() {
        return MusicModel.getInstant();
    }

    public void onResume() {
        mModel.BindVisualizerAblumView(this);
        mModel.getVisualizerAblumData();
    }

    public void onDestroy() {
        mModel.unBindVisualizerAblumView();
    }

    @Override
    public void onCurrPlayMedia(LMedia media) {
        get().onCurrPlayMedia(media);
    }

    @Override
    public void onPlayState(boolean isPlaying, int CurrentPosition, int Duration) {
        get().onPlayState(isPlaying,CurrentPosition,Duration);
    }

    public void seekTo(int pos) {
        mModel.seekTo(pos);
    }
}
