package com.xy.media_lib.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.xy.media_lib.presenter.BasePresenter;
import com.xy.media_lib.view.BaseView;

/**
 * 实现该抽象类的子类必须实现getPresenter() 方法
 * getContentView() 设置布局文件
 * getPresenter() 设置 BasePresenter 对象，设置完成后，在子类中就可以使用 mPresenter 调用方法
 */
public abstract class MAppCompatActivity<P extends BasePresenter> extends AppCompatActivity {

    public P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT); //状态栏透明
//        fullScreen();
        initView();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    protected void initView(){
        if (mPresenter == null) {
            mPresenter = getPresenter();//获取 mPresenter
            if (this instanceof BaseView){
                mPresenter.add((BaseView) this);//将 view 传递到 mPresenter 中
            }
        }
    }

    /**
     * 获取 MusicPresenter,P 为泛型,返回相对应的Presenter,(RadioPresenter/BTPresenter/MusicPresenter/VideoPresenter/)
     * @return
     */
    public abstract P getPresenter();

    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		if(mPresenter != null){
			mPresenter.delete();//将 mPresenter 中的 view 删除，防止内存泄漏
			mPresenter=null;
		}
    }

    private void fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //导航栏颜色也可以正常设置
                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}
