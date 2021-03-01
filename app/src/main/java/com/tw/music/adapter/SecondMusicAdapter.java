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

public class SecondMusicAdapter extends BaseAdapter {
    public static ArrayList<LMedia> mALLMusicList;
    LMedia CurrPlayMedia;
    private boolean isPlayingTag;
    public SecondMusicAdapter(){
    }
    public void notifyDataSetChanged(ArrayList<LMedia> ALLMusicList,LMedia currPlayMedia,boolean isPlaying){
        mALLMusicList = ALLMusicList;
        CurrPlayMedia = currPlayMedia;
        isPlayingTag = isPlaying;
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
        vh.MusicName.setText(mALLMusicList.get(position).getName());
        vh.MsuicArtist.setText(mALLMusicList.get(position).getArtist());
        vh.MsuicArtist.setSelected(true);
        if (mALLMusicList.get(position).getAlbumBitmap() != null){
            vh.MusicIcon.setImageBitmap(mALLMusicList.get(position).getAlbumBitmap());
        }else {
            vh.MusicIcon.setImageDrawable(XApp.getApp().getDrawable(R.drawable.ic_launcher));
        }
        if (mALLMusicList.get(position).getName().equals(CurrPlayMedia.getName()) && isPlayingTag){
            vh.MusicIsplaying.setVisibility(View.VISIBLE);
        }else {
            vh.MusicIsplaying.setVisibility(View.GONE);
        }
    }

    private View newView(ViewGroup parent) {
        View v = LayoutInflater.from(XApp.getApp()).inflate(R.layout.item_music_list, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.MusicName = (TextView) v.findViewById(R.id.music_tv);
        vh.MsuicArtist = (TextView) v.findViewById(R.id.music_tv2);
        vh.MusicIcon = v.findViewById(R.id.all_music_icon);
        vh.MusicIsplaying = v.findViewById(R.id.music_isplaying);
        v.setTag(vh);
        return v;
    }

    private class ViewHolder {
        TextView MusicName;
        TextView MsuicArtist;
        ImageView MusicIcon;
        ImageView MusicIsplaying;
    }
}
