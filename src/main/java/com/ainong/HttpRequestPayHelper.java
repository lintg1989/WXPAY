package com.ainong;



import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.*;


public class HttpRequestPayHelper implements IHttpRequestHelper {
	private String apiHost = null;
	private static final String[] base64Keys = new String[] { "subject", "body", "remark","respMsg","resv" };
	private static final String[] base64JsonKeys = new String[] { "paras" };
	public static final Set<String> base64Key = new HashSet<String>();
//	private static Logger logger = Logger.getLogger(HttpRequestPayHelper.class);
	
	public HttpRequestPayHelper(String apiHost) {
		this.apiHost = apiHost;
	}

	@Override
	public Map<String, String> sendGet(String apiName, Map<String, String> paras, String encoding, int sendType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> sendPost(String apiName, Map<String, String> paras, String encoding, int sendType) {
		// TODO Auto-generated method stub
		putCommArgs(paras);
		TreeMap<String, String> treeMap = new TreeMap<>();
		treeMap.putAll(paras);
		try {
			String result = HttpRequest.doPost(apiHost, treeMap, encoding, sendType);
			System.out.println("返回原报文："+result);
//			logger.info("返回原报文："+result);
			return buildResult(result,encoding);
		} catch (Exception e) {
//			logger.error("调用失败！", e);
			System.out.println("调用失败！"+ e);
		}
		return null;
	}

	@Override
	public Map<String, String> sendPut(String apiName, Map<String, String> paras, String encoding, int sendType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean sendDelete(String apiName, Map<String, String> paras, String encoding, int sendType) {
		// TODO Auto-generated method stub
		return null;
	}

	private void putCommArgs(Map<String, String> paras) {
		
	}
	

	private Map<String, String> buildResult(String jsonString, String encoding) {
		Map<String, String> result = new HashMap<>();
		String[] args = jsonString.split("&");

		for (String string : args) {
			String[] tmp = string.split("=");
			String key = tmp[0];
			String value =tmp.length<2?"":tmp[1];
			if(key.equals("respMsg")||key.equals("resv")){
				try {
					value = new String(Base64.getDecoder().decode(value.getBytes()), encoding);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			result.put(key, value);
		}
		return result;
	}

	public static void buildAndTransResult(String jsonString){
		Map<String, String> result = new HashMap<>();
		String[] args = jsonString.split("&");

		for (String string : args) {
			String[] tmp = string.split("=");
			String key = tmp[0];
			String value =tmp.length<2?"":tmp[1];
			result.put(key, value);
		}
		buildAndTransResultMap(result);
	}
	public static void buildAndTransResultMap(Map<String, String> paras){
		for (String key : base64Keys) {
			if (paras.containsKey(key)) {
				paras.put(key, new String(Base64.getDecoder().decode(paras.get(key).getBytes())));
			}
		}
		for (String key : base64JsonKeys) {
			if (paras.containsKey(key)) {
				String values = paras.get(key).replace("\r", "").replace("\n", "");
				String bavalue = "";
				try {
					bavalue = new String(Base64.getDecoder().decode(values.getBytes()), "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				paras.put(key, bavalue);
			}
		}
	}
	
}
