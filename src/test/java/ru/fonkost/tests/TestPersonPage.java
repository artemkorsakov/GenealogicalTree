/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverFactory;
import ru.fonkost.entities.Person;
import ru.fonkost.pageObjects.PersonPage;

/**
 * Тестирование страницы Персоны
 *
 * @author Артём Корсаков
 */
public class TestPersonPage {
    private String rurickName = "Рюрик";
    private String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";
    private WebDriver driver;

    @Before
    public void Start() {
	driver = DriverFactory.GetDriver();
	Person.ResetCount();
    }

    /**
     * Проверка формирования класса Person на основе страницы Рюрика.
     */
    @Test
    public void testGetPerson() throws Exception {
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	Person person = page.GetPerson();
	assertTrue(person.getUrl().equals(rurickUrl));
	assertTrue(person.getName().equals(rurickName));
    }

    /**
     * Проверка определения урлов детей Рюрика
     */
    @Test
    public void testGetChildrensUrl() throws Exception {
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	List<String> childrens = page.GetChildrensUrl();
	assertTrue(childrens.get(0).equals(
		"https://ru.wikipedia.org/wiki/%D0%98%D0%B3%D0%BE%D1%80%D1%8C_%D0%A0%D1%8E%D1%80%D0%B8%D0%BA%D0%BE%D0%B2%D0%B8%D1%87"));
    }

    /**
     * Проверка определения количества детей
     */
    @Test
    public void testChildrensSize() throws Exception {
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	List<String> childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 1);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Святославич");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 16);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Ярославич_(князь_галицкий)");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 0);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Мария_Добронега");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 0);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Ярослав_Святославич");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 3);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Людовик_VII");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 5);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Галеран_IV_де_Бомон,_граф_де_Мёлан");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 0);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Юрий_Ярославич_(князь_туровский)");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 5);
    }

    @After
    public void Stop() {
	driver.quit();
	driver = null;
    }
}