package com.jinaup.upcustoms.util;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jinaup.domian.SessionVo;
import com.jinaup.jedis.client.CacheClusterClient;

public class SessionUtil {
				public static SessionVo getSessionVo(CacheClusterClient cacheClusterClient,String sessionKey) {
								SessionVo sessionVo = null;
								String json = cacheClusterClient.get(sessionKey);
								
								if(!StringUtils.isEmpty(json)) {
									sessionVo = JSONObject.parseObject(json, SessionVo.class);
									return sessionVo;
								}
								return sessionVo;
				}
}
