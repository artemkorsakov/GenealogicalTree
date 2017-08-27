/** http://fonkost.ru */
package ru.fonkost.driverHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/** Статичный класс для описания дополнительных методов Selenium WebDriver */
public final class DriverHelper {
    private static final int TIMEOUT = 30;

    /**
     * Возвращает ChromeDriver для управления браузером Chrome.
     * 
     * Используется браузер Chrome, потому что он быстрее Firefox и других
     * браузеров. Быстрее Chrome только PhantomJs, но он не используется из-за
     * сложности отладки из-за отсутствия браузера.
     */
    public static WebDriver getDriver() {
	WebDriver driver = new ChromeDriver();
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
	return driver;
    }

    /** Возвращает true, если текущий урл содержит "якорь" */
    public static boolean hasAnchor(WebDriver driver) throws MalformedURLException {
	URL url = new URL(driver.getCurrentUrl());
	return url.getRef() != null;
    }

    /** Возвращает значение "якоря" */
    public static String getAnchor(WebDriver driver) throws MalformedURLException {
	URL url = new URL(driver.getCurrentUrl());
	return url.getRef();
    }

    /**
     * Возвращает список элементов без ожидания их появления.<br>
     * По умолчанию установлено неявное ожидание - это значит, что если на
     * странице нет заданных элементов, то пустой результат будет выведен не
     * сразу, а через таймаут, что приведет к потере времени. Чтобы не терять
     * время создан этот метод, где неявное ожидание обнуляется, а после поиска
     * восстанавливается.
     */
    public static List<WebElement> getElements(WebDriver driver, By by) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> result = driver.findElements(by);
	driver.manage().timeouts().implicitlyWait(DriverHelper.TIMEOUT, TimeUnit.SECONDS);
	return result;
    }

    /**
     * Возвращает true, если текущий элемент -
     * <a href="http://htmlbook.ru/html/sup">уточняющая ссылка</a>
     */
    public static boolean isSup(WebElement element) {
	String parentTagName = element.findElement(By.xpath(".//..")).getTagName();
	return parentTagName.equals("sup");
    }
}