/**
 * http://fonkost.ru
 */
package ru.fonkost.pageObjects;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ru.fonkost.driverHelper.DriverFactory;

/**
 * Общая страница
 *
 * @author Артём Корсаков
 */
abstract class Page {
    protected WebDriver driver;

    protected Page(WebDriver driver) {
	this.driver = driver;
    }

    /**
     * Возвращает true, если текущий урл содержит "якорь"
     */
    protected boolean IsAnchor() {
	return driver.getCurrentUrl().contains("#");
    }

    /**
     * Возвращает значение "якоря"
     */
    protected String GetAnchor() {
	int idx = driver.getCurrentUrl().indexOf("#");
	return idx == -1 ? "" : driver.getCurrentUrl().substring(idx + 1);
    }

    /**
     * Возвращает список элементов
     * 
     * @return
     */
    protected List<WebElement> GetElements(By by) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> result = driver.findElements(by);
	driver.manage().timeouts().implicitlyWait(DriverFactory.timeout, TimeUnit.SECONDS);
	return result;
    }

    /**
     * Возвращает true, если текущий элемент - уточняющая ссылка
     */
    protected boolean IsSup(WebElement element) {
	String parentTagName = element.findElement(By.xpath(".//..")).getTagName();
	return parentTagName.equals("sup");
    }
}