
package com.satton.overlay;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SystemOverlayPresenter {
    private static SystemOverlayPresenter _instance = null;

    public static SystemOverlayPresenter getInstance(Context context) {
        if (_instance == null) {
            _instance = new SystemOverlayPresenter(context.getApplicationContext());
        }

        return _instance;
    }

    private View mOverlay;
    private boolean mOverlayShown;
    private WindowManager mWindowManger;

    private SystemOverlayPresenter(Context context) {
        mOverlay = buildStagingOverlay(context);
        mOverlayShown = false;
        mWindowManger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void handleChange() {
        int DELAY_FOR_IS_FOREGROUNG = 1000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bindStagingOverlay();
            }
        }, DELAY_FOR_IS_FOREGROUNG);
    }

    private void showStagingOverlay() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                0,
                PixelFormat.TRANSLUCENT);

        mWindowManger.addView(mOverlay, lp);
    }

    private void bindStagingOverlay() {
        showStagingOverlay();
    }

    private View buildStagingOverlay(Context context) {
        LinearLayout v = new LinearLayout(context);
        v.setPadding(16, 16, 16, 16);

        TextView tv = new TextView(context);
        int wc = LinearLayout.LayoutParams.WRAP_CONTENT;
        tv.setLayoutParams(new LinearLayout.LayoutParams(wc, wc));
        tv.setText("STAGING");
        tv.setTextColor(0xffff0000);
        tv.setBackgroundColor(0x44000000);
        v.addView(tv);

        return v;
    }
}
