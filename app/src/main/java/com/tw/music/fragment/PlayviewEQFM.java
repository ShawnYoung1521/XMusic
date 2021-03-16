package com.tw.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.tw.music.R;
import com.tw.music.utils.EQManage;
import com.tw.music.widget.DashboardScaleView;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.sharedpreferences.XSPUtils;

public class PlayviewEQFM  extends Fragment implements View.OnClickListener {

    private static final String AUDIO_SESSION_ID = "com.intent.action.SEND_AUDIO_SESSION_ID";
    @Override
    public void onResume() {
        super.onResume();
        int mediaPlayerId = XSPUtils.getInstance().getInt(AUDIO_SESSION_ID);
        XLog.i(mediaPlayerId);
        mEQManage.InitEQ(mediaPlayerId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static String TAG="SoundPoolFragment";
    private DashboardScaleView mDasboardSound1;
    private DashboardScaleView mDasboardSound2;
    private DashboardScaleView mDasboardSound3;
    private DashboardScaleView mDasboardSound4;
    private DashboardScaleView mDasboardSound5;
    private DashboardScaleView mDasboardSound6;
    private Button mBtnDefaut;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sound_pool_fragment, null, false);
        mEQManage = EQManage.getInstant(getActivity());
        initView(v);
        return v;
    }

    public EQManage mEQManage;
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mEQManage.setOnTWCallBack(new EQManage.OnTWCallBack() {

            @Override
            public void onBassChange(int value) {

            }

            @Override
            public void onHighChange(int value) {

            }

            @Override
            public void onLoundChange(int value) {

            }

            @Override
            public void onZoneChange(int x, int y) {

            }

            @Override
            public void onNaviMusicVolume(int navi, int music) {
            }
        });
    }

    private void initView(View v) {
        mDasboardSound1 = (DashboardScaleView) v.findViewById(R.id.dasboard_sound1);
        mDasboardSound1.setValueChangeListener(valueChangeListener);
        mDasboardSound1.setValue(mEQManage.getEnvironmentalReverb(),false);
        mDasboardSound2 = (DashboardScaleView) v.findViewById(R.id.dasboard_sound2);
        mDasboardSound2.setValueChangeListener(valueChangeListener);
        mDasboardSound2.setValue(mEQManage.getLoundnessEnhancer(),false);
        mBtnDefaut = (Button) v.findViewById(R.id.btn_defaut);
        mBtnDefaut.setOnClickListener(this);
    }

    private DashboardScaleView.OnValueChangeListener valueChangeListener = new DashboardScaleView.OnValueChangeListener() {
        @Override
        public void onValueChanged(DashboardScaleView dashboardView, int dashboardType, int position, boolean fromUser) {
            switch (dashboardType) {
                case DashboardScaleView.DashboardType.sound_1: {
                    mEQManage.setEnvironmentalReverb(position);
                    break;
                }
                case DashboardScaleView.DashboardType.sound_2: {
                    mEQManage.setLoudnessEnhancer(position);
                    break;
                }
                case DashboardScaleView.DashboardType.sound_3: {
                    break;
                }
                case DashboardScaleView.DashboardType.sound_4: {
                    break;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_defaut: {
                mDasboardSound1.setValue(14,false);
                mDasboardSound2.setValue(14,false);
                break;
            }
        }
    }
}
