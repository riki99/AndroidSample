package app.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * 文字列ユーティリティクラス
 * @author 田辺
 */
public class StringUtil {

	public static void main(String[] args) {
		int c = countMatches("<table border='0'><tr><td>ユーザ名</td><td>：satton</td></tr><tr><td>グループ名</td><td>：</td></tr><tr><td>コンピュータ名</td><td>：AX-TANABE</td></tr><tr><td>ログオン名</td><td>：satton</td></tr><tr><td>IPアドレス</td><td>：10.250.30.104</td></tr><tr><td>表示優先順位</td><td>：低</td></tr><tr><td>メッセンジャー</td><td>：Chat&Messenger 2.22</td></tr><tr><td>暗号化	</td><td>：　対応</td></tr></table>","</tr>");
		System.out.println(c);
	}
	// ------------------------------------------------------『 文字列評価関連 』
	/**
	 * 文字列[str]が、指定された正規表現[regex]と一致するかどうかを判定します。
	 * <p>
	 * 例： boolean b = StringUtil.isMatches("image.gif",
	 * ".*\\.(gif|jpg|css|js)"); b は trueです。
	 * @param str 評価対象の文字列
	 * @param regex 正規表現パターン
	 * @return [ture]:正規表現にマッチした場合
	 */
	public static boolean isMatches(String str, String regex) {
		if (str == null || regex == null) {
			return false;
		}
		return str.matches(regex);
	}

	/**
	 * 文字列[str]内で、文字列[pattern]内の文字列の最初にマッチする文字列を返します。
	 * <p>
	 * 例： String mached =
	 * StringUtil.getFirstMached("ccccbbbbbaaa","aaa|bbb|ccc"); machedは'ccc'
	 * @param str 評価対象の文字列
	 * @param pattern '|'で区切られた文字列
	 * @return マッチする文字列が無い場合nullを返します。
	 */
	public static String getFirstMached(String str, String pattern) {
		TreeMap map = new TreeMap();
		String[] patterns = parseArray(pattern, "|");
		for (int i = 0; i < patterns.length; i++) {
			int start = str.indexOf(patterns[i]);
			if (start != -1) {
				map.put(new Integer(start), patterns[i]);
			}
		}
		if (map.size() == 0) {
			return null;
		}
		Object min = Collections.min(map.keySet());
		return (String) map.get(min);
	}

	/**
	 * 文字列[str]に[c]が含まれるカウントを返します。
	 * @param str 評価対象の文字列
	 * @param c カウント対象のchar
	 * @return cが含まれる数
	 */
	public static int getCharMatchCount(String str, char c) {
		int count = 0;
		for (int i = 0; i < str.length() ; i++) {
			if (str.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 文字列[str]に[c]が含まれるカウントを返します。
	 * @param str 評価対象の文字列
	 * @param c カウント対象のchar
	 * @return cが含まれる数
	 */
	public static int countMatches(String str, String s) {
		int count = 0;
		String[] ss = str.split(s);
		if (ss != null) {
			// 最後が評価対象の文字列となる場合、その分は分割されないため
			s = StringUtil.replace(s,"\\","");
			if (str.endsWith(s)) {
				count = ss.length;
			} else {
				count = ss.length - 1;
			}
		}
		return count;
	}

	/**
	 * 文字列[str]が、指定された接頭辞[prefix]で始まるかどうかを判定します。 大文字・小文字を問わず判定します。
	 * @param str 評価対象の文字列
	 * @param prefix 接頭辞
	 * @return [tuure]:引数によって表される文字列が、この文字列によって表される 文字列の接頭辞である場合。
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		str = str.toLowerCase();
		return (str.startsWith(prefix));
	}

	public static boolean includeIgnoreSplit(String strTarget,
			String keywordsStr, boolean and) {
		String[] s = keywordsStr.split(" |　");
		return containsStr(strTarget, s, and, false) != null;
	}

	public static String containsStr(String strTarget,String keywordsStr,
			boolean and) {
		String[] s = keywordsStr.split(" |　");
		return containsStr(strTarget, s, and, true);
	}
	public static String containsStr(String strTarget, ArrayList keywords,
			boolean and) {
		String[] types = (String[]) keywords.toArray(new String[] {});
		return containsStr(strTarget, types, and, true);
	}
	/**
	 * strTarget文字列に、keywordsの各要素が含まれるかどうかを返す。
	 * 含まれる場合、outlineフラグがtrueの場合、
	 * 直前・直後の行、または10文字をアウントライントとして返す。
	 * （outlineフラグがfalseの場合、は空の文字列とする）
	 *
	 * マッチしなかった場合は、nullを返す。
	 *
	 * @param strTarget
	 * @param keywords
	 * @param and 「keywords 文字列」をAND検索、OR検索のどちらで行うか
	 * @param outline
	 * @return
	 */
	public static String containsStr(String strTarget, String[] keywords,
			boolean and, boolean outline) {
		// マッチしたテキストを格納
		StringBuffer buff = new StringBuffer();;
		int index = -1;
		for (int i = 0; i < keywords.length; i++) {
			index = indexOfIgnoreCase(strTarget, keywords[i]);
			// マッチした場合、直前・直後の行、または10文字をアウントライントとしてバッファへ格納
			if (outline && index != -1) {
				// まだアウトライン文字列に含まれていない場合のみ格納
				if (buff.indexOf(keywords[i]) == -1) {
					String outlineStr = indexOutline(strTarget, index, 2, 45);
					buff.append(outlineStr + "\n");
				}
			}
			if (and) {
				if (index == -1) {
					return null;
				}
			}else{
				if (index != -1) {
					return buff.toString();
				}
			}
		}
		if (and && index != -1) {
			return buff.toString();
		}
		return null;
	}
	/**
	 * 指定したindexから前後の文字列を返す。
	 * 直前・直後の改行、または length 分の文字を開始・終了位置とする
	 * @param strTarget
	 * @param index
	 * @return
	 */
	public static String indexOutline(String strTarget, int index,
			int newlineN, int length){
		int start = 0;
		int end = 0;
		int newlineCount = 0;

		int startIndex = index;
		for (int i = 0; i < newlineN; i++) {
			int point = strTarget.lastIndexOf("\n", startIndex);
			if (point == -1) {
				break;
			}
			startIndex = point - 1;
			++ newlineCount;
			if (newlineCount == newlineN) {
				start = point;
				break;
			}
		}
		// 直前・直後の改行でマッチしない場合は、length分先読みした位置が開始位置
//		if (start == 0) {
//			if (index - length > 0) {
//				start = index - length;
//			}
//		}
		start = Math.max(start, index - length);

		newlineCount = 0;
		int endIndex = index;
		for (int i = 0; i < newlineN; i++) {
			int point = strTarget.indexOf("\n", endIndex);
			if (point == -1) {
				break;
			}
			endIndex = point + 1;
			++ newlineCount;
			if (newlineCount == newlineN) {
				end = point;
				break;
			}
		}
		// 直前・直後の改行でマッチしない場合は、length分先読みした位置が開始位置
		if (end == 0) {
			end = strTarget.length();
		}
		end = Math.min(end, index + length);
		String matchestr = strTarget.substring(start, end);
		return matchestr;
	}


	/**
	 * [str1]の文字列内で、[str2]文字列が最初に出現する位置のインデックスを 返します。 大文字、小文字を区別しません。
	 * 全角、半角を区別しません。
	 * @param str1 評価対象の文字列
	 * @param str2 部分文字列
	 * @return 文字列引数がこのオブジェクト内の部分文字列である場合は、 該当する最初の部分文字列の最初の文字のインデックス。
	 *         部分文字列がない場合は -1
	 */
	public static int indexOfIgnoreCase(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return -1;
		}
		str1 = allHankaku(str1);
		str2 = allHankaku(str2);
		int p = str1.toUpperCase().indexOf(str2.toUpperCase());
		if (p != -1) {
			return p;
		}
		return -1;
	}

	// -----------------------------------------------------------『変換関連 』

	/**
	 * 文字列[str]に対して[index]の位置にある文字を大文字または小文字に変換します。
	 * <p>
	 * @param str 評価対象の文字列
	 * @param index 指定する位置
	 * @param toCase 大文字に変換 ⇒ U | u 小文字に変換 ⇒ L | l
	 * @return 変換後の文字列
	 */
	public static String convString(String str, int index, String toCase) {
		if (str == null || str.trim().length() == 0) {
			return str;
		} else {
			String temp = str.substring(index, index + 1);
			if (toCase.equalsIgnoreCase("u")) {
				temp = temp.toUpperCase();
			} else {
				temp = temp.toLowerCase();
			}
			StringBuffer tempBuffer = new StringBuffer(str);
			tempBuffer.replace(index, index + 1, temp);
			return tempBuffer.toString();
		}
	}

	/**
	 * 文字列配列[strArray]の内容を、下記のようなStringで返します。 "[temp1,temp2,temp3]"
	 * @param strArray 評価対象のString配列
	 * @return 変換後の文字列
	 */
	public static String convString(String[] strArray) {
		if (!isExist(strArray))
			return null;
		StringBuffer buff = new StringBuffer("[");
		for (int i = 0; i < strArray.length; i++) {
			buff.append(strArray[i] + ", ");
		}
		buff.delete(buff.length() - 2, buff.length());
		buff.append("]");
		return buff.toString();
	}

	/**
	 * 数値[number]に対し、指定した桁数に満たない場合、前に”0”を補充します。
	 * <p>
	 * 例：getFrontZero(55,5) → "00055"
	 * @param number intでもlongでもOK
	 * @param len 補充するまでの桁数
	 * @return ”0”を補充した文字列
	 */
	public static String getFrontZero(long number, int len) {
		return fillString(Long.toString(number), "L", len, "0");
	}

	/**
	 * 数値の文字リテラル[str]に対し、指定した桁数に満たない場合、前に”0”を補充します。
	 * <p>
	 * 例：getFrontZero("55",5) → "00055"
	 * @param str 補充対象文字列
	 * @param len 補充するまでの桁数
	 * @return ”0”を補充した文字列
	 */
	public static String getFrontZero(String str, int len) {
		return fillString(str, "L", len, "0");
	}

	/**
	 * LPADを行います。
	 * 文字列[str]の左に指定した文字列[addStr]を[len]に
	 * 満たすまで挿入します。
	 * @param str 対象文字列
	 * @param len 補充するまでの桁数（LPADを行った後のサイズを指定します。）
	 * @param addStr 挿入する文字列
	 * @return 変換後の文字列。
	 */
	public static String lpad(String str, int len, String addStr) {
		return fillString(str, "L", len, addStr);
	}

	/**
	 * RPADを行います。
	 * 文字列[str]の右に指定した文字列[addStr]を[len]に
	 * 満たすまで挿入します。
	 * @param str 対象文字列
	 * @param len 補充するまでの桁数（RPADを行った後のサイズを指定します。）
	 * @param addStr 挿入する文字列
	 * @return 変換後の文字列。
	 */
	public static String rpad(String str, int len, String addStr) {
		return fillString(str, "R", len, addStr);
	}

	/**
	 * 文字列[str]に対して、補充する文字列[addStr]を
	 * [position]の位置に[len]に満たすまで挿入します。
	 *
	 * ※[str]がnullや空リテラルの場合でも[addStr]を
	 * [len]に満たすまで挿入した結果を返します。
	 * @param str 対象文字列
	 * @param position 前に挿入 ⇒ L or l 後に挿入 ⇒ R or r
	 * @param len 補充するまでの桁数
	 * @param addStr 挿入する文字列
	 * @return 変換後の文字列。
	 */
	public static String fillString(String str, String position,
			int len,
			String addStr) {
		if (addStr == null || addStr.length() == 0) {
			throw new IllegalArgumentException
			("挿入する文字列の値が不正です。addStr="+addStr);
		}
		if (str == null) {
			str = "";
		}
		StringBuffer buffer = new StringBuffer(str);
		while (len > buffer.length()) {
			if (position.equalsIgnoreCase("l")) {
				int sum = buffer.length() + addStr.length();
				if (sum > len) {
					addStr = addStr.substring
					(0,addStr.length() - (sum - len));
					buffer.insert(0, addStr);
				}else{
					buffer.insert(0, addStr);
				}
			} else {
				buffer.append(addStr);
			}
		}
		if (buffer.length() == len) {
			return buffer.toString();
		}
		return buffer.toString().substring(0, len);
	}

	/**
	 * 文字列[str]に対して補充する文字列[addStr]を[position]の位置に[len]分挿入します。
	 * <p>
	 * 例： String ss = StringUtil.addString("aaa","b","0",7); ss ⇒ "aaa0000000"
	 * ※fillString()はlenに満たすまで挿入しますが、addString()はlen分挿入します。
	 * @param str 対象文字列
	 * @param position 前に挿入 ⇒ F/f 後に挿入 ⇒ B/b
	 * @param addStr 挿入する文字列
	 * @param len 補充するまでの桁数
	 * @return 変換後の文字列。 [str]がnullや空リテラルも[addStr]を[len]分挿入した結果を返します。
	 */
	public static String addString(String str, String position, String addStr,
			int len) {
		StringBuffer tempBuffer = null;
		if (!isExist(str)) {
			tempBuffer = new StringBuffer();
			for (int i = 0; i < len; i++) {
				tempBuffer.append(addStr);
			}
			return tempBuffer.toString();
		} else {
			tempBuffer = new StringBuffer(str);
			for (int i = 0; i < len; i++) {
				if (position.equalsIgnoreCase("f")) {
					tempBuffer.insert(0, addStr);
				} else {
					tempBuffer.append(addStr);
				}
			}
			return tempBuffer.toString();
		}
	}

	/**
	 * 指定したバイト数のスペースを返します。
	 * @param len 補充するまでの桁数
	 * @return 変換後の文字列。
	 */
	public static String getStrSpace(int len) {
		StringBuffer buffSpace = new StringBuffer("");
		for (int i = 0; i < len; i++) {
			buffSpace.append(" ");
		}
		return buffSpace.toString();
	}

	/**
	 * 全角アルファベットを半角アルファベットに変換する
	 * @param s
	 * @return
	 */
	public static String allHankaku(String s) {
		s = zenkakuAlphabetToHankaku(s);
		s = zenkakuNumToHankaku(s);
		return s;
	}

	/**
	 * 全角アルファベットを半角アルファベットに変換する
	 * @param s
	 * @return
	 */
	public static String zenkakuAlphabetToHankaku(String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c >= 'ａ' && c <= 'ｚ') {
				sb.setCharAt(i, (char) (c - 'ａ' + 'a'));
			} else if (c >= 'Ａ' && c <= 'Ｚ') {
				sb.setCharAt(i, (char) (c - 'Ａ' + 'A'));
			}
		}
		return sb.toString();
	}

	/**
	 * 半角アルファベットを全角アルファベットに変換する
	 * @param s
	 * @return
	 */
	public static String hankakuAlphabetToZenkaku(String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'z') {
				sb.setCharAt(i, (char) (c - 'a' + 'ａ'));
			} else if (c >= 'A' && c <= 'Z') {
				sb.setCharAt(i, (char) (c - 'A' + 'Ａ'));
			}
		}
		return sb.toString();
	}

	public static String hankakuNumToZenkaku(String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= '0' && c <= '9') {
				sb.setCharAt(i, (char) (c - '0' + '０'));
			}
		}
		return sb.toString();
	}

	/**
	 * 全角数字を半角に変換します。
	 * @param s 変換元文字列
	 * @return 変換後文字列
	 */
	public static String zenkakuNumToHankaku(String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c >= '０' && c <= '９') {
				sb.setCharAt(i, (char) (c - '０' + '0'));
			}
		}
		return sb.toString();
	}

	// -----------------------------------------------------------------------『
	// 置換関連 』
	/**
	 * [tempString]内の[oldString]を[newString]に置換します。
	 * @param tempString 置換対象文字列
	 * @param oldString 置換される部分文字列
	 * @param newString 置換する部分文字列
	 * @return 置換された文字列
	 */
	public static String replace(String tempString, String oldString,
			String newString) {
		if (tempString == null)
			return null;
		StringBuffer sb = new StringBuffer();
		int hitIndex = -1;
		int currentIndex = 0;
		while ((hitIndex = tempString.indexOf(oldString, currentIndex)) != -1) {
			sb.append(tempString.substring(currentIndex, hitIndex));
			sb.append(newString);
			currentIndex = hitIndex + oldString.length();
		}
		sb.append(tempString.substring(currentIndex));
		return sb.toString();
	}

	/**
	 * [tempString]内の[oldString]を[newString]に置換します。
	 * @param tempString 置換対象文字列
	 * @param oldString 置換される部分文字列
	 * @param newString 置換する部分文字列
	 * @return 置換された文字列
	 */
	public static String replaceIgnoreCase(String tempString, String oldString,
			String newString) {
		String lowerCase = oldString.toLowerCase();
		tempString = replace(tempString, lowerCase, newString);

		String upperCase = oldString.toUpperCase();
		tempString = replace(tempString, upperCase, newString);

		return tempString;
	}

	/**
	 * [tempString]内の[allString]内を一文字ずつ分割し全て[newString]に置換します。
	 * @param tempString
	 * @param allString
	 * @param newString
	 * @return
	 */
	public static String replaceAll(String tempString, String allString,
			String newString) {
		if (tempString == null)
			return null;
		char[] chars = allString.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			tempString = replace(tempString, "" + chars[i], newString);
		}
		return tempString;
	}

	// -----------------------------------------------------------------『
	// 文字コード関連 』
	/**
	 * [str]をユニコードへ変換します。
	 * @param str
	 * @return
	 */
	public static String conv(String str, String enc) {
		if (str == null)
			return null;
		try {
			return new String(str.getBytes("8859_1"), enc);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * [str]をユニコードへ変換します。
	 * @param str
	 * @return
	 */
	public static String convUnicode(String str) {
		if (str == null)
			return null;
		try {
			return new String(str.getBytes("8859_1"), "JISAutoDetect");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * [str]をSJISへ変換します。
	 * @param str
	 * @return
	 */
	public static String convSJIS(String str) {
		if (str == null)
			return null;
		try {
			return new String(str.getBytes("8859_1"), "Shift_JIS");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 。
	 * @return
	 * @param str 置換対象文字列
	 */
	public static String getUnicodeEscape(String str) {
		String unicodeEscape = "";
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			String hex = Integer.toHexString(cs[i]);
			hex = StringUtil.fillString(hex, "L", 4, "0");
			unicodeEscape = unicodeEscape + "\\u" + hex;
		}
		return unicodeEscape;
	}

	/**
	 * strがASCIIを含んでいたら、その文字を;区切りで返します。 含んでいない場合はnullを返します。 注意 ： 改行の'/n'は除きます。
	 * @param str
	 * @return
	 */
	public static String extractASCII(String str) {
		StringBuffer tempBuffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			UnicodeBlock b = UnicodeBlock.of(c);
			if (c == '\r' || c == '\n') {
				continue;
			}
			if (b == UnicodeBlock.BASIC_LATIN) {
				tempBuffer.append(c);
			}
		}
		if (tempBuffer.length() != 0) {
			return tempBuffer.toString();
		} else {
			return null;
		}
	}

	/**
	 * 文字列がASCII以外を含んでいたら、その文字を;区切りで返します。
	 * @param str
	 * @return
	 */
	public static String extractNOT_ASCII(String str) {
		StringBuffer tempBuffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			UnicodeBlock b = UnicodeBlock.of(c);
			if (c == '\r' || c == '\n') {
				continue;
			}
			if (b != UnicodeBlock.BASIC_LATIN) {
				tempBuffer.append(c);
			}
		}
		if (tempBuffer.length() != 0) {
			return tempBuffer.toString();
		} else {
			return null;
		}

	}

	/**
	 * strが半角の英・記号を含んでいたら、その文字を;区切りで返します。 （ 半角数字はOK ） 含んでいない場合はnullを返します。 注意 ：
	 * 改行の'/n'は除きます。
	 * @param str
	 * @return
	 */
	public static String extractHAN_EI_KIGOU(String str) {
		StringBuffer tempBuffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			UnicodeBlock b = UnicodeBlock.of(c);
			if (c == '\r' || c == '\n') {
				continue;
			}
			if (b == UnicodeBlock.BASIC_LATIN && !Character.isDigit(c) && !Character.isLetter(c)) {
				tempBuffer.append(c);
			}
		}
		if (tempBuffer.length() != 0) {
			return tempBuffer.toString();
		} else {
			return null;
		}
	}

	/**
	 * strが半角の数字を含んでいたら、その文字を;区切りで返します。 含んでいない場合はnullを返します。 注意 ： 改行の'/n'は除きます。
	 * @param str
	 * @return
	 */
	public static String extractHAN_NUMBER(String str) {
		StringBuffer tempBuffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			UnicodeBlock b = UnicodeBlock.of(c);
			if (c == '\r' || c == '\n') {
				continue;
			}
			if (b == UnicodeBlock.BASIC_LATIN && Character.isDigit(c)) {
				tempBuffer.append(c);
			}
		}
		if (tempBuffer.length() != 0) {
			return tempBuffer.toString();
		} else {
			return null;
		}
	}

	/**
	 * strが半角カナを含んでいたら、その文字を;区切りで返します。 含んでいない場合はnullを返します。
	 * 半角カナと全角英数が[使用が推奨されていないもろもろの文字]という意味の
	 * [HALFWIDTH_FULLWIDTH_FORMS]という同じカテゴリになっている
	 * という弱点があります。そこで、半角ASCII、半角カナ、全角文字を識別するには、現在は
	 * ・UTF-8のJava文字列を、シフトJISか日本語EUCのバイトの並びに変換する。
	 * ・それらの文字コードの特性から、半角カナや全角文字が含まれるかどうかを調べる
	 * @param str
	 * @return
	 */
	public static String extractHAN_KANA(String str) {
		StringBuffer tempBuffer = new StringBuffer();
		byte[] b;
		try {
			b = str.getBytes("EUC_JP");
			for (int i = 0; i < b.length; i++) {
				if (b[i] == -114) { // 0x8e
					byte[] temp = new byte[1];
					temp[0] = b[i + 1];
					String strTemp = new String(temp);
					tempBuffer.append(strTemp);
				}
			}
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
		if (tempBuffer.length() != 0) {
			return tempBuffer.toString();
		} else {
			return null;
		}
	}

	/**
	 * ①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳ ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩ ㍉㌔㌢㍍㌘㌧㌃㌶㍑㍗㌍㌦㌣㌫㍊㌻ ㎜㎝㎞㎎㎏㏄㎡
	 * 〝〟№㏍℡㊤㊥㊦㊧㊨㈱㈲㈹㍾㍽㍼㍻ ≒≡∫∮∑√⊥∠∟⊿∵∩∪
	 * @param str
	 * @return
	 */
	public static String extractDependChar(String str) {
		System.out.println("str = " + str);
		StringBuffer tempBuffer = new StringBuffer();
		byte[] b;
		try {
			b = str.getBytes("MS932");
			for (int i = 0; i < b.length; i++) {
				if (b[i] == -121) {
					byte[] temp = { b[i], b[i + 1] };
					System.out.print(b[i] + ",");
					System.out.print(b[i + 1] + "\n");
					String strTemp = new String(temp);
					tempBuffer.append(strTemp);
				}
			}
		} catch (Exception ex) {
			return null;
		}
		if (tempBuffer.length() != 0) {
			return tempBuffer.toString();
		} else {
			return null;
		}
	}

	// -------------------------------------------------------------------------『
	// その他 』
	/**
	 * [tempArray]の内容を、ArrayListとして返します。
	 * @param tempArray [;] or [:]で区切られた文字列
	 * @return
	 */
	public static ArrayList convArrayList(String[] tempArray) {
		if (!isExist(tempArray))
			return null;
		ArrayList tempList = new ArrayList();
		for (int i = 0; i < tempArray.length; i++) {
			tempList.add(tempArray[i]);
		}
		return tempList;
	}

	/**
	 * [tempArray]の内容を、ArrayListとして返します。
	 * @param tempArray [;] or [:]で区切られた文字列
	 * @return
	 */
	public static ArrayList convArrayList(String tempArray) {
		String[] array = parseArray(tempArray);
		ArrayList list = convArrayList(array);
		return list;
	}

	/**
	 * [tempArray]内の[;] or [:]で区切られた文字列をString配列として返します。
	 * @param tempArray [;] or [:]で区切られた文字列
	 * @return String配列
	 */
	public static String[] parseArray(String tempArray) {
		return parseArray(tempArray, ";:");
	}

	/**
	 * 以下のようなキーと要素を含む文字列を配列の0番目、1番目に格納した 結果で返します。
	 *
	 * <pre>
	 *       例1
	 *            　&quot;aaa=111&quot;
	 *                  ↓
	 *          	[key]      [value]
	 *         	&quot;aaa&quot;  =  &quot;111&quot;
	 *
	 *       例2
	 *            &quot;aaa=111=@@@&quot;
	 *                  ↓
	 *          	[key]      [value]
	 *         	&quot;aaa&quot;  =  &quot;111=@@@&quot;
	 * </pre>
	 *
	 * @param str 変換対象の文字列
	 * @return 変換後のString配列 strがnullの場合、=を含まない場合
	 */
	public static String[] parseKeySet(String str) {
		return parseKeySet(str, "=");
	}

	/**
	 * 以下のようなキーと要素を含む文字列を配列の0番目、1番目に格納した 結果で返します。
	 *
	 * <pre>
	 *       例1
	 *            　&quot;aaa=111&quot;
	 *                  ↓
	 *          	[key]      [value]
	 *         	&quot;aaa&quot;  =  &quot;111&quot;
	 *
	 *       例2
	 *            &quot;aaa=111=@@@&quot;
	 *                  ↓
	 *          	[key]      [value]
	 *         	&quot;aaa&quot;  =  &quot;111=@@@&quot;
	 * </pre>
	 *
	 * @param str 変換対象の文字列
	 * @param delim キーと値の区切り文字
	 * @return 変換後のString配列 strがnullの場合、=を含まない場合
	 */
	public static String[] parseKeySet(String str, String delim) {
		if (!StringUtil.isExist(str))
			return null;
		int count = str.indexOf(delim);
		if (count == -1)
			return null;

		String key = str.substring(0, count);
		String value = str.substring(count + 1);

		// 重要 ：空の文字列""を値に入れると具合が悪い
		if (!StringUtil.isExist(value))
			value = null;
		return new String[] { key, value };
	}

	/**
	 * [tempArray]内の[delim]で区切られた文字列をString配列として返します。
	 * @param tempArray
	 * @param delim
	 * @return
	 */
	public static String[] parseArray(String tempArray, String delim) {
		if (!isExist(tempArray))
			return null;
		StringTokenizer token = new StringTokenizer(tempArray, delim);
		String[] tokenArry = new String[token.countTokens()];
		int i = 0;
		while (token.hasMoreTokens()) {
			tokenArry[i] = token.nextToken();
			i++;
		}
		return tokenArry;
	}

	/**
	 * ファイル名から拡張子を返します。
	 * @param fileName ファイル名
	 * @return ファイルの拡張子
	 */
	public static String getSuffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(point + 1);
		}
		return fileName;
	}

	/**
	 * ファイル名から拡張子を取り除いた名前を返します。
	 * @param fileName ファイル名
	 * @return ファイル名
	 */
	public static String getPreffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(0, point);
		}
		return fileName;
	}

	/**
	 * ファイル名から拡張子を取り除いた名前を返します。
	 * @param fileName ファイル名
	 * @return ファイル名
	 */
	public static String getPreffix2(String fileName) {
		if (fileName == null)
			return null;
		fileName = new File(fileName).getName();
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(0, point);
		}
		return fileName;
	}

	/**
	 * [str]が数値に変換できるかどうかを検証します。
	 * @param str 評価対象文字列
	 * @return [true]:nullでなく""でない場合
	 */
	public static boolean isNumber(String str) {
		try {
			new BigDecimal(str).doubleValue();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * [str]が整数かどうかを検証します。
	 * @param str 評価対象文字列
	 * @return [true]:nullでなく""でない場合
	 */
	public static boolean isLong(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 数値オブジェクト[number]を#,###にフォーマットします。
	 * @param number 数値オブジェクト
	 * @return
	 */
	public static String numFormat(long number) {
		return numFormat(new Long(number));
	}

	/**
	 * [value]が有効な値かどうか検証します。
	 * @param value 評価対象文字列
	 * @return [true]:nullでなく""でない場合
	 */
	public static boolean isExist(String value) {
		if (value != null && value.length() != 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String value) {
		return !isExist(value);
	}

	public static String nullString(String value) {
		if (value != null) {
			return value;
		}
		return "";
	}

	/**
	 * [values]が有効な値かどうか検証します。
	 * @param values 評価対象文字列の配列
	 * @return [true]:nullでなくサイズが0でない場合
	 */
	public static boolean isExist(String[] values) {
		if (values != null && values.length != 0) {
			return true;
		}
		return false;
	}

	public static boolean isTrue(String value) {
		value = value.toLowerCase();
		if ("true".equals(value) || "t".equals(value) || "1".equals(value)) {
			return true;
		}
		return false;
	}

	/**
	 * 改行コードを取得します。
	 * @return 改行コード
	 */
	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}

	/**
	 * 文字列をあるサイズ（バイト）数分で分割して
	 * 新しい文字列配列を返す
	 *
	 * @param str  文字列
	 * @param size 文字数制限（バイト数）
	 * @return	   新たに生成された文字列配列
	 * @since 1.00
	 */
	public static String[] sepalateString(String str, int size) {

		if (str == null || size == 0) {
			return new String[0];
		}

		/* size以下にまるめた文字列格納ArrayList */
		ArrayList array = new ArrayList();

		/* 検査対象文字列 */
		byte[] strBytes = str.getBytes();

		int startPos = 0;
		int endPos = 0;
		while (endPos < strBytes.length) {
			/* 8ビット目が立っていたら日本語なので2バイト分増やす */
			int n = ((strBytes[endPos] & 0x80) == 0x80) ? 2 : 1;

			/* sizeバイトを越えてしまったら String生成 */
			int length = endPos - startPos;
			if (length + n > size) {
				array.add(new String(strBytes, startPos, length));
				startPos = endPos;
			} else {
				endPos += n;
			}
		}
		if (endPos > startPos) {
			array.add(new String(strBytes, startPos, endPos - startPos));
		}

		return (String[])array.toArray(new String[array.size()]);
	}


	// ----------------------------------------------------------------------
	// ## : StringUtil byte to string
	// ----------------------------------------------------------------------
	/**
	 * バイト列を16進数文字列に変換します。
	 * @param b バイト配列
	 * @return 16進数文字列
	 */
	public static String byteToStringHex(byte[] b) {
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int tmpb = (b[i] < 0) ? b[i] + 0x100 : b[i];
			strbuf.append(Integer.toString(tmpb / 16, 16).toUpperCase());
			strbuf.append(Integer.toString(tmpb % 16, 16).toUpperCase());
		}
		return strbuf.toString();
	}
	/**
	 * 16進数文字列をバイト列に変換します。
	 * @param src 16進数文字列
	 * @return バイト配列
	 */
	public static byte[] stringToByteHex(String src) {
		if (src.length() % 2 != 0) {
			throw new IllegalArgumentException("引数の文字列長は偶数でなければいけません。");
		}
		byte[] buf = new byte[src.length() / 2];
		for (int i = 0; i < src.length(); i += 2) {
			int b;
			b = Integer.parseInt(src.substring(i,
					((i + 2) > src.length()) ? src.length() : i + 2), 16);
			if (b > 127) {
				b -= 0x100;
			}
			buf[i / 2] = (byte) b;
		}
		return buf;
	}

	public static String byteToString(byte[] b,String sep) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int buf = b[i];
			if (buf < 0) {
				buf = b[i] + 256;
			}
			str.append(buf + sep);
		}
		str.deleteCharAt(str.length() - 1);
		return str.toString();
	}
	public static byte[] stringToByte(String str,String sep) {
		StringTokenizer s = new StringTokenizer(str, sep);
		byte[] b = new byte[s.countTokens()];
		for (int i = 0; i < b.length; i++) {
			String ss = s.nextToken();
			b[i] = new Short(ss).byteValue();
		}
		return b;
	}

	// ----------------------------------------------------------------------
	// ## : StringUtil format
	// ----------------------------------------------------------------------
	public static String format(String pattern,Object[] values) {
		for (int i = 0; i < values.length; i++) {
			pattern = format(pattern, values[i].toString());
		}
		return pattern;
	}
	public static String format(String pattern,String value) {
		return  pattern.replaceFirst("%s", value);
	}

	public static String trim(String string) {
		return string.replaceAll("^[\\s　]*", "").replaceAll("[\\s　]*$", "");
	}
}
