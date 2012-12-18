
package com.satton.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

import app.util.StringUtil;

/**
 * オブジェクトを永続化するクラス
 * 
 * <pre>
 * ・オブジェクト内のフィールドは public にするか、private の場合はアクセサを実装してください。
 * 
 * ・オブジェクト は Serializable を implement する必要ありません。
 * 
 * ・保存された Object は、次の通りファイルに保存されます。
 *   ・本番：/data/data/<package>/files/persistence/クラス名.xml
 *     ※ root 権限を取らないと閲覧出来ないセキュアな場所
 * 
 *   ・開発：/mnt/sdcard/<package>/persistence/クラスフル名.xml
 * ※ DDMS から簡単に閲覧可能な場所とし、デバッグを容易にします。
 * 
 * <pre>
 * 
 * 
 * @example <code>
 *      // アプリ起動時の初期処理で、1度だけ呼び出す
 *      ObjectPersistence.configure(getApplicationContext(), false, null);
 *      
 *      // 任意のオブジェクトを保存
 *      StampManifest sm = new StampManifest();
 *      sm.stamps.put("111", "aaa");
 *      sm.stamps.put("222", "bbb");
 *      ObjectPersistence.store(sm);
 *      sm = null;
 *      
 *      // 指定したクラスのオブジェクトを復元
 *      sm = (StampManifest) ObjectPersistence.load(StampManifest.class);
 *      System.out.println(sm.stamps.toString());
 * </code>
 * @author tanabe.satoru 2012/12/14
 * @since 2012/12/14
 * @version 2012/12/14
 */
public class ObjectPersistence {

    private static final String DIRNAME = "persistence";
    private static final String SUFFIX = ".xml";

    private static String defaultPath;
    private static boolean isNameSimple = true;

    /**
     * オブジェクトを永続化するための初期設定を行う。
     * 
     * @param context
     * @param isProduct <pre>
     *            true ： /data/data/$PACKAGE_NAME/$dirname の場所に保存される    ※ この場所はroot権限が無いと閲覧できないセキュアな場所
     *            false ： $SDCard/$PACKAGE_NAME/$dirname の場所に保存される
     * </pre>
     * @param dirname デフォルトの persistence を変更したい場合は設定。
     */
    public static void configure(Context context, boolean isProduct, String dirname) {
        if (!StringUtil.isExist(dirname)) {
            dirname = DIRNAME;
        }
        if (isProduct) {
            defaultPath = new File(context.getFilesDir(), dirname).getAbsolutePath() + File.separator;
        } else {
            File saveDir = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
            saveDir = new File(saveDir, dirname);
            defaultPath = saveDir.getAbsolutePath() + File.separator;
        }
        IOUtil.mkdirs(defaultPath);
    }

    /**
     * オブジェクトを永続化する
     * 
     * @param object
     */
    public static void store(Object object) {
        String path = getPath(object.getClass());
        synchronized (object.getClass()) {
            try {
                IOUtil.writeXML(new File(path), object, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 指定した clazz を復元する。一度も永続化されていない場合は新規インスタンスを作成します。
     * 
     * @param clazz
     * @return
     */
    public static Object load(Class<?> clazz) {
        String path = getPath(clazz);
        synchronized (clazz) {
            try {
                Object obj = IOUtil.readXML(new File(path), null, clazz);
                if (!clazz.isInstance(obj)) {
                    throw new ClassCastException();
                }
                return obj;
            } catch (Throwable t) {
                File f = new File(path);
                f.delete();
                try {
                    Object obj = clazz.newInstance();
                    store(obj);
                    return obj;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // ----------------------------------------------------------------------
    // 指定した clazz のファイルを削除 
    // ----------------------------------------------------------------------
    public boolean remove(Class<?> clazz) {
        synchronized (clazz) {
            String path = getPath(clazz);
            return new File(path).delete();
        }
    }

    // ----------------------------------------------------------------------
    // private
    // ----------------------------------------------------------------------
    private static String getPath(Class<?> clazz) {
        if (defaultPath == null) {
            throw new IllegalStateException();
        }
        String name = (isNameSimple) ? clazz.getSimpleName() : clazz.getName();
        String path = defaultPath + name + SUFFIX;
        return path;
    }

    /** SDカードの絶対パス */
    public static String getExternalStoragePath() {
        // MOTOROLA 対応
        String path = System.getenv("EXTERNAL_ALT_STORAGE");
        if (path != null) {
            if (new File(path).exists()) {
                return path;
            }
        }
        // Sumsung 対応
        path = System.getenv("EXTERNAL_STORAGE2");
        if (path != null) {
            if (new File(path).exists()) {
                return path;
            }
        }
        // 旧 Sumsung + 標準 対応
        path = System.getenv("EXTERNAL_STORAGE");
        if (path != null) {
            if (new File(path).exists()) {
                return path;
            }
        }
        path = Environment.getExternalStorageDirectory().getPath();
        // その他機種
        return path;
    }

}
