package com.xy.media_lib.utils;


import android.content.Context;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.Virtualizer;
import android.os.Handler;
import android.util.Log;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.sharedpreferences.XSPUtils;

public class EQManage {
    private static final String TAG = "EQManage";
    private static final String EQ_MODE_TABLE = "EQ_MODE";
    private static final String EQ_MODE_NAME = "eqmode_item";
    private static final String EQ_MODE_USER_TABLE = "EQ_MODE_USER";
    private static final String EQ_MODE_USER_NAME = "eqmode_user";
    private static final String EQ_LOUNDNESS_ENHANCER_NAME = "eq_loudnessEnhancer";
    private static final String EQ_ENVIRONMENTTAL_REVERB_NAME = "eq_environmentttal_reberb";
    private static Virtualizer mVirtualizer;
    private Equalizer mEqualizer;
    private short minEQLevel=-1000 ;
    private short maxEQLevel;
    private OnTWCallBack callBack;
    private OnEQValueChangeListenner eqValueChangeListenner;
    private Context mContext;
    private short brands;
    private EnvironmentalReverb environmentalReverb;
    private static LoudnessEnhancer loudnessEnhancer;
    //    public int[][] modeValues = { { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 },// 自定义
//            { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 },// 普通
//            //kugou
//            { 14, 13, 6, 4, 10, 10, 13, 14, 14, 15 },// 舞曲
//            { 16, 15, 7, 8, 15, 14, 6, 7, 16, 14 },// 流行
//            { 16, 17, 11, 12, 9, 11, 6, 4, 3, 2 }, //古典
//            { 17, 16, 12, 11, 7, 6, 12, 11, 14, 15 }, // 摇滚
//            { 5, 5, 6, 6, 13, 12, 14, 14, 10, 10 }, //柔和
//            { 5, 4, 6, 7, 13, 14, 15, 14, 7, 7 } //人声
//
////            { 15, 12, 10, 8, 9, 11, 12, 10, 14, 15 },// 爵士
////            { 13, 14, 15, 10, 7, 12, 13, 10, 15, 14 },// 流行
////            { 12, 13, 10, 11, 10, 6, 10, 11, 14, 15 }, //古典
////            { 15, 13, 11, 12, 11, 11, 13, 10, 18, 13 }, //摇滚
////            { 7, 8, 9, 12, 10, 14, 8, 11, 13, 7 }, //natural
////            { 5, 8, 11, 10, 15, 12, 14, 10, 6, 10 } //vocal
//            };
    public int[][] modeValues = {{6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6},// 自定义
            {6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6},// 普通
            //kugou
//            {14, 13, 12, 6, 4, 5, 10, 10, 10, 13, 14, 15, 14, 15, 16},// 舞曲
//            {16, 15, 13, 7, 8, 9, 15, 14, 13, 6, 7, 8, 16, 14, 12},// 流行
//            {16, 17, 18, 11, 12, 13, 9, 11, 12, 6, 4, 5, 3, 2, 4}, //古典
//            {17, 16, 15, 12, 11, 10, 7, 6, 5, 12, 11, 10, 14, 15, 16}, // 摇滚
//            {5, 5, 5, 6, 6, 6, 13, 12, 11, 14, 14, 14, 10, 10, 10}, //柔和
//            {5, 4, 3, 6, 7, 8, 13, 14, 15, 15, 14, 13, 7, 7, 7} //人声

            {8, 9, 7, 4, 3, 5, 5, 6, 7, 9, 8, 7, 6, 8, 9},// 舞曲
            {6, 8, 5, 7, 8, 5, 6, 8, 9, 6, 3, 8, 6, 8, 5},// 流行
            {8, 9, 10, 9, 7, 6, 9, 6, 8, 6, 3, 5, 3, 2, 4}, //古典
            {6, 9, 8, 6, 7, 8, 7, 4, 5, 6, 7, 8, 6, 9, 6}, // 摇滚
            {5, 3, 5, 6, 4, 7, 8, 7, 9, 6, 8, 6, 8, 6, 3}, //柔和
            {5, 3, 3, 6, 5, 8, 6, 7, 8, 6, 7, 8, 7, 4, 7} //人声

//            { 15, 12, 10, 8, 9, 11, 12, 10, 14, 15 },// 爵士
//            { 13, 14, 15, 10, 7, 12, 13, 10, 15, 14 },// 流行
//            { 12, 13, 10, 11, 10, 6, 10, 11, 14, 15 }, //古典
//            { 15, 13, 11, 12, 11, 11, 13, 10, 18, 13 }, //摇滚
//            { 7, 8, 9, 12, 10, 14, 8, 11, 13, 7 }, //natural
//            { 5, 8, 11, 10, 15, 12, 14, 10, 6, 10 } //vocal
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x0101: {
                    byte[] obj = (byte[]) msg.obj;
                    if (callBack != null) {
                        callBack.onBassChange(obj[1]);// bass

                        callBack.onHighChange(obj[3]);// high
                        callBack.onLoundChange(obj[5]);// lound
                        callBack.onZoneChange(obj[7], obj[6]);// zone
                    }

                    break;
                }
                case 0x0106: {
                    byte obj[] = (byte[]) msg.obj;
                    callBack.onNaviMusicVolume(obj[0], obj[1]);
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public void setOnTWCallBack(OnTWCallBack callBack) {
        this.callBack = callBack;
    }

    public void setOnEQValueChangeListenner(OnEQValueChangeListenner listnner) {
        this.eqValueChangeListenner = listnner;
    }

    private static EQManage eqManage;

    public static EQManage getInstant(Context context) {
        if (eqManage == null) {
            eqManage = new EQManage(context);
        }
        return eqManage;

    }

    private EQManage(Context context) {
        this.mContext = context;
    }

    /**
     * 初始化均衡器
     *
     * @param audio_session_id
     */
    public void InitEQ(final int audio_session_id) {
        Log.d(TAG, "EQManage:audio_session_id:" + audio_session_id);
        try {
            if (audio_session_id != 0) {
                if (mEqualizer != null) {
//                    mEqualizer.release();
                    mEqualizer = null;
                }

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mEqualizer = new Equalizer(0, audio_session_id);
                        mEqualizer.setEnabled(true);
                        // 获取均衡控制器支持最小值和最大值
//                        minEQLevel = mEqualizer.getBandLevelRange()[0]; //-1500
//                        maxEQLevel = mEqualizer.getBandLevelRange()[1]; //1500
                        // 获取均衡控制器支持的所有频率
                        brands = mEqualizer.getNumberOfBands();
                    }
                }, 10);
                getUserModeValue();
                initEnvironmentalReverb(audio_session_id);
                initLoudnessEnhancer(audio_session_id);//有问题，影响增益
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setEQMode(eqManage.getEqMode());
                    }
                }, 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化混响
     *
     * @param audio_session_id
     */
    private void initEnvironmentalReverb(int audio_session_id) {
        if (environmentalReverb != null) {
            environmentalReverb = null;
        }
        environmentalReverb = new EnvironmentalReverb(0, audio_session_id);
        environmentalReverb.setEnabled(true);
        setEnvironmentalReverb(getEnvironmentalReverb());
    }

    public void initLoudnessEnhancer(int audio_session_id) {
        if (loudnessEnhancer != null) {
            loudnessEnhancer = null;
        }
        loudnessEnhancer = new LoudnessEnhancer(audio_session_id);
        loudnessEnhancer.setEnabled(true);
        setLoudnessEnhancer(getLoundnessEnhancer());
    }

    /**
     * Sets the volume level of the late reverberation.
     * <p>This level is combined with the overall room level (set using  setRoomLevel(short)}).
     *
     * @param reverbLevel reverb level in millibels. The valid range is [-9000, 2000].
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    public void setEnvironmentalReverb(int reverbLevel) {
        XLog.i("setEnvironmentalReverb:  "+reverbLevel);
        XSPUtils.getInstance().put(EQ_ENVIRONMENTTAL_REVERB_NAME, reverbLevel);
        reverbLevel = (2000 - reverbLevel * 11000 / 14);
        if (environmentalReverb != null) {
            environmentalReverb.setReverbLevel((short) reverbLevel);
            if (reverbLevel <= 0) {
                environmentalReverb.setRoomLevel((short) reverbLevel);
            }
        }
    }

    /**
     * 获取环响
     */
    public int getEnvironmentalReverb() {
        return XSPUtils.getInstance().getInt(EQ_ENVIRONMENTTAL_REVERB_NAME, 0);
    }

    /**
     * 设置响度
     *
     * @param position
     */
    public void setLoudnessEnhancer(int position) {
        XLog.i("setLoudnessEnhancer:  "+position);
        XSPUtils.getInstance().put(EQ_LOUNDNESS_ENHANCER_NAME, position);
        int gainmB = position * 1000 / 14;
        XLog.i((loudnessEnhancer != null));
        if (loudnessEnhancer != null) {
            loudnessEnhancer.setTargetGain(gainmB);
        }
    }

    /**
     * 获取响度
     */
    public int getLoundnessEnhancer() {
        return XSPUtils.getInstance().getInt(EQ_LOUNDNESS_ENHANCER_NAME, 14);
    }

    /**
     * 调节EQ
     *
     * @param band  波段值 0：60HZ
     * @param level
     */
    public void setEqBandLevel(int band, int index, int level) {
        try {
            if (getEqMode() == 0) {
                modeValues[0][band * 3 + index] = level;
                saveUserModeValue();
            }
            if (mEqualizer != null) {
                mEqualizer.setBandLevel((short) band, (short) (level * 100 + minEQLevel));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 模式
     * @param mode
     */
    public void setEQMode(int mode) {
        XSPUtils.getInstance().put(EQ_MODE_NAME, mode);
        if (mode >= 0 && mode < modeValues.length) {
            if (eqValueChangeListenner != null) {
                eqValueChangeListenner.onEQValueChange(mode, modeValues[mode]);
            }
            try {
                for (int i = 0; i < 5; i++) {
                    if (mEqualizer != null) {
                        mEqualizer.setBandLevel((short) i, (short) (modeValues[mode][i * 3 + 1] * 100 + minEQLevel));
                    } else {
                        Log.d(TAG, "error mEqualizer==null");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前模式
     *
     * @return
     */
    public int getEqMode() {
        return XSPUtils.getInstance().getInt(EQ_MODE_NAME, 0);
    }

    /**
     * 获取自定义保存得值
     */
    public void getUserModeValue() {
        for (int i = 0; i < modeValues[0].length; i++) {
            modeValues[0][i] = XSPUtils.getInstance().getInt(EQ_MODE_USER_NAME + i, 6);
        }
    }

    /**
     * 保存自定义的值
     */
    public void saveUserModeValue() {
        for (int i = 0; i < modeValues[0].length; i++) {
            XSPUtils.getInstance().put(EQ_MODE_USER_NAME + i, modeValues[0][i]);
        }
    }

    public interface OnTWCallBack {
        /**
         * bass
         *
         * @param value
         */
        void onBassChange(int value);

        /**
         * high
         *
         * @param value
         */
        void onHighChange(int value);

        /**
         * lound
         *
         * @param value
         */
        void onLoundChange(int value);

        /**
         * zone
         *
         * @param x
         * @param y
         */
        void onZoneChange(int x, int y);

        /**
         * 导航音量
         *
         * @param navi
         * @param music
         */
        void onNaviMusicVolume(int navi, int music);
    }

    public interface OnEQValueChangeListenner {
        /**
         * bass
         *
         * @param eqMode 模式
         * @param values 模式值
         */
        void onEQValueChange(int eqMode, int[] values);
    }
}
