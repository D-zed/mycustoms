package com.jinaup.upcustoms.util;

import com.alibaba.fastjson.JSONObject;
import com.jinaup.common.Common;
import com.jinaup.domian.CookieVo;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CookiesUtil {
	
    public static void setCookie(HttpServletResponse response, String cookieName,String cookieValue) {
        	response.setHeader("Content-type", "text/html;charset=UTF-8");  
        	response.setCharacterEncoding("UTF-8");
            Cookie newCookie = new Cookie(cookieName, cookieValue);
            newCookie.setDomain(Common.cookieDomain);
            newCookie.setPath("/");
            newCookie.setMaxAge(24*30*3600);
            response.addCookie(newCookie);
        }
        
        public static void editCookie(HttpServletRequest request,HttpServletResponse response,String name,String value){
            Cookie[] cookies = request.getCookies();
            if (null==cookies) {
            } else {
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals(name)){
                        cookie.setValue(value);
                        cookie.setPath("/");
                        cookie.setMaxAge(24*30*3600);// 设置为30d
                        response.addCookie(cookie);
                        break;
                    }
                }
            }
             
        }

        public static Cookie getCookie(HttpServletRequest request, String cookieName) {
            Cookie cookie = null;

            if ((request != null) && (cookieName != null) && (request.getCookies() != null)) {
                Cookie[] cookieArr = request.getCookies();
                //从后面取值，解决有时候重新赋值时会取到老值的问题。
                for(int i=cookieArr.length -1 ; i>=0; i--)
                {
                    if ((cookieArr[i] != null) && (cookieName.equals(cookieArr[i].getName()))) {
                        cookie = cookieArr[i];
                        break;
                    }
                }
            }

            return cookie;
        }
        
        
        public static CookieVo getCookieUser(HttpServletRequest request){
        	 Cookie cookie = null;
        	 try {
        		 if (request != null  && request.getCookies() != null) {
                     Cookie[] cookieArr = request.getCookies();
                     //从后面取值，解决有时候重新赋值时会取到老值的问题。
                     for(int i=cookieArr.length -1 ; i>=0; i--)
                     {
                         if ((cookieArr[i] != null) && (Common.TrackID.equals(cookieArr[i].getName()))) {
                             cookie = cookieArr[i];
                             break;
                         }
                     }
                     if(cookie != null){
                    	 String md5String = cookie.getValue();
                    	 String json = KeyUtil.decode(Common.DefaultKey,md5String);
                    	 CookieVo cookieVo = JSONObject.parseObject(json, CookieVo.class);
                    	 if(cookieVo == null){
                    		 return null;
                    	 }
                    	 return cookieVo;
                     } 
                 }
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return null;
        }
        
        public static void removeCookies(HttpServletRequest request,HttpServletResponse response){
       	 Cookie cookie = null;
       	 try {
       		 if (request != null  && request.getCookies() != null) {
                    Cookie[] cookieArr = request.getCookies();
                    //从后面取值，解决有时候重新赋值时会取到老值的问题。
                    for(int i=cookieArr.length -1 ; i>=0; i--)
                    {
                        if (cookieArr[i] != null) {
                            cookie = cookieArr[i];
                            response.setHeader("Content-type", "text/html;charset=UTF-8");  
                        	 response.setCharacterEncoding("UTF-8");
                        	 cookie.setDomain(Common.cookieDomain);
                        	 cookie.setPath("/");
                        	 cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }
                    }
                }
                
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
       }

    	/**
    	 * @throws UnsupportedEncodingException 
    	 * 
    	* @Title: setCookies 
    	* @Description: 设置cookies信息
    	* @author mac.liu
    	* @param map 
    	* @return void
    	* @throws
    	 */
    	public static void setCookies(Map<String,String> map,HttpServletResponse response) throws UnsupportedEncodingException{
    		Map<String,String> trackMap = new HashMap<String,String>();
    		if(!map.isEmpty()){
    			for(String key : map.keySet()){
    				if(!StringUtils.isEmpty(map.get(key))){
    					if(key.equals("name")){
    						try {
    							CookiesUtil.setCookie(response, key, URLEncoder.encode(map.get(key), "UTF-8"));
    							trackMap.put(key,  URLEncoder.encode(map.get(key), "UTF-8"));
    						} catch (UnsupportedEncodingException e) {
    							e.printStackTrace();
    						}
    					}else{
    						if(!StringUtils.isEmpty(map.get(key))){
    							trackMap.put(key,URLEncoder.encode(map.get(key), "UTF-8"));
    						}
    						CookiesUtil.setCookie(response, key, map.get(key));
    					}
    				}
    				
    			}
    			CookiesUtil.setCookie(response, "TrackID", KeyUtil.encode(Common.DefaultKey, JSONObject.toJSONString(trackMap)));
    		}
    	}
}
