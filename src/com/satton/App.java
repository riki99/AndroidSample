
package com.satton;

import java.util.HashMap;

import com.satton.util.MLogger;

import android.app.Application;

/**
 * android.app.Application の 拡張クラス
 * 
 * @author tanabe.satoru 2013/03/06
 * @since 2013/03/06
 * @version 2013/03/06
 */
public class App extends Application {

    static private App context;

    /** アプリ起動中に共有する情報を管理 */
    static HashMap<Object, Object> appCache = new HashMap<Object, Object>();

    // ----------------------------------------------------------------------
    // Override
    // ----------------------------------------------------------------------
    @Override
    public void onCreate() {
        MLogger.d("onCreate");
        super.onCreate();
        context = this;
    }

    @Override
    public void onTerminate() {
        MLogger.d("onTerminate");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    // ----------------------------------------------------------------------
    // public
    // ----------------------------------------------------------------------

    public static App getContext() {
        return context;
    }

    public static Object getValue(Object key) {
        return appCache.get(key);
    }

    public static Object addValue(Object key, Object val) {
        return appCache.put(key, val);
    }

}
