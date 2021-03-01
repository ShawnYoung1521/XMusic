package com.xy.media_lib.bean;

import android.graphics.Bitmap;

import java.util.ArrayList;

//歌手栏model
public class ArtistMedia {
	private ArrayList<LMedia> mArtistMediaList = new ArrayList<>();
	private String name;
	private Bitmap aritistbitmap;

	public ArtistMedia(String aname,LMedia media) {
		name = aname;
		mArtistMediaList.add(media);
	}

	public String getName() {
		return name;
	}

	public ArrayList<LMedia> getArtistMediaList() {
		return mArtistMediaList;
	}

	public void addLMedia(LMedia media){
		mArtistMediaList.add(media);
	}

	public void setAritistbitmap(Bitmap aritistbitmap) {
		this.aritistbitmap = aritistbitmap;
	}

	public Bitmap getAritistbitmap() {
		return aritistbitmap;
	}
}