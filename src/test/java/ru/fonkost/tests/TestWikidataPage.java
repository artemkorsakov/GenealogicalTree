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

public class TestWikidataPage {
    private static WebDriver driver;

    @BeforeClass
    public static void Start() {
	driver = DriverHelper.getDriver();
    }

    @Test
    public void testGetPerson() throws Exception {
	PersonPage page = PersonPage.createPersonPage(driver, "https://www.wikidata.org/wiki/Q7990");
	Person person = page.getPerson("https://www.wikidata.org/wiki/Q7990");
	assertTrue(person.getName().equals("Рюрик"));
	assertTrue(person.getUrl().equals("https://www.wikidata.org/wiki/Q7990"));
    }

    @Test
    public void testGetChildrenUrl() throws Exception {
	driver.navigate().to("https://www.wikidata.org/wiki/Q203501");
	PersonPage page = PersonPage.createPersonPage(driver, "https://www.wikidata.org/wiki/Q203501");
	List<Person> children = page.getChildrenUrl();
	assertTrue(children.size() == 2);
	Person person = children.get(0);
	assertTrue(person.getNameUrl().equals("Sviatoslav I of Kiev"));
	assertTrue(person.getUrl().equals("https://www.wikidata.org/wiki/Q1058572"));
	Person person2 = children.get(1);
	assertTrue(person2.getNameUrl().equals("Uleb Igorevich"));
	assertTrue(person2.getUrl().equals("https://www.wikidata.org/wiki/Q18279069"));
    }

    @Test
    public void testChildrenSize() throws Exception {
	driver.navigate().to("https://www.wikidata.org/wiki/Q586541");
	PersonPage page = PersonPage.createPersonPage(driver, "https://www.wikidata.org/wiki/Q586541");
	List<Person> children = page.getChildrenUrl();
	assertTrue(children.size() == 4);
	driver.navigate().to("https://www.wikidata.org/wiki/Q37085");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 2);
    }

    @Test
    public void testIsCorrectNameUrl() throws Exception {
	driver.navigate().to("https://www.wikidata.org/wiki/Q2601799");
	PersonPage page = PersonPage.createPersonPage(driver, "https://www.wikidata.org/wiki/Q2601799");
	List<Person> children = page.getChildrenUrl();
	assertTrue(children.size() == 0);
	driver.navigate().to("https://www.wikidata.org/wiki/Q785620");
	children = page.getChildrenUrl();
	assertTrue(children.size() == 1);
	Person person = children.get(0);
	assertTrue(person.getNameUrl().equals("Armenak"));
	assertTrue(person.getUrl().equals("https://www.wikidata.org/wiki/Q4068494"));
    }

    @Test
    public void testNotRuLabel() throws Exception {
	PersonPage page = PersonPage.createPersonPage(driver, "https://www.wikidata.org/wiki/Q399879");
	Person person = page.getPerson("https://www.wikidata.org/wiki/Q399879");
	assertTrue(person.getName().equals("Adela of France"));
    }

    @AfterClass
    public static void Stop() {
	driver.quit();
	driver = null;
    }
}