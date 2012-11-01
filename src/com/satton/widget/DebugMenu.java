package com.satton.widget;

import java.util.HashMap;
import java.util.Map;

import com.satton.util.ActivityUtils;
import com.satton.widget.MenuHandler;

import android.app.ProgressDialog;
import android.view.Menu;


public class DebugMenu {
	
	public static  void createDebugMenu(Menu menu){

		MenuManager.addHandler(menu, "Push通知 to 50283", 0, new MenuHandler(true)  {
			public void onClick() {
				String recipientId = "50283";
				String msg = "Simon! Come join my quest.";
				String collapseKey = "";
				String style = "";
				String iconUrl = "";
				Map<String, String> extras = new HashMap<String, String>();
				extras.put("hoge", "fuga");
			}});

		MenuManager.addHandler(menu, "ProgressDialog", 0, new MenuHandler(true)  {
					public void onClick() {
						ProgressDialog progressDialog = new ProgressDialog(ActivityUtils.getMainActivity());
						progressDialog.show();
						
					}});
	}



}
