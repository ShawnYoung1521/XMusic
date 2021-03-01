package com.tw.music.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tw.music.R;
import com.tw.music.widget.CircleImageView;
import com.xy.library_lrc.LrcTranscoding;
import com.xy.library_lrc.LrcView;
import com.xy.media_lib.base.MFragment;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.presenter.VisualizerAblumPresenter;
import com.xy.media_lib.view.MusicView;
import java.io.File;
import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;

public class VisualizerCAblumFM extends MFragment<VisualizerAblumPresenter> implements MusicView.VisualizerAblum{

    private CircleImageView allplayview_midd_mediabitmap;
    public static LrcView lrc_view; //歌词
    private View circleRoundView;

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.visualizer_circle_ablum, null);
        initData(v);
        return v;
    }

    private void initData(View v) {
        lrc_view = (LrcView)  v.findViewById(R.id.lrc_view);
        lrc_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lrc_view.setVisibility(View.INVISIBLE);
                circleRoundView.setVisibility(View.VISIBLE);
            }
        });
        circleRoundView = v.findViewById(R.id.circleRoundView);
        circleRoundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleRoundView.setVisibility(View.INVISIBLE);
                lrc_view.setVisibility(View.VISIBLE);
            }
        });
        allplayview_midd_mediabitmap = v.findViewById(R.id.allplayview_midd_mediabitmap);
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
    public VisualizerAblumPresenter createPresenter() {
        return new VisualizerAblumPresenter(XApp.getApp());
    }

    private boolean LrcFileExists = false;
    @Override
    public void onCurrPlayMedia(LMedia media) {
        if (media.getAlbumBitmap() != null){
            allplayview_midd_mediabitmap.setImageBitmap(media.getAlbumBitmap());
        }else {
            allplayview_midd_mediabitmap.setImageDrawable(XApp.getApp().getDrawable(R.drawable.ic_launcher));
        }
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
        if (isPlaying){
            allplayview_midd_mediabitmap.startRotation();
        }else {
            allplayview_midd_mediabitmap.stopRotation();
        }
        if (LrcFileExists)
            lrc_view.updateTime(CurrentPosition);
    }

    @Override
    public void onMediaPlay(MediaPlayer mediaPlayer) {

    }
}
