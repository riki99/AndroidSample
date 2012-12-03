
package com.satton.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.satton.R;
import com.satton.sample.screenlockenable.ScreenStateService;
import com.satton.sample.screenlockenable.StampManifest;
import com.satton.util.IOUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    public static MainActivity i;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    Object _this = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        i = this;

        @SuppressWarnings("rawtypes")
        Class[] as = {
                TaskActivity.class,
                ImageActivity.class, PopupNotificationActivity.class
        };
        for (final Class<Activity> c : as) {
            Button b = new Button(this);
            b.setText(c.getSimpleName());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, c));
                }
            });

            LinearLayout v = (LinearLayout) findViewById(R.id.lay);
            v.addView(b);
        }

        try {
            File stampFile = new File(getApplicationContext().getFilesDir(),
                    "stamp");
            stampFile.mkdirs();

            StampManifest m = new StampManifest();
            m.category_id = "5";
            m.category_title = "あああ";

            HashMap<String, String> in = new HashMap<String, String>();
            in.put("in", "in");

            HashMap<String, Map<String, String>> out = new HashMap<String, Map<String, String>>();
            out.put("111", in);
            out.put("222", in);

            m.stamps = out;
            //			IOUtil.writeXML(new File(stampFile, "stamp.txt"), m);

            Object obj = IOUtil.readXML(new File(stampFile, "stamp.txt"));
            System.out.println(obj);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------------------

    public void imageBtn(View v) {
        startActivity(new Intent(this, ImageActivity.class));
    }

    public void showDialog(View v) {
        showDialog();
    }

    public void showDialog2(View v) {
        startActivity(new Intent(this, PopupNotificationActivity.class));
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
