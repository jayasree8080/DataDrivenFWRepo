package DDFPack.DDFProj.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class TestDemo {
	
	WebDriver driver = null;
	@Test
	public void testingmethod() throws InterruptedException
	{
		
		//System.setProperty("webdriver.chrome.driver", "D://Jay//selenium_details//softwares//chromedriver.exe");
		
		//driver = new ChromeDriver();
		
		//driver.get("https://www.google.com/");
		
		System.setProperty("webdriver.gecko.driver", "D://Jay//selenium_details//softwares//geckodriver.exe");	
        driver = new FirefoxDriver();
		
		driver.get("https://www.zoho.com/");
		Thread.sleep(2000);
		driver.findElement(By.className("zh-login")).click();
		
		
	}

}
