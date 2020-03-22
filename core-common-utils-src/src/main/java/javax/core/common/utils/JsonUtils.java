package javax.core.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
/**
 * JSON工具类
 * @author Tanyongde
 *
 */
public class JsonUtils {
	
	private JsonUtils(){}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String ObjectToJson(Object obj){
    	return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }
    
	/**
	 * 字符还原成JSON
	 * @param str
	 * @return
	 */
    public static Object JsonToObject(String str){
    	return JSON.parse(str);
    }
}
