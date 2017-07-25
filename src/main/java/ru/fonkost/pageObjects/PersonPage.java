/**
 * http://fonkost.ru
 */
package ru.fonkost.pageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ru.fonkost.entities.Person;
import ru.fonkost.entities.PersonLink;

/**
 * Страница исторического лица.
 *
 * @author Артём Корсаков
 */
public class PersonPage extends Page {
    /**
     * Инициализация страницы с ожиданием загрузки
     *
     * @param driver
     *            the driver
     */
    public PersonPage(WebDriver driver) {
	super(driver);
	WaitLoadPage();
    }

    /**
     * Возвращает экземпляр класса Person с данными персоны, на странице которой
     * находимся в текущий момент.
     *
     * @return the person
     */
    public Person GetPerson(PersonLink personLink) throws IllegalArgumentException {
	GoToUrl(personLink.getUrl());
	WaitLoadPage();
	String name = GetName();
	String url = driver.getCurrentUrl();
	Person person = new Person(name, url, personLink.getName());
	return person;
    }

    /**
     * Возвращает имя персоны
     * 
     * @return имя персоны
     */
    public String GetName() {
	WaitLoadPage();
	String namePage = driver.findElement(By.cssSelector("#firstHeading")).getText();

	if (!IsAnchor()) {
	    return namePage;
	}

	List<WebElement> list = GetElements(By.id(GetAnchor()));
	return list.size() == 0 ? namePage : list.get(0).getText();
    }

    /**
     * Возвращает список урлов страниц детей персоны
     *
     * @return the list
     */
    public List<PersonLink> GetChildrensUrl() {
	WaitLoadPage();

	if (IsAnchor()) {
	    return new ArrayList<PersonLink>();
	}

	List<WebElement> childrensLinks = GetChildrensLinks();
	List<PersonLink> childrens = new ArrayList<PersonLink>();
	for (WebElement link : childrensLinks) {
	    if (IsSup(link)) {
		continue;
	    }
	    PersonLink personLink = new PersonLink(link.getText(), link.getAttribute("href"));
	    if (personLink.IsCorrectName()) {
		childrens.add(personLink);
	    }
	}
	return childrens;
    }

    /**
     * Вернуть список элементов, которые, предположительно являются детьми
     * персоны
     */
    private List<WebElement> GetChildrensLinks() {
	List<WebElement> childrensLinks = GetElements(
		By.xpath("//table[@class='infobox']//tr[th[.='Дети:']]//a[not(@class='new' or @class='extiw')]"));
	return childrensLinks;
    }

    /**
     * Ожидание загрузки страницы
     */
    private void WaitLoadPage() {
	this.driver.findElement(By.cssSelector("#firstHeading"));
    }
}