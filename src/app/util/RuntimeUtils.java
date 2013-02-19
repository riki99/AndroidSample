
package app.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

public class RuntimeUtils {

    public static void sleep(int sec) {
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e1) {
        }
    }

    public static void sleepSec(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e1) {
        }
    }

    public static void sleepMinutes(int minutes) {
        try {
            Thread.sleep(minutes * 60 * 1000);
        } catch (InterruptedException e1) {
        }
    }

    /**
     * ネットワークが接続状態かどうか確認する。
     * 
     * @param context
     * @return 接続状態なら true、それ以外なら false。
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }

    /**
     * 現在充電中かどうか確認する。
     * 
     * @param context
     * @return 充電中なら true、それ以外なら false。
     */
    public static boolean isCharging(Context context) {
        Intent intent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // バッテリ状態を取得する。
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
        // 充電中なら true を返す。
        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            return true;
        }
        // それ以外なら false を返す。
        return false;
    }

    public static Date getInstallTime(
            PackageManager packageManager, String packageName) {
        return firstNonNull(
                installTimeFromPackageManager(packageManager, packageName),
                apkUpdateTime(packageManager, packageName));
    }

    private static Date apkUpdateTime(
            PackageManager packageManager, String packageName) {
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, 0);
            File apkFile = new File(info.sourceDir);
            return apkFile.exists() ? new Date(apkFile.lastModified()) : null;
        } catch (NameNotFoundException e) {
            return null; // package not found
        }
    }

    private static Date installTimeFromPackageManager(
            PackageManager packageManager, String packageName) {
        // API level 9 and above have the "firstInstallTime" field.
        // Check for it with reflection and return if present. 
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            Field field = PackageInfo.class.getField("firstInstallTime");
            long timestamp = field.getLong(info);
            return new Date(timestamp);
        } catch (NameNotFoundException e) {
            return null; // package not found
        } catch (IllegalAccessException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (SecurityException e) {
        }
        // field wasn't found
        return null;
    }

    private static Date firstNonNull(Date... dates) {
        for (Date date : dates)
            if (date != null)
                return date;
        return null;
    }
}
