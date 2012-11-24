package app.util;

import java.io.File;
import java.io.IOException;




public class XMLPersistence {

	public static String defaultPath = "";
	public static String SUFFIX = ".xml";

	// ----------------------------------------------------------------------
	// ## : XMLPersistence store
	// ----------------------------------------------------------------------
	public static void storeName(Object object, String name) {
		store(object, defaultPath + name + SUFFIX);
	}

	public static void store(Object object) {
		store(object,getPath(object.getClass()));
	}
	public static void store(Object object,String path) {
		store(object, path, null);
	}
	public static void store(Object object,String path, byte[] secretKey) {
		synchronized (object.getClass()) {
			try {
				IOUtil.writeXML(path, object, secretKey);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	// ----------------------------------------------------------------------
	// ## : XMLPersistence load
	// ----------------------------------------------------------------------
	public static Object loadName(Class<?> clazz, String name) {
		return load(clazz, true, defaultPath + name + SUFFIX, null);
	}

	public static Object load(Class<?> clazz) {
		return load(clazz, false);
	}
	public static Object load(Class<?> clazz, boolean create) {
		return load(clazz ,create ,getPath(clazz));
	}
	public static Object load(Class<?> clazz, boolean create, String path) {
		return load(clazz, create, path, null);
	}
	public static Object load(Class<?> clazz, boolean create, String path, byte[] secretKey) {
		synchronized (clazz) {
			try {
				Object obj = IOUtil.readXML(path, secretKey);
				if (!clazz.isInstance(obj)) {
					throw new ClassCastException();
				}
				return obj;
			} catch (Throwable t) {
				if (create) {
					File f = new File(path);
					f.delete();
					try {
						Object obj = clazz.newInstance();
						store(obj, path, secretKey);
						return obj;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				return null;
			}
		}
	}

	// ----------------------------------------------------------------------
	// ## : XMLPersistence remove
	// ----------------------------------------------------------------------
	public static void remove(Class<?> clazz) {
		synchronized (clazz) {
			String path = getPath(clazz);
			new File(path).delete();
		}
	}


	public static String getPath(Class<?> clazz) {
		String path = defaultPath + clazz.getName() + ".xml";
		return path;
	}
	private static String getBakupPath(String path,Class<?> clazz) {
		path = StringUtil.replace(path, "\\config\\", "\\sys\\bakup\\config\\");
//		String path = bakupPath + File.separator + clazz.getName() + ".xml";
		return path;
	}

}
