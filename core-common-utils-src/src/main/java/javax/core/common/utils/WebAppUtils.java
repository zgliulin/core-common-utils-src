package javax.core.common.utils;

/**
 * 公共操作类
 * @author Tanyongde
 *
 */
public class WebAppUtils {
	
	private WebAppUtils(){}
	
	//当前类对象
	private static final Class<WebAppUtils> CURR_CLASS = WebAppUtils.class;
	//当前类所在的包名
	private static final String PACKAGE_NAME = CURR_CLASS.getPackage().getName().replaceAll("\\.", "/");
	//当前类所在的全路径
	private static final String BASE_PATH = CURR_CLASS.getResource("").getPath().replaceAll("%20", " ");
	/**
	 * 获取当前项目所在的class路径
	 */
	public static final String WEBAPP_PATH = BASE_PATH.substring(0,BASE_PATH.indexOf("WEB-INF"));
	/**
	 * 获取当前项目的web路径
	 */
	public static final String CLASS_PATH = BASE_PATH.replaceAll("/" + PACKAGE_NAME, "");
}
