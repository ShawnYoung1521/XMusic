package com.xy.media_lib.bean;

import java.util.ArrayList;

//路径栏model类
public class PathMedia {
	public ArrayList<LMedia> mPathMediaList = new ArrayList<>();;
	public String pathName;
	public String pathUrl;

	public PathMedia(String aname, String aurl,LMedia media) {
		pathName = aname;
		pathUrl = aurl;
		mPathMediaList.add(media);
	}

	public String getName() {
		return pathName;
	}

	public String getUrl() {
		return pathUrl;
	}

	public ArrayList<LMedia> getPathMediaList() {
		return mPathMediaList;
	}
}