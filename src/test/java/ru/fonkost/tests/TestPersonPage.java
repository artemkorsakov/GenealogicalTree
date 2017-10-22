/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverHelper;
import ru.fonkost.pageObjects.PersonPage;
import ru.fonkost.pageObjects.WikidataPage;
import ru.fonkost.pageObjects.WikipediaPage;

/** Тестирование страницы исторического лица */
public class TestPersonPage {
    private static WebDriver driver;

    @BeforeClass
    public static void Start() {
	driver = DriverHelper.getDriver();
    }

    /**
     * Проверка создания страницы в зависимости от урла
     */
    @Test
    public void testCreatePersonPage() throws Exception {
	String wp = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";
	String wd = "https://www.wikidata.org/wiki/Q7990";
	String gg = "https://www.google.ru/";
	PersonPage wpPage = PersonPage.createPersonPage(driver, wp);
	assertTrue(wpPage instanceof WikipediaPage);
	PersonPage wdPage = PersonPage.createPersonPage(driver, wd);
	assertTrue(wdPage instanceof WikidataPage);
	try {
	    PersonPage.createPersonPage(driver, gg);
	    fail("A page was created on the link " + gg);
	} catch (Exception ex) {
	    assertTrue(ex.getMessage().equals("The page for the link " + gg + " is not defined"));
	}
	try {
	    PersonPage.createPersonPage(driver, null);
	    fail("A page was created on the null link");
	} catch (Exception ex) {
	    assertTrue(ex.getMessage().equals("Link can not be null"));
	}
	try {
	    PersonPage.createPersonPage(driver, "");
	    fail("A page was created on the empty link");
	} catch (Exception ex) {
	    assertTrue(ex.getMessage().equals("no protocol: "));
	}
    }

    @AfterClass
    public static void Stop() {
	driver.quit();
	driver = null;
    }
}