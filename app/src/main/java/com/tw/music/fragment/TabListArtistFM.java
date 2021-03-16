package com.tw.music.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tw.music.MusicActivity;
import com.tw.music.R;
import com.tw.music.adapter.MusicArtistAdapter;
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
public class TabListArtistFM extends MV4Fragment<FristListPresenter> implements MusicView.FristMusicList, AdapterView.OnItemClickListener {
    private MusicArtistAdapter musicArtistAdapter;
    private onFragmentListener fragmentListener;
    private ListView Main_lv;
    private TextView nofile_notif;

    public TabListArtistFM(MusicActivity musicActivity) {
        fragmentListener = musicActivity;
    }

    public TabListArtistFM() {
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
        nofile_notif = v.findViewById(R.id.nofile_notif);
        Main_lv = v.findViewById(R.id.main_listview);
        Main_lv.setOnItemClickListener(this);
        musicArtistAdapter = new MusicArtistAdapter();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        fragmentListener.toSecond();
        mPresenter.setShowListMedia(MusicContract.ArtistTag,i);
    }

    @Override
    public void onMediaList(ArrayList<LMedia> ALLMusicList, ArrayList<ArtistMedia> mArtistLists, ArrayList<AlbumMedia> AlbumLists, ArrayList<PathMedia> PathLists) {
        nofile_notif.setVisibility((mArtistLists.size()>0)?View.GONE:View.VISIBLE);
        musicArtistAdapter.notifyDataSetChanged(mArtistLists);
        Main_lv.setAdapter(musicArtistAdapter);
    }

    @Override
    public void onCurrPlayMedia(int tag, int pos,String name) {
            musicArtistAdapter.notifyDataSetChanged(tag,name);
    }
}
