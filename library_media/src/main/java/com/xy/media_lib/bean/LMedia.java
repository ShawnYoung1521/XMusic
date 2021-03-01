package com.xy.media_lib.bean;

import android.graphics.Bitmap;

//电影视频的model类
public class LMedia {
	public String name = "";
	public String size = "";
	public String url = "";
	public int duration = 0;
	public int id = 0;
	public String mediaType = "";
	public String album = "";
	public String artist = "";
	public String displayName = "";
	public boolean isCollect = false;
	Bitmap albumbitmap ;
	public LMedia(){

	}
	public LMedia(String aname, String asize, String aurl, int aduration, int aid, String amediaType,String aalbum,String aartist,String adisplayName,Bitmap albumbm) {
		name = aname;
		size = asize;
		url = aurl;
		duration = aduration;
		id = aid;
		mediaType = mediaType;
		album = aalbum;
		artist = aartist;
		displayName = adisplayName;
		albumbitmap = albumbm;
	}

	public LMedia(String aname,String aurl, String aalbum,String aartist,Bitmap albumbm ){
		name = aname;
		url = aurl;
		album = aalbum;
		artist = aartist;
		albumbitmap = albumbm;
	}

	public String getName() {  
		return name;  
	}  
	public void setName(String name) {  
		this.name = name;  
	}  
	public String getSize() {
		return size;  
	}  
	public void setSize(String size) {
		this.size = size;  
	}  
	public String getUrl() {  
		return url;  
	}  
	public void setUrl(String url) {  
		this.url = url;  
	}  
	public int getDuration() {  
		return duration;  
	}  
	public void setDuration(int duration) {  
		this.duration = duration;  
	}  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getAlbum() {
		return album;
	}

	public String getArtist() {
		return artist;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setCollect(boolean collect) {
		isCollect = collect;
	}
	public boolean getIsCollect(){
		return isCollect;
	}

	public Bitmap getAlbumBitmap() {
		return albumbitmap;
	}

}