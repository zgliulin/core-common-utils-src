package javax.core.common.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 *@author Tanyongde
 */
public class IpUtil {
	
	private IpUtil(){}

	/**
	 * 获取Ip地址，如有多个ip,只取第一个
	 * 
	 * @param request
	 * @return 返回IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
	
	/**
	 * 只取ipv4且如有多个ip,只取第一个
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getLocalIp() throws SocketException {

		Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
		String ip = "";
		while (e1.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) e1.nextElement();
			Enumeration<InetAddress> e2 = ni.getInetAddresses();
			while (e2.hasMoreElements()) {
				InetAddress ia = (InetAddress) e2.nextElement();
				if (ia instanceof Inet6Address)
					continue; // ignore ipv6
				if (!ia.isLoopbackAddress()
						&& ia.getHostAddress().indexOf(":") == -1) {
					ip = ia.getHostAddress();
				}
			}
		}

		return ip;
	}
	
	/**
	 * 验证IP是否在IP列表中
	 * 支持的格式：192.168.0.1,192.168.0.*,192.168.0.1-192.168.0.130
	 * 
	 * @param userIp
	 *            用户访问IP
	 * @param superIPs
	 *            授权IP列表
	 * @return true:验证通过 false:验证未通过
	 */
	public static boolean isSuperIp(String userIp, List<String> superIPs) {
		boolean isSuperIP = false;
		if (userIp == null || userIp.equals("")) {/* 访问用户IP为空 */
			return isSuperIP;
		}

		if (superIPs == null || superIPs.equals("")) {/* 授权IP地址库为空 */
			return isSuperIP;
		}

		int position = userIp.lastIndexOf(".") + 1;
		String strGroupUserIP = userIp.substring(0, position) + "*";
		for (Iterator<String> iter = superIPs.iterator(); iter.hasNext();) {
			String strSuperIP = (String) iter.next();
			/* 验证IP区间如：192.168.1.2-192.168.1.100 */
			if (strSuperIP.indexOf("-") > 0){
				String[] intervalIP = strSuperIP.split("-");
				int index = Integer.parseInt(intervalIP[0].substring(intervalIP[0].lastIndexOf(".")+1));
				int end = Integer.parseInt(intervalIP[1].substring(intervalIP[1].lastIndexOf(".")+1));
				String topIp = intervalIP[0].substring(0,intervalIP[0].lastIndexOf("."));
				for (int i = index; i <= end; i++) {
					if (userIp.equals(topIp + "." + i)) {
						isSuperIP = true;
						break;
					}
				}
			}
			
			/* 验证IP */
			if (userIp.equals(strSuperIP) || strGroupUserIP.equals(strSuperIP)) {
				isSuperIP = true;
				break;
			}
		}
		return isSuperIP;
	}
}
