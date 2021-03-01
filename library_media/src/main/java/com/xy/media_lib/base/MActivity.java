package com.xy.media_lib.base;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
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
public abstract class MActivity<P extends BasePresenter> extends Activity {

    public P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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
}
