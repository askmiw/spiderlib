package cn.miw.spider.utils;

import com.alibaba.fastjson.JSONArray;

public interface ICatTask {

	void start(ICatCallBack callBack);
	

	public interface ICatCallBack {
		void catAPage(Object flag, long page, JSONArray pageResult);

		void catFin(Object flag, long pages, long count);
	}
	
	default<T> T PreProcess(T text) {
		return text;
	}
}
