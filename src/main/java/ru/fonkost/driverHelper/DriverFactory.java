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
 * Фабрика по созданию драйверов. Браузеры запускаются только через эту фабрику.
 */
public final class DriverFactory {
    public static final int timeout = 30;

    /**
     * Возвращает драйвер по-умолчанию (Default)
     *
     * @return драйвер
     */
    public static WebDriver GetDriver() {
	return GetDriver(Browser.Default);
    }

    /**
     * Возвращает драйвер в зависимости от заданного браузера Если передан null,
     * то возвращается драйвер по-умолчанию
     *
     * @param timeout
     *            лимит ожидания элементов
     * @return драйвер
     */
    public static WebDriver GetDriver(Browser browser) {
	if (browser == null) {
	    browser = Browser.Default;
	}

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
	    driver = new ChromeDriver();
	    break;
	}

	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	return driver;
    }
}