package com.jinaup.upcustoms.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;


public class JsonUtils {
	
	/**
	 * 是否json串
	 * 
	 * @author shihai.liu
	 * @since 2018年6月3日
	 * @param json
	 * @return
	 * @return boolean
	 */
	public static boolean isJsonString(String json){
		if(StringUtils.isEmpty(json))
			return false;
		try {
			JSONObject.parse(json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
