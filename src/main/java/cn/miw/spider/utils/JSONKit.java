package cn.miw.spider.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONKit {

	static public JSONArray getArray(JSONObject o, String key) {
		String[] keys = key.split("\\.");
		if (keys.length > 1) {
			JSONObject json = o;
			for (int i = 0; i < keys.length - 1; i++) {
				json = json.getJSONObject(keys[i]);
			}
			return json.getJSONArray(keys[keys.length - 1]);
		} else {
			return o.getJSONArray(key);
		}
	}

	static public Object get(JSONObject o, String key) {
		String[] keys = key.split("\\.");
		if (keys.length > 1) {
			JSONObject json = o;
			for (int i = 0; i < keys.length - 1; i++) {
				json = json.getJSONObject(keys[i]);
			}
			return  json.get(keys[keys.length - 1]);
		} else {
			return o.get(key);
		}
	}
	
}
