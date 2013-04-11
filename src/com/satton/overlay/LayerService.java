
package com.satton.overlay;

import com.satton.App;
import com.satton.R;
import com.satton.activity.SampleMainActivity;
import com.satton.util.UiHandler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class LayerService extends Service {
    WindowManager wm;
    View pager_overlayView;
    View main_gripView;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // Viewからインフレータを作成する
        final LayoutInflater layoutInflater = LayoutInflater.from(App.getContext());

        // WindowManagerを取得する
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        SampleMainActivity.i.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pager_overlayView = layoutInflater.inflate(com.satton.R.layout.pager_overlay, null);
        main_gripView = layoutInflater.inflate(com.satton.R.layout.main_grip, null);

        // 重ね合わせするViewの設定を行う
        final WindowManager.LayoutParams layoutparams = new WindowManager.LayoutParams(
                0,
                0,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        layoutparams.width = 100;
        layoutparams.height = 230;
        layoutparams.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        wm.addView(main_gripView, layoutparams);

        ImageButton imageButton = (ImageButton) main_gripView.findViewById(R.id.grip_button);
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    wm.removeView(main_gripView);
                    ViewPager viewPager = (ViewPager) pager_overlayView.findViewById(R.id.overlay_pager);
                    MobagePagerAdapter adapter = new MobagePagerAdapter(App.getContext());
                    viewPager.setAdapter(adapter);

                    layoutparams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    wm.addView(pager_overlayView, layoutparams);
                    //                    wm.updateViewLayout(pager_gripView, layoutparams);

                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        boolean start;

                        @Override
                        public void onPageSelected(int arg0) {
                        }

                        @Override
                        public void onPageScrolled(int arg0, float arg1, int arg2) {
                            if (arg0 == 0 && !start) {
                                new UiHandler() {
                                    @Override
                                    public void run() {
                                        wm.removeView(pager_overlayView);
                                        layoutparams.width = 100;
                                        layoutparams.height = 230;
                                        wm.addView(main_gripView, layoutparams);
                                        start = true;
                                    }
                                }.postDelayed(300);
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int arg0) {
                        }
                    });
                }
                return false;
            }
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // サービスが破棄されるときには重ね合わせしていたViewを削除する
        wm.removeView(pager_overlayView);
        wm.removeView(main_gripView);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
