/**
 * http://fonkost.ru
 */
package ru.fonkost.driverHelper;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Фабрика по созданию драйверов браузеров.
 */
public class DriverFactory {
    public static final int timeout = 30;

    /**
     * Возвращает драйвер браузера Firefox
     *
     * @return драйвер
     */
    public static WebDriver GetDriver() {
	return GetDriver(Browser.Firefox);
    }

    /**
     * Возвращает драйвер в зависимости от браузера
     *
     * @param timeout
     *            лимит ожидания элементов
     * @return драйвер
     */
    public static WebDriver GetDriver(Browser browser) {
	WebDriver driver = null;
	switch (browser) {
	case Firefox:
	    driver = new FirefoxDriver();
	    break;
	case Phantom:
	    driver = new PhantomJSDriver();
	    break;
	case Chrome:
	    driver = new ChromeDriver();
	    break;
	default:
	    driver = new FirefoxDriver();
	    break;
	}

	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	return driver;
    }
}