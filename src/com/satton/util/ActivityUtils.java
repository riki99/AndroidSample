package com.satton.util;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;


public class ActivityUtils {

	static Activity a;
	
	public static void startActivity(Class<?> clazz) {
		startActivity(clazz, null);
	}

	public static void startActivity(Class<?> clazz, HashMap<String, String> extras) {
		
		Intent intent = new Intent(getMainActivity(), clazz);
		if (extras != null) {
			for (String key : extras.keySet()) {
				intent.putExtra(key, extras.get(key));
			}
		}
		getMainActivity().startActivity(intent);
	}
	
	public static Activity getMainActivity() {
		return a;
	}
	
}
