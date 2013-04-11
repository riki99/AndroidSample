
package com.satton.util;

import android.util.Log;

/**
 * MLogger 共通クラス
 * 
 * @author tanabe.satoru 2013/03/13
 * @since 2013/03/13
 * @version 2013/03/13
 */
public class MLogger {

    /** デバッグログの出力対象となるコールスタックインデックス */
    private static final int DEBUG_STACK_INDEX = 7;

    /** ログ用のタグ */
    private static String APP_TAG = "Satton";
    /** ログ用のタグ */
    private static boolean DEBUGABLE = true;

    // ----------------------------------------------------------------------
    // 初期設定
    // ----------------------------------------------------------------------
    public static void setAppTag(String s) {
        APP_TAG = s;
    }

    public static void setDebugAble(boolean debugAble) {
        MLogger.DEBUGABLE = debugAble;
    }

    // ----------------------------------------------------------------------
    // ログ関連
    // ----------------------------------------------------------------------
    public static void d(String message) {
        log(Log.DEBUG, message);
    }

    public static void df(String message, Object... args) {
        message = String.format(message, args);
        log(Log.DEBUG, message);
    }

    public static void i(String message) {
        log(Log.INFO, message);
    }

    public static void ifo(String message, Object... args) {
        message = String.format(message, args);
        log(Log.INFO, message);
    }

    public static void w(String message) {
        log(Log.WARN, message);
    }

    public static void e(String message) {
        log(Log.ERROR, message);
    }

    public static void e(Throwable e) {
        log(Log.ERROR, "", e);
    }

    // ----------------------------------------------------------------------
    // private
    // ----------------------------------------------------------------------
    private static void log(int level, String message) {
        log(level, message, null);
    }

    private static void log(int level, String message, Throwable e) {
        // 開発中しかログは出力しない
        if (!DEBUGABLE) {
            return;
        }
        String tag = APP_TAG;
        message += getCallerMethodInfoAsString();
        switch (level) {
            case Log.VERBOSE:
                Log.v(tag, message);
                break;
            case Log.DEBUG:
                Log.d(tag, message);
                break;
            case Log.INFO:
                Log.i(tag, message);
                break;
            case Log.ERROR:
                Log.e(tag, message, e);
                break;
            case Log.WARN:
                Log.w(tag, message, e);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private static String getCallerMethodInfoAsString() {
        StackTraceElement trace = getTraceInfoByIndex(DEBUG_STACK_INDEX);
        String className = trace.getClassName();
        String methodName = trace.getMethodName();
        String caller = trace.getFileName() + ":" + trace.getLineNumber();
        return String.format("           -- at %s.%s(%s)\n", className, methodName, caller);
    }

    /**
     * 特定の階層インデックスのトレース情報を返す。
     */
    private static StackTraceElement getTraceInfoByIndex(int index) {
        return Thread.currentThread().getStackTrace()[index];
    }

}
