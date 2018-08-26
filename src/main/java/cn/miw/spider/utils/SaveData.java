package cn.miw.spider.utils;

import com.alibaba.fastjson.JSONArray;

import cn.miw.spider.utils.ICatTask.ICatCallBack;

public class SaveData implements ICatCallBack {

	@Override
	public void catAPage(Object flag, long page, JSONArray pageResult) {
		System.out.println(flag+"=="+page+"===\r\n"+pageResult);
	}

	@Override
	public void catFin(Object flag, long pages, long count) {
		System.out.println(flag+"=="+pages+"==="+count);
	}

//	public static void main(String[] args) {
//		ICatTask task = new CatHTML("", 0, 100, 1, 5000, "", "", null);
//		task = new CatJSON("", 0, 100, 1, 5000, "", "", null);
//		task.start(new SaveData());
//	}
}
