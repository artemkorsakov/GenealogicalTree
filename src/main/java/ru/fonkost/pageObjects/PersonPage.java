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
    public List<Person> GetChildrensUrl() {
	WaitLoadPage();

	if (IsAnchor()) {
	    return new ArrayList<Person>();
	}

	List<WebElement> childrensLinks = GetChildrensLinks();
	List<Person> childrens = new ArrayList<Person>();
	for (WebElement link : childrensLinks) {
	    if (IsSup(link)) {
		continue;
	    }
	    Person person = new Person(link.getAttribute("href"));
	    person.setNameUrl(link.getText());
	    if (person.IsCorrectNameUrl()) {
		childrens.add(person);
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