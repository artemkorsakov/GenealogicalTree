/**
 * http://fonkost.ru
 */
package ru.fonkost.pageObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ru.fonkost.driverHelper.DriverFactory;
import ru.fonkost.entities.Person;

/**
 * Страница исторического лица.
 *
 * @author Артём Корсаков
 */
public class PersonPage {
    private WebDriver driver;

    /**
     * Instantiates a new person page.
     *
     * @param driver
     *            the driver
     */
    public PersonPage(WebDriver driver) {
	this.driver = driver;
	WaitLoadPage();
    }

    /**
     * Возвращает экземпляр класса Person с данными персоны, на странице которой
     * находимся в текущий момент.
     *
     * @return the person
     */
    public Person GetPerson() throws Exception {
	String url = driver.getCurrentUrl();
	String name = driver.findElement(By.cssSelector("#firstHeading")).getText();
	Person person = new Person(name, url);
	return person;
    }

    /**
     * Возвращает список урлов страниц детей персоны
     *
     * @return the list
     */
    public List<String> GetChildrensUrl() {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> childrensLinks = driver.findElements(
		By.xpath("//table[@class='infobox']//tr[th[.='Дети:']]//a[not(@class='new' or @class='extiw')]"));
	driver.manage().timeouts().implicitlyWait(DriverFactory.timeout, TimeUnit.SECONDS);

	List<String> childrens = new ArrayList<String>();

	for (WebElement childrenLink : childrensLinks) {
	    String parentTagName = childrenLink.findElement(By.xpath(".//..")).getTagName();
	    if (parentTagName.equals("sup")) {
		continue;
	    }

	    String name = childrenLink.getText();
	    Pattern p = Pattern.compile("^[\\D]+.+");
	    Matcher m = p.matcher(name);
	    if (!m.matches()) {
		continue;
	    }

	    String url = childrenLink.getAttribute("href");
	    childrens.add(url);
	}

	return childrens;
    }

    /**
     * Ожидание загрузки страницы
     */
    private void WaitLoadPage() {
	this.driver.findElement(By.cssSelector("#firstHeading"));
    }
}