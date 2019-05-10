package com.jinaup.upcustoms.util;

import com.alibaba.fastjson.JSONObject;
import com.jinaup.common.Common;
import com.jinaup.domian.CookieVo;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginHeaderUtil {
	
    public static CookieVo getHeaderUser(HttpServletRequest request){
   	 try {
   		 if (request != null) {
               	 String md5String = request.getHeader("trackId");
               	 String json = KeyUtil.decode(Common.DefaultKey,md5String);
               	 CookieVo cookieVo = JSONObject.parseObject(json, CookieVo.class);
               	 if(cookieVo == null){
               		 return null;
               	 }
               	 return cookieVo;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
   }

	public static String setHeader(Map<String,String> map,HttpServletResponse response) throws UnsupportedEncodingException{
				Map<String,String> trackMap = new HashMap<String,String>();
				if(!map.isEmpty()){
					for(String key : map.keySet()){
						if(!StringUtils.isEmpty(map.get(key))){
							trackMap.put(key,URLEncoder.encode(map.get(key), "UTF-8"));
						}
					}
					String trackId = KeyUtil.encode(Common.DefaultKey, JSONObject.toJSONString(trackMap));
					response.setHeader("trackId", trackId);
					return trackId;
			}
			return "";
	}
	
	
    public static void removeHeader(HttpServletRequest request,HttpServletResponse response){
      	 try {
      		 if (request != null ) {
      			response.setHeader("trackId", "");
            }
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
      }
}
