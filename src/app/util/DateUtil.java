package app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;


/**
 * 日付関連のユーティリティクラス。
 * <p>@author 田辺</p>
 */
public class DateUtil {

    /** */
	public static final String FORMAT_DATE_SLASH = "yyyy/MM/dd";
    /** */
	public static final String FORMAT_MMdd = "MM/dd";
    /** */
	public static final String FORMAT_yyyyMMdd = "yyyyMMdd";
    /** */
	public static final String FORMAT_DATE_HYPHEN = "yyyy-MM-dd";
    /** */
	public static final String FORMAT_DATE_TIME = "yyyy/MM/dd HH:mm:ss";
    /** */
	public static final String FORMAT_DATE_TIME_MILLI = "yyyy/MM/dd HH:mm:ss.SSS";
    /** */
	public static final String FORMAT_TIME = "HH:mm:ss";
    /** */
	public static final String FORMAT_TIME_MILL = "HH:mm:ss.SSS";

	/** 1日(24時間)をm秒で表したもの */
	public static final long ONE_DATE_TIME_MILLIS = 1000 * 60 * 60 * 24;


	//--------------------------------------------------------『  long値を返す 』
	/**
	 * 現在時刻のミリ秒を表すロング値で返します。
	 * @return 現在時刻のミリ秒を表すロング値
	 */
	public static long currentTimeMillis(){
		return getCalendar().getTime().getTime();
	}

	//----------------------------------------------------------『  時間を返す 』
	/**
	 * 現在時刻をHH:mm:ddでで返します。
	 * @return HH:mm:dd形式の現在時刻
	 */
	public static String getTime(){
		String sysdate = getSysDate();
		String systime = sysdate.substring(11,19);
		return systime;
	}

	/**
	 * 現在時刻をHH:mm:ss.SSSで返します。
	 * @return HH:mm:ss.SSS形式の現在時刻
	 */
	public static String getMillTime(){
		String sysdate = getSysDate();
		String systime = sysdate.substring(11);
		return systime;
	}

	//--------------------------------------------------『  日付をStringで返す 』
	/**
	 * 当日日付をyyyy/MM/ddの文字列で返します。
	 * @return yyyy/MM/dd形式の当日日付
	 */
	public static String getStrToday(){
		String sysdate = getSysDate();
		sysdate = sysdate.substring(0,10);
		return sysdate;
	}

	/**
	 * 当日日付 現在時刻を指定のフォーマットで文字列に変換します。
	 * @param   pattern 指定のフォーマット
	 * @return  フォーマットされた日付文字列
	 */
	public static String getStrToday(String pattern){
		return toStringDate(getSysDate(),pattern);
	}

	/**
	 * 当日日付をyyyyMMddの文字列で返します。
	 * @return yyyyMMdd形式の当日日付
	 */
	public static String getStrToday8(){
		String yyyyMMdd = StringUtil.replace(getStrToday(),"/","");
		return yyyyMMdd;
	}

	/**
	 * 当日日付をyyyy/MM/ddの文字列で返します。
	 * @return yyyy/MM/dd形式の当日日付
	 */
	public static String getStrDay(int add){
		Calendar cal = DateCalculate.addDate(add);
		return toStringDate(cal);
	}

	public static String getStrDay(int add,String pattern){
		Calendar cal = DateCalculate.addDate(add);
		return toStringDate(cal,pattern);
	}

	//--------------------------------------------------『  変換しStringで返す 』
	/**
	 * java.util.Dateクラスまたは、そのサブクラスをyyyy/MM/ddの文字列に変換します。
	 * @param date 変換対象のjava.sql.Dateクラス
	 * @return 指定された日付のyyyy/MM/dd形式
	 */
	public static String toStringDate(Date date){
	    return toStringDate(date,FORMAT_DATE_SLASH);
	}

	public static String toStringDate(Calendar cal){
		return toStringDate(cal.getTime(),FORMAT_DATE_SLASH);
	}
	/**
	 * Calendar型のクラスを指定のフォーマットで文字列に変換します。
	 * @param   cal 変換対象のCalendarクラス
	 * @param   pattern 指定のフォーマット
	 * @return  フォーマットされた日付文字列
	 */
	public static String toStringDate(Calendar cal,String pattern){
		return toStringDate(cal.getTime(),pattern);
	}

	/**
	 * 時間のロング値を指定のフォーマットで文字列に変換します。
	 * @param   time 現在時刻のミリ秒を表すロング値
	 * @param   pattern 指定のフォーマット
	 * @return  フォーマットされた日付文字列
	 */
	public static String toStringDate(long time,String pattern){
		return toStringDate(new Date(time),pattern);
	}

	/**
	 * java.util.Dateクラスまたは、そのサブクラスを指定のフォーマットで
	 * 文字列に変換します。
	 * @param   date 変換対象のjava.util.Dateクラス
	 * @param   pattern 指定のフォーマット
	 * @return  フォーマットされた日付文字列
	 */
	public static String toStringDate(Date date,String pattern){
		SimpleDateFormat sdFormat = new SimpleDateFormat(pattern);
		return sdFormat.format(date);
	}

	/**
	 * 日付文字列を解析し指定のフォーマットで文字列に変換します。
	 * @param   date 変換対象の日付文字列
	 * @param   pattern 指定のフォーマット
	 * @return  フォーマットされた日付文字列
	 */
	public static String toStringDate(String date,String pattern){
		Date d = toCalendar(date).getTime();
		return toStringDate(d,pattern);
	}


	/**
	 * 引数で指定された日付文字列を解析しyyyy/MM/ddのフォーマット
	 * で文字列に再構築します。
	 * @param   date 変換対象の日付文字列
	 * @return  フォーマットされた日付文字列
	 */
	public static String formatStr(String date){
		Date d = toCalendar(date).getTime();
		return toStringDate(d,FORMAT_DATE_SLASH);
	}

	/**
	 * 一ヶ月前の日付を文字列で返します。yyyy/MM/dd
	 * @return
	 */
	public static String getStrOneMonthAgo(){
		Calendar cal = getCalendar();
		cal.set(
			cal.get(Calendar.YEAR),
			cal.get(Calendar.MONTH)-1,
			cal.get(Calendar.DATE)
		);
		return toStringDate(cal.getTime().getTime(),FORMAT_DATE_SLASH);
	}

	/**
	 * 一週間後の日付を文字列で返します。yyyy/MM/dd
	 * @return
	 */
	public static String getStrOneWeekAfter(){
		Calendar cal = getCalendar();
		cal.set(
			cal.get(Calendar.YEAR),
			cal.get(Calendar.MONTH),
			cal.get(Calendar.DATE)+7
		);
		return toStringDate(cal.getTime().getTime(),"yyyy/MM/dd");
	}


	//-----------------------------------------------------『  Calendar を返す 』

	/**
	 * システム日付のCalendarクラスを返します。
	 * @return
	 */
	public static Calendar getCalendar(){
		String sysdate = getSysDate();
		return toCalendar(sysdate);
	}

    /**
     * 指定された日付・時刻文字列を、可能であれば Calendarクラスに変換します。
	 * 以下の形式の日付文字列を変換できます。
	 *
	 * ●変換可能な形式は以下となります。
	 *  yyyy/MM/dd
	 *  yy/MM/dd
	 *  yyyy-MM-dd
	 *  yy-MM-dd
	 *  yyyyMMdd
	 *
	 * 上記に以下の時間フィールドが組み合わされた状態でも有効です。
	 *  HH:mm
	 *  HH:mm:ss
	 *  HH:mm:ss.SSS
	 *
	 * @param strDate 日付・時刻文字列。
	 * @return 変換後のCalendarクラス。
	 * @throws IllegalArgumentException
	 * 			日付文字列が変換不可能な場合
	 * 			または、矛盾している場合（例：2000/99/99）。
	 */
	public static Calendar toCalendar(String strDate){
		strDate = format(strDate);
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);

		int yyyy = Integer.parseInt(strDate.substring(0,4));
		int MM = Integer.parseInt(strDate.substring(5,7));
		int dd = Integer.parseInt(strDate.substring(8,10));
		int HH = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);
		int SSS = cal.get(Calendar.MILLISECOND);
		cal.clear();
		cal.set(yyyy,MM-1,dd);
		int len = strDate.length();
		switch (len) {
			case 10:
				break;
			case 16: // yyyy/MM/dd HH:mm
				HH = Integer.parseInt(strDate.substring(11,13));
				mm = Integer.parseInt(strDate.substring(14,16));
				cal.set(Calendar.HOUR_OF_DAY,HH);
				cal.set(Calendar.MINUTE,mm);
				break;
			case 19: //yyyy/MM/dd HH:mm:ss
				HH = Integer.parseInt(strDate.substring(11,13));
				mm = Integer.parseInt(strDate.substring(14,16));
				ss = Integer.parseInt(strDate.substring(17,19));
				cal.set(Calendar.HOUR_OF_DAY,HH);
				cal.set(Calendar.MINUTE,mm);
				cal.set(Calendar.SECOND,ss);
				break;
			case 23: //yyyy/MM/dd HH:mm:ss.SSS
				HH = Integer.parseInt(strDate.substring(11,13));
				mm = Integer.parseInt(strDate.substring(14,16));
				ss = Integer.parseInt(strDate.substring(17,19));
				SSS = Integer.parseInt(strDate.substring(20,23));
				cal.set(Calendar.HOUR_OF_DAY,HH);
				cal.set(Calendar.MINUTE,mm);
				cal.set(Calendar.SECOND,ss);
				cal.set(Calendar.MILLISECOND,SSS);
				break;
			default :
				throw new IllegalArgumentException(
						"引数の文字列["+ strDate +"]は日付文字列に変換できません");
		}
		return cal;
	}

	public static Calendar toCalendar(long time){
		return toCalendar(toStringDate(time, FORMAT_DATE_TIME_MILLI));
	}


	/**
	 * 当日日付で「time」の時刻でのCalenderを返します。
	 * ※日付は当日になります。
	 * @param time "7:00" or "07:00" or "15:35" or "15:35:20"
	 * @return
	 */
	public static Calendar toTimeCalendar(String time){
		String ss = getStrToday() + " "+ time;
		return toCalendar(ss);
	}


	/**
	 * 様々な日付、時刻文字列をデフォルトの日付・時刻フォーマットへ変換します。
	 *
	 * ●デフォルトの日付フォーマットは以下になります。
	 * 	日付だけの場合：yyyy/MM/dd
	 * 	日付+時刻の場合：yyyy/MM/dd HH:mm:ss.SSS
	 *
	 * @param str 変換対象の文字列
	 * @return デフォルトの日付・時刻フォーマット
	 * @throws IllegalArgumentException 日付文字列が変換不可能な場合
	 */
	public static String format(String str){
		if (str == null || str.trim().length() < 8) {
			throw new IllegalArgumentException(
					"引数の文字列["+ str +"]は日付文字列に変換できません");
		}
		str = str.trim();
		String yyyy = null; String MM = null; String dd = null;
		String HH = null; String mm = null;
		String ss = null;String SSS = null;
		// "-" or "/" が無い場合
		if (str.indexOf("/")==-1 && str.indexOf("-")==-1) {
			if (str.length() == 8) {
				yyyy = str.substring(0,4);
				MM = str.substring(4,6);
				dd = str.substring(6,8);
				return yyyy+"/"+MM+"/"+dd;
			}
			yyyy = str.substring(0,4);
			MM = str.substring(4,6);
			dd = str.substring(6,8);
			HH = str.substring(9,11);
			mm = str.substring(12,14);
			ss = str.substring(15,17);
			return yyyy+"/"+MM+"/"+dd+" "+HH+":"+mm+":"+ss;
		}
		StringTokenizer token = new StringTokenizer(str,"_/-:. ");
		StringBuffer result = new StringBuffer();
		for(int i = 0; token.hasMoreTokens(); i++) {
			String temp = token.nextToken();
			switch(i){
				case 0:// 年の部分
					yyyy = fillString(str, temp, "L", "20", 4);
					result.append(yyyy);
					break;
				case 1:// 月の部分
					MM = fillString(str, temp, "L", "0", 2);
					result.append("/"+MM);
					break;
				case 2:// 日の部分
					dd = fillString(str, temp, "L", "0", 2);
					result.append("/"+dd);
					break;
				case 3:// 時間の部分
					HH = fillString(str, temp, "L", "0", 2);
					result.append(" "+HH);
					break;
				case 4:// 分の部分
					mm = fillString(str, temp, "L", "0", 2);
					result.append(":"+mm);
					break;
				case 5:// 秒の部分
					ss = fillString(str, temp, "L", "0", 2);
					result.append(":"+ss);
					break;
				case 6:// ミリ秒の部分
					SSS = fillString(str, temp, "R", "0", 3);
					result.append("."+SSS);
					break;
			}
		}
		return result.toString();
	}
	private static String fillString(String strDate, String str,
										String position, String addStr,
										int len){
		if (str.length() > len) {
			throw new IllegalArgumentException(
				"引数の文字列["+ strDate +"]は日付文字列に変換できません");
		}
		return StringUtil.fillString(str, position, len,addStr);
	}

	//------------------------------------------------『  java.sql.Date を返す 』

	/**
	 * String ⇒ java.sql.Date
	 *
	 * 以下の日付文字列をjava.sql.Dateに変換
	 * "20030407"
	 * "2003/04/07"
	 * "2003-04-07"
	 * "2003/04/07 15:20:16"
	 * "2003-04-07 15:20:16"
	 * @param strDate
	 * @return
	 */
	public static java.sql.Date toSqlDate(String strDate){
		Calendar cal = toCalendar(strDate);
		return toSqlDate(cal);
	}

	/**
	 * Calendar ⇒　java.sql.Date
	 * @param cal
	 * @return
	 */
	public static java.sql.Date toSqlDate(Calendar cal){
		long l = cal.getTime().getTime();
		return new java.sql.Date(l);
	}

	/**
	 * Calendar ⇒　java.sql.Date
	 * @return
	 */
	public static java.sql.Date getSqlDate(){
		Calendar cal = getCalendar();
		long l = cal.getTime().getTime();
		return new java.sql.Date(l);
	}



 	//--------------------------------------------------『 java.util.Dateを返す 』
	/**
	 * @param strDate
	 * @return
	 */
	public static Date getUtilDate(String strDate){
		long time = toSqlDate(strDate).getTime();
		return new java.util.Date(time);
	}


	//----------------------------------------------------------------『 その他 』


	/**
	 * 本日が営業年度の最初の日付か（4月1日）を返します。
	 * @return
	 */
	public static boolean isBusinessYearFirstDay(){

		String strToday = getStrToday();
		return (strToday.endsWith("/04/01"));

	}

	/**
	 * 営業年度を返します。
	 * @return
	 */
	public static int getBusinessYear(){

		Calendar cal = getCalendar();
		int month = cal.get(Calendar.MONTH)+1;
		switch (month) {
			case 1:
				return cal.get(Calendar.YEAR)-1;
			case 2:
				return cal.get(Calendar.YEAR)-1;
			case 3:
				return cal.get(Calendar.YEAR)-1;
			default:
				return cal.get(Calendar.YEAR);
		}

	}

	/**
	 * 現在時刻が引数の時間を経過しているかどうかを返します。
	 * 現在時刻　＞　time の場合trueを返します。
	 * @param time	時刻文字列	17:40:00 or 17:40 or 5:00 or 05:00
	 * @return [true]：経過している場合
	 */
	public static boolean isElapseTime(String time) {
		Calendar cal = DateUtil.toTimeCalendar(time);
		Calendar now = DateUtil.getCalendar();
		if (now.after(cal)) {
			return true;
		}
		return false;
	}

	/**
	 * 現在の曜日を返します。
	 * @return	現在の曜日
	 */
	public static String getDayOfTheWeek() {
		return getDayOfTheWeekShort(getCalendar());
	}
	public static String getDayOfTheWeek(Calendar cal) {
	    switch (cal.get(Calendar.DAY_OF_WEEK)) {
		    case Calendar.SUNDAY: return "日曜日";
		    case Calendar.MONDAY: return "月曜日";
		    case Calendar.TUESDAY: return "火曜日";
		    case Calendar.WEDNESDAY: return "水曜日";
		    case Calendar.THURSDAY: return "木曜日";
		    case Calendar.FRIDAY: return "金曜日";
		    case Calendar.SATURDAY: return "土曜日";
	    }
	    throw new IllegalStateException();
	}

	/**
	 * 現在の曜日を返します。
	 * ※曜日は省略します。
	 * @return	現在の曜日
	 */
	public static String getDayOfTheWeekShort() {
		return getDayOfTheWeekShort(getCalendar());
	}
	public static String getDayOfTheWeekShort(Calendar cal) {
	    switch (cal.get(Calendar.DAY_OF_WEEK)) {
		    case Calendar.SUNDAY: return "日";
		    case Calendar.MONDAY: return "月";
		    case Calendar.TUESDAY: return "火";
		    case Calendar.WEDNESDAY: return "水";
		    case Calendar.THURSDAY: return "木";
		    case Calendar.FRIDAY: return "金";
		    case Calendar.SATURDAY: return "土";
	    }
	    throw new IllegalStateException();
	}


	//---------------------------------------------『 システム日付を設定する 』

	/**
	 * @return
	 */
	public static String getSysDate() {
		long systime = System.currentTimeMillis();
		return DateUtil.toStringDate(systime, DateUtil.FORMAT_DATE_TIME_MILLI);
	}

	/**
	 * 日付文字列"yyyy/MM/dd"をjava.util.Date型へ変換します。
	 * @param str 変換対象の文字列
	 * @return 変換後のjava.util.Dateオブジェクト
	 * @throws ParseException 日付文字列が"yyyy/MM/dd"以外の場合
	 */
	public static Date toDate(String str) throws ParseException {
		Date  date = toCalendar(str).getTime();
		// 中国でフォーマットエラー
//		Date  date = DateFormat.getDateInstance().parse(str);
		return date;
	}

	/**
	 * 日付の妥当性チェックを行います。
	 * 指定した日付文字列（yyyy/MM/dd or yyyy-MM-dd）が
	 * カレンダーに存在するかどうかを返します。
	 *
	 * @param strDate チェック対象の文字列
	 * @return 存在する日付の場合true
	 */
	public static boolean checkDate(String strDate) {
		if (strDate == null || strDate.length() != 10) {
			throw new IllegalArgumentException(
					"引数の文字列["+ strDate +"]" +
					"は不正です。");
		}
		strDate = strDate.replace('-', '/');
		DateFormat format = DateFormat.getDateInstance();
		// 日付/時刻解析を厳密に行うかどうかを設定する。
		format.setLenient(false);
		try {
			format.parse(strDate);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 指定した日付文字列（yyyy/MM/dd or yyyy-MM-dd）
	 * における月末日付を返します。
	 *
	 * @param strDate 対象の日付文字列
	 * @return 月末日付
	 */
	public static int getLastDay(String strDate) {
		strDate = toStringDate(strDate, FORMAT_DATE_SLASH);
		int yyyy = Integer.parseInt(strDate.substring(0,4));
		int MM = Integer.parseInt(strDate.substring(5,7));
		int dd = Integer.parseInt(strDate.substring(8,10));
		Calendar cal = Calendar.getInstance();
		cal.set(yyyy,MM-1,dd);
		int last = cal.getActualMaximum(Calendar.DATE);
		return last;
	}

	public static int getLastDay(Calendar cal) {
		int last = cal.getActualMaximum(Calendar.DATE);
		return last;
	}
	public static boolean isLastDay(String strDate) {
		int last = getLastDay(strDate);
		Calendar cal = toCalendar(strDate);
		int day = cal.get(Calendar.DATE);
		return last == day;
	}

	public static int getWeekOfMonth(Calendar cal) {
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 *
	 * @param cal
	 * @return
	 */
	public static boolean isLastWeek(Calendar cal) {
		Calendar caltemp = Calendar.getInstance();
		// Calendar copy
		caltemp.setTimeInMillis(cal.getTimeInMillis());
		int week_index = caltemp.get(Calendar.WEEK_OF_MONTH);
		int lastDay = caltemp.getActualMaximum(Calendar.DATE);
		caltemp.set(Calendar.DATE, lastDay);
		int last_index = caltemp.get(Calendar.WEEK_OF_MONTH);
		return (week_index == last_index);
	}

	/**
	 *
	 * @param cal
	 * @return
	 */
	public static Calendar getCalendarWrapper(Calendar cal) {
		GregorianCalendar ret = new GregorianCalendar(){
			public String toString() {
				return DateUtil.toStringDate(getTimeInMillis(),FORMAT_DATE_SLASH);
			}
		};
		ret.setTimeInMillis(cal.getTimeInMillis());
		return ret;
	}

	/**
	 * 渡された日付の分が次に00または30となる時刻となる日付を返す。
	 * 例<br>
	 * 入力 03:00 → 変換後 03:00<br>
	 * 入力 03:01 → 変換後 03:30<br>
	 * 入力 03:30 → 変換後 03:30<br>
	 * 入力 03:31 → 変換後 04:00<br>
	 * 入力 03:59 → 変換後 04:00<br>
	 * 入力 04:00 → 変換後 04:00<br>
	 *
	 * @param calendar 変換する日付
	 * @return 渡された日付の次の00分または30分となる時刻
	 */
	public static Calendar getNext30Minite(Calendar calendar) {
		int minute = calendar.get(Calendar.MINUTE);
		if (minute == 0) {
			return calendar;
		}
		int minuteSub = minute - 30;
		int addValue = minuteSub * -1;
		if (minuteSub > 0) {
			addValue = 60 - minute;
		}
		calendar.add(Calendar.MINUTE, addValue);
		return calendar;
	}
	public static Calendar getNext15Minite(Calendar calendar) {
		int minute = calendar.get(Calendar.MINUTE);
		if (minute == 0) {
			return calendar;
		}
		int minuteSub = minute - 15;
		int addValue = minuteSub * -1;
		if (minuteSub > 0) {
			addValue = 60 - minute;
		}
		calendar.add(Calendar.MINUTE, addValue);
		return calendar;
	}



//public static void main(String[] args) {
//    System.out.println("\n----- 日付のみ ----------------------------------");
//    // yyyy/MM/ddのパターン
//    Calendar cal = toCalendar("2007/01/01");
//    Date date = cal.getTime();
//    System.out.println("[2007/01/01] = '"+date+"'");
//
//    // yyyy-MM-ddパターン
//    cal = toCalendar("2007-01-01");
//    date = cal.getTime();
//    System.out.println("[2007-01-01] = '"+date+"'");
//
//    // yyyyMMddパターン
//    cal = toCalendar("20070101");
//    date = cal.getTime();
//    System.out.println("[20070101]   = '"+date+"'");
//
//    // yy-MM-ddパターン
//    cal = toCalendar("07-01-01");
//    date = cal.getTime();
//    System.out.println("[07-01-01]   = '"+date+"'");
//
//
//    System.out.println("\n----- 日付+時刻 ---------------------------------");
//    // yyyy/MM/dd HH:mm:ssのパターン
//    cal = toCalendar("2007/01/01 12:00:00");
//    date = cal.getTime();
//    System.out.println("[2007/01/01 12:00:00] = '"+date+"'");
//
//    // yyyyMMdd HH:mm:ssのパターン
//    cal = toCalendar("20070101 12:00:00");
//    date = cal.getTime();
//    System.out.println("[20070101 12:00:00]   = '"+date+"'");
//
//    // yyyy/MM/dd HH:mmのパターン
//    cal = toCalendar("2007/01/01 12:00");
//    date = cal.getTime();
//    System.out.println("[2007/01/01 12:00]    = '"+date+"'");
//
//
//    System.out.println("\n----- 時刻のみ ----------------------------------");
//    // HH:mm:ssのパターン。※時刻のみの場合は日付を前に付加し使用する。
//    cal = toCalendar(
//            new SimpleDateFormat("yyyy/MM/dd").format(new Date()) +
//            " 12:00:00");
//    date = cal.getTime();
//    System.out.println("[12:00:00]   = '"+date+"'");
//}




}
