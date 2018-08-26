package cn.miw.spider.utils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class Client {

	public static Document getDocument(String url) throws IOException {
		return getDocument(url, url);
	}
	public static Document JSoupGetDocument(String url) throws IOException {
		return Jsoup.connect(url).ignoreContentType(true).get();
	}

	private static final int TIMEOUT = 1000*30;
	private static Set<Cookie> cookies;

	public static Document getDocument(String url, String baseUrl) throws IOException {
		System.out.println("==========================================================================");
		System.out.println("抓取 链接:" + url);
		System.out.println("==========================================================================");
		URL link = new URL(url);
		WebRequest request = new WebRequest(link);
		request.setCharset(Charset.forName("UTF-8"));
		request.setAdditionalHeader("Referer", baseUrl);
		request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
		// 构造一个webClient 模拟Chrome 浏览器
		WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setTimeout(TIMEOUT);
		if(cookies==null){
			webClient.getPage(baseUrl);
			cookies = webClient.getCookieManager().getCookies();
		}
		if (cookies != null){
			for(Cookie cookie : cookies){
				webClient.getCookieManager().addCookie(cookie);
			}
		}
		HtmlPage rootPage = webClient.getPage(request);
		Set<Cookie> tmpcookies = webClient.getCookieManager().getCookies();
		if (tmpcookies != null && tmpcookies.size() > 0){
			cookies = tmpcookies;
		}
		webClient.waitForBackgroundJavaScript(TIMEOUT);
		String html = rootPage.asXml();
		webClient.close();
		Document document = Jsoup.parse(html, baseUrl);
		return document;
	}
}
