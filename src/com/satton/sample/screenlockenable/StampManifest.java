
package com.satton.sample.screenlockenable;

import java.util.HashMap;
import java.util.Map;

/**
 * Stamp のカテゴリ毎に１ファイル生成さる。
 * 
 * @author tanabe.satoru 2012/11/21
 * @since 2012/11/21
 * @version 2012/11/21
 */
public class StampManifest {

    public static final String NAME = "stamp.manifest";

    public String category_id = "";
    public String category_title = "";
    public HashMap<String, Map<String, String>> stamps;

}
