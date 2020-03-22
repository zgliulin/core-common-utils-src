package javax.core.common.utils;

public class LogFormat {
	
	private LogFormat(){}

    /**
	 * @param modName	模块名
	 * @param opName	操作命名
	 * @param oper		操作者
	 * @param info		具体内容
	 */
	public static void log(String modName,String opName, String oper, String info) {
		log(modName,format(opName,oper,info));
	}
	
	/**
	 * @param modName	模块名
	 * @param opName	操作命名
	 * @param oper		操作者
	 * @param ip		操作时的IP地址
	 * @param info		具体内容
	 */
	public static void log(String modName,String opName, String oper, String ip, String info) {
		log(modName, format(opName, oper+" @"+ip, info));
	}
	
	private static void log(String modName,String info){
		LogUtil.info("modName:"+modName + " | " +info);
	}
	
	/**
	 * @param opName	操作命名
	 * @param oper		操作者
	 * @param info		具体内容
	 */
	private static String format(String opName, String oper, String info) {
		StringBuffer str = new StringBuffer(512);
		str.append(opName).append(" [")
		   .append(oper).append("] ").append(info.replaceAll("\\r|\\n",""));
		return str.toString();
		
	}
}
