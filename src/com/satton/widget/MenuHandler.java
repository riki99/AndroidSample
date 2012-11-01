package com.satton.widget;

import android.content.Intent;

public class MenuHandler {

	protected boolean doResultCancel = false;
	protected String name;
	protected Class<?> actionClazz;

	public MenuHandler() {
	}

	public MenuHandler(boolean doResultCancel) {
		this.doResultCancel = doResultCancel;
	}

	public void onClick() {
	}

	public Intent nextIntet() {
		return null;
	}

	public void doResult(Intent retIntent) {
		;
	}
}
