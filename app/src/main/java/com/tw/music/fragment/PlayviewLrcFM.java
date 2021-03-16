package com.tw.music.fragment;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tw.music.R;
import com.xy.library_lrc.LrcTranscoding;
import com.xy.library_lrc.LrcView;
import com.xy.media_lib.base.MV4Fragment;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.presenter.LrcPresenter;
import com.xy.media_lib.view.MusicView;

import java.io.File;

import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;

@SuppressLint("ValidFragment")
public class PlayviewLrcFM extends MV4Fragment<LrcPresenter> implements MusicView.LrcAblum {

    public static LrcView lrc_view; //歌词
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
    }
    public PlayviewLrcFM() {
        // doesn't do anything special
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playview_lrc_layout, null);
        initData(v);
        return v;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        XLog.i(hidden);
    }

    private void initData(View v) {
        lrc_view = (LrcView)  v.findViewById(R.id.lrc_view);
    }

    private void loadLrc(String lrcPath){
        if (new File(lrcPath).exists()) {
            lrc_view.loadLrc(LrcTranscoding.converfile(lrcPath));
            lrc_view.setOnPlayClickListener(new LrcView.OnPlayClickListener() {
                @Override
                public boolean onPlayClick(long time) {
                    mPresenter.seekTo((int) time);
                    return true;
                }
            });
        }
    }
    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public LrcPresenter createPresenter() {
        return new LrcPresenter(XApp.getApp());
    }

    private boolean LrcFileExists = false;
    @Override
    public void onCurrPlayMedia(LMedia media) {
        if (!media.getUrl().equals("")){
            String Lrcpath = media.getUrl().substring(0, media.getUrl().lastIndexOf("."))+".lrc";;
            loadLrc(Lrcpath);
            LrcFileExists = true;
        }else {
            LrcFileExists = false;
        }
    }

    @Override
    public void onPlayState(boolean isPlaying, int CurrentPosition, int Duration) {
        if (LrcFileExists)
            lrc_view.updateTime(CurrentPosition);
    }
}
