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
    private WebDriver driver;

    @Before
    public void Start() {
	driver = DriverFactory.GetDriver();
    }

    /**
     * Проверка формирования имени персоны с урлом, содержащим якорь
     */
    @Test
    public void testGetName() throws Exception {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Дети_Михаила_Фёдоровича#.D0.A1.D0.BE.D1.84.D1.8C.D1.8F");
	PersonPage page = new PersonPage(driver);
	assertTrue(page.GetName().equals("Софья"));
	driver.navigate().to("https://ru.wikipedia.org/wiki/Дети_Михаила_Фёдоровича");
	assertTrue(page.GetName().equals("Дети Михаила Фёдоровича"));
	driver.navigate().to(
		"https://ru.wikipedia.org/wiki/Дети_Петра_I#.D0.9D.D0.B0.D1.82.D0.B0.D0.BB.D1.8C.D1.8F_.D0.9F.D0.B5.D1.82.D1.80.D0.BE.D0.B2.D0.BD.D0.B0_.281718.E2.80.941725.29");
	assertTrue(page.GetName().equals("Дети Петра I"));
    }

    /**
     * Проверка определения урлов детей Рюрика
     */
    @Test
    public void testGetChildrensUrl() throws Exception {
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	PersonPage page = new PersonPage(driver);
	List<Person> childrens = page.GetChildrensUrl();
	Person person = childrens.get(0);
	assertTrue(childrens.size() == 1);
	assertTrue(person.getNameUrl().equals("Игорь"));
	assertTrue(person.getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%98%D0%B3%D0%BE%D1%80%D1%8C_%D0%A0%D1%8E%D1%80%D0%B8%D0%BA%D0%BE%D0%B2%D0%B8%D1%87"));

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Александрович");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 5);
	person = childrens.get(0);
	assertTrue(person.getNameUrl().equals("Александр"));
	assertTrue(person.getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B5%D0%BA%D1%81%D0%B0%D0%BD%D0%B4%D1%80_%D0%92%D0%BB%D0%B0%D0%B4%D0%B8%D0%BC%D0%B8%D1%80%D0%BE%D0%B2%D0%B8%D1%87"));
    }

    /**
     * Проверка определения количества детей
     */
    @Test
    public void testChildrensSize() throws Exception {
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	PersonPage page = new PersonPage(driver);
	List<Person> childrens = page.GetChildrensUrl();
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

    @Test
    public void testEmptyChildrenInPersonWithAnchor() throws Exception {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Александрович");
	PersonPage page = new PersonPage(driver);
	List<Person> childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 5);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Александрович#.D0.A1.D0.B5.D0.BC.D1.8C.D1.8F");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 0);
    }

    @After
    public void Stop() {
	driver.quit();
	driver = null;
    }
}