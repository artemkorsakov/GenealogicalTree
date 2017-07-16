/**
 * http://fonkost.ru
 */
package ru.fonkost.pageObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	WaitLoadPage();
    }

    /**
     * Возвращает экземпляр класса Person с данными персоны, на странице которой
     * находимся в текущий момент.
     *
     * @return the person
     */
    public Person GetPerson() throws IllegalArgumentException {
	String url = driver.getCurrentUrl();
	String name = GetName();
	Person person = new Person(name, url);
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
    public List<String> GetChildrensUrl() {
	WaitLoadPage();

	if (IsAnchor()) {
	    return new ArrayList<String>();
	}

	List<WebElement> childrensLinks = GetChildrensLinks();
	List<String> childrens = new ArrayList<String>();
	for (WebElement childrenLink : childrensLinks) {
	    if (!IsSup(childrenLink) && IsCorrectName(childrenLink)) {
		childrens.add(childrenLink.getAttribute("href"));
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
     * Возвращает true, если текст элемента начинается не с числа
     */
    private boolean IsCorrectName(WebElement element) {
	String name = element.getText();
	Pattern p = Pattern.compile("^[\\D]+.+");
	Matcher m = p.matcher(name);
	return m.matches();
    }

    /**
     * Ожидание загрузки страницы
     */
    private void WaitLoadPage() {
	this.driver.findElement(By.cssSelector("#firstHeading"));
    }
}