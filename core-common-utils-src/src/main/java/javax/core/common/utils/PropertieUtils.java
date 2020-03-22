package javax.core.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件操作类
 * 
 * @author Tanyongde
 * @createDate Apr 17, 2009 9:05:20 PM
 */
public class PropertieUtils {
	private Properties props = new Properties();
	private InputStream jndiInput = null;

	private PropertieUtils(){}
	
	public PropertieUtils(String fileName) {
		try {
			jndiInput = PropertieUtils.class.getClassLoader()
					.getResourceAsStream(fileName);
			props.load(jndiInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String name) {
		return props.getProperty(name);
	}

	public String getString(String name) {
		return props.getProperty(name);
	}

	public String getString(String name, String df) {
		return props.getProperty(name, df);
	}

	public Boolean getBoolean(String name) {
		String val = props.getProperty(name);
		return val == null ? null : Boolean.valueOf(val);
	}

	public Boolean getBoolean(String name, Boolean df) {
		String val = props.getProperty(name);
		return val == null ? df : Boolean.valueOf(val);
	}

	public Integer getInteger(String name) {
		String val = props.getProperty(name);
		return val == null ? null : Integer.parseInt(val);
	}

	public Integer getInteger(String name, Integer df) {
		String val = props.getProperty(name);
		return val == null ? df : Integer.parseInt(val);
	}

	public Object set(String key, String value) {
		return props.setProperty(key, value);
	}

	public void close() {
		try {
			jndiInput.close();
		} catch (IOException e) {
		}
	}

	public Set stringPropertyNames() {
		return props.stringPropertyNames();
	}
}
