package app.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import app.util.BeanUtil;
import app.util.StringUtil;

public class CustomFormatter extends Formatter {
	public static int FORMAT_CONSOLE = 1;
	public static int FORMAT_FILE = 2;
	private static String lineSeparator = System.getProperty("line.separator");
	private int formatType = FORMAT_CONSOLE;
	private String pattern = "HH:mm:ss";

	/**
	 * 
	 */
	public CustomFormatter() {
	}

	/**
	 * 
	 * @param isStackTrace
	 */
	public CustomFormatter(int formatType) {
		this.formatType = formatType;
		if (formatType == FORMAT_FILE) {
			pattern = "yyyy/MM/dd HH:mm:ss";
		}
	}

	/**
	 * 
	 */
	public synchronized String format(LogRecord record) {
		StringBuffer buf = new StringBuffer();
		// ?ｽ?ｽ?ｽﾝ抵ｿｽ
		Date date = new Date();
		date.setTime(record.getMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		buf.append(formatter.format(date));
		buf.append(":");
		// ?ｽ?ｽ?ｽx?ｽ?ｽ?ｽ?ｽﾝ抵ｿｽ
		buf.append("[" + record.getLevel().getName() + "]");
		buf.append(":");
		// ?ｽN?ｽ?ｽ?ｽX?ｽ?ｽﾝ抵ｿｽ
		String className = BeanUtil.getShortClassName(record
				.getSourceClassName());
		buf.append(className);
		buf.append(":");
		if (formatType == FORMAT_FILE) {
			buf.append(record.getMessage());
		} else {
			buf.append(record.getMessage());
			buf.append(" * ");
			// ?ｽN?ｽ?ｽ?ｽX?ｽ?ｽ?ｽ?ｽﾝ抵ｿｽ
			buf.append(record.getSourceClassName());
			buf.append(".");
			buf.append(record.getSourceMethodName());
			// ?ｽ\?ｽ[?ｽX?ｽﾌ行?ｽ?ｽ?ｽ?ｽ?ｽ謫ｾ
			StackTraceElement[] ste = new Throwable().getStackTrace();
			buf.append("(" + className + ".java:" + ste[7].getLineNumber()
					+ ")");
		}
		buf.append(lineSeparator);
		return buf.toString();
	}

	private void appendTh(LogRecord record, StringBuffer sb) {
		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				sb.append(sw.toString());
			} catch (Exception ex) {
			}
		}
	}
}