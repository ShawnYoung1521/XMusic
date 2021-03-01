package com.tw.music.fragment;

import android.app.Instrumentation;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tw.music.MusicActivity;
import com.tw.music.R;
import com.tw.music.adapter.SecondMusicAdapter;
import com.tw.music.contract.MusicContract;
import com.xy.media_lib.base.MFragment;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.presenter.SecondListPresenter;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;
import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.spannable.XSpanned;

public class SecondaryFragment extends MFragment<SecondListPresenter> implements MusicView.SecondMusicList {

    private SecondMusicAdapter asecondMusicAdapter;
    private ListView Main_lv;
    TextView mTv_title;
    ImageView btn_second_back;

    public SecondaryFragment(){
        super();
    }

    @Override
    public SecondListPresenter createPresenter() {
        return new SecondListPresenter(XApp.getApp());
    }
    
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sceond_list_layout, null);
        initData(v);
        return v;
    }

    /*public void onBack(){
        new Thread(){
            public void run() {
                try{
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
                catch (Exception e) {
                    XLog.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }*/

    private void initData(View v) {
        btn_second_back = v.findViewById(R.id.btn_second_back);
        btn_second_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MusicActivity)getActivity()).popFragmentFromBackStack();
            }
        });
                asecondMusicAdapter = new SecondMusicAdapter();
        Main_lv = v.findViewById(R.id.main_listview);
        Main_lv.setAdapter(asecondMusicAdapter);
        Main_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.setClickInSecond(i);
            }
        });
        mTv_title = v.findViewById(R.id.second_title);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onShowMusicListMedias(ArrayList<LMedia> MusicList,String title,LMedia CurrPlayMedia,boolean isPlayingTag) {
        asecondMusicAdapter.notifyDataSetChanged(MusicList,CurrPlayMedia,isPlayingTag);
        if (title != null && !title.isEmpty() && title.length()>0){
            XSpanned.with(mTv_title)
                    .append(title)
                    .setFontSize(XConvert.dp2px(18))
                    .setTypeface(Typeface.DEFAULT_BOLD)
                    .appendSpace(XConvert.dp2px(5))
                    .append("(共")
                    .append(String.valueOf(MusicList.size()))
                    .append("首)")
                    .setFontSize(XConvert.dp2px(14))
                    .create();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }
}
