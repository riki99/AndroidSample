package com.satton.util.thumbnails;

import java.io.File;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;

public class VideoUtil {


	static public Bitmap getThumbnailForFile(File p_File, Activity p_activity) {
		long imageID = GetImageID(p_File, p_activity);
		if (imageID < 0) {
			return null;
		}
		return MediaStore.Images.Thumbnails.getThumbnail(
				p_activity.getContentResolver(), imageID,
				Thumbnails.MICRO_KIND, null);
	}

	public static long GetImageID(File p_File, Activity p_activity) {
		long result = -1;
		Cursor c = p_activity.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID + "" },
				MediaStore.Images.Media.DATA + " like '"
						+ p_File.getAbsolutePath(), null, null);
		c.moveToNext();
		if (!c.isAfterLast()) {
			result = c.getLong(0);
		}
		c.close();
		return result;
	}
}
