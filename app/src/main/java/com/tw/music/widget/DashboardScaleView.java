package com.tw.music.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tw.music.R;

public class DashboardScaleView extends LinearLayout {

    private int dashboardType;
    private ImageView ivDasboardScale;
    private MyDashboardView dashboardView;
    private int[] ivDasboardscales = {R.drawable.guangquan_1, R.drawable.guangquan_2, R.drawable.guangquan_3, R.drawable.guangquan_4, R.drawable.guangquan_5,
            R.drawable.guangquan_6, R.drawable.guangquan_7, R.drawable.guangquan_8, R.drawable.guangquan_9, R.drawable.guangquan_10, R.drawable.guangquan_11, R.drawable.guangquan_12,
            R.drawable.guangquan_13, R.drawable.guangquan_14, R.drawable.guangquan_15};

    public interface DashboardType {
        int fl = 0;
        int fr = 1;
        int rl = 2;
        int rr = 3;
        int sound_1 = 4;
        int sound_2 = 5;
        int sound_3 = 6;
        int sound_4 = 7;
        int sound_5 = 8;
        int sound_6 = 9;
    }

    public interface OnValueChangeListener {
        void onValueChanged(DashboardScaleView dashboardView, int dashboardType, int position, boolean fromUser);
    }

    // private static final String TAG = EqScaleView.class.getSimpleName();
    private int maxValue = 14;
    private TextView titleTxt;
    private OnValueChangeListener listener;
    private int index = -1;
    public boolean fromUser;
    private Context context;

    public DashboardScaleView(Context context) {
        this(context, null);
    }

    public DashboardScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DashboardScaleView, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.DashboardScaleView_angle:
//                    value = a.getInt(attr, -100);
                    break;
                case R.styleable.DashboardScaleView_dashboardType:
                    dashboardType = a.getInt(attr, 0);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        titleTxt = (TextView) findViewById(R.id.title);
        ivDasboardScale = (ImageView) findViewById(R.id.dasboard_scale);
        dashboardView = (MyDashboardView) findViewById(R.id.dasboard_view);
        dashboardView.setOnAngleChangeListener(angleChangeListener);
        initTitle();
    }

    private void initTitle() {
        switch (dashboardType) {
            case DashboardType.fl:
                titleTxt.setText(R.string.surround_fl);
                break;
            case DashboardType.fr:
                titleTxt.setText(R.string.surround_fr);
                break;
            case DashboardType.rl:
                titleTxt.setText(R.string.surround_rl);
                break;
            case DashboardType.rr:
                titleTxt.setText(R.string.surround_rr);
                break;
            case DashboardType.sound_1:
                titleTxt.setText(R.string.sound_1);
                break;
            case DashboardType.sound_2:
                titleTxt.setText(R.string.sound_2);
                break;
            case DashboardType.sound_3:
                titleTxt.setText(R.string.sound_3);
                break;
            case DashboardType.sound_4:
                titleTxt.setText(R.string.sound_4);
                break;
            case DashboardType.sound_5:
                break;
            case DashboardType.sound_6:
                break;
            default:
        }
    }

    private MyDashboardView.OnAngleChangeListener angleChangeListener = new MyDashboardView.OnAngleChangeListener() {


        @Override
        public void onStartTrackingTouch(MyDashboardView pergrass, int position, boolean fromUser) {
            setDashboardScale(position);
        }

        @Override
        public void onStopTrackingTouch(MyDashboardView dashboardView, int position, boolean fromUser) {
            // TODO Auto-generated method stub
            if (listener != null) {
                setDashboardScale(position);
                listener.onValueChanged(DashboardScaleView.this, dashboardType, position, fromUser);
            }
        }

    };

    public void setValueChangeListener(OnValueChangeListener l) {
        this.listener = l;
    }


    public void setValue(int value, boolean fromUser) {
        updateValue();
        setDashboardScale(value);
        updateScaleBar(value);
    }

    /**
     * 设置表盘刻度光亮
     * @param scale
     */
    private void setDashboardScale(int scale){
        if(scale>=0&&scale<ivDasboardscales.length){
            ivDasboardScale.setImageResource(ivDasboardscales[scale]);
        }
    }
    private void setTitleTxt(String txt) {
        titleTxt.setText(txt + "Hz");
        //freq_HZ = txt;
    }

    private void updateValue() {
    }

    private void updateScaleBar(int progress) {
        dashboardView.setDashboardChange(progress);
        // EQLogUtils.i(TAG, "updateScaleBar(),progress = " + progress);
        ValueAnimator animator = ValueAnimator.ofFloat(0, progress);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (Float) animation.getAnimatedValue();
            }
        });
        animator.start();
    }


}
