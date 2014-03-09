package com.cstzju.poker.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

public class Common {

	/**
	 * public static int getTime() 当前时间和整点的时间差。
	 * 
	 * @return 时间差。 如果时间差异常——大于1小时的毫秒数——则返回-1。
	 * @exception Exception
	 *                日期格式异常的时候抛出
	 * 
	 */
	public static int getTime() {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
				Locale.getDefault());
		long between = -1;
		try {
			Date end = new Date();
			// Date begin = dfs.parse("2013-12-24 21:00:00.000");
			Date begin = dfs.parse((end.getYear() + 1900) + "-"
					+ (end.getMonth() + 1) + "-" + end.getDate() + " "
					+ end.getHours() + ":00:00.000");
			between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (between > 60 * 60 * 1000) {
			return -1;
		} else {
			return (int) between;
		}
	}

	/**
	 * public String getLocalIP() 当前所在网络环境的IPV4地址
	 * 
	 * @return ipaddress
	 */
	public static String getLocalIP() {
		String ipaddress = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ipaddress = ipaddress
								+ inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return ipaddress;
	}

}
