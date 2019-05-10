package com.jinaup.upcustoms.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 快递工具类
 * @author shihai.liu
 * @date 2019年2月16日
 */
public class ExpressUtil {

	private final static String customer = "6F9DBDA26A3B352C920659A0C60311B6";
	private final static String key = "fccHHtBg994";
	private final static String reqUrl = "https://poll.kuaidi100.com/poll/query.do";
	
	/**
	 * 
	 * @author shihai.liu
	 * @date 2019年2月16日
	 * @param num 查询的快递单号， 单号的最大长度是32个字符
	 * @param com 查询的快递公司的编码， 一律用小写字母
	 * @param phone 寄件人或收件人手机号（顺丰单号必填）
	 * @return
	 */
	public static JSONObject getExpressUtil(String num,String com,String phone) {
		
		if(StringUtils.isEmpty(num) || StringUtils.isEmpty(com) || StringUtils.isEmpty(phone)) {
			return null;
		}
		
		Map<String,String> param = Maps.newTreeMap();
		param.put("com", com);
		param.put("num", num);
		param.put("phone", phone);
		String sign = getSign(param);
		Map<String,String> reqMap = Maps.newHashMap();
		reqMap.put("param", JSONObject.toJSONString(param));
		reqMap.put("sign", sign);
		reqMap.put("customer", customer);
		String body = HttpUtil.reqPost(reqUrl, null,reqMap);
		boolean bool = JsonUtils.isJsonString(body);
		if(bool) {
			return JSONObject.parseObject(body);
		}
		return null;
		
	}
	
	/**
	 * 签名
	 * @author shihai.liu
	 * @date 2019年2月16日
	 * @param param
	 * @return
	 */
	private static String getSign(Map<String,String> param) {
		String paramStr = JSONObject.toJSONString(param);
		String strSign = paramStr.concat(key).concat(customer);
		return DigestUtils.md5DigestAsHex(strSign.getBytes()).toUpperCase();
	}
}
