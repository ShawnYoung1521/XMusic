package com.tw.music;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.tw.music.adapter.SecondMusicAdapter;
import com.xy.media_lib.base.MAppCompatActivity;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.presenter.SecondListPresenter;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;
import cn.xy.library.util.convert.XConvert;
import cn.xy.library.util.spannable.XSpanned;

public class MusicListSecondActivity extends MAppCompatActivity<SecondListPresenter> implements MusicView.SecondMusicList{

    private SecondMusicAdapter asecondMusicAdapter;
    private ListView Main_lv;
    TextView mTv_title;
    ImageView btn_second_back;
    @Override
    public SecondListPresenter getPresenter() {
        return new SecondListPresenter(this);
    }

    @Override
    public void onShowMusicListMedias(ArrayList<LMedia> MusicList, String title, LMedia CurrPlayMedia, boolean isPlayingTag) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sceond_list_layout);
        //activity侧滑返回
        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                .setRelativeMoveFactor(0.5F)
                .enableAllDirections()
        ;
        initData();

    }

    private void initData() {
        btn_second_back = findViewById(R.id.btn_second_back);
        btn_second_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(MusicListSecondActivity.this, MusicActivity.class);
                startActivity(intent);
            }
        });
        asecondMusicAdapter = new SecondMusicAdapter();
        Main_lv = findViewById(R.id.main_listview);
        Main_lv.setAdapter(asecondMusicAdapter);
        Main_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.setClickInSecond(i);
            }
        });
        mTv_title = findViewById(R.id.second_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

}
