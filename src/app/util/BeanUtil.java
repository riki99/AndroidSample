package app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 *
 */
public class BeanUtil {

	private static final Logger logger = Logger.getLogger(BeanUtil.class
			.getName());

	/**   */
    private static final String GET = "GET";
    /**   */
    private static final String SET = "SET";

	//----------------------------------------------------------『 newInstance 』
    /**
     * 文字列「className」からインスタンスを生成し返します。
     * @param className		完全修飾クラス名
     * @return				完全修飾クラス名の新しいインスタンス
     * @throws Exception
     */
    public static Object newInstance(String className){
        try {
			return Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * 文字列「className」からインスタンスを生成し返します。
     * @param className		完全修飾クラス名
     * @param argObj		コンストラクタの引数
     * @return				完全修飾クラス名の新しいインスタンス
     * @throws Exception
     */
    public static Object newInstance(String className,Object[] argObj)
    														throws Exception{
		Class[] argClass = new Class[argObj.length];
		for (int i = 0; i < argObj.length; i++) {
			argClass[i] = argObj[i].getClass();
		}
		Constructor c = Class.forName(className).getConstructor(argClass);
		return c.newInstance(argObj);
    }

	/**
	 * クラス「clazz」からインスタンスを生成し返します。
	 * @param clazz		クラス
     * @return			clazzの新しいインスタンス
     * @throws Exception
	 */
	public static Object newInstance(Class clazz)  throws Exception{
		return clazz.newInstance();
	}

	/**
	 * クラス「clazz」からインスタンスを生成し返します。
	 * @param clazz		クラス
     * @param argObj	コンストラクタの引数
     * @return			clazzの新しいインスタンス
     * @throws Exception
	 */
	public static Object newInstance(Class clazz,Object[] argObj) throws Exception{
		Class[] argClass = new Class[argObj.length];
		for (int i = 0; i < argObj.length; i++) {
			argClass[i] = argObj[i].getClass();
		}
		Constructor c = clazz.getConstructor(argClass);
		return c.newInstance(argObj);
	}

	/**
	 * 引数で渡されたオブジェクトをコピーします。<br>
	 * @param object コピーしたい対象
	 * @return コピー後のオブジェクト
	 * @exception
	 * 		<li>IOException</li>
	 * 		<li>ClassNotFoundException</li>
	 */
	public static Object copy(Serializable object){
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(object);
			out.close();
			byte[] bytes = bout.toByteArray();
			ObjectInputStream in = new
			ObjectInputStream(new ByteArrayInputStream(bytes));
			Object newObject = in.readObject();
			in.close();
			return newObject;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//---------------------------------------------------------------『 Method 』


    /**
     * オブジェクト「invokeObject」のメソッド「callMethod」を実行します。
     * リターン値がある場合は、Object形として得る事ができます。
     * @param invokeObject		実行対象のオブジェクト
     * @param callMethod		実行対象のメソッド名
     * @param argObject 		引数がある場合はオブジェクトの配列として渡す。
     * 							引数が無い場合はnullを渡します。
     * @return					「callMethod」を実行したリターン値
     */
    public static Object invoke(Object invokeObject,
    							String callMethod){
    	return invoke(invokeObject, callMethod, null);
    }
    /**
     * オブジェクト「invokeObject」のメソッド「callMethod」を実行します。
     * リターン値がある場合は、Object形として得る事ができます。
     * @param invokeObject		実行対象のオブジェクト
     * @param callMethod		実行対象のメソッド名
     * @param argObject 		引数がある場合はオブジェクトの配列として渡す。
     * 							引数が無い場合はnullを渡します。
     * @return					「callMethod」を実行したリターン値
     */
    public static Object invoke(Object invokeObject,
    							String callMethod,
    							Object argObject){
    	return invoke(invokeObject, callMethod, new Object[]{argObject});
    }
    /**
     * オブジェクト「invokeObject」のメソッド「callMethod」を実行します。
     * リターン値がある場合は、Object形として得る事ができます。
     * @param invokeObject		実行対象のオブジェクト
     * @param callMethod		実行対象のメソッド名
     * @param argObjects 		引数がある場合はオブジェクトの配列として渡す。
     * 							引数が無い場合はnullを渡します。
     * @return					「callMethod」を実行したリターン値
     */
    public static Object invoke(Object invokeObject,
    							String callMethod,
    							Object[] argObjects){
		try {
			Method method = findMethod(invokeObject.getClass(),callMethod,argObjects);
	        return method.invoke(invokeObject,argObjects);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * オブジェクト「invokeObject」のメソッド「callMethod」を実行します。

     * リターン値がある場合は、Object形として得る事ができます。
     * @param invokeObject		実行対象のオブジェクト
     * @param callMethod		実行対象のメソッド名
     * @param argObjects 		引数がある場合はオブジェクトの配列として渡す。
     * 							引数が無い場合はnullを渡します。
     * @return					「callMethod」を実行したリターン値
     */
    public static Object invokeStatic(String clazz,
			String callMethod,
			Object[] argObjects){
		try {
	    	return invokeStatic(Class.forName(clazz), callMethod, argObjects);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    public static Object invokeStatic(Class clazz,
    							String callMethod,
    							Object[] argObjects){
		try {
	        Method method = findMethod(clazz,callMethod,argObjects);
	        return method.invoke(null ,argObjects);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * オブジェクト「invokeObject」のメソッド「callMethod」を検索します。
     * @param invokeObject		実行対象のオブジェクト
     * @param callMethod		実行対象のオブジェクトのメソッド名
     * @param argObjects 		引数がある場合はオブジェクトの配列として渡す。
     * 							引数が無い場合はnullを渡します。
     * @return  指定された引数の条件に一致するMethod オブジェクト
     * @throws  NoSuchMethodException 一致するメソッドが見つからない場合、
     *          あるいは名前が "<init>" または "<clinit>" の場合
     */
    public static Method findMethod(Class invoke,
    									 String callMethod,
    									 Object[] argObjects)
        								 throws NoSuchMethodException{
        Class[] paramClasses = null;
        Method[] methods = invoke.getMethods();
        top:for ( int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(callMethod)) {
				if (argObjects == null && methods[i].getParameterTypes().length == 0) {
					return methods[i];
				}
				if (argObjects == null) {
					continue;
				}
                paramClasses = methods[i].getParameterTypes();
                if (paramClasses.length == argObjects.length) {
                    //全てのパラメーターリストの型と、引数の型の検証
                    for (int j = 0; j < paramClasses.length; j++) {
                        Class paramClass = paramClasses[j];
                        Object argObj = argObjects[j];
						// 引数の型がプリミティブの場合、引数のオブジェクト
						// がnullでなくプリミティブ
						// もしくわ、NumberのサブクラスならＯＫとする。
                        if (argObj == null) {
							continue;
						}
                        if (paramClass.isPrimitive() &&
							(argObj instanceof Number || argObj instanceof Boolean || argObj.getClass().isPrimitive())){
							continue;
						}
                        if (!paramClass.isInstance(argObj)) {
                            //型に暗黙変換の互換性が無い時点で、次のメソッドへ
                            continue top;
                        }
                    }
                    return methods[i];
                }
            }
        }
        String paramLength = (paramClasses != null)? Integer.toString(paramClasses.length):"";
        String errorMes =
        getShortClassName(invoke.getName())+"にメソッド"+callMethod+"はありません。"+
        "[ paramClasses.length ] = "+ paramLength +
        ",[ argObjects.length ] = "+ argObjects.length+
        "";
        throw new NoSuchMethodException(errorMes);
    }


	//----------------------------------------------------------------『 Field 』
    /**
     * 実行対象のオブジェクト「invokeObject」のフィールド名「fieldName」に値
     * 「value 」を格納します。
     * @param invokeObject	実行対象のオブジェクト
     * @param fieldName		実行対象のオブジェクトのフィールド名
     * @param value			セットする値
     * @throws  IllegalAccessException 指定されたオブジェクトが基本と
     *          なるフィールド (またはそのサブクラスか実装側) を宣言する
     *          クラスまたはインタフェースのインスタンスではない場合、ある
     *          いはラップ解除変換が失敗した場合
     * @throws  NoSuchFieldException 指定された名前のフィールドが見つからない場合
     */
    public static void setField(Object invokeObject,String fieldName,Object value)
    													throws IllegalAccessException,
                                                                NoSuchFieldException{
        Field field = searchField(invokeObject,fieldName);
        String className = field.getType().getName();
        Object convObj = null;
        if (field.getType().isInstance(value)) {
            convObj = value;
        }
        else{
            convObj = ConvertUtil.convObject(value,className);
        }
        field.set(invokeObject,convObj);
    }

    /**
     * 実行対象のオブジェクト「invokeObject」のフィールド名「fieldName」の値を
     * 取得します。
     * @param invokeObject	実行対象のオブジェクト
     * @param fieldName		実行対象のオブジェクトのフィールド名
     * @return				リターン値
     * @throws  IllegalAccessException 指定されたオブジェクトが基本と
     *          なるフィールド (またはそのサブクラスか実装側) を宣言する
     *          クラスまたはインタフェースのインスタンスではない場合、ある
     *          いはラップ解除変換が失敗した場合
     * @throws  NoSuchFieldException 指定された名前のフィールドが見つからない場合
     */
    public static Object getField(Object invokeObject,String fieldName)
    										throws IllegalAccessException,
                                            NoSuchFieldException{
        Field field = searchField(invokeObject,fieldName);
        return field.get(invokeObject);
    }

	   /**
    *
    * @param invokeObject	実行対象のオブジェクト
    * @param fieldName		実行対象のオブジェクトのフィールド名
    * @return  指定された引数の条件に一致する Filed オブジェクト
    * @throws  NoSuchFieldException 指定された名前のフィールドが見つからない場合
    */
   private static Field searchField(Object invokeObject,String fieldName)
   											throws NoSuchFieldException{
       try {
           return invokeObject.getClass().getField(fieldName);
       }catch (NoSuchFieldException e) {
           //このスコープはテーブルカラム名からの取得
           fieldName = checkFieldName(fieldName);
           Field[] fields = invokeObject.getClass().getFields();
           for (int i = 0; i < fields.length; i++) {
               if (fields[i].getName().equalsIgnoreCase(fieldName)) {
                   return fields[i];
               }
           }
           throw new NoSuchFieldException(fieldName);
       }
   }


  	//----------------------------------------------------------------『 その他 』


    /**
     * オブジェクトから完全修飾していないクラス名を取得します。
     * @param object
     * @return
     */
    public static String getShortClassName(Object object){
        if (object == null) {
            return "null";
        }
        String name = object.getClass().getName();
        return getShortClassName(name);
    }

    /**
     * 完全修飾名からクラス名を取得します。
     * @param className
     * @return
     */
    public static String getShortClassName(String className){
        int index = className.lastIndexOf(".");
        return className.substring(index+1);
    }

	/**
	 * メソッド名からフィールド名を変えします。
	 * JavaBeansの慣例に適合している必要があります。
	 * @param methodName
	 * @return
	 */
	public static String getFieldName(String methodName){
		String fieldName = null;
		if (methodName.startsWith("is")) {
			fieldName = methodName.substring(2);
		}
		else {
			fieldName = methodName.substring(3);
		}
		fieldName = StringUtil.convString(fieldName,0,"L");
		return fieldName;
	}

    /**
     * 完全修飾名「className」が存在するクラス名かを検証します。
     * @param className
     * @return
     */
    public static boolean isClassExist(String className){
        try {
            Class.forName(className);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private final static Map beanInfoCache = new HashMap();

  	//---------------------------------------------『 以下プライベートメソッド 』


    /**
     * 引数のfieldNameがカラム名の場合をチェックし、カラム名の場合は
     * コンバートし返します。
     *
     * MAIL_ADDRESS ⇒　MAILADDRESS　
     * 				↓
     * 	mailaddress = mailAddress
     * @param fieldName	フィールド名または、カラム名
     * @return	フィールド名
     */
    private static String checkFieldName(String fieldName){
        int index = fieldName.indexOf("_");
        while (true) {
            if (index==-1) {
                return fieldName;
            }
            StringBuffer convcloumn = new StringBuffer(fieldName);
            convcloumn.deleteCharAt(index);
            fieldName = convcloumn.toString();
            index = fieldName.indexOf("_");
        }
    }



}
