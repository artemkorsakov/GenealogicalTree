/** http://fonkost.ru */
package ru.fonkost.pageObjects;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ru.fonkost.driverHelper.DriverHelper;
import ru.fonkost.entities.Person;

/**
 * Страница исторического лица на Wikipedia. Например,
 * <a href="https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA">Рюрика
 * </a>
 */
public class WikipediaPage extends PersonPage {
    WikipediaPage(WebDriver driver) {
	this.driver = driver;
    }

    /**
     * {@link ru.fonkost.pageObjects.PersonPage#getPerson(String) see parent
     * doc}
     */
    @Override
    public Person getPerson(String url) throws MalformedURLException {
	driver.navigate().to(url);

	String name = getName();

	Person person = new Person(driver.getCurrentUrl());
	person.setName(name);
	return person;
    }

    /**
     * {@link ru.fonkost.pageObjects.PersonPage#getChildrenUrl() see parent doc}
     */
    @Override
    public List<Person> getChildrenUrl() throws MalformedURLException {
	waitLoadPage();

	if (DriverHelper.hasAnchor(driver)) {
	    return new ArrayList<Person>();
	}

	List<WebElement> childrenLinks = getChildrenLinks();
	List<Person> children = new ArrayList<Person>();
	for (WebElement link : childrenLinks) {
	    if (DriverHelper.isSup(link)) {
		continue;
	    }
	    Person person = new Person(link.getAttribute("href"));
	    person.setNameUrl(link.getText());
	    if (person.isCorrectNameUrl()) {
		children.add(person);
	    }
	}
	return children;
    }

    private String getName() throws MalformedURLException {
	waitLoadPage();
	String namePage = driver.findElement(By.cssSelector("#firstHeading")).getText();

	if (!DriverHelper.hasAnchor(driver)) {
	    return namePage;
	}

	String anchor = DriverHelper.getAnchor(driver);
	List<WebElement> list = DriverHelper.getElements(driver, By.id(anchor));

	if (list.size() == 0) {
	    return namePage;
	}

	String name = list.get(0).getText().trim();
	return name.isEmpty() ? namePage : name;
    }

    private List<WebElement> getChildrenLinks() {
	List<WebElement> childrenLinks = DriverHelper.getElements(driver, By.xpath(
		"//table[contains(@class, 'infobox')]//tr[th[.='Дети:']]//a[not(@class='new' or @class='extiw')]"));
	return childrenLinks;
    }

    private void waitLoadPage() {
	this.driver.findElement(By.cssSelector("#firstHeading"));
    }
}