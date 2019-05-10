package com.jinaup.upcustoms.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;


/**
 * Created by yfs on 2017/2/6.
 * <p>
 * AES-128-CBC 加密方式
 * 注：
 * AES-128-CBC可以自己定义“密钥”和“偏移量“。
 * AES-128是jdk自动生成的“密钥”。
 */
public class AesCbcUtil {
	 static {
	        //BouncyCastle是一个开源的加解密解决方案，主页在http://www.bouncycastle.org/
	        Security.addProvider(new BouncyCastleProvider());	
	    }

	    /**
	     * AES解密
	     *
	     * @param data           //密文，被加密的数据
	     * @param key            //秘钥
	     * @param iv             //偏移量
	     * @param encodingFormat //解密后的结果需要进行的编码
	     * @return
	     * @throws Exception
	     */
	    public static String decrypt(String data, String key, String iv, String encodingFormat) throws Exception {
//	        initialize();

	        //被加密的数据
	        byte[] dataByte = Base64.decodeBase64(data);
	        //加密秘钥
	        byte[] keyByte = Base64.decodeBase64(key);
	        //偏移量
	        byte[] ivByte = Base64.decodeBase64(iv);


	        try {
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");

	            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

	            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
	            parameters.init(new IvParameterSpec(ivByte));

	            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

	            byte[] resultByte = cipher.doFinal(dataByte);
	            if (null != resultByte && resultByte.length > 0) {
	                String result = new String(resultByte, encodingFormat);
	                return result;
	            }
	            return null;
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (NoSuchPaddingException e) {
	            e.printStackTrace();
	        } catch (InvalidParameterSpecException e) {
	            e.printStackTrace();
	        } catch (InvalidKeyException e) {
	            e.printStackTrace();
	        } catch (InvalidAlgorithmParameterException e) {
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	            e.printStackTrace();
	        } catch (BadPaddingException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }

	        return null;
	    }
	    public static void main(String[] args) {
			try {
				String encryptedData = "z7i7UUiCTyF0pC4v7GC9uzsbYvl+OO4nTnxK7bDzq/Z137iBHIt+kfwFHlQ+vpVjOQqrhC6d6WLFX7xTFI1O6tZLuw7a8r2uvj2w7sEw9uiIyI2O6Mm4fcq/bFRKJ917JtWQEgcC7QhPgiZDQynyA7ZYSPZ5tJRhzTA4DQkIdwfPtaD84DBeLoNKr3uScf5akyV34ocNemGRbb0OuBgIu/bq0I+gyWw8Wn7AkPCPOakW4yprYTMVztP5Mc5IzHBAFah6ybv7goaoEL5F1GJB4loFx8gN2TuGqPyCcjR1tsm7HrAdpbtfmOcUyvlVfiskrH6apCG2k4+418+3zDrOyLx3ILfLkN7SsiQAC0Sp1yx3wAAVRmWbo1cDgujTRrw7WYA6ShUzvMwNwN4A4nmxn5ikrwIY7PZ0XQ+U48FIaNKBgTzYZ2+nP7rcIqABuy74PH37CN4mxT2qE3O4oS/bqEvrkOS5+3pTsdnCusPkgFK3nQ9QjmO0Yp8RRRzcwYOaG98NgP1LxHpNnPAzSZFvWg==";
				String sessionKey = "4YHFepkBRsxUbcv1aeOyRQ==";
				String iv = "IWVnH5ZeqSHdFyxm/ViJhg==";
				System.out.println(AesCbcUtil.decrypt(encryptedData,sessionKey,iv,"utf-8"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
