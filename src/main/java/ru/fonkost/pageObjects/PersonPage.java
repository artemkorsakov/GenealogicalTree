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
public class PersonPage {
    private WebDriver driver;

    public PersonPage(WebDriver driver) {
	this.driver = driver;
    }

    /**
     * Возвращает историческое лицо, вычисленное на основе данных её страницы.
     * <br>
     * 
     * В данном методе вычисляются все данные, кроме наименования ссылки,
     * которая есть только на странице родителя и со страницы персоны её
     * вычислить не удастся. Кроме того, здесь не вычисляются дети персоны, т.к.
     * текущая персона может "стать" "дубликатом" и может быть удалена - в этом
     * случае определение детей бессмысленно. Вычисление детей происходит в
     * отдельном методе, который вызывается только если персона - не дубликат.
     * <br>
     * 
     * Отдельно стоит упомянуть, почему переопределяется url, хотя он передается
     * в качестве параметра: дело в том, что в Wikipedia одной персоне может
     * быть посвящено несколько страниц, которые редиректятся на одну. В
     * результате, если использовать исходные урлы, то возможно возникновение
     * дубликатов. Поэтому в качестве урла используется тот урл, на который
     * редиректятся все остальные.
     */
    public Person getPerson(String url) throws MalformedURLException {
	driver.navigate().to(url);

	String name = getName();

	Person person = new Person(driver.getCurrentUrl());
	person.setName(name);
	return person;
    }

    /**
     * Возвращает список урлов страниц детей персоны.
     * 
     * Для урлов с "якорями" дети не вычисляются, т.к., вероятнее всего, этот
     * якорь указывает на определённый блок внутри страницы родителя и в
     * результате будут повторно вычислены дети родителя, т.е. братья и сестры
     * текущей персоны.
     */
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