package com.tw.music.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tw.music.R;
import com.tw.music.contract.MusicContract;
import com.tw.music.fragment.TabListAlbumFM;
import com.tw.music.listener.onTabItemListener;
import com.xy.media_lib.bean.AlbumMedia;

import java.util.ArrayList;

import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;

public class MusicAlbumAdapter extends RecyclerView.Adapter<MusicAlbumAdapter.GridViewHolder> {

    public static ArrayList<AlbumMedia> mAlbumLists ;
    onTabItemListener  tabItemListener;
    private String CurrName = "";
    private int CurrType = -1;

    public MusicAlbumAdapter(TabListAlbumFM tabListArtistFM){
        tabItemListener = tabListArtistFM;
    }
    public void notifyDataSetChanged(ArrayList<AlbumMedia> mAlbumList){
        mAlbumLists = mAlbumList;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(int tag,String name){
        CurrName = name;
        CurrType = tag;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GridViewHolder(LayoutInflater.from(XApp.getApp()).inflate(R.layout.item_music_ablum,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder gridViewHolder, @SuppressLint("RecyclerView") final int i) {
        if (mAlbumLists.get(i).getAlbumBp() != null){
//            gridViewHolder.Bitmap.setImageBitmap(mAlbumLists.get(i).getAlbumBp());
            gridViewHolder.Bitmap.setBackground(XConvert.bitmap2Drawable(mAlbumLists.get(i).getAlbumBp()));
        }else {
            gridViewHolder.Bitmap.setBackground(XApp.getApp().getDrawable(R.drawable.ic_launcher));
        }
        gridViewHolder.Name.setText(mAlbumLists.get(i).getName());
        gridViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabItemListener.onIndex(i);
            }
        });
        if (CurrName.equals(mAlbumLists.get(i).getName()) && CurrType == MusicContract.AlbumTag){
            gridViewHolder.MusicIsplaying.setVisibility(View.VISIBLE);
        }else {
            gridViewHolder.MusicIsplaying.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumLists.size();
    }
    
    public class GridViewHolder extends RecyclerView.ViewHolder{
        ImageView Bitmap;
        TextView Name;
        View view;
        ImageView MusicIsplaying;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            Bitmap = itemView.findViewById(R.id.tab_music_icon);
            Name = itemView.findViewById(R.id.tab_music_tv);
            view = itemView.findViewById(R.id.tab_music_view);
            MusicIsplaying = itemView.findViewById(R.id.music_isplaying);
        }
    }
}
