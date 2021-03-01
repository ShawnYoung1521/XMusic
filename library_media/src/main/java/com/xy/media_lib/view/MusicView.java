package com.xy.media_lib.view;

import android.media.MediaPlayer;

import com.xy.media_lib.bean.AlbumMedia;
import com.xy.media_lib.bean.ArtistMedia;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.bean.PathMedia;
import java.util.ArrayList;

public interface MusicView extends BaseView {

    /**播放界面**/
    interface PlayView extends MusicView{
        /*0 是 全循环  1 是 单曲  2 是 随机*/
        void onPlayMode(int mode);
        /**用于更新正在播放的音乐的进度/播放状态**/
        void onPlayState(boolean isPlaying,int CurrentPosition,int Duration);
        /**用于传输正在播放的歌手信息的数据，用于更新UI等操作*/
        void onCurrPlayMedia(LMedia media);
        /**用于传输正在播放的歌曲列表数据，用于更新播放列表数据*/
        void onPlayList(ArrayList<LMedia> MusicList);
    }

    /**启动界面**/
    interface MainMusicList extends MusicView{
        /**用于传输正在播放的歌手信息的数据，用于更新UI等操作*/
        void onCurrPlayMedia(LMedia media);
        /**用于更新正在播放的音乐的进度/播放状态**/
        void onPlayState(boolean isPlaying);
        void onMediaList(ArrayList<LMedia> ALLMusicList);

    }

    /**一级列表界面**/
    interface FristMusicList extends MusicView{
        /**用于传输扫描出来的所有音乐列表数据*/
        void onMediaList(ArrayList<LMedia> ALLMusicList,ArrayList<ArtistMedia> ArtistLists, ArrayList<AlbumMedia> AlbumLists,ArrayList<PathMedia> PathLists);
        /**用于传输正在播放的歌手信息的数据，用于更新UI等操作*/
        void onCurrPlayMedia(int tag,int pos,String name);
    }

    /**二级列表界面**/
    interface SecondMusicList extends MusicView{
        /**用于传输二级列表界面的数据*/
        void onShowMusicListMedias(ArrayList<LMedia> MusicList,String title,LMedia CurrPlayMedia,boolean isPlayingTag);
    }

    interface VisualizerAblum extends MusicView{
        /**用于传输正在播放的歌手信息的数据，用于更新UI等操作*/
        void onCurrPlayMedia(LMedia media);
        /**用于更新正在播放的音乐的进度/播放状态**/
        void onPlayState(boolean isPlaying,int CurrentPosition,int Duration);
        /**用于频谱**/
        void onMediaPlay(MediaPlayer mediaPlayer);
    }
}
