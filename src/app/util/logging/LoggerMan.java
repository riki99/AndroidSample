
package app.util.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import app.util.ClassContext;
import app.util.DateUtil;
import app.util.IOUtil;
import app.util.StringUtil;

public class LoggerMan {

    private static boolean startup;
    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(LoggerMan.class.getName());
    private static String propertiesName = "log.properties";
    public static String logdir = "sys/logs";
    private static int year = Calendar.getInstance().get(Calendar.YEAR);
    public static String appLogFileName = "/" + year + "application.log";

    public static String appLogFilePath = logdir + appLogFileName;

    public static Handler consoleHandler;
    public static Handler fileHandler;

    public static Formatter consoleFormatter = new CustomFormatter();

    // ----------------------------------------------------------------------
    // ## : LoggerMan ?�?�?�O?�o?�?�
    // ----------------------------------------------------------------------
    public static boolean isConfigEnable() {
        return logger.isLoggable(Level.CONFIG);
    }

    public static Logger getLogger() {
        String className = ClassContext.getCallerClassNameAt(2);
        return Logger.getLogger(className);
    }

    // ----------------------------------------------------------------------
    // ## : LoggerMan ?�ݒ�
    // ----------------------------------------------------------------------
    public static boolean defaultConfiguration() {
        return defaultConfiguration(false);
    }

    public static synchronized boolean defaultConfiguration(boolean sysoutRedirect) {
        if (startup) {
            return true;
        }
        try {
            String str = System.getProperty("java.util.logging.Level");
            LogManager.getLogManager().reset();
            consoleHandler = new CustomConsoleHandler();
            consoleHandler.setFormatter(consoleFormatter);
            logger.getParent().addHandler(consoleHandler);

            File f = new File(logdir);
            if (!f.exists()) {
                f.mkdirs();
            }
            fileHandler = new FileHandler(appLogFilePath, 500000, 1, true);
            fileHandler.setFormatter(new CustomFormatter(
                    CustomFormatter.FORMAT_FILE));
            logger.getParent().addHandler(fileHandler);

            setLevel(str);
            if (logger.isLoggable(Level.FINE)) {
                listHandlers();
            }

            //			MultiStream outStream = new MultiStream(System.out);
            //			outStream.addStream(new PrintStream(new FileOutputStream(logdir+"/"+year+"stdout.log",true)));

            //			Field field = StreamHandler.class.getDeclaredField("output");
            //			field.setAccessible(true);
            //			OutputStream output = (OutputStream) field.get(fileHandler);
            //			PrintStream outStream = new PrintStream(output);

            if (sysoutRedirect) {
                PrintStream outStream = new PrintStream(new FileOutputStream(logdir + "/" + year + "stdout.log", true));
                System.setOut(outStream);
                System.setErr(outStream);
            }

            String time = DateUtil.getSysDate();
            System.out.println("OUT : " + time);
            System.err.println("ERR : " + time);

            startup = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            startup = false;
            return false;
        }
    }

    public static void setLevel(String str) {
        Level level = null;
        if (!StringUtil.isExist(str)) {
            level = Level.INFO;
        } else {
            try {
                level = Level.parse(str);
            } catch (IllegalArgumentException e) {
                level = Level.INFO;
            }
        }
        consoleHandler.setLevel(level);
        fileHandler.setLevel(level);
        logger.getParent().setLevel(level);
    }

    static class CustomConsoleHandler extends StreamHandler {
        public CustomConsoleHandler() {
            super();
            setOutputStream(System.out);
        }

        /**
         * LogRecord ?�𔭍s?�?�?�܂�?�B ?�?�?�?�?�Ԃł́A?�?�?�M?�?�?�O?�̗v?�?�?�?� Logger ?�I?�u?�W?�F?�N?�g?�?�
         * ?�΂�?�čs?�?�?�A?�?�?�̃I?�u?�W?�F?�N?�g?�?� LogRecord ?�?�?�?�?�?�?�?� ?�?�?�?�?�ɓ]?�?�?�?�?�܂�?�?�?�B
         * 
         * @param record ?�?�?�O?�C?�x?�?�?�g?�̐�?�?�?�Bnull ?�?�?�R?�[?�h?�͒P?�ɖ�?�?�?�?�?�?�?� ?�?�?�?�?�ŁA?�ʒm?�͍s?�?�?�Ȃ�
         */
        @Override
        public void publish(LogRecord record) {
            super.publish(record);
            flush();
        }

        /**
         * StreamHandler.close ?�?�?�I?�[?�o?�[?�?�?�C?�h?�?�?�ăt?�?�?�b?�V?�?�?�?�?�܂�?�?�?�A ?�o?�̓X?�g?�?�?�[?�?�?�͕�?�܂�?�?�B?�܂�ASystem.err
         * ?�͕�?�܂�?�?�B
         */
        @Override
        public void close() {
            flush();
        }

    }

    /**
     * ?�ݒ�t?�@?�C?�?�?�̓ǂݍ�?�?�
     */
    public static void propertyConfiguration() {
        try {
            //			System.setErr(System.out);
            InputStream in = LoggerMan.class.getResourceAsStream(propertiesName);
            LogManager.getLogManager().readConfiguration(in);
            logger.fine("Logger?�̐ݒ肪?�?�?�?�?�?�?�܂�?�?�?�B");
            listHandlers();
            listPropertis();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void close() {
        consoleHandler.close();
        fileHandler.close();
    }

    // ----------------------------------------------------------------------
    // ## : LoggerMan private
    // ----------------------------------------------------------------------
    private static void listPropertis() throws IOException {
        InputStream in = LoggerMan.class.getResourceAsStream(propertiesName);
        URL url = LoggerMan.class.getResource(propertiesName);
        System.out.println(url.toString());
        byte[] b = IOUtil.readStreamByte(in);
        System.out.println(new String(b));
    }

    private static void listHandlers() {
        // System.out.println(logger.getLevel().getName());
        logger.fine("?�?�?��?��?�K?�[?�?�Handler?�?�?�ł�?�B");
        Handler[] handlers = logger.getHandlers();
        handler(handlers);
        logger.fine("?�?�?��?��?�K?�[?�̐eHandler?�?�?�ł�?�B");
        handlers = logger.getParent().getHandlers();
        handler(handlers);
    }

    private static void handler(Handler[] handlers) {
        logger.fine("Handler Size = " + handlers.length);
        for (int i = 0; i < handlers.length; i++) {
            Handler handler = handlers[i];
            logger.fine(handler.getClass().getName());
            logger.fine(handler.getFormatter().getClass().getName());
        }
    }

}
