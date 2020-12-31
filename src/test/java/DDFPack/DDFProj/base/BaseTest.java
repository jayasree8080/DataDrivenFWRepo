package DDFPack.DDFProj.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import DDFPack.DDFProj.utils.ExtentManager;
import DDFPack.DDFProj.utils.MyXLSReader;

public class BaseTest {
	
	public WebDriver driver = null;
	public Properties prop = null;
	public MyXLSReader xlsx=null;
	public ExtentReports eReport = null;
	public ExtentTest eTest = null;
	
	String locatorVal=null;
	//Initialization
	
	public void initialise()

	{
		
		//if prop file not loaded already, then load prop file
		if(prop==null)
		{
			prop = new Properties();
			File pcf = new File("src/test/resources/projectconfig.properties");
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream(pcf);
				prop.load(fis);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

	public void openBrowser(String browserType)
	{  
		eTest.log(LogStatus.INFO , "Opening the browser "+browserType);
		
		
		if(browserType.equalsIgnoreCase("firefox"))
		{
		System.setProperty("webdriver.gecko.driver", prop.getProperty("firefoxDriverPath"));
		driver = new FirefoxDriver();
		}else if((browserType.equalsIgnoreCase("chrome")))
				{
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromeDriverPath"));
			 driver = new ChromeDriver();
				}
		
		eTest.log(LogStatus.INFO , "Browser "+browserType+" got opened ");
		driver.manage().window().maximize();
		
		eTest.log(LogStatus.INFO , "Browser  got maximized ");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}
	
	public void navigate(String urlKey)
	{
		String url =prop.getProperty(urlKey);
		driver.get(url);
		
		eTest.log(LogStatus.INFO ,"Navigated to "+prop.getProperty(urlKey));
	}
	
	public boolean doLogin(String username,String password)
	{
		//click on the login link - click()
		click("Loginlink_classname");
		
		type("EmailTextBox_id",username);
		type("passTextBox_id",password);
		click("signinBtn_id");
		if (isElementPresent("CRMOption_cssselector"))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public void click(String locatorkey)
	{
		WebElement element=getElement(locatorkey);
		element.click();
		
		eTest.log(LogStatus.INFO, locatorkey + " got clicked ");
	}
	
	public void type(String locatorkey,String text)
	{
		
		WebElement element=getElement(locatorkey);
		element.sendKeys(text);
		
		eTest.log(LogStatus.INFO, text + " got typed into the "+locatorkey);
	}
	
	public boolean isElementPresent(String locatorkey)
	{
		WebElement element =getElement(locatorkey);
		
		boolean elementDisplayStatus = element.isDisplayed();
		
		return elementDisplayStatus;
	}
	public WebElement getElement(String locatorkey)
	{   
		System.out.println("coming to get Element");
		WebElement element = null;
		 locatorVal=prop.getProperty(locatorkey);
		try
		{
			System.out.println("coming to get Element try block");
			
			
			System.out.println("locator key "+locatorkey);
			System.out.println("locator value "+locatorVal);
			if(locatorkey.endsWith("_classname"))
			{
				System.out.println("above coming ");
			 element = driver.findElement(By.className(locatorVal));
				
			 System.out.println("coming ");
			} else if (locatorkey.endsWith("_id"))
				
			{
				element =driver.findElement(By.id(locatorVal));
			} else if (locatorkey.endsWith("_name"))
			{
				element =driver.findElement(By.name(locatorVal));
			} else if (locatorkey.endsWith("_linktext"))
			{
				element =driver.findElement(By.linkText(locatorVal));
			} else if (locatorkey.endsWith("cssselector"))
			{
				element =driver.findElement(By.cssSelector(locatorVal));
			} else if (locatorkey.endsWith("_xpath"))
			{
				System.out.println("coming to xpath ");
				element =driver.findElement(By.xpath(locatorVal));
				System.out.println("coming xpath after");
			}
		}
		catch(Throwable t)
		{
			reportFail(locatorkey +" holding the "+locatorVal+" is not findable");
		}
		return element;
		
		
	}
	
	public void reportPass(String testName)
	{
		eTest.log(LogStatus.PASS, testName );
	}
	
	public void reportFail(String testName)
	{
		eTest.log(LogStatus.FAIL, testName);
		//Take screenshots
		takeScreenShots();
		Assert.fail(testName );
	}
	
	public void takeScreenShots()
	{
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("screenshots//"+screenshotFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//put screenshot file in reports
		eTest.log(LogStatus.INFO,"Screenshot-> "+ eTest.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));

	}
	
}
