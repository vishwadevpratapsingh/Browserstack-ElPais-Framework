package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import utils.PropertyReader;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    public ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return driverThread.get();
    }
    //Sets up the WebDriver for BrowserStack with desired capabilities
    @Parameters({"browserName", "os", "osVersion", "deviceName", "realMobile"})
    @BeforeMethod
    public void setup(@Optional("Chrome") String browserName,
                      @Optional("Windows") String os,
                      @Optional("10") String osVersion,
                      @Optional("") String deviceName,
                      @Optional("false") String realMobile) {

    	 boolean runLocally = false;
    	  try {
              if (runLocally) {
                  System.out.println("Running test locally on: " + browserName);
                  if (browserName.equalsIgnoreCase("Chrome")) {
                      System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
                      driverThread.set(new ChromeDriver());
                  } else {
                      throw new RuntimeException("Unsupported local browser: " + browserName);
                  }
              } else {
        try {
            String username = PropertyReader.get("bs.username");
            String accessKey = PropertyReader.get("bs.access_key");

            DesiredCapabilities caps = new DesiredCapabilities();
            Map bstackOptions = new HashMap<>();

            caps.setCapability("browserName", browserName);
            if ("true".equalsIgnoreCase(realMobile)) {
                bstackOptions.put("deviceName", deviceName);
                bstackOptions.put("realMobile", true);
                bstackOptions.put("osVersion", osVersion);
            } else {
                bstackOptions.put("os", os);
                bstackOptions.put("osVersion", osVersion);
            }

            bstackOptions.put("projectName", PropertyReader.get("bs.project"));
            bstackOptions.put("buildName", PropertyReader.get("bs.build"));
            bstackOptions.put("sessionName", "Parallel Run - " + browserName);
            bstackOptions.put("userName", username);
            bstackOptions.put("accessKey", accessKey);

            caps.setCapability("bstack:options", bstackOptions);

            String remoteUrl = String.format("https://%s:%s@hub.browserstack.com/wd/hub", username, accessKey);

            System.out.println("Setting up BrowserStack WebDriver");
            driverThread.set(new RemoteWebDriver(new URI(remoteUrl).toURL(), caps));
        } catch (Exception e) {
            throw new RuntimeException("WebDriver setup failed", e);
        }
              }
          } catch (Exception e) {
              e.printStackTrace();
              throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage());
          }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
        }
    }
}
