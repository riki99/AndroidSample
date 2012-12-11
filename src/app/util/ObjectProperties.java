package app.util;

import java.util.HashMap;

public class ObjectProperties {

	static HashMap map = new HashMap();

	/**
	 *
	 * @param obj
	 * @param key
	 * @return
	 */
	public static Object getProperty(Object obj, String key) {
        Object oval = map.get(obj.hashCode() +  key);
        return oval;
    }
	public static String getPropertyString(Object obj, String key) {
		Object oval = getProperty(obj,key);
		if (oval != null) {
			return getProperty(obj,key).toString();
		}
		return "";
    }

    /**
     *
     * @param obj
     * @param key
     * @param value
     */
    public static void setProperty(Object obj, String key,Object value) {
        map.put(obj.hashCode() +  key,value);
    }

    public static void clear() {
        map.clear();
    }
}
