package DDFPack.DDFProj.tests;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import DDFPack.DDFProj.base.BaseTest;
import DDFPack.DDFProj.utils.DataUtil;
import DDFPack.DDFProj.utils.ExtentManager;
import DDFPack.DDFProj.utils.MyXLSReader;

public class LoginTest extends BaseTest 
{

	 
	@BeforeClass
	public void init()
	{
		initialise();
	}
	
	@DataProvider
	public Object[][] getData() throws Exception 
	{
		String filepath = prop.getProperty("xlsxFilePath");
		
		 xlsx = new MyXLSReader(filepath);
		
		Object[][] testData= DataUtil.getTestData(xlsx, "LoginTest", "Data");
		
		return testData;
	}
	
	
	@Test(dataProvider="getData")
	public void doLoginTest(HashMap<String,String> map)
	{
		 eReport = ExtentManager.getInstance();
		 eTest = eReport.startTest("LoginTest");
		eTest.log(LogStatus.INFO, "Login test started");
		
		if(!DataUtil.isRunnable(xlsx,"LoginTest", "Testcases") || map.get("Runmode").equals("N"))
		{
			eTest.log(LogStatus.INFO, "Skipping the test as Runmode is set to N");
			throw new SkipException("Skipping the test as Runmode is set to N");
		}
		
		//Automation code 
		
		//open the browser after getting data from xlsx
		openBrowser(map.get("Browser"));
		
		//navigate to url
		navigate("appURL");
		
		boolean actualResult=doLogin(map.get("Username"),map.get("Password"));
		
		String expectedRes=map.get("ExpectedResult");
		
		boolean expectedResult = false;
		
		if(expectedRes.equalsIgnoreCase("Failure"))
		{
			expectedResult = false;
		} else if(expectedRes.equalsIgnoreCase("Success"))
		{
			expectedResult = true;
		}
		
		if(actualResult==expectedResult)
		{
			//eTest.log(LogStatus.PASS , "doLoginTest got passed");
			
			reportPass("LoginTest got passed ");
		} else
		{
			//eTest.log(LogStatus.FAIL , "doLoginTest got failed");
			//Assert.fail("doLoginTest got failed");
			
			reportFail("LoginTest got failed ");
		}
			
	}

	@AfterMethod
	public void testClosure()
	{
		if(eReport!=null)
		{		
		eReport.endTest(eTest);
		eReport.flush();
		}
		if(driver!=null)
		{	
		driver.quit();
		}
	}
}
