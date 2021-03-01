package com.tw.music.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.tw.music.MusicActivity;
import com.tw.music.R;
import com.tw.music.adapter.MusicPathAdapter;
import com.tw.music.contract.MusicContract;
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

@SuppressLint("ValidFragment")
public class TabListPathFM extends MV4Fragment<FristListPresenter> implements MusicView.FristMusicList, AdapterView.OnItemClickListener {
    private MusicPathAdapter musicpathAdapter;
    private onFragmentListener fragmentListener;
    private ListView Main_lv;

    public TabListPathFM(MusicActivity musicActivity) {
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
        View v = inflater.inflate(R.layout.tab_list_layout, null);
        initData(v);
        return v;
    }

    private void initData(View v) {
        Main_lv = v.findViewById(R.id.main_listview);
        Main_lv.setOnItemClickListener(this);
        musicpathAdapter = new MusicPathAdapter();
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
        musicpathAdapter.notifyDataSetChanged(PathLists);
        Main_lv.setAdapter(musicpathAdapter);
    }

    @Override
    public void onCurrPlayMedia(int tag, int pos,String name) {
            musicpathAdapter.notifyDataSetChanged(tag,name);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        fragmentListener.toSecond();
        mPresenter.setShowListMedia(MusicContract.PathTag,i);
    }
}
