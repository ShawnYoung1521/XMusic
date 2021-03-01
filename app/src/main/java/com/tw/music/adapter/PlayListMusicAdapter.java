package com.tw.music.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tw.music.R;
import com.xy.media_lib.bean.LMedia;

import java.util.ArrayList;

import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;
import cn.xy.library.util.spannable.XSpanned;

public class PlayListMusicAdapter extends BaseAdapter {
    public static ArrayList<LMedia> mALLMusicList;
    private LMedia xmedia;
    public PlayListMusicAdapter(){
    }
    public void notifyDataSetChanged(ArrayList<LMedia> ALLMusicList, LMedia media){
        mALLMusicList = ALLMusicList;
        xmedia = media;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(mALLMusicList == null) {
            return 0;
        } else{
            return mALLMusicList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        if(view == null) {
            v = newView(viewGroup);
        } else {
            v = view;
        }
        bindView(v, i, viewGroup);
        return v;
    }

    private void bindView(View v, int position, ViewGroup parent) {
        ViewHolder vh = (ViewHolder) v.getTag();
        vh.MusicName.setSelected(true);
        boolean isplaying = mALLMusicList.get(position).getUrl()==xmedia.getUrl();
        vh.MusicName.setText(mALLMusicList.get(position).getName());
        if (mALLMusicList.get(position).getName() != null && !mALLMusicList.get(position).getName().isEmpty() && mALLMusicList.get(position).getName().length()>0){
            XSpanned.with(vh.MusicName)
                    .append(mALLMusicList.get(position).getName())
                    .setFontSize(XConvert.dp2px(14))
                    .setForegroundColor(isplaying?XApp.getApp().getResources().getColor(R.color.colorPrimary):XApp.getApp().getResources().getColor(R.color.black))
                    .append(mALLMusicList.get(position).getArtist())
                    .setFontSize(XConvert.dp2px(10))
                    .setForegroundColor(isplaying?XApp.getApp().getResources().getColor(R.color.colorPrimary):XApp.getApp().getResources().getColor(R.color.black))
                    .create();
        }
        vh.spectrumView.setVisibility(isplaying?View.VISIBLE:View.GONE);
    }

    private View newView(ViewGroup parent) {
        View v = LayoutInflater.from(XApp.getApp()).inflate(R.layout.item_playlist_view, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.MusicName = (TextView) v.findViewById(R.id.all_music_name);
        vh.spectrumView = (ImageView) v.findViewById(R.id.playview_item_icon);
        v.setTag(vh);
        return v;
    }

    private class ViewHolder {
        TextView MusicName;
        ImageView spectrumView;
    }
}
