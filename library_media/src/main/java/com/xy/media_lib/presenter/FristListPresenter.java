package com.xy.media_lib.presenter;

import android.content.Context;

import com.xy.media_lib.bean.AlbumMedia;
import com.xy.media_lib.bean.ArtistMedia;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.bean.PathMedia;
import com.xy.media_lib.model.MusicModel;
import com.xy.media_lib.view.MusicView;

import java.util.ArrayList;

public class FristListPresenter extends BasePresenter<MusicView.FristMusicList, MusicModel> implements MusicView.FristMusicList {

    public FristListPresenter(Context context) {
        super(context);
    }

    @Override
    public MusicModel getModel() {
        return MusicModel.getInstant();
    }

    public void onCreate(){
        mModel.BindFristMusicListView(this);
        mModel.getFristData();
    }

    public void onResume() {
    }

    public void onDestroy() {
        mModel.unBindFristMusicListView(this);
    }

    @Override
    public void onMediaList(ArrayList<LMedia> ALLMusicList, ArrayList<ArtistMedia> ArtistLists, ArrayList<AlbumMedia> AlbumLists, ArrayList<PathMedia> PathLists) {
        get().onMediaList(ALLMusicList,ArtistLists,AlbumLists,PathLists);
    }

    @Override
    public void onCurrPlayMedia(int tag, int pos,String name) {
        get().onCurrPlayMedia(tag,pos,name);
    }

    public void setClickInAllList(int tag,int pos){
        mModel.setClickInAllList(tag,pos);
    }

    public void setShowListMedia(int tag,int pos){
        mModel.setShowListMedia(tag,pos);
    }
}
