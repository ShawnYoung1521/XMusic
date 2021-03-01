package com.xy.media_lib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import com.xy.media_lib.bean.AlbumMedia;
import com.xy.media_lib.bean.ArtistMedia;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.bean.PathMedia;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import cn.xy.library.util.file.XFile;
import cn.xy.library.util.log.XLog;

public class LoadMusicFile {
    private static LoadMusicFile mLoadMusicFile;
    static ArrayList<LMedia> FastALLMusicList = new ArrayList<>();
    static ArrayList<ArtistMedia>  FastArtistLists = new ArrayList<>();
    static ArrayList<AlbumMedia>  FastAlbumLists = new ArrayList<>();
    static ArrayList<PathMedia>  FastPathLists = new ArrayList<>();

    static ArrayList<LMedia> ALLMusicList = new ArrayList<>();
    static ArrayList<ArtistMedia> ArtistLists = new ArrayList<>();
    static ArrayList<AlbumMedia> AlbumLists = new ArrayList<>();
    static ArrayList<PathMedia> PathLists = new ArrayList<>();

    static MediaMetadataRetriever mediaMetadataRetriever;
    static List<File> FastMusicfiles = new ArrayList<>();
    static List<File> Musicfiles = new ArrayList<>();

    List FastListData;
    List AllListData;
    public List getFastAllMusicList(){
        return FastListData;
    }

    public List getAllMusicList(){
        return AllListData;
    }

    private LoadMusicFile() {
        mediaMetadataRetriever = new MediaMetadataRetriever();
    }

    public LoadMusicFile FastUpdate(String path,boolean isFast){
        FastMusicfiles = allFilesInDir(path,new ArrayList<File>());
        FastALLMusicList = new ArrayList<>();
        FastArtistLists = new ArrayList<>();
        FastAlbumLists = new ArrayList<>();
        FastPathLists = new ArrayList<>();
        FastListData = new ArrayList();
        for (File s : FastMusicfiles){
            getFastMusicinfo(s);
        }
        FastListData.add(FastALLMusicList);
        FastListData.add(FastArtistLists);
        FastListData.add(FastAlbumLists);
        FastListData.add(FastPathLists);
        return mLoadMusicFile;
    }

    public LoadMusicFile Allupdate(String path,boolean isFast){
        Musicfiles = allFilesInDir(path,new ArrayList<File>());
        ALLMusicList = new ArrayList<>();
        ArtistLists = new ArrayList<>();
        AlbumLists = new ArrayList<>();
        PathLists = new ArrayList<>();
        AllListData = new ArrayList();
        for (File s : Musicfiles){
            getAllMusicinfo(s);
        }
        AllListData.add(ALLMusicList);
        AllListData.add(ArtistLists);
        AllListData.add(AlbumLists);
        AllListData.add(PathLists);
        return mLoadMusicFile;
    }



    public static LoadMusicFile getInstance() {
        if (mLoadMusicFile == null) {
            mLoadMusicFile = new LoadMusicFile();
            return mLoadMusicFile;
        } else {
            return mLoadMusicFile;
        }
    }

    private static List<File> allFilesInDir(String path, List<File> fileNameList) {
        if (XFile.isDir(path)) {
            List<File> files = XFile.listFilesInDir(path);
            for (File f:files){
                if (XFile.isDir(f)) {
                    allFilesInDir(f.getPath(),fileNameList);
                }else {
                    if (XFile.isMusicFile(XFile.getFileExtension(f))){
                        fileNameList.add(f);
                    }
                }
            }
        } else {
            if (XFile.isMusicFile(XFile.getFileExtension(XFile.getFileByPath(path)))){
                fileNameList.add(XFile.getFileByPath(path));
            }
        }
        return fileNameList;
    }

    private synchronized static void getFastMusicinfo(File f){
        String title = "未知";
        String artist = "未知";
        String album = "未知";
        Bitmap albumBitemap = null;
        title = f.getName().substring(0,f.getName().lastIndexOf("."));
        LMedia media = new LMedia(title,f.getPath(),album,artist,albumBitemap);
        FastALLMusicList.add(media);

        String o = f.getAbsolutePath();
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

    private synchronized static void getAllMusicinfo(File f){
        String title = "未知";
        String artist = "未知";
        String album = "未知";
        Bitmap albumBitemap = null;
        try {
            mediaMetadataRetriever.setDataSource(f.getAbsolutePath());
            title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            byte albumArt[] = mediaMetadataRetriever.getEmbeddedPicture();
            if (albumArt != null) {
                albumBitemap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);
            }
            if (title == null){
                title = f.getName().substring(0,f.getName().lastIndexOf("."));/*用文件名显示歌曲名*/
            }
            if (album == null){
                album = "未知";
            }
            if (artist == null){
                artist = "未知";
            }
            LMedia media = new LMedia(title,f.getPath(),album,artist,albumBitemap);
            ALLMusicList.add(media);

            String o = f.getAbsolutePath();
            int index = o.lastIndexOf("/");
            String path = o.substring(0,index);
            String oo=o.substring(o.lastIndexOf("/",o.lastIndexOf("/")-1)+1);
            int index2 = oo.lastIndexOf("/");
            String pathName = oo.substring(0,index2);
            PathMedia mPathMedia = new PathMedia(pathName,path,media);
            if (Utils.isPathInPathList(PathLists,path)){
                PathMedia media_Path = Utils.getPathIsAlbumLists(PathLists,path);
                media_Path.getPathMediaList().add(media);
            }else {
                PathLists.add(mPathMedia);
            }
            ArtistMedia mArtistMedia = new ArtistMedia(artist,media);
            if (Utils.isArtistInArtistList(ArtistLists,artist)){
                ArtistMedia media_Artist = Utils.getArtistIsArtistLists(ArtistLists,artist);
                media_Artist.getArtistMediaList().add(media);
            }else {
                ArtistLists.add(mArtistMedia);
            }
            if (albumBitemap != null){
                int indexs = Utils.isAritstInAritsts(ArtistLists,artist);
                ArtistLists.get(indexs).setAritistbitmap(albumBitemap);
            }

            AlbumMedia mAlbumMedia = new AlbumMedia(album,media);
            if (Utils.isAlbumInAlbumList(AlbumLists,album)){
                AlbumMedia media_Album = Utils.getAlbumIsAlbumLists(AlbumLists,album);
                media_Album.getAlbumMediaList().add(media);
            }else {
                AlbumLists.add(mAlbumMedia);
            }
            if (albumBitemap != null){
                int indexs = Utils.isAlbumInAAlbums(AlbumLists,album);
                AlbumLists.get(indexs).setAlbumBp(albumBitemap);
            }
        }catch (Exception e){
            XLog.i(f.getAbsolutePath()+" "+e.getMessage());
        }
    }
}
