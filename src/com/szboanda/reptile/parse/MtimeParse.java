package com.szboanda.reptile.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.szboanda.reptile.modal.PollutionPollutionInfo;
import com.szboanda.reptile.util.ImgPdfUtil;


public class MtimeParse {
	public static List<PollutionPollutionInfo> getData(Document doc){
	//	doc.get
		return null;
	}
	
	public static PollutionPollutionInfo getBean (String url){
		try {
			Document doc = Jsoup.connect(url).
					userAgent("bbb").timeout(120000).get();
			Elements ets= doc.getElementsByClass("tab0");
			Element et= ets.get(0);   //网页上只有一个
			Element tr=et.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1);  //表格行
			Elements tds = tr.getElementsByTag("td");
			PollutionPollutionInfo ppi = new PollutionPollutionInfo();
			ppi.setXkzbh(tds.get(0).text().trim());  //许可证编号
			ppi.setYwlx(tds.get(1).text().trim());
			ppi.setBb(tds.get(2).text().trim());
			ppi.setBjrq(tds.get(3).text().trim());
			ppi.setYxqx(tds.get(4).text().trim());
			String gsm=doc.getElementsByTag("table").get(0).getElementsByTag("p").get(0).text();
			ppi.setGsm(gsm);
			//获取第二个表格信息
			Elements trs =doc.getElementById("apply_table").getElementsByTag("tbody").get(0).getElementsByTag("tr");
			ppi.setDqzywrwzl(trs.get(1).getElementsByTag("td").get(0).text().trim());
			ppi.setDqwrwpfzxbz(trs.get(3).getElementsByTag("td").get(0).text().trim());
			ppi.setFszywrwzl(trs.get(4).getElementsByTag("td").get(0).text().trim());
			ppi.setFswrwpfzxbz(trs.get(6).getElementsByTag("td").get(0).text().trim());
			ppi.setPwqshjyxx(trs.get(7).getElementsByTag("td").get(0).text().trim());
			//利用正则获取js加载的信息
			String html = doc.html();
			String zywrwlb=getParamByRegEx(html,"spdataMap_zywrwlbid");
			ppi.setZywrwlb(zywrwlb);
			String dqwrwpfgl=getParamByRegEx(html, "spdataMap_airemissionid");
			ppi.setDqwrwpfgl(dqwrwpfgl);
			String fswrwpfgl=getParamByRegEx(html, "spdataMap_wateremissionname");
			ppi.setFswrwpfgl(fswrwpfgl);
			//获取地区
			String dq = getDqByRegEx(html);
			ppi.setDq(dq);
			return ppi;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据网页获取许可证正本
	 * @param doc 
	 * @param path 下载路径
	 * @param name 文件名
	 * @return
	 */
	public static String loadZB(String html,String path,String name){
		
		String regEx = "href=\"(/permitExt/upanddown\\.do.*)\"";  //正本链接
		Pattern pattern = Pattern.compile(regEx);
		String load_url = null;
		Matcher matcher = pattern.matcher(html);
		if(matcher.find()){
			load_url = matcher.group(1);
	    }else{
	    	System.err.println("没有找到正本链接");
	    }
		  if(load_url != null){
			  load_url = load_url.replace("&amp;", "&");
		    	System.out.println("http://permit.mee.gov.cn"+load_url);
		    	try {
		    		URL _url = new URL("http://permit.mee.gov.cn"+load_url);
		            int byteread = 0;
		            URLConnection conn = _url.openConnection();
		            InputStream inStream = conn.getInputStream();
		            FileOutputStream fs = new FileOutputStream(path+"/"+name);
	
		            byte[] buffer = new byte[1204];
		          
		            while ((byteread = inStream.read(buffer)) != -1) {
	
		                fs.write(buffer, 0, byteread);
		            }
		            inStream.close();
		            fs.close();
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    	return path+name;
		    }
		return null;
	}
	
	/**
	 * 获取许可证副本
	 * @param html
	 * @return
	 */
	public static String loadFB(String html,String path,String name){
		try {
			/*Document doc;
			doc = Jsoup.connect(url).
					userAgent("bbb").timeout(120000).get();*/
			/*String str = doc.html();*/
			String regEx = "href=\"(/permitExt/syssb/wysb/.*)\"";  //副本链接
			String load_url = null;
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(html);
			if(matcher.find()){
				load_url = matcher.group(1);
		    }else{
		    	System.err.println("没有找到副本链接");
		    }
			String fh = getHtmlStr("http://permit.mee.gov.cn/"+load_url);
			List<String> srcs = getFBImgUrl(fh);
			
			//创建文件夹
			File df = new File(path+"/"+name);
			if(!df.exists()){
				df.mkdirs();
			}
			
			for(int i=0;i<srcs.size();i++){
				String imgUrl =  "http://permit.mee.gov.cn/"+srcs.get(i);
				imgUrl = imgUrl.replace("&amp;", "&");
				System.out.println(imgUrl);
				URL _url = new URL(imgUrl);
				
	            int byteread = 0;
	            URLConnection conn = _url.openConnection();
	            InputStream inStream = conn.getInputStream();
	            FileOutputStream fs = new FileOutputStream(path+"/"+name+"/"+String.format("%04d", i)+".jpg");

	            byte[] buffer = new byte[1204];
	          
	            while ((byteread = inStream.read(buffer)) != -1) {

	                fs.write(buffer, 0, byteread);
	            }
	            inStream.close();
	            fs.close();
			}
			//然后转换为图片
			ImgPdfUtil.toPdf(path+"/"+name, path+"/"+name+".pdf");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "";
	}
	
	/**
	 * 根据正则获取参数
	 * @param html  网页代码
	 * @param paramName  参数名
	 * @return
	 */
	public static String getParamByRegEx(String html,String paramName){
		String regEx = paramName+"=\"(.*)\"";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matchar = pattern.matcher(html);
		if(matchar.find()){
			String zs = matchar.group(1);
			//对字符串进行处理
			zs = zs.replace(" ", "").replace("fq", "废气").replace("fs", "废水");
			zs = zs.replace("PFXS_1", "有组织").replace("PFXS_2", "无组织");
			zs = zs.replace("@", ",");
			
			return zs;
		}
		return "";	
	}
	
	/**
	 * 根据正则表达市获取地区
	 * @param html
	 * @return
	 */
	public static String getDqByRegEx(String html){
	
		String regEx = "所在地区：(.*)&nbsp;";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matchar = pattern.matcher(html);
		if(matchar.find()){
			String dq = matchar.group(1);
			//对字符串进行处理
			dq = dq.replace(" ", "").replace("&nbsp;", "");
			return dq;
		}
		return "";
	}
	
	
	
	public static void main(String[] args) {
		try {
			Document doc;
			doc = Jsoup.connect("http://permit.mee.gov.cn/permitExt/xkgkAction!xkgk.action?xkgk=getxxgkContent&dataid=cf035ce281ed4c69a0971cc209580400").
					userAgent("bbb").timeout(120000).get();
			String str = doc.html();
			String regEx = "href=\"(/permitExt/syssb/wysb/.*)\"";  //副本链接
			String load_url = null;
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(str);
			if(matcher.find()){
				load_url = matcher.group(1);
				
		    }else{
		    	System.err.println("没有找到副本链接");
		    }
			String fh = getHtmlStr("http://permit.mee.gov.cn/"+load_url);
			List<String> srcs = getFBImgUrl(fh);
			for(int i=0;i<srcs.size();i++){
				String imgUrl =  "http://permit.mee.gov.cn/"+srcs.get(i);
				imgUrl = imgUrl.replace("&amp;", "&");
				System.out.println(imgUrl);
				URL _url = new URL(imgUrl);
				
	            int byteread = 0;
	            URLConnection conn = _url.openConnection();
	            InputStream inStream = conn.getInputStream();
	            FileOutputStream fs = new FileOutputStream("D:/xkzfb/"+String.format("%04d", i)+".jpg");

	            byte[] buffer = new byte[1204];
	          
	            while ((byteread = inStream.read(buffer)) != -1) {

	                fs.write(buffer, 0, byteread);
	            }
	            inStream.close();
	            fs.close();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取网页图片副本链接
	 * @param html
	 * @return
	 */
	private static List<String> getFBImgUrl(String html){
		List<String> srcs = new ArrayList<>();
		String regEx = "<img.* src=\"(.*)\">";  //副本链接
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(html);
		while(matcher.find()){
			srcs.add(matcher.group(1));
	    }
	    System.err.println("结束");
		return srcs;
	}

	
	/**
	 * 根据url获取html代码字符串
	 * @param url
	 * @return
	 */
	public static String getHtmlStr(String url){
		Document doc;
		try {
			doc = Jsoup.connect(url).
					userAgent("bbb").timeout(120000).get();
			return doc.html();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
