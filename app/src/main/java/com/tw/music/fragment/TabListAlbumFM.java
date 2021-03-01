package com.tw.music.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tw.music.adapter.MusicAlbumAdapter;
import com.tw.music.contract.MusicContract;
import com.tw.music.listener.onTabItemListener;
import com.tw.music.MusicActivity;
import com.tw.music.R;
import com.tw.music.listener.onFragmentListener;
import com.xy.media_lib.base.MV4Fragment;
import com.xy.media_lib.bean.AlbumMedia;
import com.xy.media_lib.bean.ArtistMedia;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.bean.PathMedia;
import com.xy.media_lib.presenter.FristListPresenter;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;
import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.screen.XScreen;

@SuppressLint("ValidFragment")
public class TabListAlbumFM extends MV4Fragment<FristListPresenter> implements MusicView.FristMusicList, onTabItemListener {
    private MusicAlbumAdapter musicAlbumAdapter;
    private onFragmentListener fragmentListener;
    private RecyclerView Main_lv;
    private TextView nofile_notif;

    public TabListAlbumFM(MusicActivity musicActivity) {
        fragmentListener = musicActivity;
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mPresenter.onCreate();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.from(mContext).inflate(R.layout.tab_album_layout, null,false);
        initData(v);
        return v;
    }

    private void initData(View v) {
        Main_lv = v.findViewById(R.id.main_recyclerview);
        nofile_notif = v.findViewById(R.id.nofile_notif);
        musicAlbumAdapter = new MusicAlbumAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(mContext,XScreen.isLandscape()?3:1);//横屏3
        Main_lv.setLayoutManager(manager);
        Main_lv.setHasFixedSize(true);
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
    public FristListPresenter createPresenter() {
        return new FristListPresenter(XApp.getApp());
    }

    @Override
    public void onMediaList(ArrayList<LMedia> ALLMusicList, ArrayList<ArtistMedia> ArtistLists, ArrayList<AlbumMedia> AlbumLists, ArrayList<PathMedia> PathLists) {
        nofile_notif.setVisibility((AlbumLists.size()>0)?View.GONE:View.VISIBLE);
        musicAlbumAdapter.notifyDataSetChanged(AlbumLists);
        Main_lv.setAdapter(musicAlbumAdapter);
    }

    @Override
    public void onCurrPlayMedia(int tag, int pos,String name) {
            musicAlbumAdapter.notifyDataSetChanged(tag,name);
//            Main_lv.scrollToPosition(pos);
    }

    @Override
    public void onIndex(int position) {
        fragmentListener.toSecond();
        mPresenter.setShowListMedia(MusicContract.AlbumTag,position);
    }
}
