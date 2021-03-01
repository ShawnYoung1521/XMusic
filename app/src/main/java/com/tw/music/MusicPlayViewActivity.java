package com.tw.music;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.tw.music.adapter.PlayListMusicAdapter;
import com.tw.music.contract.MusicContract;
import com.tw.music.fragment.VisualizerCAblumFM;
import com.tw.music.fragment.VisualizerSAblumFM;
import com.tw.music.utils.Utils;
import com.xy.media_lib.base.MAppCompatActivity;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.presenter.PlayViewPresenter;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;

import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;
import cn.xy.library.util.fragment.XFragment;
import cn.xy.library.util.image.XImage;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.permissions.XPermission;
import cn.xy.library.util.spannable.XSpanned;
import cn.xy.library.util.toast.XToast;

public class MusicPlayViewActivity extends MAppCompatActivity<PlayViewPresenter> implements MusicView.PlayView {

    private ImageView allplayview_bottom_pp;
    private ImageView allplayview_bottom_mode;
    private SeekBar allplayview_bottom_progressbar;
    private TextView allplayview_bottom_currentposition;
    private TextView allplayview_bottom_duration;
    private TextView allplayview_top_name;
    private TextView allplayview_top_artist;
    private View playlistview;
    private TextView playlist_title;
    private ImageView playlist_delete;
    private View playlist_layout;
    private ListView playlist_listview;
    private PlayListMusicAdapter playListMusicAdapter;
    private boolean fromUser;
    private VisualizerCAblumFM mVisualizerCAblumFM;
    private VisualizerSAblumFM mVisualizerSAblumFM;
    private FragmentManager fm;
    private View playview_view_layout;
    private ImageView allplayview_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.black)
                .fitsSystemWindows(true).init();
        mPresenter.onCreate();
        setContentView(R.layout.playview_layout);
        initData();
        initTab();
    }

    private boolean PermissionsReady = false;
    int mVisualizerShowMode = 0;
    private void initTab() {
        fm = getFragmentManager();
        VisualizerShowMode(mVisualizerShowMode);
    }

    public void VisualizerShowMode(int mode){
        if (mVisualizerCAblumFM != null){
            XFragment.remove(mVisualizerCAblumFM);
        }
        if (mVisualizerSAblumFM != null) {
            XFragment.remove(mVisualizerSAblumFM);
        }
        switch (mode){
            case 0:
                mVisualizerCAblumFM = new VisualizerCAblumFM();
                XFragment.add(fm,mVisualizerCAblumFM,R.id.playview_frame_layout);
                XFragment.show(mVisualizerCAblumFM);
                break;
            case 1:
                if (PermissionsReady){
                    mVisualizerSAblumFM = new VisualizerSAblumFM();
                    XFragment.add(fm,mVisualizerSAblumFM,R.id.playview_frame_layout);
                    XFragment.show(mVisualizerCAblumFM);
                }else {
                    initpermissions();
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    private void initData() {
        allplayview_close = findViewById(R.id.allplayview_close);
        allplayview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
        playview_view_layout = findViewById(R.id.playview_view_layout);
        allplayview_bottom_pp = findViewById(R.id.allplayview_bottom_pp);
        allplayview_bottom_mode = findViewById(R.id.allplayview_bottom_mode);
        allplayview_bottom_progressbar = findViewById(R.id.allplayview_bottom_progressbar);
        allplayview_bottom_progressbar.setOnSeekBarChangeListener(seekbarlistener);
        allplayview_bottom_currentposition = findViewById(R.id.allplayview_bottom_currentposition);
        allplayview_bottom_duration = findViewById(R.id.allplayview_bottom_duration);
        allplayview_top_artist = findViewById(R.id.allplayview_top_artist);
        allplayview_top_name = findViewById(R.id.allplayview_top_name);
        allplayview_top_artist.setSelected(true);
        allplayview_top_name.setSelected(true);
        playlistview = LayoutInflater.from(this).inflate(R.layout.playlist_pop_layout, null, false);
        playlist_title = (TextView) playlistview.findViewById(R.id.playlist_title);
        playlist_delete = (ImageView) playlistview.findViewById(R.id.playlist_delete);
        playlist_layout = playlistview.findViewById(R.id.playlist_layout);
        XImage.setRoundedCorners(playlist_layout,50);
        playlist_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog();
            }
        });
        playlist_listview = (ListView) playlistview.findViewById(R.id.playlist_listview);
        playlist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.setClickInPlayViewList(MusicContract.PlayListTag,i);
            }
        });
        playListMusicAdapter = new PlayListMusicAdapter();
        playlist_listview.setAdapter(playListMusicAdapter);
        findViewById(R.id.allplayview_midd).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showSingleChoiceDialog();
                return true;
            }
        });
    }
    public void onBack(){
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
    }
    /**列表单选对话框**/
    int mChoice;
    private void showSingleChoiceDialog(){
        final String[] items = { "专辑图/歌词","频谱"};
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this);
        singleChoiceDialog.setTitle("显示模块选择");
        singleChoiceDialog.setSingleChoiceItems(items, mChoice,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("Positive",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mChoice != -1) {
                            if (mChoice >= 2){
                                XToast.getInstance().Text("该模块还在开发中").show();
                            }else {
                                VisualizerShowMode(mChoice);
                            }
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    SeekBar.OnSeekBarChangeListener seekbarlistener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (fromUser)
                mPresenter.seekTo(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean User) {
            fromUser = User;
        }
    };
    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setMessage("确定要清除播放列表？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.setClearCurrPlayMediaList();
                        playListMusicAdapter.notifyDataSetChanged(new ArrayList<LMedia>(),new LMedia());
                        mPresenter.stop();
                        updatePlayListTitle();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        normalDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
        initpermissions();
    }

    public void initpermissions(){
        XPermission.permissionGroup(MusicContract.RecordPermissions).callback(new XPermission.SimpleCallback() {
            @Override
            public void onGranted() {
                PermissionsReady = true;
            }

            @Override
            public void onDenied() {
                PermissionsReady = false;
            }
        }).request();
    }
    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public PlayViewPresenter getPresenter() {
        return new PlayViewPresenter(this);
    }

    public void onAllPlaylistClick(View view){
        switch (view.getId()){
            case R.id.allplayview_bottom_list:
                popRdsView(view);
                break;
            case R.id.allplayview_bottom_mode:
                mPresenter.checkPlayMode();
                break;
            case R.id.allplayview_bottom_prev:
                mPresenter.prev();
                break;
            case R.id.allplayview_bottom_pp:
                mPresenter.playPause();
                break;
            case R.id.allplayview_bottom_next:
                mPresenter.next();
                break;
        }
    }
    private void popRdsView(View view) {
        PopupWindow popRdsWindow = new PopupWindow(playlistview, ViewGroup.LayoutParams.MATCH_PARENT,XConvert.dp2px(300));
        popRdsWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popRdsWindow.setOutsideTouchable(true);
        popRdsWindow.setAnimationStyle(R.style.pop_window_anim);
        popRdsWindow.showAsDropDown(view, 0, 0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, MusicActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPlayMode(int mode) {
        allplayview_bottom_mode.getDrawable().setLevel(mode);
    }

    @Override
    public void onPlayState(boolean isPlaying, int currenttime, int totaltime) {
        allplayview_bottom_pp.getDrawable().setLevel(isPlaying?1:0);
        allplayview_bottom_progressbar.setMax(totaltime);
        allplayview_bottom_progressbar.setProgress(currenttime);
        allplayview_bottom_duration.setText(Utils.timelongToString(totaltime));
        allplayview_bottom_currentposition.setText(Utils.timelongToString(currenttime));
    }

    @Override
    public void onCurrPlayMedia(LMedia media) {
        if (media.name != null){
            XSpanned.with(allplayview_top_name)
                    .append(media.getName())
                    .setFontSize(XConvert.dp2px(20))
                    .setTypeface(Typeface.DEFAULT_BOLD)
                    .create();
        }else {
            allplayview_top_name.setText("");
            allplayview_top_artist.setText("");
            playview_view_layout.setBackgroundResource(R.drawable.ic_launcher);
        }
        allplayview_top_artist.setText(media.getArtist());
        playListMusicAdapter.notifyDataSetChanged(PlayList,media);
        if (media.getAlbumBitmap() != null){
            playview_view_layout.setBackground(XImage.bitmap2Drawable(XImage.stackBlur(media.getAlbumBitmap(),100)));
        }else {
            playview_view_layout.setBackgroundColor(XApp.getApp().getResources().getColor(R.color.black));

        }
    }

    ArrayList<LMedia> PlayList;
    @Override
    public void onPlayList(ArrayList<LMedia> MusicList) {
        PlayList = MusicList;
        updatePlayListTitle();
    }

    public void updatePlayListTitle(){
            XSpanned.with(playlist_title)
                    .append("当前播放")
                    .setFontSize(XConvert.dp2px(13))
                    .setTypeface(Typeface.DEFAULT_BOLD)
                    .append("("+String.valueOf(PlayList.size())+"首)")
                    .setFontSize(XConvert.dp2px(10))
                    .create();
    }
}
