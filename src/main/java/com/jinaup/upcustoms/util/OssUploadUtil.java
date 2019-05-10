package com.jinaup.upcustoms.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.google.common.collect.Maps;
import com.jinaup.client.OssClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * OSS 上传工具
 * 
 * @author shihai.liu
 * @date 2019年1月4日
 */
public class OssUploadUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(OssUploadUtil.class);

	/**
	 * 以流上传数据
	 * 
	 * @author shihai.liu
	 * @date 2019年1月4日
	 * @param bucketName
	 * @param objectName
	 * @param inputStream
	 */
	public static boolean putObject(String bucketName, String objectName, InputStream inputStream) {
		OSSClient oSSClient = OssClient.getOssCLient();
		if (StringUtils.isEmpty(bucketName)) {
			bucketName = OssClient.bucketName;
		}
		if (StringUtils.isEmpty(objectName)) {
			LOGGER.info("Oss putObject objectName is null");
			return false;
		}
		try {
			oSSClient.putObject(bucketName, objectName, inputStream);
		} catch (Exception e) {
			LOGGER.info("Oss putObject error info %s", e.getMessage());
			return false;
		} finally {
			oSSClient.shutdown();
		}
		return true;
	}
	
	/**
	 * 批量上传数据
	 * @author shihai.liu
	 * @date 2019年1月4日
	 * @param bucketName
	 * @param objectName
	 * @param inputStreams
	 * @return
	 */
	public static boolean putObjects(final String bucketName, Map<String,InputStream> maps) {
		OSSClient oSSClient = OssClient.getOssCLient();
		if (StringUtils.isEmpty(bucketName)) {
			LOGGER.info("Oss putObjects bucketName is null");
			return false;
		}
		if(maps == null || maps.size() == 0) {
			LOGGER.info("Oss putObjects maps is null");
			return false;
		}
		try {
			maps.forEach((k,v) -> oSSClient.putObject(bucketName, k,v));
		} catch (Exception e) {
			LOGGER.info("Oss putObject error info:"+e.getMessage());
			return false;
		} finally {
			oSSClient.shutdown();
		}
		return true;
	}

	/**
	 * 删除一个文件
	 * 
	 * @author shihai.liu
	 * @date 2019年1月4日
	 * @param bucketName
	 * @param objectName
	 * @return
	 */
	public static boolean deleteObject(String bucketName, String objectName) {
		OSSClient oSSClient = OssClient.getOssCLient();
		if (StringUtils.isEmpty(bucketName)) {
			bucketName = OssClient.bucketName;
		}
		if (StringUtils.isEmpty(objectName)) {
			LOGGER.info("Oss delObject objectName is null");
			return false;
		}
		try {
			oSSClient.deleteObject(bucketName, objectName);
		} catch (Exception e) {
			LOGGER.info("Oss delObject error info %s", e.getMessage());
			return false;
		} finally {
			oSSClient.shutdown();
		}
		return true;
	}

	/**
	 * 批量删除Oss文件
	 * 
	 * @author shihai.liu
	 * @date 2019年1月4日
	 * @param bucketName
	 * @param objectNames
	 * @return
	 */
	public static boolean deleteObjects(String bucketName, List<String> objectNames) {
		OSSClient oSSClient = OssClient.getOssCLient();
		if (StringUtils.isEmpty(bucketName)) {
			bucketName = OssClient.bucketName;
		}
		if (objectNames == null || objectNames.size() == 0) {
			LOGGER.info("deleteObjects objectNames is null");
			return false;
		}
		try {
			oSSClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(objectNames));
		} catch (Exception e) {
			LOGGER.info("Oss deleteObjects error info %s", e.getMessage());
			return false;
		} finally {
			oSSClient.shutdown();
		}
		return true;
	}

	/**
	 * 获取访问地址
	 * 
	 * @author shihai.liu
	 * @date 2019年1月4日
	 * @param bucketName
	 * @param objectName
	 * @return
	 */
	public static String getAccessUrlByObjectName(String objectName) {
		if (StringUtils.isEmpty(objectName)) {
			return null;
		}
		return String.format(OssClient.accessEndpoint, objectName);
	}
	
	/**
	 * 获取业务访问路径
	 * @author shihai.liu
	 * @time 2018年10月26日
	 * @param product
	 * @param fileName
	 * @param userId
	 * @return
	 */
	public static String getAccessUrlByObjectName(String catalog,String fileName,Long userId) {
		String objectName = getObjectName(catalog, fileName, userId);
		if(StringUtils.isEmpty(objectName)) {
			return String.format(OssClient.accessEndpoint, "default.jpg");
		}else {
			return String.format(OssClient.accessEndpoint, objectName);
		}
	}
	
	/**
	 * 获取ObjectName
	 * @author shihai.liu
	 * @date 2019年1月16日
	 * @param catalog
	 * @param fileName
	 * @param userId
	 * @return
	 */
	public static String getObjectName(String catalog,String fileName,Long userId) {
		if(StringUtils.isEmpty(fileName) || StringUtils.isEmpty(fileName)) {
			return null;
		}
		if(userId == null || userId.longValue() == 0) {
			return catalog.concat("/").concat(fileName);
		}else {
			return catalog.concat("/").concat(String.valueOf(userId%10)).concat("/").concat(userId.toString()).concat("/").concat(fileName);
		}
	}
	
	/**
	 * 获取文件名称
	 * @author shihai.liu
	 * @date 2019年1月16日
	 * @param file
	 * @return
	 */
	public static String getFileName(MultipartFile file) {
		if(file != null && file.getSize() > 0) {
			String origName = file.getOriginalFilename();
			String suffix = origName.substring(origName.lastIndexOf("."));
			return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()).concat(suffix);
		}else {
			return null;
		}
	}
	
	/**
	 * 批量上传
	 * @author shihai.liu
	 * @date 2019年2月19日
	 * @param catalog
	 * @param userId
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static Map<String,String> uploadFiles(String catalog,Long userId,MultipartFile[] files) throws IOException{
		if(files == null || files.length == 0 || StringUtils.isEmpty(catalog)) {
			return null;
		}
		Map<String,String> maps = Maps.newHashMap();
		for(MultipartFile file : files) {
			String fileName = getFileName(file);
			String objectName = getObjectName(catalog, fileName, userId);
			String accessUrl = getAccessUrlByObjectName(objectName);
			Boolean bool = putObject(OssClient.bucketName, objectName,file.getInputStream());
			if(bool) {
				maps.put(objectName, accessUrl);
			}else {
				deleteObject(OssClient.bucketName, objectName);
			}
		}
		return maps;
	}
	
	/**
	 * 单文件上传
	 * @author shihai.liu
	 * @date 2019年2月19日
	 * @param catalog
	 * @param userId
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Map<String,String> uploadFile(String catalog,Long userId,MultipartFile file) throws IOException{
		if(file == null || StringUtils.isEmpty(catalog)) {
			return null;
		}
		Map<String,String> maps = Maps.newHashMap();
		String fileName = getFileName(file);
		String objectName = getObjectName(catalog, fileName, userId);
		String accessUrl = getAccessUrlByObjectName(objectName);
		Boolean bool = putObject(OssClient.bucketName, objectName,file.getInputStream());
		if(bool) {
			maps.put(objectName, accessUrl);
		}else {
			deleteObject(OssClient.bucketName, objectName);
		}
		return maps;
	}
	
	
	public static void main(String[] args) {
//		String bucketName = OssClient.bucketName;
//		String objectName = "web/uploads/image/test.txt";
////		String url = OssUploadUtil.getObjectUrl(bucketName, objectName);
////		System.out.println("url-->"+url);
//		String content = "Hello OSS";
//		boolean bool = OssUploadUtil.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
//		System.out.println("bool-->"+bool);
	}
}