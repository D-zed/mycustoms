package com.jinaup.upcustoms.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *@author 作者:shihai.liu
 *@since 2018年3月12日
 *
 */
public class HttpUtil {

	private static RequestConfig requestConfig;

	static {
		requestConfig = RequestConfig.custom().setSocketTimeout(10000)
				.setConnectTimeout(10000).setConnectionRequestTimeout(10000)
				.setCookieSpec(CookieSpecs.DEFAULT).build();
	}

   private static class TrustAnyTrustManager implements X509TrustManager {  
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
        public X509Certificate[] getAcceptedIssuers() {  
            return new X509Certificate[]{};  
        }  
    }  
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {  
        public boolean verify(String hostname, SSLSession session) {  
            return true;  
        }  
    }  

	public static String reqGet(String url) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = httpclient.execute(httpget, content);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, Consts.UTF_8);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String reqGetByParma(String url) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		httpget.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 7.0; MHA-AL00 Build/HUAWEIMHA-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043520 Safari/537.36 MicroMessenger/6.5.16.1120 NetType/WIFI Language/zh_CN");
		try {
			CloseableHttpResponse response = httpclient.execute(httpget, content);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, Consts.UTF_8);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static String reqGetSetCookie(String url, HttpServletRequest request) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		httpget.setHeader("Cookie", HttpUtil.getCookieString(request));
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget, content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					return EntityUtils.toString(entity, Consts.UTF_8);
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String reqPost(String url, Map<String, String> headers,
			Map<String, String> bodys) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (bodys != null) {
			for (String key : bodys.keySet()) {
				formparams.add(new BasicNameValuePair(key, bodys.get(key)));
			}
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				Consts.UTF_8);
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		try {
			httppost.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(httppost,
					content);
			try {
				HttpEntity entityResponse = response.getEntity();
				if (entityResponse != null) {
					return EntityUtils.toString(entityResponse, Consts.UTF_8);
				}
			} finally {
				response.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String reqPostByRaw(String url, Map<String, String> headers,String reqJson){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		StringEntity entity = new StringEntity(reqJson, Consts.UTF_8);
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		try {
			httppost.setEntity(entity);
			if(headers != null){
				for(String key : headers.keySet()){
					httppost.setHeader(key, headers.get(key));
				}
			}
			CloseableHttpResponse response = httpclient.execute(httppost,content);
			try {
				HttpEntity entityResponse = response.getEntity();
				if (entityResponse != null) {
					return EntityUtils.toString(entityResponse, Consts.UTF_8);
				}
			} finally {
				response.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static String reqPostByMapSetCookie(String url,Map<String, String> headers, Map<String, String> bodys,HttpServletRequest request) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (bodys != null) {
			for (String key : bodys.keySet()) {
				formparams.add(new BasicNameValuePair(key, bodys.get(key)));
			}
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,Consts.UTF_8);
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		httppost.setHeader("Cookie", HttpUtil.getCookieString(request));
		try {
			httppost.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(httppost,
					content);
			try {
				HttpEntity entityResponse = response.getEntity();
				if (entityResponse != null) {
					return EntityUtils.toString(entityResponse, Consts.UTF_8);
				}
			} finally {
				response.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	
	public static String reqPostFile(String url,Map<String,String> headers,Map<String,String> bodys,MultipartFile[] myfiles,HttpServletRequest request){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		httppost.setHeader("Cookie", HttpUtil.getCookieString(request));
		MultipartEntityBuilder build = MultipartEntityBuilder.create();
		build.setMode(HttpMultipartMode.STRICT);
		for(MultipartFile myfile: myfiles){
			try {
				build.addBinaryBody("myfiles", myfile.getInputStream(), ContentType.MULTIPART_FORM_DATA, myfile.getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (bodys != null) {
			for (String key : bodys.keySet()) {
				build.addTextBody(key, bodys.get(key), ContentType.APPLICATION_JSON);
			}
		}
		HttpEntity httpEntity = build.build();
		httppost.setEntity(httpEntity);
		    try {
		    	CloseableHttpResponse response = httpclient.execute(httppost,content);
				try {
					HttpEntity entityResponse = response.getEntity();
					if (entityResponse != null) {
						return EntityUtils.toString(entityResponse, Consts.UTF_8);
					}
				} finally {
					response.close();
				}
		    } catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			return null;
	}
	
	
	public static String reqPostFile(String url,Map<String,String> headers,Map<String,String> bodys,MultipartFile myfile,HttpServletRequest request){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		httppost.setHeader("Cookie", HttpUtil.getCookieString(request));
		MultipartEntityBuilder build = MultipartEntityBuilder.create();
		build.setMode(HttpMultipartMode.STRICT);
			try {
				build.addBinaryBody("myfiles", myfile.getInputStream(), ContentType.MULTIPART_FORM_DATA, myfile.getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (bodys != null) {
			for (String key : bodys.keySet()) {
				build.addTextBody(key, bodys.get(key), ContentType.APPLICATION_JSON);
			}
		}
		HttpEntity httpEntity = build.build();
		httppost.setEntity(httpEntity);
		    try {
		    	CloseableHttpResponse response = httpclient.execute(httppost,content);
				try {
					HttpEntity entityResponse = response.getEntity();
					if (entityResponse != null) {
						return EntityUtils.toString(entityResponse, Consts.UTF_8);
					}
				} finally {
					response.close();
				}
		    } catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			return null;
	}
	
	public static String reqPostFileByStream(String url,Map<String,String> headers,Map<String,String> bodys,InputStream in,HttpServletRequest request){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		httppost.setHeader("Cookie", HttpUtil.getCookieString(request));
		MultipartEntityBuilder build = MultipartEntityBuilder.create();
		build.setMode(HttpMultipartMode.STRICT);
		build.addBinaryBody("myfiles", in, ContentType.MULTIPART_FORM_DATA,"default.jpg");
		if (bodys != null) {
			for (String key : bodys.keySet()) {
				build.addTextBody(key, bodys.get(key), ContentType.APPLICATION_JSON);
			}
		}
		HttpEntity httpEntity = build.build();
		httppost.setEntity(httpEntity);
		try {
			CloseableHttpResponse response = httpclient.execute(httppost,content);
			try {
				HttpEntity entityResponse = response.getEntity();
				if (entityResponse != null) {
					return EntityUtils.toString(entityResponse, Consts.UTF_8);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String reqPostFileByStream(String url,Map<String,String> headers,Map<String,String> bodys,InputStream in){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		MultipartEntityBuilder build = MultipartEntityBuilder.create();
		build.setMode(HttpMultipartMode.STRICT);
		build.addBinaryBody("receipt-data", in);
		HttpEntity httpEntity = build.build();
		httppost.setEntity(httpEntity);
		try {
			CloseableHttpResponse response = httpclient.execute(httppost,content);
			try {
				HttpEntity entityResponse = response.getEntity();
				if (entityResponse != null) {
					return EntityUtils.toString(entityResponse, Consts.UTF_8);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getCookieString(HttpServletRequest request){
		 StringBuffer sb = new StringBuffer(); 
		 if ((request != null) && (request.getCookies() != null)) {
	        	Cookie cookie = null;
	            Cookie[] cookieArr = request.getCookies();
	            //从后面取值，解决有时候重新赋值时会取到老值的问题。
	            for(int i=cookieArr.length -1 ; i>=0; i--)
	            {
	                if ((cookieArr[i] != null) ) {
	                	cookie = cookieArr[i];
	                }
	                if(cookie != null){
	                	sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
	                }
	            }
	            return sb.toString();
	    }else{
	    	return "";
	    }
	}
	
	public static String reqPostFileByReceipt(String url,String receiptData){
//	       String buyCode=getBASE64(receipt);  
	       try{  
	           SSLContext sc = SSLContext.getInstance("SSL");  
	           sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());  
	           URL console = new URL(url);  
	           HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();  
	           conn.setSSLSocketFactory(sc.getSocketFactory());  
	           conn.setHostnameVerifier(new TrustAnyHostnameVerifier());  
	           conn.setRequestMethod("POST");  
	           conn.setRequestProperty("content-type", "text/json");  
	           conn.setRequestProperty("Proxy-Connection", "Keep-Alive");  
	           conn.setDoInput(true);  
	           conn.setDoOutput(true);  
	           BufferedOutputStream hurlBufOus=new BufferedOutputStream(conn.getOutputStream());  
	             
	           String str= String.format(Locale.CHINA,"{\"receipt-data\":\"" + receiptData+"\"}");  
	           hurlBufOus.write(str.getBytes());  
	           hurlBufOus.flush();  
	                     
	            InputStream is = conn.getInputStream();  
	            BufferedReader reader=new BufferedReader(new InputStreamReader(is));  
	            String line = null;  
	            StringBuffer sb = new StringBuffer();  
	            while((line = reader.readLine()) != null){  
	              sb.append(line);  
	            }  
	  
	            return sb.toString();  
	       }catch(Exception ex)  
	       {  
	           ex.printStackTrace();  
	       }  
	       return null;  
	}
	
	public static String[] agents = {
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
			"Opera/8.0 (Windows NT 5.1; U; en)",
			"Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50",
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 9.50",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0",
			"Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36",
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
			"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)",
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
			"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0",
			"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; SE 2.X MetaSr 1.0)",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.3.4000 Chrome/30.0.1599.101 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 UBrowser/4.0.3214.0 Safari/537.36"
	};
	
	public static String reqGetByParmaByMMM(String url,String referer) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpContext content = HttpClientContext.create();
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		int random = RandomUtils.nextInt(0,23);
		httpget.addHeader("User-Agent", agents[random]);
		httpget.addHeader("Referer",referer);
		try {
			CloseableHttpResponse response = httpclient.execute(httpget, content);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, Consts.UTF_8);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
