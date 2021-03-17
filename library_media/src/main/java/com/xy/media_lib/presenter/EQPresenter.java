package com.xy.media_lib.presenter;

import android.content.Context;
import com.xy.media_lib.model.MusicModel;
import com.xy.media_lib.view.MusicView;

public class EQPresenter extends BasePresenter<MusicView.EqView, MusicModel> implements MusicView.EqView {

    public EQPresenter(Context context) {
        super(context);
    }

    @Override
    public MusicModel getModel() {
        return MusicModel.getInstant();
    }

    public void onCreate(MusicView.EqView context){
        mModel.BindEQView(this);
    }

    public void onResume() {
        mModel.getEQDate();
    }

    public void onDestroy() {
        mModel.unBindEQView();
    }

    public void setEnvironmentalReverb(int value) {
        mModel.setEnvironmentalReverb(value);
    }

    public void setLoudnessEnhancer(int value) {
        mModel.setLoudnessEnhancer(value);
    }

    public void setEQMode(int mode){
        mModel.mModel(mode);
    }

    @Override
    public void onEnvironmentalReverb(int volue) {
        get().onEnvironmentalReverb(volue);
    }

    @Override
    public void onLoundnessEnhancer(int volue) {
        get().onLoundnessEnhancer(volue);

    }

    @Override
    public void onEQMode(int mdoe) {
        get().onEQMode(mdoe);
    }
}
