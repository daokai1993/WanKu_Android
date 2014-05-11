package com.wanku.tool;

import org.json.JSONArray;

public class SimpleQueryJson {
	public JSONArray getSimpleJson(String url) throws Exception{
		
		SyncHttp syncHttp = new SyncHttp();
		String retStr = syncHttp.httpGet(url);

		JSONArray array = new JSONArray(retStr);
		return array;
	}
	public JSONArray getSimpleJson(String url, String params) throws Exception {
		SyncHttp syncHttp = new SyncHttp();
		
		String retStr = syncHttp.httpGet_params(url, params);
		JSONArray array = new JSONArray(retStr);
		return array;
	
		
	}
}
