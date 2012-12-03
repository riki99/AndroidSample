
package com.satton.activity;

import java.util.List;

import com.satton.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class TaskActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // 起動中のタスク情報
        // <uses-permission android:name="android.permission.GET_TASKS" />
        // パーミッションが必要
        ActivityManager activityManager = ((ActivityManager) getSystemService(ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager
                .getRunningTasks(5);// 直近5つを取得
        if (taskInfo != null) {
            for (RunningTaskInfo task : taskInfo) {

                // エラー情報
                Log.d("ActivityManager", "task.id  :" + task.id + "-------------------------------");
                Log.d("ActivityManager", "task.description  :"
                        + task.description);
                Log.d("ActivityManager", "task.numActivities:"
                        + task.numActivities);
                Log.d("ActivityManager", "task.numRunning   :"
                        + task.numRunning);
                Log.d("ActivityManager", "task.baseActivity   :"
                        + task.baseActivity.getClassName());
                Log.d("ActivityManager", "task.topActivity   :"
                        + task.topActivity.getClassName());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task, menu);
        return true;
    }
}
