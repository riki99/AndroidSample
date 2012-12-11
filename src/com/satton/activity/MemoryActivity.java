
package com.satton.activity;

import com.satton.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

public class MemoryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        overridePendingTransition(R.anim.slidein_start, R.anim.slidein_end);

        ActivityManager activityManager = ((ActivityManager) getSystemService(ACTIVITY_SERVICE));

        //メモリ情報の取得
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        Log.d("ActivityManager", "memoryInfo availMem  :" + memoryInfo.availMem);
        Log.d("ActivityManager", "memoryInfo lowMemory :" + memoryInfo.lowMemory);
        Log.d("ActivityManager", "memoryInfo threshold :" + memoryInfo.threshold);

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
}
