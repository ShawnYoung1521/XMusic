package com.tw.music.utils;

import com.tw.music.R;

import java.util.Locale;

public class Utils {
    public static String timelongToString(int totaltime){
        totaltime = totaltime / 1000;
        int stotaltime = totaltime;
        int mtotaltime = stotaltime / 60;
        int htotaltime = mtotaltime / 60;
        stotaltime %= 60;
        mtotaltime %= 60;
        htotaltime %= 24;
        if (htotaltime == 0) {
            return String.format(Locale.US, "%d:%02d", mtotaltime, stotaltime);
        } else {
            return String.format(Locale.US, "%d:%02d:%02d", htotaltime, mtotaltime, stotaltime);
        }
    }
}
