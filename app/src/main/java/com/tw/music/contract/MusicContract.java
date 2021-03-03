package com.tw.music.contract;

public class MusicContract {
    public static String permission_FORCE_STOP_PACKAGES = "android.permission.FORCE_STOP_PACKAGES";
    public static String permission_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static String permission_READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static String permission_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static String permission_SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";
    public static String[] FileReadPermissions = {
            permission_READ_EXTERNAL_STORAGE
            ,permission_WRITE_EXTERNAL_STORAGE
            /*,permission_SYSTEM_ALERT_WINDOW*/};
    public static String[] RecordPermissions = {
            permission_RECORD_AUDIO
    };

    public static String PathStorage = "/storage/emulated/0/Music/";
    public static String PathUsb1 = "/usb1/";
    public static String PathUsb2 = "/usb2/";
    public static String PathExtsd1 = "/extsd/";

    /*
    0:全歌曲 1歌手 2专辑 3文件夹
     */
    public static int AllMusicTag = 0;
    public static int ArtistTag = 1;
    public static int AlbumTag = 2;
    public static int PathTag = 3;
    public static int PlayListTag = 4;
    public static int SecondTag = 5;


    public final static int REFRESH_REQUEST = 0xf000;
    public final static int REFRESH_SUCCESS = 0xf001;
    public final static int REFRESH_FAILED  = 0xf002;
}
