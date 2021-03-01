package com.xy.media_lib.presenter;


import android.content.Context;
import android.os.Handler;

import com.xy.media_lib.model.BaseModel;
import com.xy.media_lib.view.BaseView;

import java.lang.ref.WeakReference;


public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {
    public Context mContext;
    private WeakReference<V> mWeakReference;
    public M mModel;
    public Handler mHandler = new Handler();
    public BasePresenter(Context context) {
        this.mContext=context;
        mModel = getModel();
    }

    /**
     * 获取 model，实现该抽象类的子类必须实现此方法，之后就可以使用 mModel操作Model层业务逻辑
     * @return
     */
    public abstract M getModel();

    /**
     * 将 view 添加到弱引用中
     * @param view
     */
    public void add(V view) {
        mWeakReference = new WeakReference<V>(view);
    }

    /**
     * 获取弱引用中的 view
     * @return
     */
    public V get() {
        return mWeakReference == null ? null : mWeakReference.get();
    }

    /**
     * 删除弱引用中的 view
     */
    public void delete() {
        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }
    }
    public void onDestroy(){

    }
}
