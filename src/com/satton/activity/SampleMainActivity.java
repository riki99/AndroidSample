
package com.satton.activity;

import com.satton.R;
import com.satton.sample.screenlockenable.ScreenStateService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class SampleMainActivity extends Activity {
    public static SampleMainActivity i;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        i = this;

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        win.setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        @SuppressWarnings("rawtypes")
        Class[] as = {
                ContactActivity.class,
                WebViewDialog.class,
                WebViewActivity.class,
                BitmapMemoryActivity.class,
                DestoryActivity.class
        };
        for (final Class<Activity> c : as) {
            Button b = new Button(this);
            b.setText(c.getSimpleName());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SampleMainActivity.this, c));
                }
            });

            LinearLayout v = (LinearLayout) findViewById(R.id.lay);
            v.addView(b);

        }
        final WifiManager wMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String macAddress = wMgr.getConnectionInfo().getMacAddress();
        System.out.println(macAddress);
        //        startService(new Intent(this, LayerService.class));
        //        finish();
    }

    // ----------------------------------------------------------------------

    public void imageBtn(View v) {
        startActivity(new Intent(this, BitmapMemoryActivity.class));
    }

    public void showDialog(View v) {
        showDialog();
    }

    public void startService(View v) {
        ScreenStateService.start();
    }

    public static void showDialog() {
        LayoutInflater inflater = i.getLayoutInflater();
        View view = inflater.inflate(R.layout.balloon_dialog, null);

        Drawable bitmapDrawable = i.getResources().getDrawable(
                R.drawable.icon_balloon);
        bitmapDrawable.setBounds(32, 32, 32, 32);

        // ダイアログを作成します。
        final AlertDialog dlg = new AlertDialog.Builder(i)
                .setIcon(R.drawable.icon_balloon).setTitle("太郎さん")
                .setView(view)
                .setPositiveButton("表示", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("閉じる", null).create();

        dlg.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        dlg.show();
    }

}
