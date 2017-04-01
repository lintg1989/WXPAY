package com.ainong;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

public class HttpRequest {

	private static final String SERVLET_POST = "POST";
	private static final String SERVLET_GET = "GET";
	private static final String SERVLET_DELETE = "DELETE";
	private static final String SERVLET_PUT = "PUT";
	private static final String REQUEST_SEC = "MD5";
	private static final String[] base64Keys = new String[] { "subject", "body", "remark" };
	private static final String[] base64JsonKeys = new String[] { "customerInfo", "accResv", "riskRateInfo",
			"billQueryInfo", "billDetailInfo" };
	private static final String payKey = "7YxEXknepy22F7sVazSHuMbsuTzKcwTe";
//	protected static Logger logger = Logger.getLogger(HttpRequest.class);
	public static String doPost(String urlStr, TreeMap<String, String> paramMap, String charSet, int sendType)
			throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_POST);

		String paramStr = prepareParam(paramMap, sendType);
//		logger.info("请求报文:" + paramStr);
		System.out.println("请求报文:" + paramStr);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(paramStr.toString().getBytes(charSet));
		os.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charSet));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		br.close();
		return result;
	}

	public static String doGet(String urlStr, TreeMap<String, String> paramMap, String charSet, int sendType)
			throws Exception {
		String paramStr = prepareParam(paramMap, sendType);
		if (paramStr == null || paramStr.trim().length() < 1) {

		} else {
			urlStr += "?" + paramStr;
		}
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_GET);
		setUrlconnection(conn, charSet, sendType);
		conn.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		br.close();
		return result;
	}

	public static String doPut(String urlStr, TreeMap<String, String> paramMap, String charSet, int sendType)
			throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_PUT);

		String paramStr = prepareParam(paramMap, sendType);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(paramStr.toString().getBytes(charSet));
		os.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		br.close();
		return result;
	}

	public static String doDelete(String urlStr, TreeMap<String, String> paramMap, String charSet, int sendType)
			throws Exception {
		String paramStr = prepareParam(paramMap, sendType);
		if (paramStr == null || paramStr.trim().length() < 1) {

		} else {
			urlStr += "?" + paramStr;
		}
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(SERVLET_DELETE);

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		br.close();
		return result;
	}

	private static String prepareParam(TreeMap<String, String> paramMap, int sendType) {
		CheckSign(paramMap);
		String result = "";
		StringBuffer sb = new StringBuffer();
		if (paramMap.isEmpty()) {
			return "";
		} else {
			for (String key : paramMap.keySet()) {
				String value = (String) paramMap.get(key);
				if (value == null || value.isEmpty()) {
					continue;
				}
				if (key.equals("user-agent")) {
					try {
						value = URLEncoder.encode(value, "utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					value = URLEncoder.encode(value, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (sb.length() < 1) {
					sb.append(key).append("=").append(value);
				} else {
					sb.append("&").append(key).append("=").append(value);
				}
			}
			result = sb.toString();
		}
		// result+="7YxEXknepy22F7sVazSHuMbsuTzKcwTe";
		System.err.println(result);
		return result;
	}

	private static void setUrlconnection(HttpURLConnection connection, String charSet, int sendType) {
		// 设置通用的请求属性
		if (charSet == null || charSet.isEmpty()) {
			charSet = "UTF-8";
		}
		connection.setRequestProperty("Charsert", charSet);
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		switch (sendType) {
		case 1:// Text

			break;
		case 2:// XML
			connection.setRequestProperty("Content-Type", "text/xml;");
			break;
		case 3:// Json
			break;
		default:
			break;
		}
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

	}

	private static void CheckSign(TreeMap<String, String> treeMap) {
		String signString = "";
		Iterator<String> sgIt = treeMap.keySet().iterator();

		while (sgIt.hasNext()) {
			String key = (String) sgIt.next();
			String value = (String) treeMap.get(key);
			if (key.equals("sign")) {
				continue;
			}
			if (value == null || value.isEmpty()) {
				continue;
			}
			if (signString.isEmpty()) {
				signString = key + "=" + value;
			} else {
				signString = signString + "&" + key + "=" + value;
			}
		}
		signString += payKey;
		// System.err.println(signString.toLowerCase());
		byte[] checkSign = null;
		try {
			switch (REQUEST_SEC) {
			case "MD5":
				checkSign = MD5Util.encodebyte(signString.getBytes("utf-8"));
				break;
			default:
				checkSign = MD5Util.encodebyte(signString.getBytes("utf-8"));
				break;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println(signString);
		for (String key : base64Keys) {
			if (treeMap.containsKey(key)) {
				treeMap.put(key, Base64.getEncoder().encodeToString(treeMap.get(key).getBytes()));
			}
		}
		for (String key : base64JsonKeys) {
			if (treeMap.containsKey(key)) {
				treeMap.put(key, Base64.getEncoder().encodeToString(treeMap.get(key).getBytes()));
			}
		}
		treeMap.put("signMethod", "MD5");
		treeMap.put("signature", Base64.getEncoder().encodeToString(checkSign));
	}

	public static boolean VerifySign(Map<String, String> paramMap) {

		String signMethod = (String) paramMap.get("signMethod");
		String signature = (String) paramMap.get("signature");
		TreeMap<String, String> treeMap = new TreeMap<>();
		treeMap.putAll(paramMap);
		// 计算签名
		Set<String> removeKey = new HashSet<String>();
		removeKey.add("signMethod");
		removeKey.add("signature");
		String signString = "";
		byte[] checkSign = null;
		
		for (String key : removeKey) {
			treeMap.remove(key);
		}
		Iterator<String> sgIt = treeMap.keySet().iterator();

		while (sgIt.hasNext()) {
			String key = (String) sgIt.next();
			String value = (String) treeMap.get(key);
			if (key.equals("sign")) {
				continue;
			}
			if (value == null || value.isEmpty()) {
				continue;
			}
			if (signString.isEmpty()) {
				signString = key + "=" + value;
			} else {
				signString = signString + "&" + key + "=" + value;
			}
		}
		String chsign = "";
		if (signature == null || signature.isEmpty() || signMethod == null || signMethod.isEmpty()) {
			return false;
		}
//		byte[] sibyte = Base64.getDecoder().decode(signature);
		try {
			switch (signMethod.toUpperCase()) {
			case "MD5":
				checkSign = MD5Util.encodebyte(signString.getBytes("UTF-8"));
				break;
			default:
				checkSign = MD5Util.encodebyte(signString.getBytes("UTF-8"));
				break;
			}
			chsign = new String(checkSign, "UTF-8");
//			signature = new String(sibyte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block

		}
//		logger.info(signString);
//		logger.info("爱农回调参数校验：signature："+signature+"   checkSign:"+chsign+"  校验通过："+chsign.equals(signature));
		return chsign.equals(signature);
	}

	public String sendPost(String url, String param) {
		// PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		OutputStreamWriter out = null;
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			byte[] xmlData = param.getBytes();
			out.write(new String(xmlData));
			out.flush();
			out.close();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		return result;
	}

	public String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
		// String ss = ChangeUnicode.getEncoding(result);
		return result;
	}
}
