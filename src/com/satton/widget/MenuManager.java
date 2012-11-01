package com.satton.widget;

import java.util.HashMap;


import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;


public class MenuManager {

	private static HashMap<String, MenuHandler> menuHandlers = new HashMap<String, MenuHandler>();
	private static volatile int requestCode = 90000;


	public static void addHandler(Menu menu, String name, int icon, final MenuHandler handler) {
		handler.name = name;
		final int no = ++ requestCode;
		pushHandler(no, handler);

		MenuItem item = menu.add(0, no, 0, name);
		item.setIcon(icon);
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem menuitem) {
				callIntent(no, handler);
				return false;
			}
		});
	}


	public static void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		MenuHandler handler = menuHandlers.get(Integer.toString(requestCode));
		if (handler == null) {
			return;
		}
		if (!handler.doResultCancel) {
			if (resultCode != Activity.RESULT_OK) {
				return;
			}
		}
		handler.doResult(intent);
	}


	
	// ----------------------------------------------------------------------
	// private
	// ----------------------------------------------------------------------

	private static void pushHandler(int no, MenuHandler handler) {
		menuHandlers.put(Integer.toString(no), handler);
	}

	private static  void callIntent(int no, MenuHandler hook) {
		hook.onClick();
		Intent intent = hook.nextIntet();
		if (intent == null) {
			return;
		}
//		if (hook.actionClazz != null && android.app.Service.class.isAssignableFrom(hook.actionClazz)) {
//			getMainActivity().startService(intent);
//		}else{
//			getMainActivity().startActivityForResult(intent, no);
//		}

	}

}
