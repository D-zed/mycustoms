package com.jinaup.upcustoms.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.text.MessageFormat;
import java.util.*;

@SuppressWarnings("deprecation")
public class WXHttpUtil {
	private final static Logger LOG = LoggerFactory.getLogger(WXHttpUtil.class.getName());

	public static String reqPostHost(String url, String param, String host) {
		CloseableHttpClient client = HttpClients.createDefault();
		String respBody = "";
		try {
			HttpPost post = new HttpPost(url);
			post.setConfig(WXHttpUtil.getRequestConfig());
			post.setHeader("Host", host);
			post.setEntity(new StringEntity(param, HTTP.UTF_8));
			CloseableHttpResponse resp = client.execute(post);
			try {
				if (resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = resp.getEntity();
					respBody = EntityUtils.toString(entity).toString();
				} else {
					respBody = null;
					LOG.info("Connection refused!!!");
					throw new Exception("Connection refused");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				resp.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respBody;
	}

	public static String reqPostScanCodeByMap(String url, Map<String, String> map) {
		CloseableHttpClient client = HttpClients.createDefault();
		String respBody = "";
		try {
			HttpPost post = new HttpPost(url);
			post.setConfig(WXHttpUtil.getRequestConfig());
			post.setEntity(new StringEntity(WeixinUtil.getWeixinScanCodeXml(map), "UTF-8"));
			CloseableHttpResponse resp = client.execute(post);
			try {
				if (resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = resp.getEntity();
					respBody = EntityUtils.toString(entity, "UTF-8");
					LOG.info("Connection success, code:" + resp.getStatusLine().getStatusCode() + ",body:" + respBody);
				} else {
					HttpEntity entity = resp.getEntity();
					respBody = EntityUtils.toString(entity).toString();
					LOG.info("Connection refused, code:" + resp.getStatusLine().getStatusCode() + ",body:" + respBody);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				resp.close();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respBody;
	}

	public static String reqPostByMap(String url, Map<String, String> map) {
		CloseableHttpClient client = HttpClients.createDefault();
		String respBody = "";
		try {
			HttpPost post = new HttpPost(url);
			post.setConfig(WXHttpUtil.getRequestConfig());
			post.setEntity(new StringEntity(WeixinUtil.getWeixinXml(map), "UTF-8"));
			CloseableHttpResponse resp = client.execute(post);
			try {
				if (resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = resp.getEntity();
					respBody = EntityUtils.toString(entity, "UTF-8");
					LOG.info("Connection success, code:" + resp.getStatusLine().getStatusCode() + ",body:" + respBody);
				} else {
					HttpEntity entity = resp.getEntity();
					respBody = EntityUtils.toString(entity).toString();
					LOG.info("Connection refused, code:" + resp.getStatusLine().getStatusCode() + ",body:" + respBody);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				resp.close();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respBody;
	}

	public static String reqPostSSLByMap(String url, Map<String, String> map) {
		String respBody = "";
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			String CERT = map.get("cert");
			map.remove("cert");
			String MCHID = map.get("mch_id");
			FileInputStream instream = new FileInputStream(new File(CERT));
			try {
				keyStore.load(instream, MCHID.toCharArray());
			} finally {
				instream.close();
			}
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, MCHID.toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost post = new HttpPost(url);
			post.setConfig(WXHttpUtil.getRequestConfig());
			post.setEntity(new StringEntity(WeixinUtil.getWeixinXml(map), "UTF-8"));
			CloseableHttpResponse resp = httpclient.execute(post);
			if (resp.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = resp.getEntity();
				respBody = EntityUtils.toString(entity, "UTF-8");
				LOG.info("Connection success, code:" + resp.getStatusLine().getStatusCode() + ",body:" + respBody);
			} else {
				HttpEntity entity = resp.getEntity();
				respBody = EntityUtils.toString(entity, "UTF-8");
				LOG.info("Connection refused, code:" + resp.getStatusLine().getStatusCode() + ",body:" + respBody);
				respBody = null;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
		}
		return respBody;
	}

	public static String reqPost(String url, Map<String, String> bodys) {
		CloseableHttpClient client = HttpClients.createDefault();
		String respBody = "";
		try {
			HttpPost post = new HttpPost(url);
			post.setConfig(WXHttpUtil.getRequestConfig());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = bodys.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, bodys.get(key)));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			CloseableHttpResponse resp = client.execute(post);
			try {
				if (resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = resp.getEntity();
					respBody = EntityUtils.toString(entity).toString();
				} else {
					respBody = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				resp.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respBody;
	}

	/**
	 * POST请求数据，无参数提交JSON
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String reqPost(String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		Long s = System.currentTimeMillis();
		String respBody = "";
		try {
			HttpPost post = new HttpPost(url);
			post.setConfig(WXHttpUtil.getRequestConfig());
			CloseableHttpResponse resp = client.execute(post);
			try {
				if (resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = resp.getEntity();
					respBody = EntityUtils.toString(entity).toString();
				} else {
					respBody = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		Long e = System.currentTimeMillis();
		LOG.info(MessageFormat.format("请求经过:{0} ", e - s));
		return respBody;
	}

	public static String reqGet(String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		String respBody = "";
		HttpGet get = new HttpGet(url);
		try {
			get.setConfig(WXHttpUtil.getRequestConfig());
			CloseableHttpResponse resp = client.execute(get);
			try {
				if (resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = resp.getEntity();
					respBody = EntityUtils.toString(entity).toString();
				} else {
					respBody = "";
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				resp.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respBody;
	}

	public static boolean reqGetRange(String url) throws IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		boolean acceptRanges = false;
		Long s = System.currentTimeMillis();
		HttpHead httpHead = new HttpHead(url);
		HttpResponse resp;
		try {
			resp = client.execute(httpHead);
			if (resp.getStatusLine().getStatusCode() == 200) {
				Header[] headers = resp.getHeaders("Content-Length");
				if (headers.length > 0) {
					Long contentLength = Long.valueOf(headers[0].getValue());
					httpHead.abort();
					httpHead = new HttpHead(url);
					httpHead.addHeader("Range", "bytes=0-" + (contentLength - 1));
					resp = client.execute(httpHead);
					if (resp.getStatusLine().getStatusCode() == 206) {
						acceptRanges = true;
					}
					httpHead.abort();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		Long e = System.currentTimeMillis();
		LOG.info(MessageFormat.format("请求经过:{0}s", e - s));
		return acceptRanges;
	}

	public static RequestConfig getRequestConfig() {
		RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).setExpectContinueEnabled(true).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
		RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();

		return requestConfig;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

	}
}
