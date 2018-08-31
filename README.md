# spiderlib
### 使用样例
```java
// type=0 为爬取HTML数据；type=1 派去API 数据
ICatTask task;
if (taskinfo.getType() == 0) {
	task = new CatHTML(taskinfo.getFlag(), taskinfo.getPage(), taskinfo.getFin(), taskinfo.getStep(),
			taskinfo.getDelay(), taskinfo.getUrl(), taskinfo.getListSelector(), aDefs);
	if (taskinfo.getEnaleJS() == 1) {
		((CatHTML) task).enaleJS();
	}
	// 爬取API 数据
} else {
	task = new CatJSON(taskinfo.getFlag(), taskinfo.getPage(), taskinfo.getFin(), taskinfo.getStep(),
			taskinfo.getDelay(), taskinfo.getUrl(), taskinfo.getListSelector(), def);
}
SaveData saveAction = new SaveData();
task.start(saveAction);

String url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?tpl=3&page=detail&date=2018-08-24&topid=4&type=top&song_begin= {{page}}&song_num=30&g_tk=5381&jsonpCallback=MusicJsonCallbacktoplist&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
ICatTask task = new CatJSONP("QQ音乐", 0, 1000, 30, 2000, url, "songlist", null, "MusicJsonCallbacktoplist(", ")");
		
SaveData saveAction = new SaveData();
task.start(saveAction);
```
