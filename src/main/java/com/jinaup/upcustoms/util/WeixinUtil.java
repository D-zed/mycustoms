package com.jinaup.upcustoms.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jinaup.common.ApplicationKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 
 * @author mac.liu
 *
 */
public class WeixinUtil {

	private final static Logger LOG = LoggerFactory.getLogger(WeixinUtil.class.getName());

	/**
	 * 微信xml
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @param map
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getWeixinXml(Map<String, String> map) throws NoSuchAlgorithmException {
		// step1 向map中追加默认
		map.put("payMethod", "");
		// step2 生成getNonce_str
		String nonce_str = WeixinUtil.getNonce_str();
		map.put("nonce_str", nonce_str);
		// step3 key=value 格式 ASCII字典序排序
		List<String> keys = new ArrayList<String>();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (map.get(key).toString().isEmpty()) {
				iterator.remove();
			} else {
				keys.add(key);
			}
		}
		Collections.sort(keys);

		StringBuilder sbA = new StringBuilder();
		StringBuilder xmlString = new StringBuilder();
		xmlString.append("<xml>");
		if (keys.size() > 0) {
			for (String key : keys) {
				sbA.append(key).append("=").append(map.get(key)).append("&");
				xmlString.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
			}
			sbA.append("key").append("=").append(ApplicationKey.apiSecret);

		}
		LOG.info("生成预付单签名串：" + sbA);
		// step4 Sign 签名 MD5
		String sign = sbA == null ? null : DigestUtils.md5DigestAsHex(sbA.toString().getBytes()).toUpperCase();
		xmlString.append("<").append("sign").append(">").append(sign).append("</").append("sign").append(">");
		xmlString.append("</xml>");
		LOG.info("生成xml：" + xmlString.toString());
		return xmlString.toString();
	}
	
	/**
	 * 获取签名数据
	 * @author shihai.liu
	 * @date 2019年2月21日
	 * @param map
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<String,Object> getWeiXinSignJson(Map<String, String> map) throws NoSuchAlgorithmException {
		// step1 向map中追加默认
		map.put("payMethod", "");
		// step2 生成getNonce_str
//		String nonce_str = WeixinUtil.getNonce_str();
//		map.put("nonce_str", nonce_str);
		// step3 key=value 格式 ASCII字典序排序
		List<String> keys = new ArrayList<String>();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (map.get(key).toString().isEmpty()) {
				iterator.remove();
			} else {
				keys.add(key);
			}
		}
		Collections.sort(keys);
		
		StringBuilder sbA = new StringBuilder();
		Map<String,Object> item = Maps.newHashMap();
		if (keys.size() > 0) {
			for (String key : keys) {
				sbA.append(key).append("=").append(map.get(key)).append("&");
				item.put(key,map.get(key));
			}
			sbA.append("key").append("=").append(ApplicationKey.apiSecret);
			item.put("key", ApplicationKey.apiSecret);
			
		}
		LOG.info("生成预付单签名串：" + sbA);
		// step4 Sign 签名 MD5
		String sign = sbA == null ? null : DigestUtils.md5DigestAsHex(sbA.toString().getBytes()).toUpperCase();
		item.put("sign",sign);
		return item;
	}

	/**
	 * 扫码签名xml
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @param map
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getWeixinScanCodeXml(Map<String, String> map) throws NoSuchAlgorithmException {
		// step3 key=value 格式 ASCII字典序排序
		List<String> keys = new ArrayList<String>();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (map.get(key).toString().isEmpty()) {
				iterator.remove();
			} else {
				keys.add(key);
			}
		}
		Collections.sort(keys);

		StringBuilder sbA = new StringBuilder();
		StringBuilder xmlString = new StringBuilder();
		xmlString.append("<xml>");
		if (keys.size() > 0) {
			for (String key : keys) {
				sbA.append(key).append("=").append(map.get(key)).append("&");
				xmlString.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
			}
			// "m591EAE75BF0d50C47C5626372A059E8"
			sbA.append("key").append("=").append(ApplicationKey.apiSecret);
		}
		LOG.info("生成预付单签名串：" + sbA);
		// step4 Sign 签名 MD5
		String sign = sbA == null ? null : DigestUtils.md5DigestAsHex(sbA.toString().getBytes()).toUpperCase();
		xmlString.append("<").append("sign").append(">").append(sign).append("</").append("sign").append(">");
		xmlString.append("</xml>");
		LOG.info("生成XML：" + xmlString);
		return xmlString.toString();
	}

	/**
	 * 生成随机数据1000000000以内
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @return
	 */
	public static String getNonce_str() {
		Random random = new Random(System.currentTimeMillis());
		int randomNum = random.nextInt(1000000000);
		return DigestUtils.md5DigestAsHex(String.valueOf(randomNum).getBytes()).toUpperCase();
	}

	/**
	 * xmlToMap
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> convertNodesFromXml(String xml) throws Exception {
		xml = xml.replaceAll("\\<\\!\\[CDATA\\[", "").replaceAll("\\]\\]\\>", "").replaceAll("\n", "").replaceAll("\r", "");
		LOG.info("WeixinUtil convertNodesFromXml, xml:" + xml);
		InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(is);
		return createMap(document.getDocumentElement());
	}
	public static Map<String, String> createMap(Node node) {
		Map<String, String> map = new HashMap<String, String>();
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.hasAttributes()) {
				for (int j = 0; j < currentNode.getAttributes().getLength(); j++) {
					Node item = currentNode.getAttributes().item(i);
					map.put(item.getNodeName(), item.getTextContent());
				}
			}
			if (node.getFirstChild() != null && node.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
				map.putAll(createMap(currentNode));
			} else if (node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
				map.put(node.getLocalName(), node.getTextContent());
			}
		}

		return map;
	}

	/**
	 * Json格式转Map
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> JsonToMap(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = JSONObject.parseObject(jsonStr);
		Set<String> set = json.keySet();
		Iterator<String> keys = set.iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			map.put(key, json.get(key));

		}
		return map;
	}

	/**
	 * 获取微信签名
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @param map
	 * @return
	 */
	public static String getWeiXinSignXml(Map<String, String> map) {
		List<String> keys = new ArrayList<String>();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (map.get(key).toString().isEmpty()) {
				iterator.remove();
			} else {
				keys.add(key);
			}
		}
		Collections.sort(keys);

		StringBuilder sbA = new StringBuilder();
		StringBuilder xmlString = new StringBuilder();
		xmlString.append("<xml>");
		if (keys.size() > 0) {
			for (String key : keys) {
				sbA.append(key).append("=").append(map.get(key)).append("&");
				xmlString.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
			}
			sbA.append("key").append("=").append(ApplicationKey.apiSecret);
		}
		LOG.info("拉起微信支付签名串：" + sbA);
		// step4 Sign 签名 MD5
		String sign = sbA == null ? null : DigestUtils.md5DigestAsHex(sbA.toString().getBytes()).toUpperCase();
		LOG.info("拉起微信支付生成的签名：" + sign);
		xmlString.append("<").append("sign").append(">").append(sign).append("</").append("sign").append(">");
		xmlString.append("</xml>");
		return xmlString.toString();
	}

	/**
	 *  去掉多余的数据
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @param xml
	 * @return
	 */
	public static String repalceCDATA(String xml) {
		xml = xml.replaceAll("\\<\\!\\[CDATA\\[", "").replaceAll("\\]\\]\\>", "").replaceAll("\n", "").replaceAll("\r", "");
		return xml;
	}

	/**
	 * 返回成功xml
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @return
	 */
	public static String returnSuccess() {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<return_code>").append("SUCCESS").append("</return_code>");
		sb.append("<return_msg>").append("OK").append("</return_msg>");
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 返回错误xml
	 * @author shihai.liu
	 * @date 2019年2月13日
	 * @return
	 */
	public static String returnFail() {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<return_code>").append("FAIL").append("</return_code>");
		sb.append("<return_msg>").append("NO").append("</return_msg>");
		sb.append("</xml>");
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {

		String xml = "<xml>\n<appid>wx5345b0f0d9c4dbbc</appid><cash_fee>10</cash_fee><mch_id>1269718101</mch_id><nonce_str>2TGPQDPnliEPddd7</nonce_str><out_refund_no_0>17</out_refund_no_0><out_trade_no>201511091201160594866</out_trade_no><refund_channel_0>ORIGINAL</refund_channel_0><refund_count>1</refund_count><refund_fee>5</refund_fee><refund_fee_0>5</refund_fee_0><refund_id_0>2003960693201511100073999566</refund_id_0><refund_status_0>SUCCESS</refund_status_0><result_code>SUCCESS</result_code><return_code>SUCCESS</return_code><return_msg>OK</return_msg><sign>4188AFEE89CB97569430AA51AFF95E35</sign><total_fee>10</total_fee><transaction_id>1003960693201511091518786446</transaction_id></xml>";
		System.out.println(JSONObject.toJSONString(WeixinUtil.convertNodesFromXml(xml)));
	}
}
