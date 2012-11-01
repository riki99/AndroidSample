package com.satton.sample.screenlockenable;

import com.satton.activity.BalloneActivity;
import com.satton.activity.MainActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class ScreenStateService extends Service {

	static boolean callService = false;
	
	public static void start() {
		Intent intent = new Intent(MainActivity.i,
				ScreenStateService.class);
		MainActivity.i.startService(intent);
	}
	
	public static void stop() {
		Intent intent = new Intent(MainActivity.i,
				ScreenStateService.class);
		MainActivity.i.stopService(intent);
        System.out.println("ScreenLock:stop service.....");
	}
	
	
    private BroadcastReceiver mScreenOnListener = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("ScreenLock:BroadcastReceiver " + action);
            // 画面の電源が入ったらActivityを起動
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
//                Intent i = new Intent(context, ScreenLockEnabledActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
            }
        }
    };

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("ScreenLock:onStart service");

        // ACTION_SCREEN_ONを受け取るBroadcastReceiverを登録
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);	
        registerReceiver(mScreenOnListener, filter);
        
        final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(5 *1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
							System.out.println("ScreenLock: run... 111");
							// スリープ状態から復帰する
							WakeLock wakelock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
						        .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
						            | PowerManager.ACQUIRE_CAUSES_WAKEUP
						            | PowerManager.ON_AFTER_RELEASE, "disableLock");
						    wakelock.acquire();
						    
							Intent i = new Intent(MainActivity.i,
									BalloneActivity.class);
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);

			                wakelock.release();
							
						}
					});
				}
			}
		}).start();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mScreenOnListener);
        super.onDestroy();

        System.out.println("ScreenLock:stop service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
