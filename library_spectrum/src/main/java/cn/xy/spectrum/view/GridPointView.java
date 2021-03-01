package cn.xy.spectrum.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.xy.spectrum.AppConstant;

/**
 * 悦动网格
 */
public class GridPointView extends View {
    //每一格宽度
    private int landscapeWidth = 50;
    //每一格高度
    private int verticalWidth = 50;
    //横向格子数量
    private int landscapeCount;
    //纵向格子数量
    private int verticaCount;
    //背景网格
    private List<Rect> rectList = new ArrayList<>();
    //实时网格
    private List<Rect> showList = new ArrayList<>();
    private Paint paint = new Paint();
    private Random random = new Random();

    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    public GridPointView(Context context) {
        this(context, null);
    }

    public GridPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
    }

    public void setWaveData(byte[] data) {
        if (!enable) {
            return;
        }
        float energy = 0f;
        for (int i = 0; i < data.length; i++) {
            energy += Math.abs(data[i]);
        }
//        Log.e("SSSSS",energy+"");
        showList.clear();
        for (int i = 0; i < energy / (AppConstant.isFFT ? 100 : 1000); i++) {
            showList.add(rectList.get(random.nextInt(rectList.size() - 1)));
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectList.clear();
        landscapeCount = w / landscapeWidth;
        verticaCount = w / verticalWidth;
        for (int i = 0; i < landscapeCount; i++) {
            for (int j = 0; j < verticaCount; j++) {
                Rect rect = new Rect();
                if (rectList.size() == 0) {
                    rect.left = 0;
                    rect.top = 0;
                    rect.right = rect.left + landscapeWidth;
                    rect.bottom = rect.top + verticalWidth;
                } else {
                    rect.left = i == 0 ? 0 : i * landscapeWidth;
                    rect.top = j == 0 ? 0 : j * verticalWidth;
                    rect.right = rect.left + landscapeWidth;
                    rect.bottom = rect.top + verticalWidth;
                }
                rectList.add(rect);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(AppConstant.COLOR);
        for (int i = 0; i < rectList.size(); i++) {
            canvas.drawRect(rectList.get(i), paint);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(AppConstant.COLOR);
        for (int i = 0; i < showList.size(); i++) {
            canvas.drawRect(showList.get(i), paint);
        }
    }
}
