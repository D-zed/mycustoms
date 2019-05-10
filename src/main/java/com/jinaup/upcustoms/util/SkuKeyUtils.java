package com.jinaup.upcustoms.util;

import org.springframework.util.DigestUtils;

import java.util.Collections;
import java.util.List;

public class SkuKeyUtils {

	/**
	 * 生成skuKey
	 * 
	 * @author shihai.liu
	 * @date 2019年3月1日
	 * @param pid
	 * @param vids
	 * @return
	 */
	public static String createSkuKey(Long pid, List<Long> vids) {
		StringBuffer skuKey = new StringBuffer();
		skuKey.append(pid);
		Collections.sort(vids);
		for (Long vid : vids) {
			skuKey.append("_").append(vid);
		}

		return DigestUtils.md5DigestAsHex(skuKey.toString().getBytes());
	}

	/**
	 * 拼串sukName
	 * 
	 * @author shihai.liu
	 * @date 2019年3月1日
	 * @param vNames
	 * @return
	 */
	public static String createSkuName(List<String> vNames) {
		StringBuffer skuName = new StringBuffer();
		for (String vname : vNames) {
			skuName.append(vname).append(" ");
		}
		return skuName.toString().substring(0, skuName.length() - 1);
	}
}
