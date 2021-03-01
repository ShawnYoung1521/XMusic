package com.tw.music.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tw.music.R;
import com.tw.music.contract.MusicContract;
import com.xy.media_lib.bean.ArtistMedia;
import java.util.ArrayList;
import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;
import cn.xy.library.util.image.XImage;

public class MusicArtistAdapter extends BaseAdapter {
    public static ArrayList<ArtistMedia> mArtistLists ;
    private String CurrName = "";
    private int CurrType = -1;
    public MusicArtistAdapter(){
    }
    public void notifyDataSetChanged(ArrayList<ArtistMedia> ArtistLists){
        mArtistLists = ArtistLists;
        notifyDataSetChanged();
    }
    public void notifyDataSetChanged(int tag,String name){
        CurrName = name;
        CurrType = tag;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(mArtistLists == null) {
            return 0;
        } else{
            return mArtistLists.size();
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
        vh.MusicName.setText(mArtistLists.get(position).getName());
        vh.MsuicArtist.setText(mArtistLists.get(position).getArtistMediaList().size()+"首");
        if (mArtistLists.get(position).getAritistbitmap()!=null){
            vh.MusicIcon.setImageBitmap(XImage.toRound(mArtistLists.get(position).getAritistbitmap()));
        }else {
//            vh.MusicIcon.setImageDrawable(XApp.getApp().getDrawable(R.drawable.artise));
            vh.MusicIcon.setImageBitmap(XImage.toRound(XConvert.drawable2Bitmap(XApp.getApp().getDrawable(R.drawable.artise))));
        }
        if (CurrName.equals(mArtistLists.get(position).getName()) && CurrType == MusicContract.ArtistTag){
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
