package com.ainong;

import java.util.Map;

public interface IHttpRequestHelper {
	Map<String,String> sendGet(String apiName, Map<String, String> paras, String encoding, int sendType);
	Map<String,String> sendPost(String apiName, Map<String, String> paras, String encoding, int sendType);
	Map<String, String> sendPut(String apiName, Map<String, String> paras, String encoding, int sendType);
	Boolean sendDelete(String apiName, Map<String, String> paras, String encoding, int sendType);
}
