package javax.core.common.utils;

import org.apache.log4j.Logger;

public class LogUtil {
	
	private final static Logger logger = Logger.getLogger(LogUtil.class);

	
	private LogUtil(){}
	
	// user general interface.
	public static void error(String info, Throwable t) {
		logger.error(info, t);
	}

	public static void error(Throwable t) {
		error("::ERROR::", t);
	}

	/**
	 * 
	 * @param error
	 */
	public static void error(String error) {
		logger.error(error);
	}

	public static void info(String info) {
		logger.info(info);
	}

//	public static void info(String fomart, Object obj1, Object obj2) {
//		logger.info(fomart, obj1, obj2);
//	}

	public static void warn(String msg) {
		logger.warn(msg);
	}
}
