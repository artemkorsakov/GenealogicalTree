/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverFactory;
import ru.fonkost.entities.Person;
import ru.fonkost.pageObjects.PersonPage;

/**
 * Тесты, проверяющие корректность определения детей представителя династии.
 *
 * @author Артём Корсаков
 */
public class TestChildren {
    private String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";
    private WebDriver driver;

    @Before
    public void Start() {
	driver = DriverFactory.GetDriver();
    }

    /**
     * Проверка определения детей Рюрика.
     */
    @Test
    public void testRurickChildren() throws Exception {
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	List<String> childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 1);

	driver.navigate().to(childrens.get(0));
	Person person = page.GetPerson();
	assertTrue(person.getName().equals("Игорь Рюрикович"));
	assertTrue(person.getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%98%D0%B3%D0%BE%D1%80%D1%8C_%D0%A0%D1%8E%D1%80%D0%B8%D0%BA%D0%BE%D0%B2%D0%B8%D1%87"));
    }

    /**
     * Проверка определения количества детей
     */
    @Test
    public void testTheNumberOfChildren() throws Exception {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Святославич");
	PersonPage page = new PersonPage(driver);
	List<String> childrens = page.GetChildrensUrl();
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

    /**
     * Проверка добавления ребенка и формирования номера поколения
     */
    @Test
    public void testSetChild() throws Exception {
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	Person rurick = page.GetPerson();
	assertPersonFields(rurick, 1, 1, 0);
	assertTrue(rurick.getChildrens().isEmpty());
	rurick.setChildren(rurick);
	assertPersonFields(rurick, 1, 1, 0);
	assertTrue(rurick.getChildrens().isEmpty());

	List<String> childrensUrl = page.GetChildrensUrl();
	driver.navigate().to(childrensUrl.get(0));
	Person igor = page.GetPerson();
	assertPersonFields(rurick, 1, 1, 0);
	assertPersonFields(igor, 2, 1, 0);
	assertTrue(rurick.getChildrens().isEmpty());
	assertTrue(igor.getChildrens().isEmpty());
	rurick.setChildren(igor);
	assertPersonFields(rurick, 1, 1, 1);
	assertPersonFields(igor, 2, 2, 0);
	assertTrue(rurick.getChildrens().get(0) == 2);
	assertTrue(igor.getChildrens().isEmpty());
	rurick.setChildren(igor);
	assertPersonFields(rurick, 1, 1, 1);
	assertPersonFields(igor, 2, 2, 0);
	assertTrue(rurick.getChildrens().get(0) == 2);
	assertTrue(igor.getChildrens().isEmpty());
    }

    /**
     * Проверка добавления ребенка и формирования номера поколения до третьего
     * колена Рюрика
     */
    @Test
    public void testThreeGenerations() throws Exception {
	// Рюрик
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	Person rurick = page.GetPerson();
	assertPersonFields(rurick, 1, 1, 0);
	assertTrue(rurick.getChildrens().isEmpty());

	// 1-е поколение
	List<String> childrensRurick = page.GetChildrensUrl();
	assertTrue(childrensRurick.size() == 1);
	driver.navigate().to(childrensRurick.get(0));
	Person igor = page.GetPerson();
	rurick.setChildren(igor);
	assertPersonFields(rurick, 1, 1, 1);
	assertPersonFields(igor, 2, 2, 0);
	assertTrue(rurick.getChildrens().get(0) == 2);
	assertTrue(igor.getChildrens().isEmpty());

	// 2-е поколение
	List<String> childrensIgor = page.GetChildrensUrl();
	assertTrue(childrensIgor.size() == 1);
	driver.navigate().to(childrensIgor.get(0));
	Person svyatoslav = page.GetPerson();
	igor.setChildren(svyatoslav);
	assertPersonFields(rurick, 1, 1, 1);
	assertPersonFields(igor, 2, 2, 1);
	assertPersonFields(svyatoslav, 3, 3, 0);
	assertTrue(rurick.getChildrens().get(0) == 2);
	assertTrue(igor.getChildrens().get(0) == 3);
	assertTrue(svyatoslav.getChildrens().isEmpty());

	// 3-е поколение
	List<String> childrensSvyatoslavUrl = page.GetChildrensUrl();
	assertTrue(childrensSvyatoslavUrl.size() == 3);
	List<Person> childrensSvyatoslav = new ArrayList<Person>();
	for (String link : childrensSvyatoslavUrl) {
	    driver.navigate().to(link);
	    Person person = page.GetPerson();
	    svyatoslav.setChildren(person);
	    childrensSvyatoslav.add(person);
	}
	assertPersonFields(rurick, 1, 1, 1);
	assertTrue(rurick.getChildrens().get(0) == 2);
	assertPersonFields(igor, 2, 2, 1);
	assertTrue(igor.getChildrens().get(0) == 3);
	assertPersonFields(svyatoslav, 3, 3, 3);
	assertTrue(svyatoslav.getChildrens().get(0) == 4);
	assertTrue(svyatoslav.getChildrens().get(1) == 5);
	assertTrue(svyatoslav.getChildrens().get(2) == 6);
	assertPersonFields(childrensSvyatoslav.get(0), 4, 4, 0);
	assertTrue(childrensSvyatoslav.get(0).getChildrens().isEmpty());
	assertPersonFields(childrensSvyatoslav.get(1), 5, 4, 0);
	assertTrue(childrensSvyatoslav.get(1).getChildrens().isEmpty());
	assertPersonFields(childrensSvyatoslav.get(2), 6, 4, 0);
	assertTrue(childrensSvyatoslav.get(2).getChildrens().isEmpty());
    }

    private void assertPersonFields(Person person, int id, int number, int count) throws Exception {
	assertTrue(person.getId() == id);
	assertTrue(person.getNumberGeneration() == number);
	assertTrue(person.getCountOfChildrens() == count);
    }

    @After
    public void Stop() {
	driver.quit();
	driver = null;
	Person.ResetCount();
    }
}