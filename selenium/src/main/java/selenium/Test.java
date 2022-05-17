package selenium;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class Test {
	public static void main(String[] args) throws Exception {
		String str = "https://rev-static.yogiyo.co.kr/franchise_logos/이삭토스트_20191021_Franchise_crop_200x200.jpg";
		
		System.out.println(encode(str));
		

		String url = "https://rev-static.yogiyo.co.kr/25ab5bf9e468fc380f5fb0fdd9eba76d_tn.jpg";
		String url2 = "https://rev-static.yogiyo.co.kr/franchise_logos/%EC%9D%B4%EC%82%AD%ED%86%A0%EC%8A%A4%ED%8A%B8_20191021_Franchise_crop_200x200.jpg";
		
		String path = "D:\\photo";
		String path2 = "\\logo";
		
		String uuid = UUID.randomUUID().toString();
		saveFile(path, path2, url2, uuid);
		
	}
	public static String encode(String str) {
		char[] chs = str.toCharArray();
		String result = "";
		for(char c : chs) {
			try {
				result += c >= '가' && c <= '힣' ? URLEncoder.encode(c+"", "utf-8") : c;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	static void saveFile(String path, String path2, String imgSrc, String uuid) throws Exception{
		URL url = new URL(imgSrc);
		
		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		File file = new File(path, path2);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		file = new File(file, uuid + ".jpg");
		
		FileOutputStream fos = new FileOutputStream(file);
		int b = 0;
		while((b=bis.read()) != -1) {
			fos.write(b);
		}
		bis.close();
		fos.close();
		
	}
}
