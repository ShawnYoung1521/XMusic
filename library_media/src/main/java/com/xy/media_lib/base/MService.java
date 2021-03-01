package com.xy.media_lib.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xy.media_lib.presenter.BasePresenter;
import com.xy.media_lib.view.BaseView;

public abstract class MService< P extends BasePresenter> extends Service {

	private static final String TAG = "XService";
	public P mPresenter;

	protected void initView(){
		if (mPresenter == null) {
			mPresenter = getPresenter();//获取 mPresenter
			if (this instanceof BaseView){
				mPresenter.add((BaseView) this);//将 view 传递到 mPresenter 中
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initView();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 获取 MusicPresenter,P 为泛型,返回相对应的Presenter,(RadioPresenter/BTPresenter/MusicPresenter/VideoPresenter/)
	 * @return
	 */
	public abstract P getPresenter();

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPresenter.delete();//将 mPresenter 中的 view 删除，防止内存泄漏
		mPresenter=null;
	}

}
