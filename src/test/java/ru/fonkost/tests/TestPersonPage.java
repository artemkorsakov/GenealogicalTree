/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverHelper;
import ru.fonkost.entities.Person;
import ru.fonkost.pageObjects.PersonPage;

/** Тестирование страницы исторического лица */
public class TestPersonPage {
    private static WebDriver driver;

    @BeforeClass
    public static void Start() {
	driver = DriverHelper.getDriver();
    }

    /**
     * Проверка формирования персоны.
     * <ul>
     * <li>Вычислилось имя персоны</li>
     * <li>Url равен текущему урлу, а не заданной строке</li>
     * </ul>
     */
    @Test
    public void testGetPerson() throws Exception {
	PersonPage page = new PersonPage(driver);
	Person person = page.getPerson("https://ru.wikipedia.org/wiki/Владимир_Александрович");
	assertTrue(person.getName().equals("Владимир Александрович"));
	assertTrue(person.getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%92%D0%BB%D0%B0%D0%B4%D0%B8%D0%BC%D0%B8%D1%80_%D0%90%D0%BB%D0%B5%D0%BA%D1%81%D0%B0%D0%BD%D0%B4%D1%80%D0%BE%D0%B2%D0%B8%D1%87"));
    }

    /**
     * Проверка формирования имени персоны.
     * <ul>
     * <li>Урл с существующим "якорем": имя равно наименованию блока с якорем
     * </li>
     * <li>Урл без "якоря": имя равно наименованию страницы</li>
     * <li>Урл с несуществующим "якорем": имя равно наименованию страницы</li>
     * </ul>
     */
    @Test
    public void testGetName() throws Exception {
	PersonPage page = new PersonPage(driver);
	Person person = page
		.getPerson("https://ru.wikipedia.org/wiki/Дети_Михаила_Фёдоровича#.D0.A1.D0.BE.D1.84.D1.8C.D1.8F");
	assertTrue(person.getName().equals("Софья"));
	person = page.getPerson("https://ru.wikipedia.org/wiki/Дети_Михаила_Фёдоровича");
	assertTrue(person.getName().equals("Дети Михаила Фёдоровича"));
	person = page.getPerson(
		"https://ru.wikipedia.org/wiki/Дети_Петра_I#.D0.9D.D0.B0.D1.82.D0.B0.D0.BB.D1.8C.D1.8F_.D0.9F.D0.B5.D1.82.D1.80.D0.BE.D0.B2.D0.BD.D0.B0_.281718.E2.80.941725.29");
	assertTrue(person.getName().equals("Дети Петра I"));
    }

    /**
     * Проверка определения урлов и их наименований.
     * <ul>
     * <li>url перенаправляет на другую страницу</li>
     * <li>url с "якорем" перенапрявляет на заданный блок текущей страницы</li>
     * </ul>
     */
    @Test
    public void testGetChildrenUrl() throws Exception {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Рюрик");
	PersonPage page = new PersonPage(driver);
	List<Person> children = page.getChildrenUrl();
	assertTrue(children.size() == 1);
	Person person = children.get(0);
	assertTrue(person.getNameUrl().equals("Игорь"));
	assertTrue(person.getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%98%D0%B3%D0%BE%D1%80%D1%8C_%D0%A0%D1%8E%D1%80%D0%B8%D0%BA%D0%BE%D0%B2%D0%B8%D1%87"));

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Александрович");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 5);
	person = children.get(0);
	assertTrue(person.getNameUrl().equals("Александр"));
	assertTrue(person.getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B5%D0%BA%D1%81%D0%B0%D0%BD%D0%B4%D1%80_%D0%92%D0%BB%D0%B0%D0%B4%D0%B8%D0%BC%D0%B8%D1%80%D0%BE%D0%B2%D0%B8%D1%87"));
    }

    /**
     * Проверка определения количества детей. <br>
     * Случаи:
     * <ul>
     * <li><a href=
     * "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA">Рюрик</a>
     * с единственным ребенком</li>
     * <li><a href=
     * "https://ru.wikipedia.org/wiki/%D0%92%D0%BB%D0%B0%D0%B4%D0%B8%D0%BC%D0%B8%D1%80_%D0%A1%D0%B2%D1%8F%D1%82%D0%BE%D1%81%D0%BB%D0%B0%D0%B2%D0%B8%D1%87">
     * Владимир Святославич</a>, где в списке детей нераспарсенная информация
     * "5 неизвестных по имени дочерей"</li>
     * <li><a href=
     * "https://ru.wikipedia.org/wiki/%D0%92%D0%BB%D0%B0%D0%B4%D0%B8%D0%BC%D0%B8%D1%80_%D0%AF%D1%80%D0%BE%D1%81%D0%BB%D0%B0%D0%B2%D0%B8%D1%87_(%D0%BA%D0%BD%D1%8F%D0%B7%D1%8C_%D0%B3%D0%B0%D0%BB%D0%B8%D1%86%D0%BA%D0%B8%D0%B9)">
     * Владимир Ярославич (князь галицкий)</a>, у которого информация о детях
     * расположена в основной области</li>
     * <li><a href=
     * "https://ru.wikipedia.org/wiki/%D0%9C%D0%B0%D1%80%D0%B8%D1%8F_%D0%94%D0%BE%D0%B1%D1%80%D0%BE%D0%BD%D0%B5%D0%B3%D0%B0">
     * Мария Добронега</a> - аналогично предыдущему пункту</li>
     * <li><a href=
     * "https://ru.wikipedia.org/wiki/%D0%AF%D1%80%D0%BE%D1%81%D0%BB%D0%B0%D0%B2_%D0%A1%D0%B2%D1%8F%D1%82%D0%BE%D1%81%D0%BB%D0%B0%D0%B2%D0%B8%D1%87">
     * Ярослав Святослави</a>, у которого в списке детей также присутствуют
     * ссылки на года</li>
     * <li>Несколько персон с уточняющими ссылками в списке детей: <a href=
     * "https://ru.wikipedia.org/wiki/%D0%9B%D1%8E%D0%B4%D0%BE%D0%B2%D0%B8%D0%BA_VII">
     * Людовик VII</a>, <a href=
     * "https://ru.wikipedia.org/wiki/%D0%93%D0%B0%D0%BB%D0%B5%D1%80%D0%B0%D0%BD_IV_%D0%B4%D0%B5_%D0%91%D0%BE%D0%BC%D0%BE%D0%BD,_%D0%B3%D1%80%D0%B0%D1%84_%D0%B4%D0%B5_%D0%9C%D1%91%D0%BB%D0%B0%D0%BD">
     * Галеран IV де Бомон</a>, <a href=
     * "https://ru.wikipedia.org/wiki/%D0%AE%D1%80%D0%B8%D0%B9_%D0%AF%D1%80%D0%BE%D1%81%D0%BB%D0%B0%D0%B2%D0%B8%D1%87_(%D0%BA%D0%BD%D1%8F%D0%B7%D1%8C_%D1%82%D1%83%D1%80%D0%BE%D0%B2%D1%81%D0%BA%D0%B8%D0%B9)">
     * Юрий Ярославич (князь туровский)</a></li>
     * </ul>
     */
    @Test
    public void testChildrenSize() throws Exception {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Рюрик");
	PersonPage page = new PersonPage(driver);
	List<Person> children = page.getChildrenUrl();
	assertTrue(children.size() == 1);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Святославич");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 16);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Ярославич_(князь_галицкий)");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 0);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Мария_Добронега");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 0);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Ярослав_Святославич");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 3);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Людовик_VII");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 5);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Галеран_IV_де_Бомон,_граф_де_Мёлан");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 0);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Юрий_Ярославич_(князь_туровский)");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 5);
    }

    /**
     * Проверка, что для персон с урлами, содержащими "якорь", дети не
     * вычисляются
     */
    @Test
    public void testEmptyChildrenInPersonWithAnchor() throws Exception {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Александрович");
	PersonPage page = new PersonPage(driver);
	List<Person> children = page.getChildrenUrl();
	assertTrue(children.size() == 5);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Александрович#.D0.A1.D0.B5.D0.BC.D1.8C.D1.8F");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 0);
    }

    @AfterClass
    public static void Stop() {
	driver.quit();
	driver = null;
    }
}