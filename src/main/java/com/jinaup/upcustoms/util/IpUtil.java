package com.jinaup.upcustoms.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtil {
	/**
	 * 
	* @Title: getHttpAddrIp 
	* @Description: 获取请求ip
	* @author mac.liu
	* @param request
	* @return 
	* @return String
	* @throws
	 */
	public static String getHttpAddrIp(HttpServletRequest request) {
		String ipFromNginx = request.getHeader("X-Forwarded-For");
		//LOG.info("----------------------------GET_IP_X-Forwarded-For="+ipFromNginx+",X-Real-IP="+request.getHeader("X-Real-IP")+",GET_IP_RemoteAddr="+request.getRemoteAddr());
		if (StringUtils.isEmpty(ipFromNginx)) {
			ipFromNginx = request.getHeader("Proxy-Client-IP");
//			LOG.info("----------------------------GET_IP_Proxy-Client-IP="+ipFromNginx);
		}
		if (StringUtils.isEmpty(ipFromNginx)) {
			ipFromNginx = request.getHeader("WL-Proxy-Client-IP");
//			LOG.info("----------------------------GET_IP_WL-Proxy-Client-IP="+ipFromNginx);
		}
		if (StringUtils.isEmpty(ipFromNginx)) {
			ipFromNginx = request.getRemoteAddr();
//			LOG.info("----------------------------GET_IP_RemoteAddr="+ipFromNginx);
		}
		String[] ipList =  ipFromNginx.split(",");
		for(int i = 0;i<ipList.length;i++){
			if(!IpUtil.isInnerIp(ipList[i].trim())){
				return ipList[i].trim();
			}
		}
		int idx = ipFromNginx.indexOf(",");
		return idx > 0 ? ipFromNginx.substring(0, idx) : ipFromNginx;
	}
	
	/**
	 * 
	* @Title: checkIp
	* @Description: TODO(验证是否为正确Ip)
	* @param @param params_ip
	* @param @return    设定文件
	* @return boolean    返回类型
	* @author  youle.heng
	* @throws
	 */
	public static boolean checkIp(String params_ip){
		String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";  
  
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(params_ip);
        return matcher.matches();
	}
	
	/**
	 * 
	* @Title: isInnerIp
	* @Description: TODO(判断是否为内网IP)
	* @param @param ip
	* @param @return    设定文件
	* @return boolean    返回类型
	* @author  youle.heng
	* @throws
	 */
	public static boolean  isInnerIp(String ip){
	    String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";//正则表达式=。 =、懒得做文字处理了、
	    Pattern p = Pattern.compile(reg);
	    Matcher matcher = p.matcher(ip);
	    return matcher.find();
	}
}
