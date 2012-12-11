
package com.satton.activity;

import com.satton.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

public class DestoryActivity extends Activity {

    static int count = 0;

    int mycount = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println(mycount + ":destroy");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.gc();
//                System.out.println(mycount + ":System.gc()");
            }
        }).start();
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        overridePendingTransition(R.anim.slidein_start, R.anim.slidein_end);

        ++count;
        mycount = count;

    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.slideout_start, R.anim.slideout_end);
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_image, menu);

        ((Button) findViewById(R.id.button))
                .setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.icon));
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println(mycount + ":finalize");
    };

}
