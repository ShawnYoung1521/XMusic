package com.tw.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.gyf.immersionbar.ImmersionBar;
import com.tw.music.contract.MusicContract;
import com.tw.music.fragment.PlayviewLrcFM;
import com.tw.music.fragment.PlayviewPlayerFM;
import com.tw.music.listener.onFragmentListener;
import java.util.ArrayList;
import java.util.List;
import cn.xy.library.util.permissions.XPermission;
import cn.xy.library.util.tab.XTab;

public class PlayViewActivity extends AppCompatActivity implements onFragmentListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initpermissions();
        ImmersionBar.with(this)
                .statusBarColor(R.color.black)
                .fitsSystemWindows(true).init();
        //activity侧滑返回
        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                .setRelativeMoveFactor(0.5F)
                .enableLeft()
                .enableRight()
        ;
        setContentView(R.layout.main_playview_layout);
        initFragment();
    }

    private void initpermissions() {
        if (!XPermission.isGranted(MusicContract.RecordPermissions)){
            XPermission.permissionGroup(MusicContract.RecordPermissions).callback(new XPermission.SimpleCallback() {
                @Override
                public void onGranted() {}

                @Override
                public void onDenied() {}
            }).request();
        }
    }

    private List<Fragment> mFragment;
    private List<String> mTitle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private void initFragment() {
        mTabLayout = (TabLayout)findViewById(R.id.playview_tab);
        mViewPager = (ViewPager)findViewById(R.id.playview_midd_vp);
        mFragment = new ArrayList<>();
        mFragment.add(new PlayviewPlayerFM(this));
        mFragment.add(new PlayviewLrcFM());
        mTitle = new ArrayList<>();
        mTitle.add("歌曲");
        mTitle.add("歌词");
        XTab.addTab(mTabLayout,
                mViewPager,
                mFragment,
                mTitle,
                getSupportFragmentManager(),
                1,
                new XTab.onPageSelected() {
            @Override
            public void onPageSelected(int position) {

            }
        });
    }

    @Override
    public void toSecond() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, MusicActivity.class);
        startActivity(intent);
    }
}
