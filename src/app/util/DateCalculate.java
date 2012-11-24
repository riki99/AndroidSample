package app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCalculate {

	/**
	 * 2つの日付の日数の差を求めます。
	 *
	 * @param strDate1	日付文字列"yyyy/MM/dd"
	 * @param strDate2	日付文字列"yyyy/MM/dd"
	 * @return
	 * @throws ParseException
	 */
	public static int differenceDays(String strDate1,String strDate2)
		throws ParseException {
		return differenceDays(DateUtil.toDate(strDate1),
				DateUtil.toDate(strDate2));
	}
	/**
	 * 2つの日付の日数の差を求めます。
	 *
	 * 計算方法は以下となります。
	 * 最初に2つの日付を long 値に変換します。
	 * ※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの
	 * 　経過ミリ秒数となります。
	 * 次にその差を求めます。
	 * 上記の計算で出た差を 1 日の時間で割ります、
	 * ※1 日 ( 24 時間) は、86,400,000 ミリ秒です。
	 *
	 * @param date1	日付1
	 * @param date2	日付2
	 * @return	2つの日付の日数の差
	 */
	public static int differenceDays(Date date1,Date date2) {
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long result = (time1 - time2) / DateUtil.ONE_DATE_TIME_MILLIS;
		return (int)result;
	}

	/**
	 * 2つの日付の月数の差を求めます。
	 * 日付文字列の date1 - date2 が何ヵ月かを整数で返します。
	 *
	 * @param date1	日付文字列"yyyy/MM/dd"
	 * @param date2	日付文字列"yyyy/MM/dd"
	 * @return
	 * @throws ParseException
	 * @throws ParseException
	 */
	public static int differenceMonth(String date1, String date2)
		throws ParseException {
		return differenceMonth(DateUtil.toDate(date1), DateUtil
				.toDate(date2));
	}

	/**
	 * 2つの日付の月数の差を求めます。
	 * java.util.Date 型の日付 date1 - date2 が何ヵ月かを整数で返します。
	 * ※端数の日数は無視します。
	 *
	 * @param date1    日付1 java.util.Date
	 * @param date2    日付2 java.util.Date
	 * @return 2つの日付の月数の差
	 */
	public static int differenceMonth(Date date1, Date date2) {
	    Calendar cal1 = Calendar.getInstance();
	    cal1.setTime(date1);
	    cal1.set(Calendar.DATE, 1);
	    Calendar cal2 = Calendar.getInstance();
	    cal2.setTime(date2);
	    cal2.set(Calendar.DATE, 1);
	    int count = 0;
	    if (cal1.before(cal2)) {
	        while (cal1.before(cal2)) {
	            cal1.add(Calendar.MONTH, 1);
	            count--;
	        }
	    } else {
	        count--;
	        while (!cal1.before(cal2)) {
	            cal1.add(Calendar.MONTH, -1);
	            count++;
	        }
	    }
	    return count;
	}


	/**
	 * 比較元が比較対象より前の日付かチェックします。<br>
	 *
	 * @param origin
	 *            比較元
	 * @param target
	 *            比較対象
	 * @return <li>比較元が比較対象より前の日付の場合は true。 </li>
	 * 			<li>そうでない場合はfalse。 </li>
	 */
	public static boolean isBeforeDate(java.util.Date origin,
			java.util.Date target) {
		return compareDate(origin, target) < 0 ? true : false;
	}

	/**
	 * 比較元が比較対象より後の日付かチェックします。<br>
	 *
	 * @param origin
	 *            比較元
	 * @param target
	 *            比較対象
	 * @return <li>比較元が比較対象より後の日付の場合は true。 </li>
	 * 			<li>そうでない場合はfalse。 </li>
	 */
	public static boolean isAfterDate(java.util.Date origin,
			java.util.Date target) {
		return compareDate(origin, target) > 0 ? true : false;
	}

	/**
	 * 比較元が比較対象と一致した日付かチェックします。<br>
	 *
	 * @param origin
	 *            比較元
	 * @param target
	 *            比較対象
	 * @return <li>比較元が比較対象と同じ日付の場合は true。 </li>
	 * 			<li>そうでない場合はfalse。 </li>
	 */
	public static boolean isEqualDate(java.util.Date origin,
			java.util.Date target) {
		return compareDate(origin, target) == 0 ? true : false;
	}

	/**
	 * 2つの日付を比較します。<br>
	 *
	 * @param origin
	 *            比較元
	 * @param target
	 *            比較対象
	 * @return <li>比較元と比較先が等しい Date の場合は値 0。 </li>
	 * 			<li>比較対象が比較元より後の Date の場合は 0 より小さい値。</li>
	 * 			<li>比較対象が比較元の前の Date の場合は 0 より大きい値。</li>
	 */
	public static int compareDate(java.util.Date origin, java.util.Date target) {

		// 日付から時間を削る
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_DATE_SLASH);
		origin = DateUtil.toSqlDate(sdf.format(origin));
		target = DateUtil.toSqlDate(sdf.format(target));

		return origin.compareTo(target);
	}


	// --- ------------------------------------------------------------
	/**
	 * 現在の日付・時刻から指定の【年数】を加算・減算した結果を返します。
	 * @param addYera 加算・減算する年数
	 * @return    計算後の Calendar インスタンス。
	 */
	public static Calendar addYera(int addYera){
	    return add(null,addYera,0,0,0,0,0,0);
	}
	/**
	 * 現在の日付・時刻から指定の【年数】を加算・減算した結果を返します。
	 * @param addYera 加算・減算する年数
	 * @return    計算後の Calendar インスタンス。
	 */
	public static Calendar addYera(Calendar cal,int addYera){
	    return add(cal,addYera,0,0,0,0,0,0);
	}

	/**
	 * 現在の日付・時刻から指定の【月数】を加算・減算した結果を返します。
	 * @param addMonth 加算・減算する月数
	 * @return    計算後の Calendar インスタンス。
	 */
	public static Calendar addMonth(int addMonth){
	    return add(null,0,addMonth,0,0,0,0,0);
	}
	public static Calendar addMonth(Calendar cal,int addMonth){
		return add(cal,0,addMonth,0,0,0,0,0);
	}

	/**
	 * 現在の日付・時刻から指定の【日数】を加算・減算した結果を返します。
	 * @param addDate 加算・減算する日数
	 * @return    計算後の Calendar インスタンス。
	 */
	public static Calendar addDate(int addDate){
	    return add(null,0,0,addDate,0,0,0,0);
	}
	/**
	 * 現在の日付・時刻から指定の【時間】を加算・減算した結果を返します。
	 * @param addHour 加算・減算する時間
	 * @return    計算後の Calendar インスタンス。
	 */
	public static Calendar addHour(int addHour){
	    return add(null,0,0,0,addHour,0,0,0);
	}
	/**
	 * 現在の日付・時刻から指定の【分】を加算・減算した結果を返します。
	 * @param addMinute 加算・減算する分
	 * @return    計算後の Calendar インスタンス。
	 */
	public static Calendar addMinute(int addMinute){
	    return add(null,0,0,0,0,addMinute,0,0);
	}
	public static Calendar addMinute(Calendar cal, int addMinute){
	    return add(cal,0,0,0,0,addMinute,0,0);
	}

	/**
	 * 現在の日付・時刻から指定の【秒】を加算・減算した結果を返します。
	 * @param addSecond 加算・減算する秒
	 * @return    計算後の Calendar インスタンス。
	 */
	public static Calendar addSecond(int addSecond){
	    return add(null,0,0,0,0,0,addSecond,0);
	}
	/**
	 * 現在の日付・時刻から指定の時間量を加算・減算した結果を返します。
	 * 年、月、日、時間、分、秒、ミリ秒の各時間フィールドに対し、
	 * 任意の時間量を設定できます。
	 * たとえば、現在の日付時刻から 10 日前を計算する場合は以下となります。
	 * Calendar cal = add(null,0,0,-10,0,0,0,0);
	 *
	 * 各時間フィールドの値がその範囲を超えた場合、次の大きい時間フィールドが
	 * 増分または減分されます。
	 * たとえば、以下では1時間と5分進めることになります。
	 * Calendar cal = add(null,0,0,0,0,65,0,0);
	 *
	 * 各時間フィールドに設定する数量が0の場合は、現在の値が設定されます。
	 * java.util.GregorianCalendarの内部処理では以下の分岐を行っている。
	 *     if (amount == 0) {
	 *         return;
	 *     }
	 *
	 * @param cal 日付時刻の指定があればセットする。
	 * 		nullの場合、現在の日付時刻で新しいCalendarインスタンスを生成する。
	 * @param addYera 加算・減算する年数
	 * @param addMonth 加算・減算する月数
	 * @param addDate 加算・減算する日数
	 * @param addHour 加算・減算する時間
	 * @param addMinute 加算・減算する分
	 * @param addSecond 加算・減算する秒
	 * @param addMillisecond 加算・減算するミリ秒
	 * @return    計算後の Calendar インスタンス。
	 */
	public static Calendar add(Calendar cal,
								int addYera,int addMonth,int addDate,
	                             int addHour,int addMinute,int addSecond,
	                             int addMillisecond){
	    if (cal == null) {
			cal = Calendar.getInstance();
		}
	    cal.add(Calendar.YEAR, addYera);
	    cal.add(Calendar.MONTH, addMonth);
	    cal.add(Calendar.DATE, addDate);
	    cal.add(Calendar.HOUR_OF_DAY, addHour);
	    cal.add(Calendar.MINUTE, addMinute);
	    cal.add(Calendar.SECOND, addSecond);
	    cal.add(Calendar.MILLISECOND, addMillisecond);

//		if (logger.isLoggable(Level.CONFIG)) {
//			logger.config("date = "+DateUtil.toStringDate(cal)); //$NON-NLS-1$
//		}
	    return cal;
	}


//	public static int getDifferenceDays(Calendar cal1, Calendar cal2) {
//	if (isSameDate(cal1, cal2)) {
//		return 0;
//	}
//	Calendar buf = (Calendar) cal2.clone();
//	int diff = 0;
//	int addition = 1;
//	if (cal1.before(cal2)) {
//		addition = -1;
//	}
//	while (!isSameDate(cal1, buf)) {
//		buf.add(Calendar.DATE, addition);
//		diff++;
//	}
//	return diff;
//}

//private static boolean isSameDate(Calendar cal1, Calendar cal2) {
//	if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
//		return false;
//	}
//	if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {
//		return false;
//	}
//	if (cal1.get(Calendar.DATE) != cal2.get(Calendar.DATE)) {
//		return false;
//	}
//	return true;
//}

	public static void main(String[] args) throws Exception {

//		Date date1 = DateFormat.getDateInstance().parse("2009/6/6");
//		Date date2 = DateFormat.getDateInstance().parse("2008/6/3");
//		int ret = differenceDays(date1,date2);

		int ret = differenceDays("2009/6/3","2009/6/1");
		System.out.println(ret);

//		SystemDate.getInstance().setSysDate("2008/02/29 00:00:00");
//		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		System.out.println(f.format(add(DateUtil.getCalendar(),0,1,0,0,0,0,0).getTime()));


//		System.out.println("現在　　　の日付・時刻 = "+ f.format(add(null,0,0,0,0,0,0,0).getTime()));
//		System.out.println("１年後　　の日付・時刻 = "+ f.format(addYera(1).getTime()));
//		System.out.println("１ヶ月前　の日付・時刻 = "+ f.format(addMonth(-1).getTime()));
//		System.out.println("３時間後　の日付・時刻 = "+ f.format(addHour(3).getTime()));
//		System.out.println("３０時間前の日付・時刻 = "+ f.format(addHour(-30).getTime()));
//		System.out.println("４０分後　の日付・時刻 = "+ f.format(addMinute(40).getTime()));
//		System.out.println("８０秒後　の日付・時刻 = "+ f.format(addSecond(80).getTime()));
//		System.out.println("１ヶ月前から、５日後の日付・時刻 = "+ f.format(add(null,0,-1,4,0,0,0,0).getTime()));



	}

}
