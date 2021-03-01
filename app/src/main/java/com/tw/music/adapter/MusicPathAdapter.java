package com.tw.music.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tw.music.R;
import com.tw.music.contract.MusicContract;
import com.xy.media_lib.bean.LMedia;
import com.xy.media_lib.bean.PathMedia;
import java.util.ArrayList;
import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;

public class MusicPathAdapter extends BaseAdapter {
    public static ArrayList<PathMedia> mPathLists ;
    private String CurrName = "";
    private int CurrTag = -1;
    public MusicPathAdapter(){
    }
    public void notifyDataSetChanged(ArrayList<PathMedia> PathLists ){
        mPathLists = PathLists;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(int tag,String name){
        CurrTag = tag;
        CurrName = name;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(mPathLists == null) {
            return 0;
        } else{
            return mPathLists.size();
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
        vh.MusicName.setText(mPathLists.get(position).getName());
        vh.MsuicArtist.setText(mPathLists.get(position).getPathMediaList().size()+"首 "+mPathLists.get(position).pathUrl);
        if (CurrName.equals(mPathLists.get(position).getName()) && CurrTag == MusicContract.PathTag){
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
        vh.MusicIcon.setImageDrawable(XApp.getApp().getDrawable(R.drawable.icon_files));
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
