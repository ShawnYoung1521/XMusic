package com.xy.media_lib.bean;

import android.graphics.Bitmap;
import java.util.ArrayList;

//所有歌曲栏model
public class AllMusicMedia {
	public ArrayList<LMedia> AllMusicList = new ArrayList<>();
	public String name;
	public Bitmap albumBp;
	private int tag;

	public AllMusicMedia(String n) {
		name = n;
		tag = 0;
	}

	public void setAllMusicList(ArrayList<LMedia> allMusicList) {
		AllMusicList = allMusicList;
	}

	public String getName() {
		return name;
	}

	public ArrayList<LMedia> getAllMusicList() {
		return AllMusicList;
	}

	public void setAlbumBp(Bitmap albumBp) {
		this.albumBp = albumBp;
	}

	public Bitmap getAlbumBp() {
		return albumBp;
	}
	public int getTag() {
		return tag;
	}
}