package com.jinaup.upcustoms.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AlipayNotify {
	 
	private final static Logger LOG = LoggerFactory.getLogger(AlipayNotify.class.getName());
     public static  boolean verify(Map<String,String> params){
    	 String alipayPulicKey= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    	 String charset="UTF-8";
    	 boolean result=false;
    	 try {
			result= AlipaySignature.rsaCheckV1(params, alipayPulicKey, charset);
			 //写日志记录（若要调试，请取消下面两行注释）
	        String sWord = "\n 验签结果=" + result + "\n 返回回来的参数：" + Createlink.createLinkString(params);
	        LOG.info("sWord:"+sWord);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return result;
        }
}
