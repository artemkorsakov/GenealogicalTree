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
     * Проверка формирования класса Person на основе страницы Рюрика.
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
	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Ярославич_(князь_галицкий)");
	PersonPage page = new PersonPage(driver);
	List<String> childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 0);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Мария_Добронега");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 0);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Святославич");
	childrens = page.GetChildrensUrl();
	assertTrue(childrens.size() == 16);
    }

    /**
     * Проверка добавления ребенка и формирования номера поколения
     */
    @Test
    public void testNumberGenerationAndAddChild() throws Exception {
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	Person rurick = page.GetPerson();
	assertTrue(rurick.getNumberGeneration() == 1);
	assertTrue(rurick.getCountOfChildrens() == 0);

	rurick.setChildren(rurick);
	assertTrue(rurick.getNumberGeneration() == 1);
	assertTrue(rurick.getCountOfChildrens() == 0);

	List<String> childrens = page.GetChildrensUrl();
	driver.navigate().to(childrens.get(0));
	Person igor = page.GetPerson();
	assertTrue(igor.getNumberGeneration() == 1);
	assertTrue(igor.getCountOfChildrens() == 0);

	rurick.setChildren(igor);
	assertTrue(rurick.getNumberGeneration() == 1);
	assertTrue(rurick.getCountOfChildrens() == 1);
	assertTrue(igor.getNumberGeneration() == 2);
	assertTrue(igor.getCountOfChildrens() == 0);

	rurick.setChildren(igor);
	assertTrue(rurick.getNumberGeneration() == 1);
	assertTrue(rurick.getCountOfChildrens() == 1);
	assertTrue(igor.getNumberGeneration() == 2);
	assertTrue(igor.getCountOfChildrens() == 0);
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
	assertTrue(rurick.getNumberGeneration() == 1);
	assertTrue(rurick.getCountOfChildrens() == 0);

	// 1-е поколение
	List<String> childrensRurick = page.GetChildrensUrl();
	assertTrue(childrensRurick.size() == 1);
	driver.navigate().to(childrensRurick.get(0));
	Person igor = page.GetPerson();
	rurick.setChildren(igor);
	assertTrue(rurick.getNumberGeneration() == 1);
	assertTrue(rurick.getCountOfChildrens() == 1);
	assertTrue(igor.getNumberGeneration() == 2);
	assertTrue(igor.getCountOfChildrens() == 0);

	// 2-е поколение
	List<String> childrensIgor = page.GetChildrensUrl();
	assertTrue(childrensIgor.size() == 1);
	driver.navigate().to(childrensIgor.get(0));
	Person svyatoslav = page.GetPerson();
	igor.setChildren(svyatoslav);
	assertTrue(rurick.getNumberGeneration() == 1);
	assertTrue(rurick.getCountOfChildrens() == 1);
	assertTrue(igor.getNumberGeneration() == 2);
	assertTrue(igor.getCountOfChildrens() == 1);
	assertTrue(svyatoslav.getNumberGeneration() == 3);
	assertTrue(svyatoslav.getCountOfChildrens() == 0);

	// 3-е поколение
	List<String> childrensSvyatoslav = page.GetChildrensUrl();
	assertTrue(childrensSvyatoslav.size() == 3);
	for (String link : childrensSvyatoslav) {
	    driver.navigate().to(link);
	    Person person = page.GetPerson();
	    svyatoslav.setChildren(person);
	}
	assertTrue(rurick.getNumberGeneration() == 1);
	assertTrue(rurick.getCountOfChildrens() == 1);
	assertTrue(igor.getNumberGeneration() == 2);
	assertTrue(igor.getCountOfChildrens() == 1);
	assertTrue(svyatoslav.getNumberGeneration() == 3);
	assertTrue(svyatoslav.getCountOfChildrens() == 3);
    }

    @After
    public void Stop() {
	driver.quit();
	driver = null;
    }
}