package app.util;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Date;

import app.common.AppLogger;

/**
 * ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽﾌ型・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌ・ｿｽ・ｽ[・ｽe・ｽB・ｽ・ｽ・ｽe・ｽB・ｽN・ｽ・ｽ・ｽX・ｽB
 * <p>
 *
 * @author ・ｽc・ｽ・ｽ
 *         </p>
 */
public class ConvertUtil {

    /**
     * ・ｽﾏ奇ｿｽ・ｽﾎ象のオ・ｽu・ｽW・ｽF・ｽN・ｽg object ・ｽ・ｽ convClass ・ｽﾌ型・ｽﾖ変奇ｿｽ・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     *
     * @param object
     *            ・ｽﾏ奇ｿｽ・ｽﾎ象のオ・ｽu・ｽW・ｽF・ｽN・ｽg
     * @param convClass
     *            ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ・ｽ^
     * @return ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ黷ｽ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg
     */
    public static Object convObject(Object object, Class convClass) {
        return convObject(object, convClass.getName());
    }

    /**
     * ・ｽﾏ奇ｿｽ・ｽﾎ象のオ・ｽu・ｽW・ｽF・ｽN・ｽg object ・ｽ・ｽ convClassName ・ｽﾌ型・ｽﾖ変奇ｿｽ・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     *
     * @param object
     *            ・ｽﾏ奇ｿｽ・ｽﾎ象のオ・ｽu・ｽW・ｽF・ｽN・ｽg
     * @param convClassName
     *            ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ・ｽ^・ｽﾌク・ｽ・ｽ・ｽX・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
     * @return ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ黷ｽ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg
     */
    public static Object convObject(Object object, String convClassName) {
        if (object == null) {
            // ・ｽv・ｽ・ｽ・ｽ~・ｽe・ｽB・ｽu・ｽﾈ型・ｽﾖの変奇ｿｽ・ｽ・ｽnull・ｽﾅ返ゑｿｽ・ｽﾆエ・ｽ・ｽ・ｽ[・ｽﾉなゑｿｽB
            // 0・ｽﾌ・ｿｽ・ｽb・ｽp・ｽ[・ｽﾉゑｿｽ・ｽ・ｽB
            if (convClassName.equals("int")) {
                return new Integer(0);
            } else if (convClassName.equals("long")) {
                return new Long(0);
            } else {
                return null;
            }
        }
        if (object.getClass().getName().equals(convClassName)) {
            return object;
        }

        //----------------------------------------・ｽw object instanceof String ・ｽx
        if (object instanceof String) {
            if (convClassName.equals("java.lang.String")) {
                return object;
            } else if (convClassName.equals("java.lang.Long")
                    || convClassName.equals("long")) {
                String str = (String) object;
                if (StringUtil.isExist(str)) {
                    // ・ｽ・ｽxBigDecimal・ｽﾉ変奇ｿｽ・ｽ・ｽ・ｽﾈゑｿｽ・ｽﾆ具合・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
                    // 1000.00000
                    BigDecimal big = new BigDecimal(str);
                    return new Long(big.longValue());
                } else {
                    // str ・ｽ・ｽ・ｽk・ｽ・ｽ・ｽe・ｽ・ｽ・ｽ・ｽ・ｽﾌ場合・ｽﾍ擾ｿｽ・ｽ・ｽl・ｽ・ｽ"0"・ｽ・ｽ
                    return new Long(0);
                }
            } else if (convClassName.equals("java.sql.Date")) {
                return DateUtil.toSqlDate((String) object);
            } else if (convClassName.equals("java.sql.Timestamp")) {
                Date date = DateUtil.toSqlDate((String) object);
                return new Timestamp(date.getTime());
            } else if (convClassName.equals("java.lang.Integer")
                    || convClassName.equals("int")) {
                // str ・ｽ・ｽ・ｽk・ｽ・ｽ・ｽe・ｽ・ｽ・ｽ・ｽ・ｽﾌ場合・ｽﾍ擾ｿｽ・ｽ・ｽl・ｽ・ｽ"0"・ｽ・ｽ
                String str = (String) object;
                if (StringUtil.isExist(str)) {
                    BigDecimal big = new BigDecimal(str);
                    return new Integer(big.intValue());
                } else {
                    return new Integer(0);
                }
            } else if (convClassName.equals("boolean")) {
                return Boolean.valueOf(object.toString());
            } else if (convClassName.equals("java.math.BigDecimal")) {
                String temp = ((String) object).trim();
                //temp.length() == 0・ｽﾌ場合0・ｽﾅはなゑｿｽnull・ｽﾉゑｿｽ・ｽ・ｽﾌゑｿｽ・ｽ・ｽ・ｽ・ｽB
                if (temp.length() == 0) {
                    return null;
                } else {
                    return new BigDecimal(temp);
                }
            }
            throwNoSupprt(object, convClassName);
        }

        //---------------------------------・ｽw object instanceof java.sql.Date ・ｽx
        else if (object instanceof java.sql.Date) {

            if (convClassName.equals("java.lang.String")) {
                return DateUtil.toStringDate((java.sql.Date) object,
                        "yyyy/MM/dd");
            } else if (convClassName.equals("java.sql.Date")) {
                return object;
            } else if (convClassName.equals("java.sql.Timestamp")) {
                return new Timestamp(((Date) object).getTime());
            }
            throwNoSupprt(object, convClassName);
        }

        //-------------------------------------・ｽw object instanceof Timestamp ・ｽx
        else if (object instanceof Timestamp) {
            long time = ((Timestamp) object).getTime();
            if (convClassName.equals("java.lang.String")) {
                return DateUtil.toStringDate(time, "yyyy/MM/dd HH:mm:ss");
            } else if (convClassName.equals("java.sql.Date")) {
                return new java.sql.Date(time);
            } else if (convClassName.equals("java.sql.Timestamp")) {
                return object;
            }
            throwNoSupprt(object, convClassName);
        }

        //----------------------------------------・ｽw object instanceof Integer ・ｽx
        else if (object instanceof Integer) {
            if (convClassName.equals("java.lang.Integer")
                    || convClassName.equals("int")) {
                return object;
            } else if (convClassName.equals("java.lang.String")) {
                return object.toString();
            } else if (convClassName.equals("java.lang.Long")
                    || convClassName.equals("long")) {
                return new Long(((Integer) object).longValue());
            } else if (convClassName.equals("java.math.BigDecimal")) {
                return new BigDecimal(((Integer) object).intValue());
            }
            throwNoSupprt(object, convClassName);
        }

        //------------------------------------------・ｽw object instanceof Long ・ｽx
        else if (object instanceof Long) {
            if (convClassName.equals("java.lang.Long")
                    || convClassName.equals("long")) {
                return object;
            } else if (convClassName.equals("java.lang.String")) {
                return object.toString();
            } else if (convClassName.equals("java.lang.Integer")
                    || convClassName.equals("int")) {
                return new Integer(((Long) object).intValue());
            } else if (convClassName.equals("java.math.BigDecimal")) {
                return new BigDecimal(((Long) object).longValue());
            }
            throwNoSupprt(object, convClassName);
        }

        //----------------------------------------・ｽw object instanceof Double ・ｽx
        else if (object instanceof Double) {
            if (convClassName.equals("java.lang.String")) {
                // COLUMN NUMBER(8,0)
                // windows oracle > BigDecimal
                // UNIX oracle > Double
                BigDecimal big = new BigDecimal(((Double) object).doubleValue());
                int scale = big.scale();
                if (scale == 0) {
                    return big.toString();
                } else {
                    // ・ｽﾛめゑｿｽ・ｽK・ｽv・ｽﾈ場合・ｽﾍサ・ｽ|・ｽ[・ｽg・ｽ・ｽ・ｽﾈゑｿｽ・ｽB
                    throwNoSupprt(object, convClassName);
                }
            }
            if (convClassName.equals("java.lang.Integer")
                    || convClassName.equals("int")) {
                return new Integer(((Double) object).intValue());
            } else if (convClassName.equals("java.lang.Long")
                    || convClassName.equals("long")) {
                return new Long(((Double) object).longValue());
            } else if (convClassName.equals("java.math.BigDecimal")) {
                return new BigDecimal(((Double) object).doubleValue());
            }
            throwNoSupprt(object, convClassName);
        }

        //------------------------------------・ｽw object instanceof BigDecimal ・ｽx
        else if (object instanceof BigDecimal) {
            if (convClassName.equals("java.lang.String")) {
                return object.toString();
            } else if (convClassName.equals("java.lang.Long")
                    || convClassName.equals("long")) {
                return new Long(((BigDecimal) object).longValue());
            } else if (convClassName.equals("java.lang.Integer")
                    || convClassName.equals("int")) {
                return new Integer(((BigDecimal) object).intValue());
            }
            throwNoSupprt(object, convClassName);
        }

        //----------------------------------------・ｽw object instanceof byte[] ・ｽx
        else if (object instanceof byte[]) {
            if (convClassName.equals("java.sql.Blob")) {
                return object;
            }
            throwNoSupprt(object, convClassName);
        }

        //------------------------------------------・ｽw object instanceof Blob ・ｽx

        //------------------------------------------------・ｽw object ・ｽ・ｽ Boolean ・ｽx
        else if (object instanceof Boolean) {
            if (convClassName.equals("boolean")) {
                return object;
            }
            throwNoSupprt(object, convClassName);
        }

        //----------------------------------------------・ｽw object ・ｽ・ｽ boolean[] ・ｽx
        else if (object instanceof boolean[]) {
            if (convClassName.equals("java.lang.String")) {
                boolean[] bs = (boolean[]) object;
                StringBuffer buff = new StringBuffer("[");
                for (int i = 0; i < bs.length; i++) {
                    buff.append(bs[i] + ",");
                }
                buff.deleteCharAt(buff.length() - 1);
                buff.append("]");
                return buff.toString();
            }
            throwNoSupprt(object, convClassName);
        }
        throwNoSupprt(object, convClassName);
        return null;

    }

    /**
     * ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽT・ｽ|・ｽ[・ｽg・ｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽﾈゑｿｽ・ｽ鼾・ｿｽﾉス・ｽ・ｽ・ｽ[・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     *
     * @param object
     *            ・ｽﾏ奇ｿｽ・ｽﾎ象のオ・ｽu・ｽW・ｽF・ｽN・ｽg
     * @param convClassName
     *            ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ・ｽ^
     */
    private static void throwNoSupprt(Object object, String convClassName) {
        String className = (object != null) ? object.getClass().getName()
                : "null";
        String errorMess = "\n・ｽ・ｽ・ｽ・ｽObject・ｽﾌ型・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾍまゑｿｽ・ｽT・ｽ|・ｽ[・ｽg・ｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽﾜゑｿｽ・ｽ・ｽB\n"
                + " [ Object ] = " + object + ",[ Object・ｽﾌ型 ] = " + className
                + ",[ convertClass ] = " + convClassName + "";
        throw new UnsupportedOperationException(errorMess);
    }

    /**
     *
     * @param dataType
     * @param typeName
     * @return
     */
    public static String getTypeMatchClass(int dataType, String typeName) {

        //・ｽ・ｽ・ｽ・ｽ・ｽ^
        if (dataType == Types.VARCHAR || dataType == Types.CHAR
                || dataType == Types.LONGVARCHAR) {
            return "java.lang.String";
        }
        //・ｽ・ｽ・ｽl・ｽ^・ｽi・ｽ・ｽ・ｽ・ｽ・ｽj
        else if (dataType == Types.INTEGER || dataType == Types.SMALLINT
                || dataType == Types.BIGINT || dataType == Types.TINYINT) {
            return "java.lang.Integer";
        }
        //・ｽ・ｽ・ｽl・ｽ^・ｽi・ｽ・ｽ・ｽ・ｽ・ｽj
        else if (dataType == Types.NUMERIC) {
            return "java.lang.Long";
        }
        //・ｽ・ｽ・ｽl・ｽ^・ｽi・ｽ・ｽ・ｽ・ｽ・ｽj
        else if (dataType == Types.DOUBLE || dataType == Types.FLOAT) {
            return "java.lang.Double";
        }
        //DECIMAL
        else if (dataType == Types.DECIMAL) {
            return "java.math.BigDecimal";
        }
        //・ｽ・ｽt・ｽ^
        else if (dataType == Types.DATE || dataType == Types.TIMESTAMP) {
            return "java.sql.Date";
        }
        //DB2・ｽ・ｽBLOB・ｽ^・ｽﾎ会ｿｽ
        else if (dataType == Types.BLOB || dataType == Types.LONGVARBINARY ||dataType == Types.SMALLINT){
            if (typeName.equals("BLOB")) {
                return "java.sql.Blob";
            }
        }
        //DB2・ｽ・ｽCLOB・ｽ^・ｽﾎ会ｿｽ
        else if (dataType == Types.LONGVARCHAR) {
            if (typeName.equals("CLOB")) {
                return "java.sql.Clob";
            }
        }
        //SQL ・ｽ^・ｽ・ｽ・ｽf・ｽ[・ｽ^・ｽx・ｽ[・ｽX・ｽﾅ有・ｽﾌゑｿｽ・ｽﾌの場合
        else if (dataType == Types.OTHER) {
            if (typeName.equals("BLOB")) {
                return "java.sql.Blob";
            } else if (typeName.equals("UNDEFINED")) {
                AppLogger.error(ConvertUtil.class,"・ｽe・ｽ[・ｽu・ｽ・ｽ・ｽﾌカ・ｽ・ｽ・ｽ・ｽ・ｽﾌ型・ｽ・ｽ・ｽ・ｽ・ｽﾊでゑｿｽ・ｽﾜゑｿｽ・ｽ・ｽB");
                return null;
            }
        }
        return null;
    }

    /**
     * ・ｽ・ｽ・ｽNumber・ｽ・ｽBigDecimal・ｽﾉ変奇ｿｽ・ｽ・ｽ・ｽﾜゑｿｽ・ｽB
     *
     * @param num
     *            ・ｽﾏ奇ｿｽ・ｽﾎ象ゑｿｽNumber
     * @return num・ｽ・ｽBigDecimal・ｽﾉ変奇ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌ。num・ｽ・ｽnull・ｽﾌ場合・ｽ・ｽ"0"・ｽ・ｽBigDecimal・ｽ・ｽﾔゑｿｽ・ｽﾜゑｿｽ・ｽB
     */
    public static BigDecimal numberToDecimal(Object num) {
        if (num == null) {
            return new BigDecimal("0");
        }

        if (num instanceof Double || num instanceof Float) {
            return new BigDecimal(new DecimalFormat("0.00000")
                    .format(((Number) num).doubleValue()));
        } else {
            return new BigDecimal(num.toString());
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        //		Debug.outputSystemProperty();

        String s1 = (String) convObject(new Double(1.89E7), String.class);
        System.out.println(s1);

        System.out.println(new Double(1.89E7).doubleValue());

        System.out.println(new BigDecimal(new Double("1.89E7").doubleValue()));

        System.out.println(new BigDecimal("1.89E7"));

        //		Long lo = (Long)convObject(new Double(5.555555555E3),Long.class);
        //		System.out.println(lo);
        //
        //		BigDecimal big = (BigDecimal)convObject(new
        // Double(5.555555555E3),BigDecimal.class);
        //		System.out.println(big);
        //
        //		big = (BigDecimal)convObject(new
        // Double("3.3332111E7"),BigDecimal.class);
        //		System.out.println(big);
        //
        //
        //		String s = (String)convObject(new Double(33332111),String.class);
        //		System.out.println(s);
        //		System.out.println(new Double(33332111));
        //
        //		s = (String)convObject(new Double(5.555555555E3),String.class);
        //		System.out.println(s);
        //

    }
}

/*
 * //---・ｽw BigDecimal ・ｽ・ｽ・ｽ・ｽ Double
 * ・ｽx-------------------------------------------------------------------
 *
 * BigDecimal big = null; Double d = null; int scale = 0;
 *
 * big = new BigDecimal(123); System.err.println("1 : BigDecimal / ・ｽ・ｽ・ｽ・ｽ(123) /
 * big.toString() ・ｽ・ｽ"+big.toString());
 *
 * big = new BigDecimal(123.3); System.err.println("2 : BigDecimal / ・ｽ・ｽ・ｽ・ｽ(123.3) /
 * big.toString() ・ｽ・ｽ"+big.toString());
 *
 * big = new BigDecimal("123.30"); System.err.println("3 : BigDecimal /
 * ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ(123.30) / big.toString() ・ｽ・ｽ"+big.toString());
 *
 * d = new Double(1234567); System.err.println("4 : Double(1234567) /
 * d.toString() ・ｽ・ｽ"+d.toString());
 *
 * d = new Double(12345678); System.err.println("5 : Double(12345678) /
 * d.toString() ・ｽ・ｽ"+d.toString());
 *
 * d = new Double(12345678); big = new BigDecimal(d.doubleValue()); scale =
 * big.scale(); System.err.println("6 : Double(12345678)・ｽ・ｽBigDecima /
 * big.toString() ・ｽ・ｽ"+big.toString()); System.err.println("7 : 12345678 scale
 * ・ｽ・ｽ"+scale);
 *
 * d = new Double(1234567.8); big = new BigDecimal(d.doubleValue()); scale =
 * big.scale(); System.err.println("8 : Double(1234567.8)・ｽ・ｽBigDecima /
 * big.toString() ・ｽ・ｽ"+big.toString()); System.err.println("9 : 1234567.8 scale
 * ・ｽ・ｽ"+scale);
 *
 * d = new Double(1234567.8); big = new BigDecimal(d.doubleValue()); scale =
 * big.scale(); System.err.println("10 : 1234567.8 scale ・ｽ・ｽ"+scale); BigDecimal
 * nbig = big.setScale(1,BigDecimal.ROUND_DOWN); System.err.println("11 :
 * Double(1234567.8)・ｽ・ｽBigDecima.setScale(1,BigDecimal.ROUND_DOWN /
 * nbig.toString() ・ｽ・ｽ"+nbig.toString());
 *
 * d = new Double(0.51); big = new BigDecimal(d.doubleValue()); scale =
 * big.scale(); System.err.println("12 : 0.51 scale ・ｽ・ｽ"+scale);
 * System.err.println("13 : Double(0.51) / d.toString() ・ｽ・ｽ"+d.toString());
 *
 * 1 : BigDecimal / ・ｽ・ｽ・ｽ・ｽ(123) / big.toString() ・ｽ・ｽ123 2 : BigDecimal / ・ｽ・ｽ・ｽ・ｽ(123.3) /
 * big.toString() ・ｽ・ｽ123.2999999999999971578290569595992565155029296875 3 :
 * BigDecimal / ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ(123.30) / big.toString() ・ｽ・ｽ123.30 4 : Double(1234567) /
 * d.toString() ・ｽ・ｽ1234567.0 5 : Double(12345678) / d.toString() ・ｽ・ｽ1.2345678E7 6 :
 * Double(12345678)・ｽ・ｽBigDecima / big.toString() ・ｽ・ｽ12345678 7 : 12345678 scale ・ｽ・ｽ0 8 :
 * Double(1234567.8)・ｽ・ｽBigDecima / big.toString()
 * ・ｽ・ｽ1234567.80000000004656612873077392578125 9 : 1234567.8 scale ・ｽ・ｽ32 10 :
 * 1234567.8 scale ・ｽ・ｽ32 11 :
 * Double(1234567.8)・ｽ・ｽBigDecima.setScale(1,BigDecimal.ROUND_DOWN /
 * nbig.toString() ・ｽ・ｽ1234567.8 12 : 0.51 scale ・ｽ・ｽ52 13 : Double(0.51) /
 * d.toString() ・ｽ・ｽ0.51
 *
 *
 * //---・ｽw Types
 * ・ｽx------------------------------------------------------------------------------------
 *
 * BIT = -7 TINYINT = -6 ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽint・ｽ・ｽ・ｽ・ｽ SMALLINT = 5 INTEGER = 4 BIGINT = -5 FLOAT = 6
 * REAL = 7 DOUBLE = 8 NUMERIC = 2 DECIMAL = 3 CHAR = 1 VARCHAR = 12 LONGVARCHAR =
 * -1 DATE = 91 TIME = 92 TIMESTAMP = 93 BINARY = -2 VARBINARY = -3
 * LONGVARBINARY = -4 NULL = 0 OTHER = 1111 JAVA_OBJECT = 2000 DISTINCT = 2001
 * STRUCT = 2002 ARRAY = 2003 BLOB = 2004 CLOB = 2005 REF = 2006 DATALINK = 70
 * BOOLEAN = 16
 *
 */

