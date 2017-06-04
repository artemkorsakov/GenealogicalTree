/**
 * Артём Корсаков 
 * site: http://fonkost.ru
 * email: artemkorsakov@mail.ru
 */
package ru.fonkost.driverHelper;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Фабрика по созданию драйверов браузеров.
 */
public class DriverFactory {

    /**
     * Возвращает драйвер браузера Firefox.
     *
     * @param timeout
     *            лимит ожидания элементов
     * @return драйвер
     */
    public static WebDriver GetDriver(int timeout) {
	WebDriver driver = new FirefoxDriver();
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	return driver;
    }
}