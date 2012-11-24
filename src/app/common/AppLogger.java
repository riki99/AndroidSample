package app.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import app.util.logging.LoggerMan;


/**
 * ログ出力機能を提供するクラスです。log4jを使用しログを出力します。
 * <p>
 * ●log4jプロパティの指定について
 * <br>
 * javaコマンドに-Dオプションとして、以下のように設定すると
 * 指定したlog4jのプロパティファイルを読み込み初期化することができます。
 * <br>
 * -Dapp.common.AppLogger.log4jPath=$WL_HOME/osfserver/tanpo/WEB-INF/conf/log4j.properties
 * <br>
 * このプロパティファイルには、置換変数${HOST_NAME}を使用することができます。
 * 変数は実際のHOST名に展開されます
 * <p>
 * 尚、-Dオプションの指定が無い場合、標準出力のみのログ出力となり、
 * 以下の内容でlog4jを初期化します。
 * <pre>
 * 		log4j.category.app.common.AppLogger=DEBUG, stdout, D
 * 		log4j.appender.D=org.apache.log4j.ConsoleAppender
 * 		log4j.appender.D.DatePattern='.'yyyy-MM-dd
 * 		log4j.appender.D.layout=org.apache.log4j.PatternLayout
 * 		log4j.appender.D.layout.ConversionPattern=%d{HH:mm:ss} : [%-5p] : TANPO : %m%n
 * </pre>
 *
 * ※logLevelの値は、開発環境ではDEBUG(2)、本番環境ではINFO(3)に設定します。
 * @author 田辺
 */
public class AppLogger {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AppLogger.class
			.getName());

	static {
		try {
//			writer = new FileWriter("application.log",true);
			LoggerMan.defaultConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** log4j の初期化プロパティファイルの名前	 */
	public static String LOGDIR_KEY = "app.common.AppLogger.logDir";
	/** log4j の初期化プロパティファイルの名前	 */
	public static String log4jPath;
	/** トレースレベル */
	public static final int TRACE = 1;
	/** デバッグレベル */
	public static final int DEBUG = 2;
	/** 情報レベル システム安定稼働後はレベル3にしてください。 */
	public static final int INFO = 3;
	/** ワーニングレベル */
	public static final int WARN = 4;
	/** エラーレベル */
	public static final int ERROR = 5;
	/** 致命的レベル */
	public static final int FATAL = 6;
	/**
	 * アプリケーションのログレベルです。
	 * デフォルトではDEBUG(2)以上のログを出力するように設定しています。
	 */
	public static int logLevel = 2;


    /**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
	    info("TEST");
	}

    //------------------------------------------------------------------『 trace 』
	/**
	 * 詳細レベルでメッセージを表示します。
	 * @param msg 出力するメッセージ
	 */
	public static void trace(String msg) {
		println(AppLogger.class, TRACE, msg);
	}
	/**
	 * 詳細レベルでメッセージを表示します。
	 * @param object ログを出力するオブジェクト。
	 * 通常thisを渡しますが、staticなブロック内では、そのクラスを渡します。
	 * @param msg 出力するメッセージ
	 */
	public static void trace(Object object, String msg) {
		println(object, TRACE, msg);
	}


	//------------------------------------------------------------------『 debug 』
	/**
	 * デバッグレベルでメッセージを表示します。
	 * @param msg 出力するメッセージ
	 */
	public static void debug(String msg) {
		println(AppLogger.class, DEBUG, msg);
	}
	/**
	 * デバッグレベルでメッセージを表示します。
	 * @param object ログを出力するオブジェクト。
	 * 通常thisを渡しますが、staticなブロック内では、そのクラスを渡します。
	 * @param msg 出力するメッセージ
	 */
	public static void debug(Object object, String msg) {
		println(object, DEBUG, msg);
	}

	//-------------------------------------------------------------------『 info 』
	/**
	 * 情報レベルでメッセージを表示します。
	 * @param msg 出力するメッセージ
	 */
	public static void info(String msg) {
		println(AppLogger.class, INFO, msg);
	}
	/**
	 * 情報レベルでメッセージを表示します。
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param msg		出力するメッセージ
	 */
	public static void info(Object object, String msg) {
		println(object, INFO, msg);
	}

	//-------------------------------------------------------------------『 warn 』
	/**
	 * ワーニングレベルでメッセージを表示します。
	 * @param msg 出力するメッセージ
	 */
	public static void warn(String msg) {
		println(AppLogger.class, WARN, msg);
	}
	/**
	 * ワーニングレベルでメッセージを表示します。
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param msg		出力するメッセージ
	 */
	public static void warn(Object object, String msg) {
		println(object, WARN, msg);
	}

	/**
	 * ワーニングレベルでメッセージを表示します。
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param msg		出力するメッセージ
	 * @param e		発生したExcepionのスタックトレースを出力する場合は指定する。
	 */
	public static void warn(Object object, String msg, Throwable e) {
		println(object, WARN, msg, e);
	}


	//------------------------------------------------------------------『 error 』
	/**
	 * エラーレベルでメッセージを表示します。
	 * @param msg 出力するメッセージ
	 */
	public static void error(String msg) {
		println(AppLogger.class, ERROR, msg);
	}
	/**
	 * エラーレベルでメッセージを表示します。
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param msg 		出力するメッセージ
	 */
	public static void error(Object object, String msg) {
		println(object, ERROR, msg);
	}
	/**
	 * エラーレベルでメッセージを表示します。
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param msg 		出力するメッセージ
	 * @param e 		発生したExcepionのスタックトレースを出力する場合は指定する。
	 */
	public static void error(Object object, String msg, Throwable e) {
		println(object, ERROR, msg, e);
	}


	//------------------------------------------------------------------『 fatal 』
	/**
	 * コンソールへ致命的なエラーのメッセージを表示します。
	 * @param msg 出力するメッセージ
	 */
	public static void fatal(String msg) {
		println(AppLogger.class, FATAL, msg);
	}
	/**
	 * コンソールへ致命的なエラーのメッセージを表示します。
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param msg 		出力するメッセージ
	 */
	public static void fatal(Object object, String msg) {
		println(object, FATAL, msg);
	}
	/**
	 * コンソールへ致命的なエラーのメッセージを表示します。
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param msg 		出力するメッセージ
	 * @param e 		発生したExcepionのスタックトレースを出力する場合は指定する。
	 */
	public static void fatal(Object object, String msg, Throwable e) {
		println(object, FATAL, msg, e);
	}

	/**
	 * チボリが監視するログファイルへメッセージを出力します。
	 * @param msg 出力するメッセージ
	 */
	public static void printTivoliLog(String msg) {
	    fatal(msg);
	    ErrorHandler.fatal(msg);
	}

	/**
	 * 引数のデバッグレベルをチェックしタイトルを付けコンソールへ表示します。
	 *
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param level 	デバッグレベル。
	 * @param msg 		出力するメッセージ。
	 */
	public static void println(Object object, int level, String msg) {
		println(object, level, msg, null);
	}

	/**
	 * 引数のデバッグレベルをチェックしタイトルを付けコンソールへ表示します。
	 *
	 * @param object	ログを出力するオブジェクト。
	 * 					通常thisを渡しますが、staticなブロック内では、
	 * 					そのクラスを渡します。
	 * @param level	デバッグレベル。
	 * @param msg		出力するメッセージ。
	 * @param e		発生したExcepionのスタックトレースを出力する場合は指定する。
	 */
	public static void println(Object object, int level, String msg, Throwable e) {
		if (level < logLevel) {
			return;
		}
		String title = "";
		if (object instanceof Class) {
			Class clazz = (Class) object;
			title = clazz.getSimpleName();
		} else {
			title = object.getClass().getSimpleName();
		}
		msg = title + " : " + msg;
		switch (level) {
			case TRACE :
				print(Level.FINE,msg, e);
				break;
			case DEBUG :
				print(Level.CONFIG, msg, e);
				break;
			case INFO :
				print(Level.INFO, msg, e);
				break;
			case WARN :
				print(Level.WARNING, msg, e);
				break;
			case ERROR :
				print(Level.WARNING, msg, e);
				break;
			case FATAL :
				print(Level.SEVERE, msg, e);
				break;
			default :
				throw new IllegalStateException();
		}
	}

	private static void print(Level level,String msg, Throwable e) {
		logger.log(level, msg, e);
//		try {
//			msg = new SimpleDateFormat("HH:mm:ss").format(new Date())+":"+ level.getName() +":" + msg;
//			System.out.println(msg);
//			writer.write(msg);
//				if (e != null) {
//					System.out.println(ErrorHandler.toString(e));
//					writer.write(ErrorHandler.toString(e));
//			}
//			writer.flush();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
	}



	/**
	 * アプリケーションでTRACEレベルのログ出力が有効かの論理値を返します。
	 * @return true:TRACEレベルのログ出力が有効な場合
	 */
	public static boolean isTraceEnabled() {
		return logLevel <= TRACE;
	}

	/**
	 * アプリケーションでDEBUGレベルのログ出力が有効かの論理値を返します。
	 * @return true:DEBUGレベルのログ出力が有効な場合
	 */
	public static boolean isDebugEnabled() {
		return logLevel <= DEBUG;
	}


}
