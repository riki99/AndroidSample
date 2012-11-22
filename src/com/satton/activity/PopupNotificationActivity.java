package com.satton.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.satton.R;

public class PopupNotificationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		boolean isDisplayLocked = keyguardManager
				.inKeyguardRestrictedInputMode();

		System.out.println("isDisplayLocked=" +isDisplayLocked);

		Window win = getWindow();
		win.addFlags(+WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
				+ WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				+ WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		setContentView(R.layout.popup_notification);
	}

	public static void showBallone(Activity mainActivity) {
		Intent i = new Intent(null, PopupNotificationActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mainActivity.startActivity(i);
	}

	public void close(View v) {
		finish();
		onDestroy();
		com.satton.sample.screenlockenable.ScreenStateService.stop();
	}

	public void startTalkActivity(View v) {
	}

}
