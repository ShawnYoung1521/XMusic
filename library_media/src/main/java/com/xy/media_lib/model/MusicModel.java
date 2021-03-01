package com.xy.media_lib.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import com.xy.media_lib.bean.AlbumMedia;
import com.xy.media_lib.bean.ArtistMedia;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.bean.PathMedia;
import com.xy.media_lib.utils.LoadMediaFile;
import com.xy.media_lib.utils.LoadMusicFile;
import com.xy.media_lib.utils.Utils;
import com.xy.media_lib.view.MusicView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import cn.xy.library.XApp;

public class MusicModel extends BaseModel {

    public static String PathStorage = "/storage/emulated/0/Music/";
    public static String PathUsb1 = "/usb1/";
    public static String PathUsb2 = "/usb2/";
    public static String PathExtsd1 = "/extsd/";

    public static MusicModel getInstant(){
        if (mModel == null){
            mModel = new MusicModel();
        }
        return mModel;
    }
    private static MusicModel mModel=null;
    ArrayList<LMedia> FastALLMusicList = new ArrayList<>();
    ArrayList<ArtistMedia> FastArtistLists = new ArrayList<>();
    ArrayList<AlbumMedia> FastAlbumLists = new ArrayList<>();
    ArrayList<PathMedia> FastPathLists = new ArrayList<>();

    ArrayList<LMedia> ALLMusicList = new ArrayList<>();
    ArrayList<ArtistMedia> ArtistLists = new ArrayList<>();
    ArrayList<AlbumMedia> AlbumLists = new ArrayList<>();
    ArrayList<PathMedia> PathLists = new ArrayList<>();

    ArrayList<LMedia> CurrPlayMediaList = new ArrayList<>();
    LMedia CurrPlayMedia = new LMedia();
    LMedia PrePlayMedia = new LMedia();
    LMedia NextPlayMedia = new LMedia();

    /**快速扫描
     * 0 默认
     * 1 开始读取、正在读取
     * 2 读取完成
     * */
    int FastLoadType = 0;
    public void onLoadFastData(){
        if (LoadAllType == 2) { //全数据加载好
            handler.sendEmptyMessage(0x04);
        }else if (FastLoadType == 2){ ////快数据加载好
            handler.sendEmptyMessage(0x03);
        }else{
            if (PermissionsReady){
                FastLoadType = 1;
                GetFastData(PathStorage,true);
                FastLoadType = 2;
            }
        }
    }

    /**慢速扫描
     * 0 默认
     * 1 开始读取、正在读取
     * 2 读取完成
     * */
    int LoadAllType = 0;
    public void onLoadAllData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (PermissionsReady){
                    LoadAllType = 1;
                    GetAllData(PathStorage,false);
                    FastALLMusicList = ALLMusicList;
                    FastArtistLists = ArtistLists;
                    FastAlbumLists = AlbumLists;
                    FastPathLists = PathLists;
                    LoadAllType = 2;
                    handler.sendEmptyMessage(0x04);
                }
            }
        }).start();
    }

    /**列表Fragment准备就绪
     * 取数据**/
    public void getFristData(){
        if (LoadAllType == 2) { //全数据加载好
            handler.sendEmptyMessage(0x04);
        }else if (FastLoadType == 2){ //快数据加载好
            handler.sendEmptyMessage(0x03);
        }else {//快数据加载中
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getFristData();
                }
            },300);
        }
        for (MusicView.FristMusicList view : fristMusicLists){
            view.onCurrPlayMedia(CurrPlayListTag,CurrPlayListTagPosition,secondTitle);
        }
        if (mainMusicList != null){
            mainMusicList.onMediaList(FastALLMusicList);
        }
    }


    /**
     * PlayView
     */
    private MusicView.PlayView  playview;
    public void BindPlayView(MusicView.PlayView  view){
        playview = view;
    }
    public void unBindPlayView(){
        playview = null;
    }
    public void getPlayViewDate() {
        handler.sendEmptyMessage(0x01);
        setPlayViewData();
    }
    /*end*/

    /**
     * SecondMusicList
     */
    private MusicView.SecondMusicList  secondview;
    public void BindSecondView(MusicView.SecondMusicList  view){
        secondview = view;
    }
    public void unBindSecondView(){
        secondview = null;
    }
    /*end*/

    /**
     * VisualizerAblum
     */
    private MusicView.VisualizerAblum  visualizerAblumview;
    public void BindVisualizerAblumView(MusicView.VisualizerAblum  view){
        visualizerAblumview = view;
    }
    public void unBindVisualizerAblumView(){
        visualizerAblumview = null;
    }
    public void getVisualizerAblumData(){
        if (visualizerAblumview != null){
            visualizerAblumview.onCurrPlayMedia(CurrPlayMedia);
            visualizerAblumview.onMediaPlay(getMediaPlayer());
        }
    }
    /*end*/

    /**
     * MainMusicList
     */
    private MusicView.MainMusicList  mainMusicList;
    private boolean PermissionsReady = false;
    public void BindMainMusicListView(MusicView.MainMusicList  view){
        mainMusicList = view;
    }
    public void unBindMainMusicListView(){
        mainMusicList = null;
    }
    public void onCreate(){
        mMediaPlayer = getMediaPlayer();
        am = (AudioManager) XApp.getApp().getSystemService(Context.AUDIO_SERVICE);
        handler = new mHandler();
    }

    public void getMainData(){
        if (mainMusicList != null){
            mainMusicList.onPlayState(isPlaying());
            mainMusicList.onCurrPlayMedia(CurrPlayMedia);
        }
    }
    public void setPermissions(boolean has){
        PermissionsReady = has;
    }

    public void setGrantedPermissions(){
        PermissionsReady = true;
        onLoadFastData();
        onLoadAllData();
    }
    /*end*/

    /**
     * FristMusicList
     */
    private static ArrayList<MusicView.FristMusicList> fristMusicLists = new ArrayList<MusicView.FristMusicList>();
    public void BindFristMusicListView(MusicView.FristMusicList  view){
        if (!Utils.isPInPs(fristMusicLists,view)){
            fristMusicLists.add(view);
        }
    }
    public void unBindFristMusicListView(MusicView.FristMusicList mPV){
        if (Utils.isPInPs(fristMusicLists,mPV)){
            fristMusicLists.remove(mPV);
        }
    }
    /*end*/

    /**
     * ServiceView/all
     */
    public void onServiceCreate() {
        onLoadAllData();
    }

    public void onServiceDestroy() {
        clear();
    }
    /*end*/

    /*
        第一种获取文件的方式
     */
    public void FristGetData(boolean isFast){
        FastALLMusicList = LoadMediaFile.getMusicList(isFast);
        updateSecondaryList(FastALLMusicList);
    }
    List FastListData = new ArrayList();
    List AllListData = new ArrayList();
    public void GetFastData(String PathStorage,boolean isFast){
        FastListData = LoadMusicFile.getInstance().FastUpdate(PathStorage,isFast).getFastAllMusicList();
        FastALLMusicList = (ArrayList<LMedia>)FastListData.get(0);
        FastArtistLists = (ArrayList<ArtistMedia>)FastListData.get(1);
        FastAlbumLists = (ArrayList<AlbumMedia>)FastListData.get(2);
        FastPathLists = (ArrayList<PathMedia>)FastListData.get(3);
    }

    public void GetAllData(String PathStorage,boolean isFast){
        AllListData = LoadMusicFile.getInstance().Allupdate(PathStorage,isFast).getAllMusicList();
        ALLMusicList = (ArrayList<LMedia>)AllListData.get(0);
        ArtistLists = (ArrayList<ArtistMedia>)AllListData.get(1);
        AlbumLists = (ArrayList<AlbumMedia>)AllListData.get(2);
        PathLists = (ArrayList<PathMedia>)AllListData.get(3);
    }

    public MediaPlayer getMediaPlayer(){
        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return true;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    next();
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    start();
                }
            });
        }
        return mMediaPlayer;
    }

    private synchronized void updateSecondaryList(ArrayList<LMedia> ALLMusicList) {
        FastArtistLists = new ArrayList<>();
        FastAlbumLists = new ArrayList<>();
        FastPathLists = new ArrayList<>();
        for (int i = 0;i < ALLMusicList.size();i++){
            LMedia media = ALLMusicList.get(i);
            String artist = ALLMusicList.get(i).getArtist();
            Bitmap bitmap = media.getAlbumBitmap();
            String album = ALLMusicList.get(i).getAlbum();

            ArtistMedia mArtistMedia = new ArtistMedia(artist,media);
            if (Utils.isArtistInArtistList(FastArtistLists,artist)){
                ArtistMedia media_Artist = Utils.getArtistIsArtistLists(FastArtistLists,artist);
                media_Artist.getArtistMediaList().add(media);
            }else {
                FastArtistLists.add(mArtistMedia);
            }
            if (bitmap != null){
                int indexs = Utils.isAritstInAritsts(FastArtistLists,artist);
                FastArtistLists.get(indexs).setAritistbitmap(bitmap);
            }

            AlbumMedia mAlbumMedia = new AlbumMedia(album,media);
            if (Utils.isAlbumInAlbumList(FastAlbumLists,album)){
                AlbumMedia media_Album = Utils.getAlbumIsAlbumLists(FastAlbumLists,album);
                media_Album.getAlbumMediaList().add(media);
            }else {
                FastAlbumLists.add(mAlbumMedia);
            }
            if (bitmap != null){
                int indexs = Utils.isAlbumInAAlbums(FastAlbumLists,album);
                FastAlbumLists.get(indexs).setAlbumBp(bitmap);
            }

            String o = ALLMusicList.get(i).getUrl();
            int index = o.lastIndexOf("/");
            String path = o.substring(0,index);
            String oo=o.substring(o.lastIndexOf("/",o.lastIndexOf("/")-1)+1);
            int index2 = oo.lastIndexOf("/");
            String pathName = oo.substring(0,index2);
            PathMedia mPathMedia = new PathMedia(pathName,path,media);
            if (Utils.isPathInPathList(FastPathLists,path)){
                PathMedia media_Path = Utils.getPathIsAlbumLists(FastPathLists,path);
                media_Path.getPathMediaList().add(media);
            }else {
                FastPathLists.add(mPathMedia);
            }
        }
    }

    private void setPlayViewData() {
        if (playview != null){
            if (CurrPlayMediaList != null ){
                playview.onPlayList(CurrPlayMediaList);
            }
            if (CurrPlayMedia != null){
                playview.onCurrPlayMedia(CurrPlayMedia);
            }
            playview.onPlayMode(PlayMode);
        }
    }

    public final void clear() {
        if (visualizerAblumview != null){
            visualizerAblumview = null;
        }
        if (playview != null){
            playview = null;
        }
        if (secondview != null){
            secondview = null;
        }
        if (mainMusicList != null){
            mainMusicList = null;
        }
        for (MusicView.FristMusicList view : fristMusicLists){
            view = null;
        }
        FastALLMusicList.clear();
        FastArtistLists.clear();
        FastAlbumLists.clear();
        FastPathLists.clear();
        ALLMusicList.clear();
        ArtistLists.clear();
        AlbumLists.clear();
        PathLists.clear();
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (handler != null){
            handler.removeMessages(0x01);
            handler = null;
        }
        mModel = null;
    }

    private mHandler handler;
    private class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01: //循环更新播放界面的信息
                    handler.removeMessages(0x01);
                    handler.sendEmptyMessageDelayed(0x01,1000);
                    if (playview != null){
                        playview.onPlayState(isPlaying(),getCurrentPosition(),getDuration());
                    }
                    if (mainMusicList != null){
                        mainMusicList.onPlayState(isPlaying());
                    }
                    if (visualizerAblumview != null){
                        visualizerAblumview.onPlayState(isPlaying(),getCurrentPosition(),getDuration());
                    }
                    break;
                case 0x03: //快速更新
                    for (MusicView.FristMusicList view : fristMusicLists){
                        view.onMediaList(FastALLMusicList,FastArtistLists,FastAlbumLists,FastPathLists);
                    }
                    if (mainMusicList != null){
                        mainMusicList.onMediaList(FastALLMusicList);
                    }
                    break;
                case 0x04: //慢速更新
                    /**更新当前播放的列表数据**/
                    if(CurrPlayListTagPosition != -1){
                        getPlayInfo(CurrPlayListTag,CurrPlayListTagPosition);
                        if (mainMusicList != null){
                            mainMusicList.onCurrPlayMedia(CurrPlayMediaList.get(CurrPlayListTagPosition));
                        }
                        if (visualizerAblumview != null){
                            visualizerAblumview.onCurrPlayMedia(CurrPlayMediaList.get(CurrPlayListTagPosition));
                        }
                        if (playview != null){
                            playview.onCurrPlayMedia(CurrPlayMediaList.get(CurrPlayListTagPosition));
                            playview.onPlayList(CurrPlayMediaList);
                        }
                        if (mainMusicList != null){
                            mainMusicList.onMediaList(FastALLMusicList);
                        }
                    }
                    for (MusicView.FristMusicList view : fristMusicLists){
                        view.onMediaList(ALLMusicList,ArtistLists,AlbumLists,PathLists);
                    }
                    if (secondview != null){
                        if (CurrPlayListTag == 3){
                                secondMedias = FastPathLists.get(showPostion).getPathMediaList();
                                secondTitle = FastPathLists.get(showPostion).getName();
                        }
                        secondview.onShowMusicListMedias(secondMedias,secondTitle,CurrPlayMedia,CurrPlayListTag == SecondTag);
                    }
                    break;
            }
        }
    }

    /**
     * 在二级歌曲列表（歌手，专辑，路径）点击了歌曲
     * 更新当前播放数据
     * 执行播放
     */
    public void setClickInSecond(int pos){
        CurrPlayMediaList = secondMedias;
        CurrPlayListTagPosition = pos;
        CurrPlayListTag = SecondTag;
        setCurrPlayMedia(pos);
        play(CurrPlayMedia);
    }

    /**
     * 在全歌曲列表点击了歌曲
     * 更新当前播放数据
     * CurrPlayListTag 用于查找当前播放的列表数据0:全歌曲  1歌手 2专辑 3文件夹
     * CurrPlayListTagPosition ：index
     * 执行播放
     */
    public static int CurrPlayListTag = 0;
    /**一个临时的tag值**/
    private int SecondTag = -1;
    public static int CurrPlayListTagPosition = -1;
    public void setClickInAllList(int tag,int TagPosition){
        CurrPlayListTag = tag;
        CurrPlayListTagPosition = TagPosition;
        getPlayInfo(tag,TagPosition);
        play(CurrPlayMedia);
    }

    /**
     * 在播放列表（歌手，专辑，路径）点击了歌曲
     * 更新当前播放数据
     * 执行播放
     */
    public void setClickInPlayViewList(int tag,int pos){
        getPlayInfo(tag,pos);
        /**执行播放操作**/
        play(CurrPlayMedia);
    }

    /**清空播放列表*/
    public void setClearCurrPlayMediaList(){
        CurrPlayMediaList.clear();
    }

    /**
     * 根据TAG来更新二级显示的列表数据
     */
    ArrayList<LMedia> secondMedias = new ArrayList<>();
    String secondTitle = "";
    /**注意这个值，这个值不同于CurrPlayListTagPosition
     * 是点击了（歌手专辑路径）列表的坐标值
     * 用于Second界面取数据
     */
    private int showPostion = -1;
    public void setShowListMedia(int tag,int pos){
        SecondTag = tag;
        showPostion = pos;
        switch (tag){
            case 1:
                secondMedias = FastArtistLists.get(pos).getArtistMediaList();
                secondTitle = FastArtistLists.get(pos).getName();
                break;
            case 2:
                secondMedias = FastAlbumLists.get(pos).getAlbumMediaList();
                secondTitle = FastAlbumLists.get(pos).getName();
                break;
            case 3:
                secondMedias = FastPathLists.get(pos).getPathMediaList();
                secondTitle = FastPathLists.get(pos).getName();
                break;
        }
    }

    public void getSecondData(){
        if (secondview != null){
            secondview.onShowMusicListMedias(secondMedias,secondTitle,CurrPlayMedia,CurrPlayListTag == SecondTag);
        }
    }
    public void onCurrMedia(){
        if (playview != null){
            playview.onCurrPlayMedia(CurrPlayMedia);
        }
        if (visualizerAblumview != null){
            visualizerAblumview.onCurrPlayMedia(CurrPlayMedia);
            visualizerAblumview.onMediaPlay(getMediaPlayer());
        }
        if (mainMusicList != null){
            mainMusicList.onCurrPlayMedia(CurrPlayMedia);
        }
        for (MusicView.FristMusicList view : fristMusicLists){
            view.onCurrPlayMedia(CurrPlayListTag,CurrPlayListTagPosition,secondTitle);
        }
        if (secondview != null){
            secondview.onShowMusicListMedias(secondMedias,secondTitle,CurrPlayMedia,CurrPlayListTag == SecondTag);
        }
    }

    private MediaPlayer mMediaPlayer;
    private void play(LMedia media) {
        if(prepare(media.getUrl()) == 0) {
            start();
            seekTo(0);
            handler.removeMessages(0x01);
            handler.sendEmptyMessage(0x01);
            onCurrMedia();
        }
    }

    AudioManager am ;
    private int prepare(String path) {
        getMediaPlayer().stop();
        getMediaPlayer().reset();
        try {
            getMediaPlayer().setDataSource(path);
            am.requestAudioFocus(afChangeListener , AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
            getMediaPlayer().prepareAsync();
            return 0;
        } catch (IllegalArgumentException e) {
            return -1;
        } catch (IllegalStateException e) {
            return -2;
        } catch (IOException e) {
            return -3;
        } catch (Exception e) {
            return -4;
        }
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                stop();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) 0.2f, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        }
    };

    public void start() {
        if (CurrPlayMedia != null && CurrPlayListTagPosition != -1){
            getMediaPlayer().start();
        }else if (CurrPlayMediaList != null && CurrPlayMediaList.size() >0 && CurrPlayListTagPosition != -1){
            setCurrPlayMedia(CurrPlayListTagPosition);
            play(CurrPlayMedia);
        }else if (CurrPlayMediaList != null && CurrPlayMediaList.size() >0){
            setCurrPlayMedia(0);
            play(CurrPlayMedia);
        }
    }

    public void pause() {
        getMediaPlayer().pause();
    }

    public boolean isPlaying() {
        return getMediaPlayer().isPlaying();
    }

    public void stop() {
        getMediaPlayer().stop();
        setCurrPlayMedia(-1);
        if (playview != null){
            playview.onCurrPlayMedia(CurrPlayMedia);
            playview.onPlayState(false,0,0);
        }
    }

    public int getCurrentPosition(){
        return getMediaPlayer().getCurrentPosition();
    }

    public int getDuration(){
        return getMediaPlayer().getDuration();
    }

    public void playPause() {
        if (isPlaying()){
            pause();
        }else {
            start();
        }
    }

    public void seekTo(int msec) {
        if(getMediaPlayer().isPlaying()) {
            int duration = getMediaPlayer().getDuration();
            if((msec > 0) && (duration > 0) && (msec < duration)) {
                getMediaPlayer().seekTo(msec);
            }
        }
    }

    public void next() {
        if (CurrPlayMediaList == null || CurrPlayMediaList.size() == 0){
            return;
        }
        CurrPlayMedia = NextPlayMedia;
        play(CurrPlayMedia);
        CurrPlayListTagPosition = NextPosition;
        setCurrPlayMedia(NextPosition);
    }

    public void prev() {
        if (CurrPlayMediaList == null || CurrPlayMediaList.size() == 0){
            return;
        }
        CurrPlayMedia = PrePlayMedia;
        CurrPlayListTagPosition = PrvePosition;
        play(CurrPlayMedia);
        setCurrPlayMedia(PrvePosition);
    }

    int PlayMode = 0;
    public void checkPlayMode() {
        PlayMode++;
        if (PlayMode > 2){
            PlayMode = 0;
        }
        if (playview != null) {
            playview.onPlayMode(PlayMode);
        }
        setCurrPlayMedia(CurrPlayListTagPosition);
    }


    private void getPlayInfo(int tag,int pos){
        switch (tag){
            case 0:
                CurrPlayMediaList = FastALLMusicList;
                CurrPlayListTagPosition = pos;
                setCurrPlayMedia(pos);
                break;
            case 1:
                secondMedias = FastArtistLists.get(showPostion).getArtistMediaList();
                break;
            case 2:
                secondMedias = FastAlbumLists.get(showPostion).getAlbumMediaList();
                break;
            case 3:
                secondMedias = FastPathLists.get(showPostion).getPathMediaList();
                break;
            case 4:
                CurrPlayListTagPosition = pos;
                setCurrPlayMedia(pos);
                break;
        }
    }

    public void setCurrPlayMedia(int CurrPlayMediaListPosition){
        if (CurrPlayMediaListPosition != -1){
            CurrPlayMedia = CurrPlayMediaList.get(CurrPlayMediaListPosition);
            setPrePlayMedia();
            setNextPlayMedia();
        }else {
            CurrPlayMedia = new LMedia();
            PrePlayMedia = new LMedia();
            NextPlayMedia = new LMedia();
        }
    }

    int PrvePosition = 0;
    public void setPrePlayMedia() {
        PrvePosition = CurrPlayListTagPosition;
        switch (PlayMode){
            case 0:
                PrvePosition--;
                if (PrvePosition < 0){
                    PrvePosition = CurrPlayMediaList.size()-1;
                }
                PrePlayMedia = CurrPlayMediaList.get(PrvePosition);
                break;
            case 1:
                PrePlayMedia = CurrPlayMedia;
                break;
            case 2:
                Random r = new Random();
                PrvePosition = r.nextInt(CurrPlayMediaList.size());
                PrePlayMedia = CurrPlayMediaList.get(PrvePosition);
                break;
        }
    }

    int NextPosition = 0;
    public void setNextPlayMedia() {
        NextPosition = CurrPlayListTagPosition;
        switch (PlayMode){
            case 0:
                NextPosition++;
                if (NextPosition >= CurrPlayMediaList.size()){
                    NextPosition = 0;
                }
                NextPlayMedia = CurrPlayMediaList.get(NextPosition);
                break;
            case 1:
                NextPlayMedia = CurrPlayMedia;
                break;
            case 2:
                Random r = new Random();
                NextPosition = r.nextInt(CurrPlayMediaList.size());
                NextPlayMedia = CurrPlayMediaList.get(NextPosition);
                break;
        }
    }
}
