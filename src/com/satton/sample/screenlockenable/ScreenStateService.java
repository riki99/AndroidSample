
package com.satton.sample.screenlockenable;

import com.satton.activity.SampleMainActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class ScreenStateService extends Service {

    static boolean callService = false;

    public static void start() {
        Intent intent = new Intent(SampleMainActivity.i, ScreenStateService.class);
        SampleMainActivity.i.startService(intent);
    }

    public static void stop() {
        try {
            Intent intent = new Intent(SampleMainActivity.i, ScreenStateService.class);
            SampleMainActivity.i.stopService(intent);
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
