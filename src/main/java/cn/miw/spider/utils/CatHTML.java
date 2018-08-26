package cn.miw.spider.utils;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CatHTML implements ICatTask{
	private Object flag = "智联招聘";
	private long page = 0, fin = 1;
	private int step = 100;
	private int delay = 5000;
	private String url = "https://www.zhaopin.com/citymap?x={{page}}";
	private String listSelector = ".cities-show_lists li";
	private AttDef[] attrs = new AttDef[] { new AttDef("a", "href", false, "href"), new AttDef("", "", true, "city") };

	private boolean enabelJS=false;
	public CatHTML enaleJS(){
		this.enabelJS = true;
		return this;
	}
	
//	public static void main(String[] args) throws IOException {
//		new CatHTML("云南慈善", 1, 1, 1, 5000, "http://w.m.miw.cn/pro", ".item ul",
//				new AttDef[] { new AttDef("img:eq(0)", "abs:src", false, "img"), new AttDef("h3.proName", "", true, "name") })
//						.enaleJS().start(new SaveData());
//	}

	public CatHTML(Object flag, long page, long fin, int step, int delay, String url, String listSelector,
			AttDef[] attrs) {
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

	public void start(ICatCallBack callBack){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					do {
						String thisUrl = url.replace("{{page}}", page + "");
						catAPage(thisUrl, callBack);
						page += step;
						Thread.sleep(delay);
					} while (page < fin);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (callBack != null) {
					callBack.catFin(flag, page, count);
				}
			}
		}).start();
	}

	private long count = 0;

	protected void catAPage(String url, ICatCallBack callBack) throws IOException {
		Document doc = enabelJS ? Client.getDocument(url) : Client.JSoupGetDocument(url);
		doc = PreProcess(doc);
		Elements list = doc.select(listSelector);
		JSONArray result = new JSONArray();
		for (Element item : list) {
			JSONObject o = new JSONObject();
			for (AttDef def : attrs) {
				String v;
				if (def.selector != null && def.selector.trim().length() > 0) {
					Element x = item.select(def.selector).first();
					if (x != null) {
						v = def.isText || def.attrName == null || def.attrName.trim().length() == 0 ? x.text()
								: x.attr(def.attrName);
					} else {
						v = null;
					}
				} else {
					v = def.isText || def.attrName == null || def.attrName.trim().length() == 0 ? item.text()
							: item.attr(def.attrName);
				}
				if (v != null)
					o.put(def.target, v);
			}
			if (o.size() > 0){
				count++;
				result.add(o);
			}
		}
		if (callBack != null) {
			callBack.catAPage(flag, page, result);
		}
	}

	static public class AttDef {
		String selector, attrName, target;
		boolean isText;

		public AttDef(String selector, String attrName, boolean isText, String target) {
			super();
			this.selector = selector;
			this.attrName = attrName;
			this.target = target;
			this.isText = isText;
		}

	}
}
