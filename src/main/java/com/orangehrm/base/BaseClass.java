package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
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
    //  protected static WebDriver driver;
    //  private static ActionDriver actionDriver;

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

//    public  WebDriver getDriver() {
//        return driver;
//    }


    public static WebDriver getDriver() {
        if (driver.get() == null) {
            System.out.println("Webdriver not initialized");
            throw new IllegalStateException("Webdriver is not initialized");
        }
        return driver.get();
    }

    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            System.out.println("Actiondriver not initialized");
            throw new IllegalStateException("Actiondriver is not initialized");
        }
        return actionDriver.get();
    }


    public void setDriver(ThreadLocal<WebDriver> driver) {
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
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.prop file loaded");

    }

    @BeforeMethod
    public void setup() throws IOException {

        System.out.println("Setting up the Webdriver for : " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);
        logger.info("Webdriver initiated and window maximized");

        // Initialize the action driver only once
//        if (actionDriver == null){
//            actionDriver = new ActionDriver(driver);
//           logger.info("Action Driver instance is created");
//
//        }
        // Initialize the action driver for the current thread

        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver initialized for thread : " + Thread.currentThread().getName());


    }

    // Initialize the webdriver based on browser defined in config.properties
    private void launchBrowser() {

        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            // driver = new ChromeDriver();
            driver.set(new ChromeDriver());
            logger.info("Chrome driver instance is created");
        } else if (browser.equalsIgnoreCase("firefox")) {
            // driver = new FirefoxDriver();
            driver.set(new FirefoxDriver());
            logger.info("Firefox driver instance is created");
        } else {
            throw new IllegalArgumentException("incorrect browser selected");
        }
    }

    // Configure Browser with implicit wait and maximize the browser
    private void configureBrowser() {
        // implicit wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        getDriver().manage().window().maximize();

        //Navigate to URL
        try {
            getDriver().get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("url is inaccessible");
        }
    }


    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                logger.info("Unable to quit driver");
            }
        }
        logger.info("Closing Webdriver instance");
        //driver = null;
        // actionDriver = null;
        driver.remove();
        actionDriver.remove();
    }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

}
