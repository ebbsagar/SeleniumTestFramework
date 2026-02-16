package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {



    protected static Properties prop;
    protected static WebDriver driver;
    private static ActionDriver actionDriver;

//    public  WebDriver getDriver() {
//        return driver;
//    }


   public static WebDriver getDriver() {
       if(driver == null){
           System.out.println("Webdriver not initialized");
           throw new IllegalStateException("Webdriver is not initialized");
       }
       return driver;
   }

    public static ActionDriver getActionDriver() {
        if(actionDriver == null){
            System.out.println("Actiondriver not initialized");
            throw new IllegalStateException("Actiondriver is not initialized");
        }
        return actionDriver;
    }



    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public static Properties getProp() {
        return prop;
    }

    public static void setProp(Properties prop) {
        BaseClass.prop = prop;
    }

    @BeforeSuite
    // Load the config file
    public void loadConfig() throws IOException {

        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

    }

    @BeforeMethod
    public void setup() throws IOException{

        System.out.println("Setting up the Webdriver for : " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);

        actionDriver = new ActionDriver(driver);
    // Initialize the action driver only once
        if (actionDriver == null){

            System.out.println("Action Driver instance is created");

        }

    }

    // Initialize the webdriver based on browser defined in config.properties
    private void launchBrowser() {

        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException("incorrect browser selected");
        }
    }

    // Configure Browser with implicit wait and maximize the browser
    private void configureBrowser() {
        // implicit wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().window().maximize();

        //Navigate to URL
        try {
            driver.get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("url is inaccessible");
        }
    }


    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Unable to quit driver");
            }
        }
        System.out.println("Closing Webdriver instance");
        driver = null;
        actionDriver = null;
    }

    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

}
