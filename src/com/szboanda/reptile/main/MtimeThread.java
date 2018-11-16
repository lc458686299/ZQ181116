package com.szboanda.reptile.main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.szboanda.reptile.db.SqlControl;
import com.szboanda.reptile.modal.PollutionPollutionInfo;
import com.szboanda.reptile.parse.MtimeParse;
import com.szboanda.reptile.util.Config;
import com.szboanda.reptile.util.Property;


public class MtimeThread extends Thread{
	private String starturl= "";
//	private static List<PollutionPollutionInfo> ppis = new ArrayList<>();
	public MtimeThread(String starturl){
		this.starturl=starturl;
	}
	@Override
	public void run() {
		PollutionPollutionInfo ppi = MtimeParse.getBean(starturl);
		//这里先获取网页代码
		String html = MtimeParse.getHtmlStr(starturl);
		if(ppi != null){
		//	ppis.add(ppi);
			SqlControl.save(ppi);
			MtimeParse.loadZB(html,Config.PDF_ZB_PATH,ppi.getXkzbh()+".pdf");
			MtimeParse.loadFB(html, Config.PDF_FB_PATH,ppi.getXkzbh());
		
		}
	}
	
	public static void main(String[] args) {
		new Property().loadProperty();
		List<String> urls = getUrls();
		 //创建固定大小的线程池
        ExecutorService exec = Executors.newFixedThreadPool(9);
		for(int i=0;i<urls.size();i++){
			exec.execute(new MtimeThread(urls.get(i)));
		}
		//线程关闭
		exec.shutdown();
		/*try {
			exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			System.out.println(ppis.size());
			SqlControl.executeUpdate(ppis);
			
		} catch (InterruptedException e) {
		  
		}*/
	}
	
	private static List<String> getUrls(){
		List<String> urls = new ArrayList<>();
		for(int i=1;i<=65;i++){
			System.out.println(i+"--page");
			try {
				Document doc = Jsoup.connect("http://permit.mee.gov.cn/permitExt/outside/Publicity?pageno="+i+"&enterName=&province=430000000000&city=&treadcode=&treadname=&xkznum=").
						userAgent("bbb").timeout(120000).get();
				Elements ets= doc.getElementsByClass("bgcolor1");
				for(int j=0;j<ets.size();j++){
					String url = ets.get(j).getElementsByTag("a").get(0).attr("href");
					if(url != null){
						urls.add("http://permit.mee.gov.cn"+url);
					}				
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(urls.size());
		return urls;
	}
}
