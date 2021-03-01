package com.xy.media_lib.bean;

import android.graphics.Bitmap;

import java.util.ArrayList;

//专辑栏model
public class AlbumMedia {
	public ArrayList<LMedia> mAlbumMediaList = new ArrayList<>();
	public String name;
	public Bitmap albumBp;

	public AlbumMedia(String aname,LMedia media) {
		name = aname;
		mAlbumMediaList.add(media);
	}

	public String getName() {
		return name;
	}

	public ArrayList<LMedia> getAlbumMediaList() {
		return mAlbumMediaList;
	}

	public void setAlbumBp(Bitmap albumBp) {
		this.albumBp = albumBp;
	}

	public Bitmap getAlbumBp() {
		return albumBp;
	}
}