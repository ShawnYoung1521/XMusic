package com.tw.music;

import android.app.FragmentManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeRefresh;
import com.billy.android.swipe.consumer.StretchConsumer;
import com.gyf.immersionbar.ImmersionBar;
import com.tw.music.contract.MusicContract;
import com.tw.music.fragment.TabListArtistFM;
import com.tw.music.fragment.TabListAlbumFM;
import com.tw.music.fragment.TabListPathFM;
import com.tw.music.fragment.SecondaryFragment;
import com.tw.music.fragment.TabListAllMusicFM;
import com.tw.music.widget.CircleImageView;
import com.tw.music.listener.onFragmentListener;
import com.xy.media_lib.base.MAppCompatActivity;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.presenter.MainPresenter;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;
import cn.xy.library.util.fragment.XFragment;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.permissions.XPermission;
import cn.xy.library.util.screen.XScreen;
import cn.xy.library.util.service.XService;
import cn.xy.library.util.spannable.XSpanned;
import cn.xy.library.util.tab.XTab;

public class MusicActivity extends MAppCompatActivity<MainPresenter> implements MusicView.MainMusicList,onFragmentListener {
    private FragmentManager fm;
    private CircleImageView playview_bottom_icon;
    private TextView playview_bottom_music_info;
    private ImageView playview_bottom_pp;
    private ProgressBar main_wait_pb;
    private LinearLayout playview_bottom_tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true).init();
        setContentView(R.layout.activity_main);
        XService.startForegroundService(MusicService.class);
        XService.bindService(MusicService.class,mConnection, Service.BIND_AUTO_CREATE);
        fm = getFragmentManager();
        initFragment();
        initData();
        initpermissions();
//        mPresenter.onRefreshrequest();
        mPresenter.onCreate();
        mPresenter.onLoadData();
    }
    private boolean PermissionsReady = false;
    private void initpermissions() {
        if (!XPermission.isGranted(MusicContract.FileReadPermissions)){
            XPermission.permissionGroup(MusicContract.FileReadPermissions).callback(new XPermission.SimpleCallback() {
                @Override
                public void onGranted() {
                    mPresenter.onGrantedPermissions();
                    PermissionsReady = true;
                }

                @Override
                public void onDenied() {
                    mPresenter.setPermissions(false);
                    XPermission.launchAppDetailsSettings();
                }
            }).request();
        }else {
            PermissionsReady = true;
            mPresenter.setPermissions(true);
        }
    }

    private void initData() {
        playview_bottom_tab = (LinearLayout) findViewById(R.id.playview_bottom_tab);
        playview_bottom_icon = findViewById(R.id.playview_bottom_icon);
        playview_bottom_music_info = findViewById(R.id.playview_bottom_music_info);
        playview_bottom_music_info.setSelected(true);
        playview_bottom_pp = findViewById(R.id.playview_bottom_pp);
        main_wait_pb = findViewById(R.id.main_wait_pb);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (main_wait_pb.getVisibility() == View.VISIBLE){
                    main_wait_pb.setVisibility(View.GONE);
                }
            }
        },10000);
        ViewGroup.LayoutParams params = playview_bottom_icon.getLayoutParams();
        params.height=XConvert.dp2px(XScreen.isLandscape()?85:50);
        params.width =XConvert.dp2px(XScreen.isLandscape()?85:50);
        playview_bottom_icon.setLayoutParams(params);
        ViewGroup.LayoutParams lp;
        lp= playview_bottom_tab.getLayoutParams();
        lp.width=ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height=XConvert.dp2px(XScreen.isLandscape()?85:50);
        playview_bottom_tab.setLayoutParams(lp);
        findViewById(R.id.playview_bottom_prev).setVisibility(XScreen.isLandscape()?View.VISIBLE:View.GONE);
    }


    private List<Fragment> mFragment;
    private List<String> mTitle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private void initFragment() {
        mTabLayout = (TabLayout)findViewById(R.id.allplayview_midd_tab);
        mViewPager = (ViewPager)findViewById(R.id.allplayview_midd_vp);
        mFragment = new ArrayList<>();
        mFragment.add(new TabListAllMusicFM());
        mFragment.add(new TabListArtistFM(this));
        mFragment.add(new TabListAlbumFM(this));
        mFragment.add(new TabListPathFM(this));
        mTitle = new ArrayList<>();
        mTitle.add("歌曲");
        mTitle.add("歌手");
        mTitle.add("专辑");
        mTitle.add("文件夹");
        XTab.addTab(mTabLayout,mViewPager,mFragment,mTitle,getSupportFragmentManager());
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public MainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onResume() {
        mPresenter.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        if(mConnection != null){
            XService.unbindService(mConnection);
        }
        super.onDestroy();
    }


    @Override
    public void onCurrPlayMedia(LMedia media) {
        if (media.name != null && !media.name.isEmpty() && media.name.length()>0){
            XSpanned.with(playview_bottom_music_info)
                    .append(media.name)
                    .setFontSize(XConvert.dp2px(18))
                    .setTypeface(Typeface.DEFAULT_BOLD)
                    .appendSpace(XConvert.dp2px(14))
                    .append(media.artist)
                    .setFontSize(XConvert.dp2px(16))
                    .create();
        }
        if (media.getAlbumBitmap() != null){
            playview_bottom_icon.setImageBitmap(media.getAlbumBitmap());
        }else {
            playview_bottom_icon.setImageDrawable(XApp.getApp().getDrawable(R.drawable.ic_launcher));
        }
    }

    @Override
    public void onPlayState(boolean isPlaying) {
        if (isPlaying){
            playview_bottom_icon.startRotation();
        }else {
            playview_bottom_icon.stopRotation();
        }
        playview_bottom_pp.getDrawable().setLevel(isPlaying?1:0);
    }

    @Override
    public void onMediaList(ArrayList<LMedia> ALLMusicList) {
        main_wait_pb.setVisibility(View.GONE);
    }

    @Override
    public void success() {
    }

    @Override
    public void failed() {

    }

    public void onPlaylistClick(View view){
        switch (view.getId()){
            case R.id.playview_bottom_icon:
            case R.id.playview_bottom_music_info:
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(this, PlayViewActivity.class);
                startActivity(intent);
                break;
            case R.id.playview_bottom_prev:
                mPresenter.prev();
                break;
            case R.id.playview_bottom_next:
                mPresenter.next();
                break;
            case R.id.playview_bottom_pp:
                mPresenter.playPause();
                break;
        }
    }

    @Override
    public void toSecond() {
        XFragment.addFragmentToBackStack(fm,new SecondaryFragment(),R.id.main_frame_layout);
    }

    public void popFragmentFromBackStack(){
        mPresenter.onResume();
        fm.popBackStack();
        fm.executePendingTransactions();
    }
}