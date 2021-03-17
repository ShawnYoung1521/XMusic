package com.tw.music.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.tw.music.R;
import com.tw.music.widget.DashboardScaleView;
import com.xy.media_lib.base.MV4Fragment;
import com.xy.media_lib.presenter.EQPresenter;
import com.xy.media_lib.view.MusicView;
import cn.xy.library.util.log.XLog;

public class PlayviewEQFM extends MV4Fragment<EQPresenter> implements MusicView.EqView,View.OnClickListener {

    private static String TAG="PlayviewEQFM";
    private static final String AUDIO_SESSION_ID = "com.intent.action.SEND_AUDIO_SESSION_ID";
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private DashboardScaleView mDasboardSound1;
    private DashboardScaleView mDasboardSound2;
    private Button mBtnDefaut;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sound_pool_fragment, null, false);
        initView(v);
        return v;
    }

    @Override
    public EQPresenter createPresenter() {
        return new EQPresenter(mContext);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mPresenter.onCreate(this);
    }

    private void initView(View v) {
        btn_ai_switch = v.findViewById(R.id.btn_ai);
        btn_ai_switch.setOnClickListener(this);
        btn_re_switch = v.findViewById(R.id.btn_rs);
        btn_re_switch.setOnClickListener(this);
        btn_zdy_switch = v.findViewById(R.id.btn_zdy);
        btn_zdy_switch.setOnClickListener(this);
        mDasboardSound1 = (DashboardScaleView) v.findViewById(R.id.dasboard_sound1);
        mDasboardSound1.setValueChangeListener(valueChangeListener);
        mDasboardSound2 = (DashboardScaleView) v.findViewById(R.id.dasboard_sound2);
        mDasboardSound2.setValueChangeListener(valueChangeListener);
        mBtnDefaut = (Button) v.findViewById(R.id.btn_defaut);
        mBtnDefaut.setOnClickListener(this);
    }

    private DashboardScaleView.OnValueChangeListener valueChangeListener = new DashboardScaleView.OnValueChangeListener() {
        @Override
        public void onValueChanged(DashboardScaleView dashboardView, int dashboardType, int position, boolean fromUser) {
            switch (dashboardType) {
                case DashboardScaleView.DashboardType.sound_1: {
                    mPresenter.setEnvironmentalReverb(position);
                    break;
                }
                case DashboardScaleView.DashboardType.sound_2: {
                    mPresenter.setLoudnessEnhancer(position);
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
                mPresenter.setEnvironmentalReverb(14);
                mDasboardSound2.setValue(14,false);
                mPresenter.setLoudnessEnhancer(14);
                break;
            }
            case R.id.btn_ai:
                setEQMode(3);
                break;
            case R.id.btn_rs:
                setEQMode(5);
                break;
            case R.id.btn_zdy:
                setEQMode(7);
                break;
        }
    }

    private RadioButton btn_ai_switch;
    private RadioButton btn_re_switch;
    private RadioButton btn_zdy_switch;

    private void onChangMode(int mEqMode){
        switch (mEqMode){
            case 3:
                btn_ai_switch.setChecked(true);
                break;
            case 5:
                btn_re_switch.setChecked(true);
                break;
            case 7:
                btn_zdy_switch.setChecked(true);
                break;
        }
    }
    private void setEQMode(int i) {
        mPresenter.setEQMode(i);
    }

    @Override
    public void onEnvironmentalReverb(int volue) {
        mDasboardSound1.setValue(volue,false);
    }

    @Override
    public void onLoundnessEnhancer(int volue) {
        mDasboardSound2.setValue(volue,false);
    }

    @Override
    public void onEQMode(int mdoe) {
        onChangMode(mdoe);
    }
}
