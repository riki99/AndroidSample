
package com.satton.util;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.WeakHashMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Debug;
import android.util.Log;

/**
 * <pre>
 * ■ 概要
 * Activity の onCreate 、onStop に仕掛ける事で、Activity 生成後アクティブ中のメモリ増加量を追跡する<br>
 * 
 * ■ 参考
 * ・アプリケーションヒープの最大サイズを上回ると、java.lang.OutOfMemoryErrorが投げられる。
 * 
 * ・ アプリケーションヒープはネイティブヒープとJavaヒープに分けられ、adb shell getprop dalvik.vm.heapsize で確認可能
 *      
 * ・良くメモリーエラーになる端末は、アプリケーションヒープが32mに設定されている
 *      SO-01C      32m
 *      IS03        40m
 *      Galaxy S2   72m
 * 
 * ・ 大きい画像を読み込むと直ぐに超えてしまう。
 *      Bitmapオブジェクトは、Naiveヒープに画像データを保存する Android 3.0 より古いバージョンでは。
 *      GCによって、Bitmapオブジェクトが解放されても、画像データが残っている時があるようです。
 *  
 * ・Android 2.2ではGCがどうやらネィティブヒープも管理してくれるようです。
 * 
 * ・android:largeHeap="true"  が効くのは、Android 3.0 ( APIレベル11 ) から
 *   AndroidManifest.xml の application に android:largeHeap="true" を加える事でアプリケーションで使用するHeapサイズを
 *   拡大できるが、このサイズは 256MB 辺りのようだ。
 *   
 * ・ http://www.saturn.dti.ne.jp/npaka/android/memory/index.html
 * </pre>
 * 
 * @author tanabe.satoru 2012/12/05
 * @since 2012/12/05
 * @version 2012/12/05
 */
public class MemoryInfoDumper {

    // ----------------------------------------------------------------------
    //  Activity の onCreate 、onStop に仕掛ける事で、Activity 生成後アクティブ中のメモリ増加量を追跡する
    // ----------------------------------------------------------------------
    private static Map<Activity, StringBuilder> mAcMemoryInfo = new WeakHashMap<Activity, StringBuilder>();

    /**
     * onCreate の開始直後に呼び出す。 ※ この段階でログには出力しない
     * 
     * @param ac
     */
    public static void dumpOnCreateInfo(Activity ac) {
        printActivityMemoryInfo(ac, true);
    }

    /**
     * onStop に設定し、前後の情報を出力する
     * 
     * @param ac
     */
    public static void dumpOnStopInfo(Activity ac) {
        printActivityMemoryInfo(ac, false);

        Log.v(MemoryInfoDumper.class.getSimpleName(), mAcMemoryInfo.get(ac).toString());
        mAcMemoryInfo.remove(ac);
    }

    // ----------------------------------------------------------------------
    //   その他のメモリ関連のメソッド
    // ----------------------------------------------------------------------
    /**
     * AppliecationHeap が引数より低いかどうか
     * 
     * @return
     */
    public static boolean isLowerAppliecationHeap(int MB) {
        long max = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        return max < MB;
    }

    /**
     * VM Heap 情報を返す
     * 
     * @return
     */
    public static String getVMHeapInfo() {
        long free = Runtime.getRuntime().freeMemory() / 1024;
        long total = Runtime.getRuntime().totalMemory() / 1024;
        long max = Runtime.getRuntime().maxMemory() / 1024;
        long used = total - free;
        double ratio = (used * 100 / (double) total);
        String info = String.format("VM Heap Mem : total=%s, used=%s (%s %%), max=%s",
                format(total), format(used), new DecimalFormat(
                        "##.#").format(ratio), format(max));
        return info;
    }

    /**
     * NativeHeap 情報を返す
     * 
     * @return
     */
    public static String getNativeHeapInfo() {
        String info;
        long nmax = Debug.getNativeHeapSize() / 1024; // 確保しているnativeHeap
        long nallocated = Debug.getNativeHeapAllocatedSize() / 1024; // 使用中nativeHeap
        long nfree = Debug.getNativeHeapFreeSize() / 1024; // 空きnativeHeap
        double nratio = (nallocated * 100 / (double) nmax);
        info = String.format("native Heap : max=%s, allocated=%s (%s %%), free=%s",
                format(nmax), format(nallocated), new DecimalFormat(
                        "##.#").format(nratio), format(nfree));
        return info;
    }

    // ----------------------------------------------------------------------
    //  private
    // ----------------------------------------------------------------------
    private static void printActivityMemoryInfo(Activity ac, boolean start) {
        ActivityManager am = ((ActivityManager) ac.getSystemService(Activity.ACTIVITY_SERVICE));
        // メモリ情報の取得
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        if (start) {
            append(ac, " ===== MemoryInfoDumper : " + ac.getClass().getName() + " " + Build.VERSION.RELEASE + "============================== ");
        }
        append(ac, ((start) ? "[create] " : "[stop] "));

        // システムの利用可能な空きメモリ
        String availMem = format(memoryInfo.availMem / 1024);
        // メモリが少なくなってきた際、積極的にプロセスを殺すLowMemoryKillerが実行されるしきい値。
        // 下回った際に、バックグラウンドサービスや、その他無関係なプロセスからKillし始める
        String threshold = format(memoryInfo.threshold / 1024);
        // 低メモリ状態を示すフラグ(trueでメモリ不足状態)
        boolean lowMemory = memoryInfo.lowMemory;
        append(ac, String.format("System  Mem : avail=%s, threshold=%s, lowMemory=%s", availMem, threshold, lowMemory));

        // 自プロセスが使用中のメモリー
        int[] pids = new int[1];
        pids[0] = android.os.Process.myPid();
        android.os.Debug.MemoryInfo[] dmi = am.getProcessMemoryInfo(pids);

        // 使用中のメモリーサイズ(KB)
        String totalPrivate = format(dmi[0].getTotalPrivateDirty());
        String totalPss = format(dmi[0].getTotalPss());
        String totalShared = format(dmi[0].getTotalSharedDirty());
        append(ac, String.format("Process Mem : TotalPrivate=%s, TotalPss=%s, TotalShared=%s", totalPrivate, totalPss, totalShared));

        // VM Heap
        String info = getVMHeapInfo();
        append(ac, info);

        // nativeHeap 
        String ninfo = getNativeHeapInfo();
        append(ac, ninfo);
    }

    private static void append(Activity ac, String str) {
        StringBuilder buff = mAcMemoryInfo.get(ac);
        if (buff == null) {
            buff = new StringBuilder();
            mAcMemoryInfo.put(ac, buff);
        }
        buff.append(str).append("\n");
    }

    private static String format(long num) {
        DecimalFormat f1 = new DecimalFormat("#,###KB");
        return f1.format(num);
    }

}
