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
 * <a href="https://www.wikidata.org/wiki/Q7990">Рюрика </a>
 */
public class WikidataPage extends PersonPage {
    private final By namePageBy = By.cssSelector("#firstHeading");
    private final By nameRuBy = By
	    .xpath("//div[@class='wikibase-labelview'][@lang='ru']//span[@class='wikibase-labelview-text']");
    private final By childrenLinks = By
	    .xpath("//div[@id='P40']//div[contains(@class, 'wikibase-statementview-mainsnak')]//a");

    WikidataPage(WebDriver driver) {
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
	List<WebElement> childrenEl = DriverHelper.getElements(driver, childrenLinks);
	List<Person> children = new ArrayList<Person>();
	for (WebElement el : childrenEl) {
	    Person person = new Person(el.getAttribute("href"));
	    person.setNameUrl(el.getText());
	    children.add(person);
	}
	return children;
    }

    private String getName() throws MalformedURLException {
	String namePage = driver.findElement(namePageBy).getText();
	List<WebElement> list = DriverHelper.getElements(driver, nameRuBy);
	if (list.size() == 0) {
	    return namePage;
	}
	String name = list.get(0).getText().trim();
	return name.isEmpty() ? namePage : name;
    }

    private void waitLoadPage() {
	this.driver.findElement(namePageBy);
    }
}