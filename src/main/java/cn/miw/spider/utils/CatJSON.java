package cn.miw.spider.utils;

import java.io.IOException;

import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CatJSON implements ICatTask{
	private Object flag = "智联招聘";
	private long page = 0, fin = 40000;
	private int step = 100;
	private int delay = 5000;
	private String url = "https://fe-api.zhaopin.com/c/i/sou?pageSize=100&cityId=831&workExperience=-1&education=-1&companyType=-1&employmentType=-1&jobWelfareTag=-1&kt=3&start={{page}}";
	private String listSelector = "data.results";
	private String attrs = null;// "id:id,number:seq,jobType:jobType,company.name:comp,positionURL:jobUrl,jobName,welfare";

	public CatJSON(Object flag, long page, long fin, int step, int delay, String url, String listSelector,
			String attrs) {
		super();
		this.flag = flag;
		this.page = page;
		this.fin = fin;
		this.step = step;
		this.delay = delay;
		this.url = url;
		this.listSelector = listSelector;
		this.attrs = attrs;
	}

	public void start(ICatCallBack callBack) {
		catPage(flag, url, page, step, fin, listSelector, attrs, delay, callBack);
	}

	private void catPage(Object flag, String sourceUrl, long startPage, long step, long fin, String listSelector,
			String attrs, int delay, ICatCallBack callBack) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				long page = startPage;
				do {
					String url = sourceUrl.replace("{{page}}", page + "");
					Document doc;
					try {
						doc = Client.JSoupGetDocument(url);
						System.out.println(doc.text());
						JSONArray result = parseList(PreProcess(doc.text()), listSelector, attrs);
						if (callBack != null)
							callBack.catAPage(flag, page, result);
						page = page + step;
						pages++;
						Thread.sleep(delay);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (page < fin);
				if (callBack != null)
					callBack.catFin(flag, pages, count);
			}
		}).start();
	}

	private JSONArray parseList(String jsonStr, String list, String attrs) {
		JSONObject json = JSONObject.parseObject(jsonStr);
		JSONArray result = new JSONArray();
		JSONArray data = JSONKit.getArray(json, list);
		for (Object d : data) {
			JSONObject j = (JSONObject) d;
			JSONObject item = j;
			if (attrs != null && attrs.trim().length() > 0) {
				item = new JSONObject();
				for (String key : attrs.split(",")) {
					String k = key.trim();
					String s = k;
					if (key.contains(":")) {
						k = key.split(":")[0].trim();
						s = key.split(":")[1].trim();
					}
					// System.out.println(s + "\t==>" + JSONKit.get(j, k));
					item.put(s, JSONKit.get(j, k));
				}
			}
			result.add(item);
		}
		count += result.size();
		return result;
	}

	private long count = 0;
	private long pages = 0;
}
