package com.tw.music.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.tw.music.R;
import com.tw.music.adapter.AllMusicAdapter;
import com.tw.music.contract.MusicContract;
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
public class TabListAllMusicFM extends MV4Fragment<FristListPresenter> implements MusicView.FristMusicList, AdapterView.OnItemClickListener {

    private AllMusicAdapter allMusicAdapter;
    private ListView Main_lv;

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
        allMusicAdapter = new AllMusicAdapter();
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
    }

    @Override
    public FristListPresenter createPresenter() {
        return new FristListPresenter(XApp.getApp());
    }

    @Override
    public void onMediaList(ArrayList<LMedia> ALLMusicList, ArrayList<ArtistMedia> ArtistLists, ArrayList<AlbumMedia> AlbumLists, ArrayList<PathMedia> PathLists) {
        allMusicAdapter.notifyDataSetChanged(ALLMusicList);
        Main_lv.setAdapter(allMusicAdapter);
    }

    @Override
    public void onCurrPlayMedia(int tag, int pos,String name) {
        allMusicAdapter.notifyDataSetChanged(tag,pos);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.setClickInAllList(MusicContract.AllMusicTag,i);
    }
}
