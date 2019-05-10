package com.jinaup.upcustoms.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WorkCatSignUtil {
		//正式环境 339455d4e0f646959a0ab9adc854d872  测试环境 587019a7cbb74d22a16857e9d8684844
		public static final String APPKEY = "339455d4e0f646959a0ab9adc854d872";
		
		//正式环境 d54054dcb98c7da648ab9d0368022285  测试环境 7c7fc10bf85ffcdd8613f11b49266e07
	    public static final String APPSECRET = "d54054dcb98c7da648ab9d0368022285";
	    public static final String NONCE = "nonce";
	    public static final String TIMESTAMP = "timestamp";
	    public static final String SIGN = "sign";
	    /**
	     * 获取签名
	     * @param paramMap 包含所有业务参数，和appKey,nonce,timestamp这3个公共参数
	     * @param appSecret
	     * @return
	     */
	    public static String getSign(Map<String, Object> paramMap, String appSecret) {
	        String text = getUrlText(paramMap);
	        text += "&appSecret=" + appSecret;
	        return MD5SHA256Util.md5(text).toUpperCase();
	    }
	    
	    public static String getUrlText(Map<String, Object> beanMap) {
	        beanMap = getSortedMap(beanMap);
	        StringBuilder builder = new StringBuilder();
	        for (String key : beanMap.keySet()) {
	            String value = beanMap.get(key).toString();
	            builder.append(key);
	            builder.append('=');
	            builder.append(value);
	            builder.append('&');
	        }
	        String text = builder.toString();
	        return text.substring(0, text.length() - 1);
	    }
	    /**
	     *  对普通map进行排序
	     * @param paramMap
	     * @return
	     */
	    private static Map<String, Object> getSortedMap(Map<String, Object> paramMap) {
	        SortedMap<String, Object> map = new TreeMap<String, Object>();
	        for (String key : paramMap.keySet()) {
	            if (key != null && !APPSECRET.equals(key)) {
	                Object value = paramMap.get(key);
	                if (value != null) {
	                    String valueStr = String.valueOf(value);
	                    if (valueStr != null && !"".equals(valueStr)) {
	                        map.put(key, value);
	                    }
	                }
	            }
	        }
	        return map;
	    }
}
