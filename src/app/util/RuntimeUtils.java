
package app.util;

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
     * Checks the Operating System.
     * 
     * @return true if the current os is Windows
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("windows") != -1 || os.indexOf("nt") != -1;
    }

    public static boolean isWindowsVist() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("vista") != -1;
    }

    public static boolean isWindows7() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("windows 7") != -1;
    }

    public static boolean isWindowsServer2008() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("Windows Server 2008") != -1;
    }

    private static Boolean windowsVistaAbove;

    public static boolean isWindowsVistaAbove() {
        if (windowsVistaAbove == null) {
            if (RuntimeUtils.isWindowsVist() || RuntimeUtils.isWindows7()) {
                windowsVistaAbove = new Boolean(true);
            } else {
                windowsVistaAbove = new Boolean(false);
            }
        }
        return windowsVistaAbove.booleanValue();
    }

    public static boolean isWindowsXP() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("windows xp") != -1;
    }

    /**
     * Checks the Operating System.
     * 
     * @return true if the current os is Windows
     */
    public static boolean isWindows9X() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.equals("windows 95") || os.equals("windows 98");
    }

    public static boolean isWindows2000() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.equals("windows 2000");
    }

    /**
     * Checks the Operating System.
     * 
     * @return true if the current os is Apple
     */
    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("mac") != -1;
    }

    /**
     * Checks the Operating System.
     * 
     * @return true if the current os is Linux
     */
    public static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("linux") != -1;
    }

    /**
     * ���s����OS��UNIX�ŉғ����Ă��邩�ǂ����̘_���l��Ԃ��܂��B
     * 
     * @return [true]�FUNIX�ŉғ����Ă���ꍇ
     */
    public static boolean runUnix() {
        String os = System.getProperty("os.name");
        if (os.indexOf("Windows") != -1) {
            return false;
        }
        return true;
    }

    /**
     * JDK1.6�ȏ�
     * 
     * @return
     */
    public static boolean isJava16Above() {
        String version = System.getProperty("java.version");
        return version.compareTo("1.6") >= 0;
    }

    /**
     * JDK1.5�ȏ�
     * 
     * @return
     */
    public static boolean isJava15Above() {
        String version = System.getProperty("java.version");
        return version.compareTo("1.5") >= 0;
    }

    /**
     * JDK1.4�ȏ�
     * 
     * @return
     */
    public static boolean isJava14Above() {
        String version = System.getProperty("java.version");
        return version.compareTo("1.4") >= 0;
    }

    private static long currentTimeNumber = System.currentTimeMillis();

    public synchronized static long currentTimeMillis() {
        long number = System.currentTimeMillis();
        int i = 0;
        while (currentTimeNumber == number) {
            number = number + ++i;
        }
        sleep(i + 1);
        currentTimeNumber = number;
        return number;
    }

}
