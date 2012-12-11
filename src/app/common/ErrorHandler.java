package app.common;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import app.util.CollectionsUtil;
import app.util.StringUtil;

/**
 * 例外処理クラス。
 * @author 田辺
 */
public class ErrorHandler{

	//---------------------------------------------------------------『 Static Variables 』
	/** */
	private static final String LINE_SEPA = System.getProperty("line.separator");

	/**
	 * Logger for this class
	 */
	private static final Logger errorLogger = Logger.getLogger(ErrorHandler.class
			.getName());

	/** 発生したExceptionに対し、FATALにするかERRORにするかのフィルタリングを行う。 */
	private static HashMap filterExceptionMap = new HashMap();
    
	/** 発生したExceptionに対し、FATALにするかERRORにするかのフィルタリングを行う。 */
	private static List stackTraceFilters = new ArrayList();

	/** エラーログを別にファイル出力するかどうか。 */
	private static boolean errorLogWrite;

	//-------------------------------------------------------『 Public Methods 』
    
    /**
     * 致命的な例外かどうかを判定し、発生した例外を処理します。
     * @param catchObject 例外をキャッチしたオブジェクトをわたします。
     * 						staticブロック内では、クラスを渡す。
     * @param e			発生した例外
     */
    public static void dispose(Throwable e) {
        dispose(e,"エラーが発生しました。");
    }

    /**
     * 致命的な例外かどうかを判定し、発生した例外を処理します。
     * @param catchObject	例外をキャッチしたオブジェクトをわたします。
     * 						staticブロック内では、クラスを渡す。
     * @param e			発生した例外
     * @param errorTitle 	発生した例外に対するタイトルをつけます。
     */
    public static void dispose(Throwable e,String errorTitle) {
        try {
            // 致命的な例外かどうかを判定する
			boolean isFilter = filterException(e);
			if (isFilter) {
				AppLogger.error(ErrorHandler.class,"予期しているエラーが発生しました(問題ありません)。",e);
				return;
			}
			
            String errorlog = createlog(e,errorTitle);
			
			// ログ出力モードになっていた場合は、error.logにも出力させる。			
//		    System.err.println(DateUtil.getSysDate()+" : "+errorlog);
			
			// 通常のログファイルapplication.logにさせる。			
			AppLogger.fatal(ErrorHandler.class,errorlog);
			
			// メールを送信する。			
			sendErrorMail(errorTitle,errorlog);
       
        } catch (Throwable t) {
			AppLogger.fatal(ErrorHandler.class,"ErrorHandlerがエラー処理中に再度エラーが発生しました",t);
			AppLogger.fatal(ErrorHandler.class,"根本原因のExceptionです。",e);
        }
    }

	/**
	 * 発生した例外のprintStackTraceの情報を文字列で取得します。
	 * 
	 * @param th 発生した例外
	 * @return printStackTraceした時にSystem.errに生成される情報の文字列
	 */
	public static String toString(Throwable th) {
		if (th == null) {
			return "Throwable null";
		}
		CharArrayWriter ca = new CharArrayWriter();
		th.printStackTrace(new PrintWriter(ca));
		return ca.toString();
	}

	public static void trace(Throwable th) {
		String stackTrace = toString(th);
		// 改行を削除
		stackTrace = StringUtil.replace(stackTrace,LINE_SEPA,""); 
		// タブを削除
		stackTrace = StringUtil.replace(stackTrace,"\t",""); 
		AppLogger.fatal(ErrorHandler.class,stackTrace);
	}
	
	/**
	 * 致命的なエラーのメッセージをログへ出力します。
	 * @param msg 出力するメッセージ
	 */
	public static void fatal(String msg) {
		// ログ出力モードになっていた場合は、error.logにも出力させる。			
		if (errorLogWrite) errorLogger.warning(msg);
	}
	

	//-----------------------------------------------------------------『 private Method 』
	/**
	 * 発生した例外をメールで通知します。
	 * （ただし、mailFlgをメール送信有効に設定していた場合です。）
     * @param errorTitle 	発生した例外に対するタイトルをつけます。
	 * @param errorlog		送信するエラーログ
	 */    
	private static void sendErrorMail(String errorTitle,String errorlog){
	}


	/**
	 * フィルタ対象のThrowableかどうかの論理値を返します。
	 * @param e 発生したThrowable
	 * @return [true]:フィルタ対象のThrowableの場合
	 */
	private static boolean filterException(Throwable e){
		if (e instanceof IOException) {
			// ブラウザが逃げた時のエラー ⇒ java.io.IOException: ???????????????????
			// このメッセージは文字化けしていて取れない。
			String browserCansel	 
			= "\u0080\u0465\u0080\u0080\u05e4\u0080\u0080\u0080\u0080\u01e4\u0080\u0080\u0080\u07a4\u0080\u0080\u0080\u0080\u0080";
			IOException io = (IOException) e;
			if (io.getMessage() != null && io.getMessage().equals(browserCansel)) {
				return true;
			}			
		}
		
		String stackTrace = toString(e);
		// 改行を削除
		stackTrace = StringUtil.replace(stackTrace,LINE_SEPA,""); 
		// タブを削除
		stackTrace = StringUtil.replace(stackTrace,"\t",""); 

		Iterator itr = stackTraceFilters.iterator();
		while (itr.hasNext()) {
			String filter = (String) itr.next();
			boolean b = StringUtil.isMatches(stackTrace,filter);
			if (b) {
				return true;
			}
		}
		return false; 	
	}

	private static String createlog(Throwable e,String errorTitle){
		String stackTrace = toString(e); 
		String errorlog =
		errorTitle +
		"\n" + stackTrace +
		"";
		return errorlog;			
	}

	//------------------------------------------------------------『 Accessor 』


	/**
	 * @param filterList
	 */
	public void setFilterExceptionMap(String filterList) {
		if (StringUtil.isExist(filterList)) {
			CollectionsUtil.putToken(filterExceptionMap,filterList);
		}
	}

	/**
	 * @param filters
	 */
	public void setStackTraceFilters(String filters) {
		if (StringUtil.isExist(filters)) {
			stackTraceFilters.add(".*" + filters + ".*");
		}
	}

	
    /**
     * を返します。
     * @return errorLogWrite
     */
    public static boolean isErrorLogWrite() {
        return errorLogWrite;
    }
    /**
     * errorLogWrite を設定します。
     * @param errorLogWrite 
     */
    public static void setErrorLogWrite(boolean errorLogWrite) {
        ErrorHandler.errorLogWrite = errorLogWrite;
    }
}
