package com.jinaup.upcustoms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultUtil {

	static Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(19[0-9])|(16[0-9])|(17[0-9])|(14[7|5,\\D]))\\d{8}$");
	/**
	 * 
	* @Title: isPhoneNum 
	* @Description: 校验是否是正常的手机号码
	* @author mac.liu
	* @param phoneNum
	* @return 
	* @return boolean
	* @throws
	 */
	public static boolean isPhoneNum(String phoneNum){
		Matcher m = p.matcher(phoneNum);
		return m.find();
	}
}
