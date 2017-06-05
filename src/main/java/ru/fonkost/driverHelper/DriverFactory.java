/**
 * http://fonkost.ru
 */
package ru.fonkost.driverHelper;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Фабрика по созданию драйверов браузеров.
 */
public class DriverFactory {
    public static final int timeout = 30;

    /**
     * Возвращает драйвер браузера Firefox.
     *
     * @param timeout
     *            лимит ожидания элементов
     * @return драйвер
     */
    public static WebDriver GetDriver() {
	WebDriver driver = new FirefoxDriver();
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	return driver;
    }
}