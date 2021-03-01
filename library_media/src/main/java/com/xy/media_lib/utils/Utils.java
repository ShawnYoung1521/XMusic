package com.xy.media_lib.utils;

import com.xy.media_lib.bean.AlbumMedia;
import com.xy.media_lib.bean.ArtistMedia;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.bean.PathMedia;
import com.xy.media_lib.view.MusicView;
import java.util.ArrayList;


public class Utils {
    /**根据该歌手否存在与歌手列表中**/
    public static boolean isArtistInArtistList(ArrayList<ArtistMedia> mArtistList, String Artist) {
        for(ArtistMedia media: mArtistList){
            if(media.getName().equals(Artist))
                return true;
        }
        return false;
    }

    /**根据歌手找到该歌手对应的ArtistMedia**/
    public static ArtistMedia getArtistIsArtistLists(ArrayList<ArtistMedia> ArtistLists, String Artist){
        for(ArtistMedia media: ArtistLists){
            if(media.getName().equals(Artist)) {
                return media;
            }
        }
        return null;
    }

    /**根据该专辑否存在与专辑列表中**/
    public static boolean isAlbumInAlbumList(ArrayList<AlbumMedia> mArtistList, String Album) {
        for(AlbumMedia media: mArtistList){
            if(media.getName().equals(Album))
                return true;
        }
        return false;
    }

    /**根据专辑找到该专辑对应的AlbumMedia**/
    public static AlbumMedia getAlbumIsAlbumLists(ArrayList<AlbumMedia> AlbumLists, String Album){
        for(AlbumMedia media: AlbumLists){
            if(media.getName().equals(Album)) {
                return media;
            }
        }
        return null;
    }

    /**根据该文件夹路径否存在与路径列表中**/
    public static boolean isPathInPathList(ArrayList<PathMedia> mArtistList, String Path) {
        for(PathMedia media: mArtistList){
            if(media.getUrl().equals(Path))
                return true;
        }
        return false;
    }

    /**根据路径找到该路径对应的PathMedia**/
    public static PathMedia getPathIsAlbumLists(ArrayList<PathMedia> PathLists, String path){
        for(PathMedia media: PathLists){
            if(media.getUrl().equals(path)) {
                return media;
            }
        }
        return null;
    }

    public static boolean isPInPs(ArrayList<MusicView.FristMusicList> mViews, MusicView.FristMusicList musicView) {
        for(MusicView.FristMusicList media: mViews){
            if(media.equals(musicView))
                return true;
        }
        return false;
    }

    public static int isMediaInMedias(ArrayList<LMedia> PathLists, LMedia media) {
        for (int i = 0; i< PathLists.size(); i++){
            if (PathLists.get(i).getUrl() == media.getUrl()){
                return i;
            }
        }
        return 0;
    }


    public static int isAritstInAritsts(ArrayList<ArtistMedia> ArtistLists, String name) {
        for (int i = 0; i< ArtistLists.size(); i++){
            if (ArtistLists.get(i).getName().equals(name)){
                return i;
            }
        }
        return 0;
    }

    public static int isAlbumInAAlbums(ArrayList<AlbumMedia> AlbumLists, String name) {
        for (int i = 0; i< AlbumLists.size(); i++){
            if (AlbumLists.get(i).getName().equals(name)){
                return i;
            }
        }
        return 0;
    }

    public static int isPathNameInPaths(ArrayList<PathMedia> PathLists, String name) {
        for (int i = 0; i< PathLists.size(); i++){
            if (PathLists.get(i).getName().equals(name)){
                return i;
            }
        }
        return 0;
    }
}
