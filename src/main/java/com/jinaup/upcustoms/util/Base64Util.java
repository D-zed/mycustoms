package com.jinaup.upcustoms.util;

import org.springframework.util.Base64Utils;

import java.io.*;

public class Base64Util {

	/**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 
	 * @author shihai.liu
	 * @date 2019年1月8日
	 * @param in
	 * @return
	 */
	public static String encodeImgageToBase64(InputStream in) {
		byte[] data = null;
		// 读取图片字节数组
		try {
			// in.available() 只能在读取本地图片使用 若是网络图片会出错
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		return Base64Utils.encodeToString(data);
	}

	/**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 
	 * @author shihai.liu
	 * @date 2019年1月8日
	 * @param in
	 * @return
	 */
	public static String encodeImgageToBase64(ByteArrayOutputStream in) {
		// 返回Base64编码过的字节数组字符串
		return Base64Utils.encodeToString(in.toByteArray());
	}

	public static void decodeBase64ToImage(String base64, String path, String imgName) {
		try {
			FileOutputStream write = new FileOutputStream(new File(path + imgName));
			byte[] decoderBytes = Base64Utils.decodeFromString(base64);
			write.write(decoderBytes);
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static InputStream decodeBase64ToInputStream(String base64) {
		byte[] decoderBytes = Base64Utils.decodeFromString(base64);
		InputStream in = new ByteArrayInputStream(decoderBytes);
		return in;
	}

	/**
	 * 
	 * @TODO base64ToString
	 * @author shihai.liu
	 * @date 2018年1月8日上午11:26:22
	 * @return String
	 */
	public static String decodeBase64ToString(String base64) {
		byte[] decodeByte = Base64Utils.decodeFromString(base64);
		return new String(decodeByte);
	}

}
