package com.xy.media_lib.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.xy.media_lib.R;
import com.xy.media_lib.bean.LMedia;
import java.util.ArrayList;
import java.util.List;

import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;

public class LoadMediaFile {
    /**
     * 获取本地视频信息
     **/
    public static List<LMedia> getVideoList() {
        ArrayList<LMedia> mALLVideoList = new ArrayList<>();
        if (XApp.getApp() != null) {
            Cursor cursor = XApp.getApp().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String mimeType /*= cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))*/;
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    String size = XConvert.byte2FitMemorySize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)),0);
                    int index = path.lastIndexOf(".")+1;
                    mimeType = path.substring(index);
                    LMedia video = new LMedia(title, size, path, duration, id, mimeType,album,artist,displayName,null);
                    video.setName(title);
                    video.setSize(size);
                    video.setUrl(path);
                    video.setDuration(duration);
                    video.setId(id);
                    video.setMediaType(mimeType);
                    mALLVideoList.add(video);
                }
                cursor.close();
            }
        }
        return mALLVideoList;
    }
    /**
     * 获取本地音乐信息
     **/
    public static ArrayList<LMedia> getMusicList(boolean isFast) {
        ArrayList<LMedia> mALLMusicList = new ArrayList<>();
        if (XApp.getApp() != null) {
            Cursor cursor = XApp.getApp().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    int artistID = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String mimeType /*= cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))*/;
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    String size = XConvert.byte2FitMemorySize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)),0);
                    int index = path.lastIndexOf(".")+1;
                    mimeType = path.substring(index);
                    int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    Bitmap bitmap = null;
                    Bitmap bitmap1 = null;
                    if (!isFast){
                        bitmap = getAlbumArt(albumId);
//                        bitmap1 = getArtiseArt(artistID);
                    }
                    mALLMusicList.add(new LMedia(title, size, path, duration, id, mimeType,album,artist,displayName,bitmap));
                }
                cursor.close();
            }
        }
        return mALLMusicList;
    }
    /**
     * 根据专辑ID获取专辑封面图
     * @param album_id 专辑ID
     * @return
     */
    public static Bitmap getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = XApp.getApp().getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {
            bm = null;
        }
        return bm;
    }

    /**
     * 根据专辑ID获取专辑封面图
     * @param Artise_id 专辑ID
     * @return
     */
    public static Bitmap getArtiseArt(int Artise_id) {
        String mUriAlbums = "content://media/external/audio/artists";
        String[] projection = new String[]{"artist_art"};
        Cursor cur = XApp.getApp().getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(Artise_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {
            bm = null;
        }
        return bm;
    }

}
