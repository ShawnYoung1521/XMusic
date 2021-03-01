package com.xy.media_lib.presenter;

import android.content.Context;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.model.MusicModel;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;

public class SecondListPresenter extends BasePresenter<MusicView.SecondMusicList, MusicModel> implements MusicView.SecondMusicList {

    public SecondListPresenter(Context context) {
        super(context);
    }

    @Override
    public MusicModel getModel() {
        return MusicModel.getInstant();
    }

    public void onResume() {
        mModel.BindSecondView(this);
        mModel.getSecondData();
    }

    public void onDestroy() {
        mModel.unBindSecondView();
    }

    @Override
    public void onShowMusicListMedias(ArrayList<LMedia> MusicList,String title,LMedia CurrPlayMedia, boolean secondtag) {
        get().onShowMusicListMedias(MusicList,title,CurrPlayMedia,secondtag);
    }


    public void setClickInSecond(int pos){
        mModel.setClickInSecond(pos);
    }

}
