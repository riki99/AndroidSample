package app.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * java.util.Collections・ｽﾌ・ｿｽ・ｽ[・ｽe・ｽB・ｽ・ｽ・ｽe・ｽB・ｽN・ｽ・ｽ・ｽX・ｽB
 * <p>
 *
 * @author ・ｽc・ｽ・ｽ
 *         </p>
 */
public class CollectionsUtil {

	/**
	 *
	 * @param list
	 * @param clazz
	 * @return
	 */
	public static List extract(List list, Class clazz){
        ArrayList newlist = new ArrayList();
    	Iterator itr = list.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			if (obj.getClass().equals(clazz)) {
				newlist.add(obj);
			}
		}
        return newlist;
    }

    /**
     *
     * @param froms
     * @return
     */
    public static List convertList(Object[] froms){
        ArrayList list = new ArrayList();
        for (int i = 0; i < froms.length; i++) {
            list.add(froms[i]);
        }
        return list;
    }

    /**
     * ・ｽucollection・ｽv・ｽ・ｽ・ｽL・ｽ・ｽﾈ値・ｽ・ｽ・ｽﾇゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾘゑｿｽ・ｽﾜゑｿｽ・ｽB null・ｽﾅなゑｿｽ・ｽT・ｽC・ｽY0・ｽﾅなゑｿｽ・ｽ鼾・ｿｽL・ｽ・ｽﾆゑｿｽ・ｽﾜゑｿｽ・ｽB
     *
     * @param collection
     * @return
     */
    public static boolean isExist(Collection collection) {
        if (collection == null) {
            return false;
        }
        if (collection != null && collection.size() != 0) {
            return true;
        }
        return false;
    }

    /**
     * ・ｽuMap・ｽv・ｽ・ｽ・ｽL・ｽ・ｽﾈ値・ｽ・ｽ・ｽﾇゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾘゑｿｽ・ｽﾜゑｿｽ・ｽB null・ｽﾅなゑｿｽ・ｽT・ｽC・ｽY0・ｽﾅなゑｿｽ・ｽ鼾・ｿｽL・ｽ・ｽﾆゑｿｽ・ｽﾜゑｿｽ・ｽB
     *
     * @param map
     * @return
     */
    public static boolean isExist(Map map) {
        if (map == null) {
            return false;
        }
        if (map != null && map.size() != 0) {
            return true;
        }
        return false;
    }

    /**
     * @param collection
     * @param object
     * @return
     */
    public static boolean contains(Collection collection, Object object) {
        if (collection == null)
            return false;
        if (collection.contains(object)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param map
     * @param object
     * @return
     */
    public static boolean containsKey(Map map, Object object) {
        if (map == null)
            return false;
        if (map.containsKey(object)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param map
     * @param object
     * @return
     */
    public static boolean containsValue(Map map, Object object) {
        if (map == null)
            return false;
        if (map.containsValue(object)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ・ｽutempStr・ｽv・ｽ・ｽ・ｽ・ｽ;・ｽﾅ具ｿｽﾘゑｿｽ黷ｽ・ｽe・ｽv・ｽf・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽucollection・ｽv・ｽﾉ格・ｽ[・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     * @param tempStr
     * @param collection
     */
    public static void addToken(Collection collection, String tempStr) {
        if (!StringUtil.isExist(tempStr) || collection == null)
            return;
        StringTokenizer token = new StringTokenizer(tempStr, ";,");
        while (token.hasMoreTokens()) {
            collection.add(token.nextToken());
        }
    }

    /**
     * ・ｽutempStr・ｽv・ｽ・ｽ・ｽ・ｽ;・ｽﾅ具ｿｽﾘゑｿｽ黷ｽ・ｽe・ｽv・ｽf・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽucollection・ｽv・ｽﾉ格・ｽ[・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     * @param delim
     * @param tempStr
     * @param collection
     */
    public static void addToken(Collection collection, String tempStr,String delim) {
        if (!StringUtil.isExist(tempStr) || collection == null)
            return;
        StringTokenizer token = new StringTokenizer(tempStr, delim);
        while (token.hasMoreTokens()) {
            collection.add(token.nextToken());
        }
    }

    /**
     * <pre>
     *  ・ｽutempStr・ｽv・ｽ・ｽ・ｽﾌ「;,・ｽv・ｽﾅ具ｿｽﾘゑｿｽ黷ｽ・ｽu・ｽL・ｽ[=・ｽv・ｽf・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽumap・ｽv・ｽﾉ格・ｽ[・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     *  ・ｽ・ｽF
     *     putToken(map,&quot;key1=value1;key2=value2&quot;);
     *                       ・ｽ・ｽ
     *     map.put(&quot;key1&quot;,&quot;value1&quot;);
     *     map.put(&quot;key2&quot;,&quot;value2&quot;);
     * </pre>
     *  @param map
     *  @param tempStr
     */
    public static void putToken(Map map, String tempStr) {
        putToken(map, tempStr, ";,");
    }

    /**
     * <pre>
     *
     *  ・ｽutempStr・ｽv・ｽ・ｽ・ｽﾌ「delim・ｽv・ｽﾅ具ｿｽﾘゑｿｽ黷ｽ・ｽu・ｽL・ｽ[=・ｽv・ｽf・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽumap・ｽv・ｽﾉ格・ｽ[・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     *  ・ｽ・ｽF
     *     putToken(map,&quot;key1=value1;key2=value2&quot;);
     *                       ・ｽ・ｽ
     *     map.put(&quot;key1&quot;,&quot;value1&quot;);
     *     map.put(&quot;key2&quot;,&quot;value2&quot;);
     * </pre>
     *  @param tempStr
     *  @param map
     *  @param delim ・ｽ・ｽﾘり文・ｽ・ｽ
     */
    public static void putToken(Map map, String tempStr, String delim) {
        if (!StringUtil.isExist(tempStr) || map == null)
            return;
        StringTokenizer token1 = new StringTokenizer(tempStr, delim);
        while (token1.hasMoreTokens()) {
            String valueStr = token1.nextToken();
            if (valueStr.indexOf("=") != -1) {	// ・ｽ・ｽ・ｽﾌ費ｿｽ・ｽ閧ｪ・ｽﾈゑｿｽ・ｽ・ｽnull pointo
                String[] keySet = StringUtil.parseKeySet(valueStr);
                map.put(keySet[0], keySet[1]);
			}
        }
    }

    /**
     * ・ｽ・ｽﾌ・ｿｽ・ｽX・ｽg・ｽ・ｽ・ｽﾌ要・ｽf・ｽ・ｽ・ｽ・ｽﾗゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾔゑｿｽ・ｽﾜゑｿｽ・ｽB
     * 1・ｽv・ｽf・ｽﾃつ会ｿｽs・ｽ・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     * @param list	・ｽw・ｽ・ｽﾌ・ｿｽ・ｽX・ｽg
     * @return		・ｽ・ｽ・ｽX・ｽg・ｽ・ｽ・ｽﾌ要・ｽf・ｽﾌ包ｿｽ・ｽ・ｽ・ｽ・ｽ
     */
    public static String entryList(List list) {
        return entryList(list,"\n");
    }

    /**
     *
     * ・ｽ・ｽﾌ・ｿｽ・ｽX・ｽg・ｽ・ｽ・ｽﾌ要・ｽf・ｽ・ｽ・ｽ・ｽﾗゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾔゑｿｽ・ｽﾜゑｿｽ・ｽB
     * 1・ｽﾂの要・ｽf・ｽﾍ、delim・ｽﾅ仕・ｽﾘゑｿｽ・ｽﾜゑｿｽ・ｽB
     * @param list		・ｽw・ｽ・ｽﾌ・ｿｽ・ｽX・ｽg
     * @param delim	・ｽ・ｽ・ｽX・ｽg・ｽ・ｽ・ｽﾌ要・ｽf・ｽﾌ仕・ｽﾘり文・ｽ・ｽ・ｽ・ｽ
     * @return			・ｽ・ｽ・ｽX・ｽg・ｽ・ｽ・ｽﾌ要・ｽf・ｽﾌ包ｿｽ・ｽ・ｽ・ｽ・ｽ
     */
    public static String entryList(List list,String delim) {
        if (list != null && list.size() == 0) {
            return null;
        }
		StringBuffer buffer = new StringBuffer();
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			buffer.append(obj);
			if (itr.hasNext()) {
				buffer.append(delim);
			}
		}
		return buffer.toString();
    }

    /**
     * @param map
     * @return
     */
    public static String entrySetDump(Map map) {
        if (map == null) return "";
        if (map.size() == 0)return "";

        int length = 0;
        StringBuffer buffer = new StringBuffer();
        Iterator itr = map.keySet().iterator();
        while (itr.hasNext()) {
            Object element = (Object) itr.next();
            if (element == null) {
                continue;
            }
            String key = (String) ConvertUtil.convObject(element, String.class);
            if (length < key.length()) {
                length = key.length();
            }
        }

        itr = map.keySet().iterator();
        while (itr.hasNext()) {
            Object key = itr.next();
            Object value = map.get(key);
            String temp = StringUtil.fillString(" [" + key + "] ", "R",
                    length + 5, " ");
            buffer.append(temp + "= '" + value + "'\n");
        }
        if (buffer.length() != 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * @param value
     * @return
     */
    public static String stratum(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String[]) {
            return StringUtil.convString((String[]) value);
        } else {
            return value.toString();
        }
    }

    /**
     *
     * @param list
     * @param from
     * @param to
     * @return
     */
    public static List copyList(List list, int from, int to) {
        List newlist = null;
        try {
            newlist = (List) list.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (int i = from; i <= to; i++) {
            newlist.add(list.get(i));
        }
        return newlist;
    }

    /**
     *
     * @param list
     * @return
     */
    public static List copyList(List list) {
        List newlist = null;
        try {
            newlist = (List) list.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Iterator itr = list.iterator();
        while (itr.hasNext()) {
            newlist.add(itr.next());
        }
        return newlist;
    }

    public static String getValue(List list, int index,String defaultVal) {
    	if (!isExist(list)) {
			return defaultVal;
		}
    	String ret = "";
    	if (list.size() > index) {
    		ret =  (String)list.get(index);
		}
    	if (!StringUtil.isExist(ret)) {
            return defaultVal;
		}
        return ret;
    }

}