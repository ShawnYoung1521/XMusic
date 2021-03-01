package com.tw.music.utils.imagecache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class ImageLoader {
	private ImageCache mImageCache;
	private Animation animationShow,animationHide;
	 
    public ImageLoader(ImageCache imageCache) {
        mImageCache = imageCache;
        animationShow = new AlphaAnimation(0.1f,1.0f);
        animationHide = new AlphaAnimation(1.0f,0f);
    }
 
    public void displayImage(String url, ImageView imageView, int id) {
    	imageView.setImageResource(id);
        Bitmap bitmap = mImageCache.getBitmap(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
        		loadImage(imageView, url, id);
        }
    }
 
    private void loadImage(final ImageView imageView, final String url, int id) {
		try {
			MediaMetadataRetriever r = new MediaMetadataRetriever();
			r.setDataSource(url);
			if (r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE) != null) {
				byte albumArt[] = r.getEmbeddedPicture();
				if (albumArt != null) {
					Bitmap b = BitmapFactory.decodeByteArray(albumArt, 0,
							albumArt.length);
					b = setBitmapWH(b, 40, 40);
					if(b != null){
						mImageCache.putBitmap(url, b);
						imageView.setImageBitmap(b);
					}else{
						imageView.setImageResource(id);
					}
				}
			}
			r.release();
		} catch (Exception e) {
		}
    }
    
    private Bitmap setBitmapWH(Bitmap bitmap, int w, int h){
    	BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int mh = options.outHeight;
        int mw = options.outWidth;
        int beWidth = mw / w;
        int beHeight = mh / h;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, w, h,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
    
    private Bitmap drawable2Bitmap(Drawable drawable) {  
		Bitmap bitmap = Bitmap  
                .createBitmap(  
                        drawable.getIntrinsicWidth(),  
                        drawable.getIntrinsicHeight(),  
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                : Bitmap.Config.RGB_565);  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
                drawable.getIntrinsicHeight());  
        drawable.draw(canvas);  
        return bitmap;  
	}
}
