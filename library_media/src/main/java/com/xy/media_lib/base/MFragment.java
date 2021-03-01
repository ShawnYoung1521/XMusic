package com.xy.media_lib.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.xy.media_lib.presenter.BasePresenter;
import com.xy.media_lib.view.BaseView;

public abstract class MFragment<P extends BasePresenter> extends Fragment implements BaseView {
    private static final String TAG = "MFragment";
    public P mPresenter;
    protected Context mContext;
    public abstract P createPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (mPresenter == null) {
            mPresenter = createPresenter();
            if(mPresenter != null){
                this.mPresenter.add(this);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null){
            mPresenter.delete();
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }


}
