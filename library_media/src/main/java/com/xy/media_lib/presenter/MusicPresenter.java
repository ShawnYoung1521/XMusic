package com.xy.media_lib.presenter;

import android.content.Context;
import com.xy.media_lib.model.MusicModel;
import com.xy.media_lib.view.MusicView;

public class MusicPresenter extends BasePresenter<MusicView, MusicModel> implements MusicView {

    public MusicPresenter(Context context) {
        super(context);
    }

    @Override
    public MusicModel getModel() {
        return MusicModel.getInstant();
    }

    public void onServiceCreate(){
        mModel.onServiceCreate();
    }

    public void onServiceDestroy() {
        mModel.onServiceDestroy();
    }

}
