package com.satton.sample.screenlockenable;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.satton.activity.MainActivity;
import com.satton.activity.PopupNotificationActivity;

public class ScreenStateService extends Service {

	static boolean callService = false;

	public static void start() {
		Intent intent = new Intent(MainActivity.i, ScreenStateService.class);
		MainActivity.i.startService(intent);
	}

	public static void stop() {
		try {
			Intent intent = new Intent(MainActivity.i, ScreenStateService.class);
			MainActivity.i.stopService(intent);
			System.out.println("ScreenLock:stop service.....");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		System.out.println("ScreenLock:onStart service");

		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(7 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
							System.out.println("ScreenLock: run... 111");
							// スリープ状態から復帰する
							// WakeLock wakelock = ((PowerManager)
							// getSystemService(Context.POWER_SERVICE))
							// .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
							// | PowerManager.ACQUIRE_CAUSES_WAKEUP
							// | PowerManager.ON_AFTER_RELEASE, "disableLock");
							// wakelock.acquire();

							Intent i = new Intent(MainActivity.i,
									PopupNotificationActivity.class);
							i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);

							// wakelock.release();

						}
					});
				}
			}
		}).start();
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
