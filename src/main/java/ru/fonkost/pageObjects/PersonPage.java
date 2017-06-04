/**
 * Артём Корсаков 
 * site: http://fonkost.ru
 * email: artemkorsakov@mail.ru
 */
package ru.fonkost.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
    public Person GetPerson() {
	Person person = new Person();
	String url = driver.getCurrentUrl();
	String name = driver.findElement(By.cssSelector("#firstHeading")).getText();
	person.setUrl(url);
	person.setName(name);
	return person;
    }
}