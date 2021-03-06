package selenium;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import domain.ReviewAttach;
import domain.Menu;
import domain.MenuAttach;
import domain.Review;
import domain.Store;
import domain.StoreAttach;

public class Selenium {
	private WebDriver driver;
	
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_RATH = "D:/devtools/chromdriver_win32/chromedriver.exe"; // academy
	
//	public static final String WEB_DRIVER_RATH = "E:/devtools/chromedriver/chromedriver.exe"; // home
	
	private String base_url;
	
	public Selenium() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_RATH);
		
		driver = new ChromeDriver();
		base_url = "https://www.yogiyo.co.kr/mobile/?gclid=EAIaIQobChMIgbuUjoev9wIVT1lgCh0LhQl4EAAYASAAEgLaG_D_BwE#/";
	}
	
	public void crawl(int category, int storeNum) throws Exception {
		String path = "D:\\photo"; // academy
//		String path = "E:\\photo"; // home
		String path2 = "\\logo";
		List<Store> stores = new ArrayList<Store>();
		List<Menu> menus = new ArrayList<Menu>();
		List<Review> reviews = new ArrayList<Review>();
		List<StoreAttach> storeAttachs = new ArrayList<StoreAttach>();
		List<MenuAttach> menuAttachs = new ArrayList<MenuAttach>();
		List<ReviewAttach> reviewAttachs = new ArrayList<ReviewAttach>();
		
		Long sno = 120L;
		Long mno = 30000L;
		Long rno = 6000L;
		
		
		driver.get(base_url);
		Thread.sleep(2000);
		String urlCategory = driver.getCurrentUrl();
		for( ; category < 11 ; category++) {
			
			
			driver.findElement(By.cssSelector(".category-list > div.row > div:nth-child(" + category + ")")).click();
			Thread.sleep(2000);
			String urlStore = driver.getCurrentUrl();
			
			for(int i = 1; i <= storeNum ; i++) {
				Store store = new Store();
				StoreAttach storeAttach = new StoreAttach();
				
				store.setCategory(Long.parseLong(category-4+""));// ????????????
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("window.scrollBy(0,5000)", "");
				Thread.sleep(2000);
				executor.executeScript("window.scrollBy(0,-5000)", "");
				Thread.sleep(2000);
				
				WebElement clsContent = driver.findElement(By.id("content")).findElements(By.cssSelector(".content > div")).get(3);
				
				Thread.sleep(2000);
				WebElement clsContent2 = clsContent.findElements(By.cssSelector(".restaurant-list > div")).get(i);
				clsContent2.findElement(By.tagName("div")).click();
				Thread.sleep(2000);
				
				String storeName = driver.findElement(By.cssSelector(".restaurant-name")).getText(); // ????????????
				store.setName(storeName);
				String storeStar = driver.findElement(By.cssSelector(".stars")).getText().substring(6); // ??????
				store.setScope(Double.parseDouble(storeStar));
				String delTime = driver.findElement(By.cssSelector(".delivery-time-tooltip span")).getText().substring(0, 2); // ????????????
				store.setDelTime(delTime);
				String minPrice = driver.findElement(By.cssSelector(".discount-desc + li span")).getText(); // ??????????????????
				
				while(minPrice.contains(",")) {
					int d = minPrice.indexOf(",");
					String result = minPrice.substring(0, d);
					String result2 = minPrice.substring(d+1);
					minPrice = result.concat(result2);
				}
				while(minPrice.contains("???")) {
					int d = minPrice.indexOf("???");
					String result = minPrice.substring(0, d);
					String result2 = minPrice.substring(d+1);
					minPrice = result.concat(result2);
				}
				store.setMinPrice(Long.parseLong(minPrice));
				// ?????? ????????????
				String delPrice = driver.findElement(By.cssSelector(".restaurant-cart [ng-show='cart.get_delivery_fee(restaurant) > 0']")).getText();
				if(delPrice.length() > 3) {
					delPrice = delPrice.substring(5);
				}
				while(delPrice.contains(",")) {
					int e = delPrice.indexOf(",");
					String result = delPrice.substring(0, e);
					String result2 = delPrice.substring(e+1);
					delPrice = result.concat(result2);
				}
				if(delPrice.contains("???")) {
					int f = delPrice.indexOf("???");
					delPrice = delPrice.substring(0, f);
				}
				store.setDelPrice(delPrice);
				
				String logo = driver.findElement(By.cssSelector(".restaurant-info .restaurant-content")).findElement(By.cssSelector("[ng-style]")).getAttribute("style");
				int a = logo.indexOf("url(");
				int b = logo.indexOf(")");
				logo = logo.substring(a+5, b-1);
				
				
				System.out.println(logo);
				UUID logoUuid = UUID.randomUUID();
				
				
				
				// ?????? ????????????
				driver.findElement(By.linkText("??????")).click();
				Thread.sleep(2000);
				
				WebElement infomation = driver.findElement(By.id("info"));
				
				List<WebElement> infoList = infomation.findElements(By.cssSelector(".info-item"));
				
				// ???????????????, ????????????, ????????????, ???????????????, ???????????????
				String notice = infoList.get(0).findElement(By.cssSelector("[ng-bind-html='introduction_text()|strip_html']")).getText(); // ????????? ??????
				store.setNotice(notice);
				String time = infoList.get(1).findElement(By.cssSelector("span")).getText(); // ????????????
				String startTime = time.substring(0, 5);
				String endTime = time.substring(8);
				store.setStartTime(startTime);
				store.setEndTime(endTime);
				
				String tel = infoList.get(1).findElement(By.cssSelector("[ng-show='restaurant.phone.length > 0'] span")).getText(); // ????????????
				store.setTel(tel);
				String address = infoList.get(1).findElement(By.cssSelector("[ng-show='restaurant.address.length > 0'] span")).getText(); // ??????
				store.setAddress(address);
				String bno = infoList.get(3).findElement(By.cssSelector("[ng-bind='restaurant_info.crmdata.company_number']")).getText(); // ?????????????????????
				store.setBno(bno);
				String originInfo = infoList.get(4).findElement(By.cssSelector("pre")).getText(); // ???????????????
				store.setOriginInfo(originInfo);
				saveFile(path, path2, encode(logo), logoUuid.toString());
				store.setSno(++sno);
				storeAttach.setUuid(logoUuid.toString());
				storeAttach.setPath(path + path2);
				storeAttach.setOrigin(logo);
				storeAttach.setSno(store.getSno());
				
				stores.add(store);
				storeAttachs.add(storeAttach);
				
//				saveDBStore(store, storeAttach);
				
//				saveDBStoreAttach(storeAttach);
				
				driver.findElements(By.cssSelector("ul.restaurant-tab li")).get(0).click();
				Thread.sleep(2000);
				
				int k = driver.findElement(By.id("menu")).findElements(By.cssSelector(".panel-group > .panel")).size();
				for(int u = 2 ; u < k-1 ; u++) {
					
					
					
					WebElement clsContent3 = driver.findElement(By.id("menu")).findElements(By.cssSelector(".panel-group > .panel")).get(u);
					if(u >= 2) {clsContent3.findElement(By.cssSelector(".panel-heading")).click();}
					int l = clsContent3.findElements(By.cssSelector("ul.sub-list > li")).size();
					for(int j = 0 ; j < l ; j++) {
						Menu menu = new Menu();
						MenuAttach menuAttach = new MenuAttach();
						menu.setSno(store.getSno());
						WebElement clscontent4 = clsContent3.findElements(By.cssSelector("ul.sub-list > li")).get(j);
						menu.setName(clscontent4.findElement(By.cssSelector("table .menu-name")).getText()); // ????????????
						menu.setInfo(clscontent4.findElement(By.cssSelector("table .menu-desc")).getText()); // ????????????
						
						String menuPrice = clscontent4.findElement(By.cssSelector("table .menu-price")).getText();
						while(menuPrice.contains(",")) {
							int p = menuPrice.indexOf(",");
							String result = menuPrice.substring(0, p);
							String result2 = menuPrice.substring(p+1);
							menuPrice = result.concat(result2);
						}
						while(menuPrice.contains("???")) {
							int p = menuPrice.indexOf("???");
							String result = menuPrice.substring(0, p);
							String result2 = menuPrice.substring(p+1);
							menuPrice = result.concat(result2);
						}
						if(menuPrice.length() > 5) {
							continue;
						}
						if(!menuPrice.isEmpty()){
							menu.setPrice(Long.parseLong(menuPrice)); // ????????????
						}
						else {
							menu.setPrice(0L);
						}
						
						String imgUrl = clscontent4.findElement(By.cssSelector(".photo")).getAttribute("style"); // ????????????
//						System.out.println(imgUrl);
						menu.setMno(++mno);
//						menuAttach.setMno(menu.getMno());
						int st = imgUrl.indexOf("url(");
						int en = imgUrl.indexOf(")");
						imgUrl = imgUrl.substring(st+5, en-1);
//						System.out.println(imgUrl);
						menus.add(menu);
						if(!imgUrl.isEmpty()){
							UUID menuUuid = UUID.randomUUID();
							path2 = "\\menu";
							
							saveFile(path, path2, encode(imgUrl), menuUuid.toString());
							menuAttach.setUuid(menuUuid.toString());
							menuAttach.setMno(menu.getMno());
							menuAttach.setPath(path + path2);
							menuAttach.setOrigin(imgUrl);
							menuAttachs.add(menuAttach);
//							saveDBMenu(menu, menuAttach);
//							saveDBMenuAttach(menuAttach);
						}
						
						
						
					}
				}
				
				
				
				
				// ????????????
				driver.findElements(By.cssSelector(".restaurant-tab li")).get(1).click();
				Thread.sleep(2000);
				
				driver.findElement(By.cssSelector("label[for='cmn-toggle']")).click(); // ?????? ?????? ????????? ?????????
				Thread.sleep(2000);
				try {
					driver.findElement(By.cssSelector("[ng-click='get_next_reviews()'")).click();
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				
				WebElement reviewInfo = driver.findElement(By.id("review"));
				List<WebElement> reviewList = reviewInfo.findElements(By.cssSelector("li.star-point"));
				for(int j = 0; j < reviewList.size() ; j++) {
					Review review = new Review();
					String reviewer = reviewList.get(j).findElement(By.cssSelector("[ng-show='review.phone']")).getText();
					
					String img = reviewList.get(j).findElement(By.cssSelector("img")).getAttribute("src");
					String comment = reviewList.get(j).findElement(By.cssSelector("[ng-show='review.comment']")).getText();
					List<WebElement> points =  reviewList.get(j).findElements(By.cssSelector(".points"));
					double sum = 0;
					for(WebElement el : points) {
						if(!el.getText().isEmpty()) {
							sum += Double.parseDouble(el.getText());
						}
					}
					String point = ((int)(sum / points.size() * 10) / 10d) + "";
					
					review.setContent(comment);
					review.setId(reviewer);
					review.setSno(store.getSno());
					review.setLikes(Double.parseDouble(point));
					review.setRno(++rno);
					review.setGroupId(review.getRno());
					
					UUID reviewUuid = UUID.randomUUID();
					path2 = "\\review";
					
					saveFile(path, path2, encode(img), reviewUuid.toString());
					ReviewAttach reviewAttach = new ReviewAttach();
					reviewAttach.setUuid(reviewUuid.toString());
					reviewAttach.setOrigin(img);
					reviewAttach.setPath(path + path2);
					reviewAttach.setRno(review.getRno());
					
					reviews.add(review);
					reviewAttachs.add(reviewAttach);
					
					
					//??????????????? ???????????????
					
					try {
						reviewList.get(j).findElements(By.cssSelector(".review-answer"));
						Review review2 = new Review();
						review2.setId("?????????");
						review2.setContent(reviewList.get(j).findElement(By.cssSelector(".review-answer")).findElement(By.cssSelector("p")).getText());
						review2.setGroupId(review.getRno());
						review2.setRno(++rno);
						review2.setSno(store.getSno());
						review2.setLikes(0d);
						reviews.add(review2);
					} catch (Exception e) {
					}
					
//					saveDBReview(review, reviewAttach);
//					saveDBReviewAttach(reviewAttach);
					path2 = "\\logo";
				}
				System.out.println(category-4 + " ??? ????????????" + i + " ?????? ?????? ????????? ??????");
				System.out.println("?????? : " + stores.size() + " ???, ?????? ?????? : " + storeAttachs.size() + " ???");
				System.out.println("?????? : " + menus.size() + " ???, ?????? ?????? : " + menuAttachs.size() + " ???");
				System.out.println("?????? : " + reviews.size() + " ???, ?????? ?????? : " + reviewAttachs.size() + " ???");
				driver.get(urlStore);
				Thread.sleep(2000);
			}
			driver.get(urlCategory);
			Thread.sleep(2000);
		}
		driver.close();
		saveDBStore(stores, storeAttachs);
		saveDBMenu(menus, menuAttachs);
		saveDBReview(reviews, reviewAttachs);
		
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		Selenium crawl = new Selenium();
		crawl.crawl(8, 40);
		
	}

	static void saveDBMenu(List<Menu> menus, List<MenuAttach> attachs) throws Exception {
		DBConn DBConn = new DBConn();
//		PreparedStatement pstmt = DBConn.getConnection().prepareStatement(
//				"SELECT SEQ_MENU.NEXTVAL FROM DUAL");
//		ResultSet rs = pstmt.executeQuery();
//		rs.next();
//		menu.setMno(rs.getLong(1));
//		pstmt.close();
		
		PreparedStatement pstmt = DBConn.getConnection().prepareStatement(
				"INSERT INTO TBL_MENU VALUES(?, ?, ?, ?, ?, '1')");
		
		for(Menu menu : menus) {
			int idx = 1;
			pstmt.setLong(idx++, menu.getSno());
			pstmt.setLong(idx++, menu.getMno());
			pstmt.setString(idx++, menu.getName());
			pstmt.setLong(idx++, menu.getPrice());
			pstmt.setString(idx++, menu.getInfo());
			
			pstmt.executeUpdate();
		}
		pstmt.close();
		
		pstmt = DBConn.getConnection().prepareStatement(
				"INSERT INTO TBL_MENU_ATTACH VALUES(?, SEQ_ATTACH.NEXTVAL, ?, ?, ?, ?)");
		for(MenuAttach attach : attachs) {
			int idx2 = 1;
			pstmt.setLong(idx2++, attach.getMno());
			pstmt.setString(idx2++, attach.getUuid());
			pstmt.setString(idx2++, attach.getOrigin());
			pstmt.setString(idx2++, attach.getPath());
			pstmt.setString(idx2++, attach.getRegDate());
			
			pstmt.executeUpdate();
		}
		pstmt.close();
		
		
		DBConn.getConnection().close();
			
	}
	static void saveDBReview(List<Review> reviews, List<ReviewAttach> attachs) throws Exception {
		DBConn DBConn = new DBConn();
//		PreparedStatement pstmt = DBConn.getConnection().prepareStatement(
//				"SELECT SEQ_REVIEW.NEXTVAL FROM DUAL");
//		ResultSet rs = pstmt.executeQuery();
//		rs.next();
//		review.setRno(rs.getLong(1));
//		pstmt.close();
		
		PreparedStatement pstmt = DBConn.getConnection().prepareStatement(
				"INSERT INTO TBL_REVIEWS(RNO, ID, SNO, CONTENT, SCOPE, GROUPID) VALUES(?, ?, ?, ?, ?, ?)");
		
		for(Review review : reviews) {
			int idx = 1;
			pstmt.setLong(idx++, review.getRno());
			pstmt.setString(idx++, review.getId());
			pstmt.setLong(idx++, review.getSno());
			pstmt.setString(idx++, review.getContent());
			pstmt.setDouble(idx++, review.getLikes());
			pstmt.setLong(idx++, review.getGroupId());
			
			pstmt.executeUpdate();
		}
		pstmt.close();
		
		pstmt = DBConn.getConnection().prepareStatement(
				"INSERT INTO TBL_REVIEWS_ATTACH VALUES(?, SEQ_ATTACH.NEXTVAL, ?, ?, ?, ?)");
		
		for(ReviewAttach attach : attachs) {
			int idx2 = 1;
			pstmt.setLong(idx2++, attach.getRno());
			pstmt.setString(idx2++, attach.getUuid());
			pstmt.setString(idx2++, attach.getOrigin());
			pstmt.setString(idx2++, attach.getPath());
			pstmt.setString(idx2++, attach.getRegDate());
			
			pstmt.executeUpdate();
		}
		
		pstmt.close();
		
		DBConn.getConnection().close();
			
	}
	static void saveDBStore(List<Store> stores, List<StoreAttach> attachs) throws Exception {
		DBConn DBConn = new DBConn();
//		PreparedStatement pstmt = DBConn.getConnection().prepareStatement(
//				"SELECT SEQ_STORE.NEXTVAL FROM DUAL");
//		ResultSet rs = pstmt.executeQuery();
//		
//		rs.next();
//		store.setSno(rs.getLong(1));
//		pstmt.close();
		
		PreparedStatement pstmt = DBConn.getConnection().prepareStatement(
				"INSERT INTO TBL_STORE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)");
		
		for(Store store : stores) {
			int idx = 1;
			
			pstmt.setLong(idx++, store.getCategory());
			pstmt.setString(idx++, store.getAddress());
			pstmt.setString(idx++, store.getTel());
			pstmt.setString(idx++, store.getStartTime());
			pstmt.setString(idx++, store.getEndTime());
			pstmt.setString(idx++, store.getOriginInfo());
			pstmt.setLong(idx++, store.getMinPrice());
			pstmt.setString(idx++, store.getDelTime());
			pstmt.setDouble(idx++, store.getScope());
			pstmt.setInt(idx++, 10);
			pstmt.setInt(idx++, 0);
			pstmt.setString(idx++, store.getBno());
			pstmt.setLong(idx++, store.getSno());
			pstmt.setString(idx++, store.getNotice());
			pstmt.setString(idx++, store.getDelPrice());
			pstmt.setString(idx++, store.getName());
			
			pstmt.executeUpdate();
			
		}
		pstmt.close();
		
		pstmt = DBConn.getConnection().prepareStatement(
				"INSERT INTO TBL_STORE_ATTACH VALUES(?, SEQ_ATTACH.NEXTVAL, ?, ?, ?, ?)");
		for(StoreAttach attach : attachs) {
			int idx2 = 1;
			pstmt.setLong(idx2++, attach.getSno());
			pstmt.setString(idx2++, attach.getUuid());
			pstmt.setString(idx2++, attach.getOrigin());
			pstmt.setString(idx2++, attach.getPath());
			pstmt.setString(idx2++, attach.getRegDate());
			
			pstmt.executeUpdate();
			
		}
		
		pstmt.close();
		DBConn.getConnection().close();
			
	}
	
	
	public static String encode(String str) {
		char[] chs = str.toCharArray();
		String result = "";
		for(char c : chs) {
			try {
				result += c >= '???' && c <= '???' ? URLEncoder.encode(c+"", "utf-8") : c;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	static void saveFile(String path, String path2, String imgSrc, String uuid){
		try {
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
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
