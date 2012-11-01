package com.satton.sample.screenlockenable;

import com.satton.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ScreenLockEnabledActivity extends Activity {
	
	public static ScreenLockEnabledActivity i;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock);
		i = this;

		intent = new Intent(ScreenLockEnabledActivity.this,
				ScreenStateService.class);

		System.out.println("ScreenLock:ScreenLockEnabledActivity start 1111");
		Window win = getWindow();
		win.addFlags(
//					+ WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
		            + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		            + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		            + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		   
		if (!ScreenStateService.callService) {
			System.out.println("ScreenLock:service start");

			startService(intent);
			ScreenStateService.callService = true;
		}
		
	}
	
	public void _stop(View view) {
		if (ScreenStateService.callService) {
			stopService(intent);
			System.out.println("ScreenLock:stop service");
		}
		finish();
		onDestroy();
		System.exit(0);		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
