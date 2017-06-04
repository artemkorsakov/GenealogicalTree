/**
 * Артём Корсаков 
 * site: http://fonkost.ru
 * email: artemkorsakov@mail.ru
 */
package ru.fonkost.pageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ru.fonkost.entities.Person;

/**
 * Страница, посвященная историческому лицу.
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
    }

    /**
     * Возвращает экземпляр класса Person с данными личности, на странице
     * которой находимся в текущий момент.
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
     * Возвращает список урлов страниц детей личности
     *
     * @return the list
     */
    public List<String> GetChildrensUrl() {
	List<WebElement> childrensLinks = driver
		.findElements(By.xpath("//table[@class='infobox']//tr[th[.='Дети:']]//a"));
	List<String> childrens = new ArrayList<String>();

	for (WebElement childrenLink : childrensLinks) {
	    String url = childrenLink.getAttribute("href");
	    childrens.add(url);
	}

	return childrens;
    }
}