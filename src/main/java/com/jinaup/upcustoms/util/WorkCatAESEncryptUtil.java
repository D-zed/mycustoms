package com.jinaup.upcustoms.util;

import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class WorkCatAESEncryptUtil {
	private static final Logger logger = LoggerFactory.getLogger(WorkCatAESEncryptUtil.class);
    private static final String UTF8 = "UTF-8";
    private static final String AES_ALG = "AES";
    private static final String AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";
    private static final byte[] AES_IV = initIv(AES_CBC_PCK_ALG);
    static {
        fixKeyLength();
    }
    /**
     * 针对AES算法密钥长度限制进行hack
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void fixKeyLength() {
        String errorString = "Failed manually overriding key-length permissions.";
        int newMaxKeyLength;
        try {
            if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
                Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
                Constructor con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissionCollection = con.newInstance();
                Field f = c.getDeclaredField("all_allowed");
                f.setAccessible(true);
                f.setBoolean(allPermissionCollection, true);
                c = Class.forName("javax.crypto.CryptoPermissions");
                con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissions = con.newInstance();
                f = c.getDeclaredField("perms");
                f.setAccessible(true);
                ((Map) f.get(allPermissions)).put("*", allPermissionCollection);
                c = Class.forName("javax.crypto.JceSecurityManager");
                f = c.getDeclaredField("defaultPolicy");
                f.setAccessible(true);
                Field mf = Field.class.getDeclaredField("modifiers");
                mf.setAccessible(true);
                mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                f.set(null, allPermissions);
                newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
            }
        } catch (Exception e) {
            throw new RuntimeException(errorString, e);
        }
        if (newMaxKeyLength < 256)
            throw new RuntimeException(errorString); // hack failed
    }
    /**
     * AES加密
     * 
     * @param content
     * @param encryptType
     * @return
     */
    public static String aesEncrypt(String content, String aesKey) {
        return aesEncrypt(content, aesKey, UTF8);
    }
    /**
     * AES解密
     * 
     * @param content
     * @param encryptType
     * @return
     */
    public static String aesDecrypt(String content, String aesKey) {
        return aesDecrypt(content, aesKey, UTF8);
    }
    /**
     * AES加密
     * 
     * @param content
     * @param aesKey
     * @param charset
     * @return
     */
    public static String aesEncrypt(String content, String aesKey, String charset) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(Base64.decodeBase64(aesKey.getBytes()), AES_ALG), iv);
            byte[] encryptBytes = cipher.doFinal(content.getBytes(charset));
            return new String(Base64.encodeBase64(encryptBytes));
        } catch (Exception e) {
            logger.error("AES加密失败：Aescontent = " + content + "; charset = " + charset, e);
            return null;
        }
    }
    /**
     * AES解密
     * 
     * @param content
     * @param key
     * @param charset
     * @return
     */
    public static String aesDecrypt(String content, String aesKey, String charset) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(aesKey.getBytes()),
                AES_ALG), iv);
            byte[] cleanBytes = cipher.doFinal(Base64.decodeBase64(content.getBytes()));
            return new String(cleanBytes, charset);
        } catch (Exception e) {
            logger.error("AES解密失败：Aescontent = " + content + "; charset = " + charset, e);
            return null;
        }
    }
    /**
     * 初始向量的方法, 全部为0. 针对AES算法的话,IV值一定是128位的(16字节).
     *
     * @param fullAlg
     * @return
     */
    private static byte[] initIv(String fullAlg) {
        try {
            Cipher cipher = Cipher.getInstance(fullAlg);
            int blockSize = cipher.getBlockSize();
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        } catch (Exception e) {
            int blockSize = 16;
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        }
    }
    
    public static void main(String[] args) {
		String ssString = MD5SHA256Util.md5("061f449a73644770bdbe5a7598f2de74aa233864d1f9204ac3aee5d19969e9ba");
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("name", "王星星");
		paramMap.put("mobile","15212345678");
		paramMap.put("certificateType","1");
		paramMap.put("idNumber", "620402198709215456");
		paramMap.put("bankName", "招商银行");
		paramMap.put("bankNum", "6214830100799652");
		
		String bb = WorkCatSignUtil.getUrlText(paramMap);
		System.out.println(ssString.toUpperCase());
		//lbrMBX7iME/iutEdBZKq/+dzI6EBnU0WwKQU1r5NEJ5rbYgHK7i7XR2+FPpFmU+BGQ50/PPLyR6Jb2O7FDn6dUjmF3zrrRwPVinQAtmZJU/O8BCGGZxpTM/W1FAW9SHzkdk5afOcUsT9xHLsIx4e5Q==
		//lbrMBX7iME/iutEdBZKq/+dzI6EBnU0WwKQU1r5NEJ42evQt+RuqSa+8rk9BvRuYbT9jWJBQOo3kUw+48+MHVLv3SDWIFEgT6DNhKVQaEwmcOe2rhPtgF4NLALMkoGfoFglg57fJgmnUnLjIoyRGYQ==
		System.out.println(WorkCatAESEncryptUtil.aesEncrypt(bb, ssString.toUpperCase()));
		System.out.println(WorkCatAESEncryptUtil.aesDecrypt("PtxJ73oU3wIxocEhh5WvjL5eAbmsa7HzALFyzDnAJVcDXuwsn7ZoFiVNOJnfQhdO8zqsd9ABX0XPQoKeN50xGOtpWEf+mX3xj5ZKPJBbvRlwFrC4ZpSWmSnEeC8GqBiLQasclON9mJDXCJjr9Tavbg+DNPB+alRn2YSmZIXpvas=", ssString.toUpperCase()));
	}
}
