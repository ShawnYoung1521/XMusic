package com.tw.music.fragment;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tw.music.R;
import com.xy.media_lib.base.MFragment;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.presenter.VisualizerAblumPresenter;
import com.xy.media_lib.view.MusicView;
import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
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

public class VisualizerSAblumFM extends MFragment<VisualizerAblumPresenter> implements MusicView.VisualizerAblum {

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.visualizer_square_ablum, null);
        initData(v);
        return v;
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }


    private void initData(View v) {
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
        v.findViewById(R.id.spuare_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chengView();
            }
        });
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
    public VisualizerAblumPresenter createPresenter() {
        return new VisualizerAblumPresenter(XApp.getApp());
    }

    @Override
    public void onCurrPlayMedia(LMedia media) {
        Bitmap ablumbitmap = media.getAlbumBitmap();
    }

    @Override
    public void onPlayState(boolean isPlaying, int CurrentPosition, int Duration) {

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
}
