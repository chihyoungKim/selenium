package selenium;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {
	private WebDriver driver;
	
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_RATH = "E:/devtools/chromedriver/chromedriver.exe";
	
	private String base_url;
	
	public SeleniumTest() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_RATH);
		
		driver = new ChromeDriver();
		base_url = "https://www.yogiyo.co.kr/mobile/?gclid=EAIaIQobChMIgbuUjoev9wIVT1lgCh0LhQl4EAAYASAAEgLaG_D_BwE#/";
	}
	
	public void crawl() throws Exception {
		driver.get(base_url);
//		System.out.println(driver.getPageSource());
		driver.findElement(By.name("address_input")).click();
//		new Actions(driver).pause(1000);
		Thread.sleep(1000);
		driver.findElement(By.linkText("현재 위치로 설정합니다.")).click();
		Thread.sleep(2000);
		
		
		WebElement clsContent = driver.findElement(By.id("content")).findElements(By.cssSelector(".content > div")).get(3);
		List<WebElement> infos = clsContent.findElements(By.cssSelector(".restaurant-list > div table .restaurants-info"));
		
		if(infos.size() < 70) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("window.scrollBy(0,10240)", "");
			Thread.sleep(2000);
			infos = clsContent.findElements(By.cssSelector(".restaurant-list > div table .restaurants-info"));
		}

		
//		System.out.println(driver.getPageSource());
//		driver.findElement(By.id("content")).findElements(By.cssSelector(".content")).get(0);
		
		for(WebElement el : infos) {
			System.out.println(el.getText());
		}
		
		// 총 가게 몇개고
		System.out.println(infos.size());
//		name = "address_input"
	}
	
	public void crawl2(int c) throws Exception {
		driver.get(base_url);
		
		// 3~14까지 있음 1은 할인정보고 2는 전체보기로 되어있다.
		driver.findElement(By.cssSelector(".category-list > div.row > div:nth-child(" + c + ")")).click();
		Thread.sleep(1000);
		
		
		WebElement clsContent = driver.findElement(By.id("content")).findElements(By.cssSelector(".content > div")).get(3);
		List<WebElement> infos = clsContent.findElements(By.cssSelector(".restaurant-list > div table .restaurants-info"));
		
		if(infos.size() < 70) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("window.scrollBy(0,10240)", "");
			Thread.sleep(2000);
			infos = clsContent.findElements(By.cssSelector(".restaurant-list > div table .restaurants-info"));
		}

		int cnt = 0;
		
		for(int i = 1 ; cnt < 70 ; i++) {
			System.out.println(infos.get(i).getText());
			cnt++;
		}
		System.out.println(cnt);
		driver.close();
		
	}
	public void crawl3(int c, int d) throws Exception { // 가게상세정보
		driver.get(base_url);
		Thread.sleep(5000);

		driver.findElement(By.cssSelector(".category-list > div.row > div:nth-child(" + c + ")")).click();
		Thread.sleep(5000);
		
		WebElement clsContent = driver.findElement(By.id("content")).findElements(By.cssSelector(".content > div")).get(3);
		List<WebElement> infos = clsContent.findElements(By.cssSelector(".restaurant-list > div table .restaurants-info"));
		
		if(infos.size() < 70) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("window.scrollBy(0,10240)", "");
			Thread.sleep(5000);
			infos = clsContent.findElements(By.cssSelector(".restaurant-list > div table .restaurants-info"));
		}
		
		driver.findElement(By.cssSelector("#content > .content > div:nth-child(4) div:nth-child(" + d + ") > div > table")).click();
		Thread.sleep(5000);
		

		
		List<WebElement> infos2 = new ArrayList<WebElement>();
		
		driver.findElement(By.linkText("정보")).click();
		Thread.sleep(5000);
		
		infos2 = driver.findElement(By.id("info")).findElements(By.cssSelector(".info-item > *"));
		
//		int k = driver.findElement(By.id("menu")).findElements(By.cssSelector(".panel-group > .panel")).size();
//		
//		for(int i = 1 ; i < k-1 ; i++) {
//			WebElement clsContent2 = driver.findElement(By.id("menu")).findElements(By.cssSelector(".panel-group > .panel")).get(i);
//			if(i >= 2) {clsContent2.findElement(By.cssSelector(".panel-heading")).click();}
//			int l = clsContent2.findElements(By.cssSelector("ul.sub-list > li")).size();
//			for(int j = 0 ; j < l-1 ; j++) {
//				WebElement clscontent3 = clsContent2.findElements(By.cssSelector("ul.sub-list > li")).get(j);
//				infos2.addAll(clscontent3.findElements(By.cssSelector("table .menu-name, table .menu-price")));
//			}
//		}
		for(WebElement el2 : infos2) {
			System.out.println(el2.getText());
		}
		
		
		
//		driver.close();
	}
	
	public static void main(String[] args) throws Exception {
//		for(int i = 3 ; i <= 14 ; i++) {
//			new SeleniumTest().crawl2(i);
//		}
//		new SeleniumTest().crawl2(3);
		new SeleniumTest().crawl3(3, 2);
		
	}
	
}
