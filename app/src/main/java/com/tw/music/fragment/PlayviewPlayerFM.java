package com.tw.music.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.tw.music.PlayViewActivity;
import com.tw.music.R;
import com.tw.music.adapter.PlayListMusicAdapter;
import com.tw.music.contract.MusicContract;
import com.tw.music.listener.onFragmentListener;
import com.tw.music.utils.Utils;
import com.xy.media_lib.base.MV4Fragment;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.presenter.PlayViewPresenter;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;
import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;
import cn.xy.library.util.image.XImage;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.screen.XScreen;
import cn.xy.library.util.spannable.XSpanned;
import cn.xy.spectrum.AppConstant;
import cn.xy.spectrum.view.AiVoiceView;
import cn.xy.spectrum.view.BesselView;
import cn.xy.spectrum.view.CircleRoundView;
import cn.xy.spectrum.view.ColumnarView;
import cn.xy.spectrum.view.GridPointView;
import cn.xy.spectrum.view.HexagramView;
import cn.xy.spectrum.view.SiriView;
import cn.xy.spectrum.view.SpeedometerView;
import cn.xy.spectrum.view.WaveformView;

@SuppressLint("ValidFragment")
public class PlayviewPlayerFM extends MV4Fragment<PlayViewPresenter> implements MusicView.PlayView,View.OnClickListener{
    private onFragmentListener fragmentListener;
    private ImageView allplayview_bottom_pp;
    private ImageView allplayview_bottom_mode;
    private SeekBar allplayview_bottom_progressbar;
    private TextView allplayview_bottom_currentposition;
    private TextView allplayview_bottom_duration;
    private TextView allplayview_top_name;
    private TextView allplayview_top_artist;
    private View playlistview;
    private TextView playlist_title;
    private ImageView playlist_delete;
    private View playlist_layout;
    private ListView playlist_listview;
    private PlayListMusicAdapter playListMusicAdapter;
    private boolean fromUser;
    private View playview_view_layout;
    public PlayviewPlayerFM(PlayViewActivity playViewActivity) {
        fragmentListener = playViewActivity;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mPresenter.onCreate(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playview_player_layout, null);
        initData(v);
        return v;
    }

    private void initData(View v) {

        playview_view_layout = v.findViewById(R.id.playview_view_layout);
        allplayview_bottom_pp = v.findViewById(R.id.allplayview_bottom_pp);
        allplayview_bottom_mode = v.findViewById(R.id.allplayview_bottom_mode);
        allplayview_bottom_progressbar = v.findViewById(R.id.allplayview_bottom_progressbar);
        allplayview_bottom_progressbar.setOnSeekBarChangeListener(seekbarlistener);
        allplayview_bottom_currentposition = v.findViewById(R.id.allplayview_bottom_currentposition);
        allplayview_bottom_duration = v.findViewById(R.id.allplayview_bottom_duration);
        allplayview_top_artist = v.findViewById(R.id.allplayview_top_artist);
        allplayview_top_name = v.findViewById(R.id.allplayview_top_name);
        allplayview_top_artist.setSelected(true);
        allplayview_top_name.setSelected(true);
        playlistview = LayoutInflater.from(getActivity()).inflate(R.layout.playlist_pop_layout, null, false);
        playlist_title = (TextView) playlistview.findViewById(R.id.playlist_title);
        playlist_delete = (ImageView) playlistview.findViewById(R.id.playlist_delete);
        playlist_layout = playlistview.findViewById(R.id.playlist_layout);
        XImage.setRoundedCorners(playlist_layout,XConvert.dp2px(XScreen.isLandscape()?5:10));
        playlist_listview = (ListView) playlistview.findViewById(R.id.playlist_listview);
        playlist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.setClickInPlayViewList(MusicContract.PlayListTag,i);
            }
        });
        playListMusicAdapter = new PlayListMusicAdapter();
        playlist_listview.setAdapter(playListMusicAdapter);
        columnar = v.findViewById(R.id.columnar);
        waveform = v.findViewById(R.id.waveform);
        aiVoice = v.findViewById(R.id.aiVoice);
        gridPointView = v.findViewById(R.id.gridPointView);
        speedometerView = v.findViewById(R.id.speedometerView);
        circleRoundView = v.findViewById(R.id.circleRoundView);
        besselView = v.findViewById(R.id.besselView);
        hexagramView = v.findViewById(R.id.hexagramView);
        siriView = v.findViewById(R.id.siriView);
        car_effect_driving = v.findViewById(R.id.car_effect_driving);
        v.findViewById(R.id.visualizer_view).setOnClickListener(this);
        playlist_delete.setOnClickListener(this);
        v.findViewById(R.id.allplayview_close).setOnClickListener(this);
        v.findViewById(R.id.allplayview_bottom_list).setOnClickListener(this);
        v.findViewById(R.id.allplayview_bottom_mode).setOnClickListener(this);
        v.findViewById(R.id.allplayview_bottom_prev).setOnClickListener(this);
        v.findViewById(R.id.allplayview_bottom_pp).setOnClickListener(this);
        v.findViewById(R.id.allplayview_bottom_next).setOnClickListener(this);
    }

    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
        normalDialog.setMessage("确定要清除播放列表？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.setClearCurrPlayMediaList();
                        playListMusicAdapter.notifyDataSetChanged(new ArrayList<LMedia>(),new LMedia());
                        mPresenter.stop();
                        updatePlayListTitle();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        normalDialog.show();
    }
    private SeekBar.OnSeekBarChangeListener seekbarlistener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (fromUser)
                mPresenter.seekTo(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean User) {
            fromUser = User;
        }
    };

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
    public PlayViewPresenter createPresenter() {
        return new PlayViewPresenter(XApp.getApp());
    }

    @Override
    public void onPlayMode(int mode) {
        allplayview_bottom_mode.getDrawable().setLevel(mode);
    }

    @Override
    public void onPlayState(boolean isPlaying, int currenttime, int totaltime) {
        allplayview_bottom_pp.getDrawable().setLevel(isPlaying?1:0);
        allplayview_bottom_progressbar.setMax(totaltime);
        allplayview_bottom_progressbar.setProgress(currenttime);
        allplayview_bottom_duration.setText(Utils.timelongToString(totaltime));
        allplayview_bottom_currentposition.setText(Utils.timelongToString(currenttime));
    }

    private void popRdsView(View view) {
        PopupWindow popRdsWindow = new PopupWindow(playlistview, ViewGroup.LayoutParams.MATCH_PARENT,XConvert.dp2px(300));
        popRdsWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popRdsWindow.setOutsideTouchable(true);
        popRdsWindow.setAnimationStyle(R.style.pop_window_anim);
        popRdsWindow.showAsDropDown(view, 0, 0);
    }
    @Override
    public void onCurrPlayMedia(LMedia media) {
        if (media.name != null){
            XSpanned.with(allplayview_top_name)
                    .append(media.getName())
                    .setFontSize(XConvert.dp2px(20))
                    .setTypeface(Typeface.DEFAULT_BOLD)
                    .create();
        }else {
            allplayview_top_name.setText("");
            allplayview_top_artist.setText("");
            playview_view_layout.setBackgroundResource(R.drawable.ic_launcher);
        }
        allplayview_top_artist.setText(media.getArtist());
        playListMusicAdapter.notifyDataSetChanged(PlayList,media);
//        if (media.getAlbumBitmap() != null){
//            playview_view_layout.setBackground(XImage.bitmap2Drawable(XImage.stackBlur(media.getAlbumBitmap(),100)));
//        }else {
//            playview_view_layout.setBackgroundColor(getResources().getColor(R.color.black));
//        }
    }

    ArrayList<LMedia> PlayList;
    @Override
    public void onPlayList(ArrayList<LMedia> MusicList) {
        PlayList = MusicList;
        updatePlayListTitle();
    }

    public void updatePlayListTitle(){
        XSpanned.with(playlist_title)
                .append("当前播放")
                .setFontSize(XConvert.dp2px(13))
                .setTypeface(Typeface.DEFAULT_BOLD)
                .append("("+String.valueOf(PlayList.size())+"首)")
                .setFontSize(XConvert.dp2px(10))
                .create();
    }

    private Visualizer visualizer;
    public MediaPlayer player;

    @Override
    public void onMediaPlay(MediaPlayer mPlayer) {
        player = mPlayer;
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        player.setLooping(true);
        player.start();
        if(visualizer != null){
            visualizer = null;
        }
        int mediaPlayerId = player.getAudioSessionId();
        if (visualizer == null) {
            visualizer = new Visualizer(mediaPlayerId);
            visualizer.setEnabled(false);
        } else {
            visualizer.release();
        }
        int captureSize = Visualizer.getCaptureSizeRange()[0];
        int captureRate = Visualizer.getMaxCaptureRate() * 3 / 4;

        try{
            visualizer.setCaptureSize(captureSize);
            visualizer.setDataCaptureListener(dataCaptureListener, captureRate, true, true);
            visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
            visualizer.setEnabled(true);
        }catch (Exception E){
            XLog.i(E.getMessage());
        }
        circleRoundView.setEnable(true);
    }

    private Visualizer.OnDataCaptureListener dataCaptureListener = new Visualizer.OnDataCaptureListener() {

        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate) {
            if (!AppConstant.isFFT) {
                dispose(fft);
            }
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate) {
            if (AppConstant.isFFT) {
                float energy = (float)Math.hypot(fft[0], fft[0 + 1]);
                if (energy>0 && energy <=20){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_1));
                }else if (energy > 20 && energy <= 40){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_1));
                }else if (energy > 40 && energy <= 60){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_1));
                }else if (energy > 60 && energy <= 80){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_2));
                }else if (energy > 80 && energy <= 100){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_2));
                }else if (energy > 100 && energy <= 120){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_2));
                }else if (energy > 120 && energy <= 140){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_3));
                }else if (energy > 140 && energy <= 160){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_3));
                }else if (energy > 160 && energy <= 180){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_3));
                }else if (energy > 180 && energy <= 200){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_3));
                }else if (energy > 200 && energy <= 220){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_3));
                }else if (energy > 220 && energy <= 240){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_3));
                }else if (energy > 240 && energy <= 255){
                    car_effect_driving.setImageDrawable(mContext.getDrawable(R.drawable.car_effect_driving_3));
                }
                dispose(fft);
            }
        }
    };

    private long time;
    private void dispose(byte[] data) {
        if (System.currentTimeMillis() - time < 2000 / AppConstant.FPS) {
            return;
        }
        byte[] newData = new byte[AppConstant.LUMP_COUNT];
        byte abs;
        for (int i = 0; i < AppConstant.LUMP_COUNT; i++) {
            abs = (byte) Math.abs(data[i]);
            newData[i] = abs < 0 ? AppConstant.LUMP_COUNT : abs;
        }
        columnar.setWaveData(newData);
        waveform.setWaveData(newData);
//        rotatingCircle.setWaveData(newData);
        aiVoice.setWaveData(newData);
        gridPointView.setWaveData(newData);
        speedometerView.setWaveData(newData);
        circleRoundView.setWaveData(newData);
        besselView.setWaveData(newData);
        hexagramView.setWaveData(newData);
//        slipBallView.setWaveData(newData);
        siriView.setWaveData(newData);
//        horizontalEnergyView.setWaveData(newData);
        time = System.currentTimeMillis();
    }

    int i = 0;
    private ColumnarView columnar;
    private WaveformView waveform;
    private AiVoiceView aiVoice;
    private GridPointView gridPointView;
    private SpeedometerView speedometerView;
    private CircleRoundView circleRoundView;
    private BesselView besselView;
    private HexagramView hexagramView;
    private SiriView siriView;
    private ImageView car_effect_driving;
    public void chengView(){
        i ++;
        if (i>8){
            i = 0;
        }
        if ((i)==0){
            siriView.setVisibility(View.GONE);
            circleRoundView.setVisibility(View.VISIBLE);
            circleRoundView.setEnable(true);
        }else if ((i)==1){
            circleRoundView.setVisibility(View.GONE);
            columnar.setVisibility(View.VISIBLE);
            columnar.setEnable(true);
        }else if ((i)==2){
            columnar.setVisibility(View.GONE);
            waveform.setVisibility(View.VISIBLE);
            waveform.setEnable(true);
        }else if ((i)==3){
            waveform.setVisibility(View.GONE);
            aiVoice.setVisibility(View.VISIBLE);
            aiVoice.setEnable(true);
        }else if ((i)==4){
            aiVoice.setVisibility(View.GONE);
            gridPointView.setVisibility(View.VISIBLE);
            gridPointView.setEnable(true);
        }else if ((i)==5){
            gridPointView.setVisibility(View.GONE);
            speedometerView.setVisibility(View.VISIBLE);
            speedometerView.setEnable(true);
        }else if ((i)==6){
            speedometerView.setVisibility(View.GONE);
            besselView.setVisibility(View.VISIBLE);
            besselView.setEnable(true);
        }else if ((i)==7){
            besselView.setVisibility(View.GONE);
            hexagramView.setVisibility(View.VISIBLE);
            hexagramView.setEnable(true);
        }else if ((i)==8){
            hexagramView.setVisibility(View.GONE);
            siriView.setVisibility(View.VISIBLE);
            siriView.setEnable(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.allplayview_bottom_list:
                popRdsView(view);
                break;
            case R.id.allplayview_bottom_mode:
                mPresenter.checkPlayMode();
                break;
            case R.id.allplayview_bottom_prev:
                mPresenter.prev();
                break;
            case R.id.allplayview_bottom_pp:
                mPresenter.playPause();
                break;
            case R.id.allplayview_bottom_next:
                mPresenter.next();
                break;
            case R.id.allplayview_close:
                fragmentListener.toSecond();
                break;
            case R.id.playlist_delete:
                showNormalDialog();
                break;
            case R.id.visualizer_view:
                chengView();
                break;
        }
    }
}
