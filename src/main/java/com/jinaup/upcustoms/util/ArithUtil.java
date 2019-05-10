package com.jinaup.upcustoms.util;

import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

/**
 * 
 * 数据均来自网络
 *
 */
public class ArithUtil {
	

	/**  
	* 提供精确的加法运算。  
	* @param v1 被加数  
	* @param v2 加数  
	* @return double  
	*/
	public static BigDecimal add(BigDecimal b1,BigDecimal b2){   
		return b1.add(b2);   
	}   
	/**  
	* 提供精确的减法运算。  
	* @param v1 被减数  
	* @param v2 减数  
	* @return double  
	*/
	public static BigDecimal sub(BigDecimal b1,BigDecimal b2){   
		return b1.subtract(b2);   
	}   
	/**  
	* 提供精确的乘法运算。  
	* @param v1 被乘数  
	* @param v2 乘数  
	* @return double 
	*/
	public static BigDecimal mul(BigDecimal b1,BigDecimal b2){   
		return b1.multiply(b2);   
	}
	
	/**  
	* 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到  
	* 小数点以后10位，以后的数字四舍五入。  
	* @param v1 被除数  
	* @param v2 除数 
	* @param scale 表示表示需要精确到小数点以后几位。 
	* @return double  
	*/
	public static BigDecimal divide(BigDecimal b1,BigDecimal b2,int scale){   
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP);     
	}
	
	/**
	 * 计算关系md5值
	 * @author shihai.liu
	 * @date 2019年1月24日
	 * @param userId
	 * @param parentId
	 * @return
	 */
	public static String getRelationMd5(Long userId,Long parentId) {
		StringBuilder sb = new StringBuilder();
		if(userId > parentId) {
			sb.append(parentId).append("_").append(userId);
		}else {
			sb.append(userId).append("_").append(parentId);
		}
		return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
	}
	
	public static void main(String[] args) {
		System.out.println(ArithUtil.getRelationMd5(100529L, 11873L));
	}
}
