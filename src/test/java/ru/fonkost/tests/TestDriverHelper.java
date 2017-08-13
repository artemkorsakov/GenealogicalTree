/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ru.fonkost.driverHelper.DriverHelper;

/** Тесты покрытия класса DriverHelper */
public class TestDriverHelper {
    private static WebDriver driver;

    @BeforeClass
    public static void Start() {
	driver = DriverHelper.getDriver();
    }

    @Test
    public void testDriverHelperAvailability() {
	new DriverHelper();
    }

    @Test
    public void testGetDriver() {
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    @Test
    public void testGetAnchorAndHasAnchor() throws MalformedURLException {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Александрович");
	assertFalse(DriverHelper.hasAnchor(driver));
	assertTrue(DriverHelper.getAnchor(driver) == null);
	driver.navigate().to("https://ru.wikipedia.org/wiki/Владимир_Александрович#.D0.A1.D0.B5.D0.BC.D1.8C.D1.8F");
	assertTrue(DriverHelper.hasAnchor(driver));
	assertTrue(DriverHelper.getAnchor(driver).equals(".D0.A1.D0.B5.D0.BC.D1.8C.D1.8F"));
    }

    /**
     * Данный тест проверяет, что метод
     * {@link ru.fonkost.driverHelper.DriverHelper#getElements(WebDriver, By)
     * DriverHelper.getElements()} не только выдает корректные результаты, но и
     * то, что при его вызове не происходит потеря времени на неявное ожидание.
     */
    @Test
    public void testGetElements() {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Николай_II");
	String locator = "//table[@class='infobox']//tr[th[.='Дети:']]//a";
	long start = System.currentTimeMillis();
	List<WebElement> elements = DriverHelper.getElements(driver, By.xpath(locator));
	long finish = System.currentTimeMillis();
	assertTrue((finish - start) < 1000);
	assertTrue(elements.size() == 5);

	driver.navigate().to("https://ru.wikipedia.org/wiki/Алексей_Николаевич");
	start = System.currentTimeMillis();
	elements = DriverHelper.getElements(driver, By.xpath(locator));
	finish = System.currentTimeMillis();
	assertTrue((finish - start) < 1000);
	assertTrue(elements.size() == 0);
    }

    @Test
    public void testIsSup() {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Людовик_VII");
	WebElement element = driver.findElement(By.xpath("//a[@title='Бастард']"));
	assertTrue(DriverHelper.isSup(element));
	element = driver.findElement(By.xpath("//a[@title='Анна Французская (императрица Византии)']"));
	assertFalse(DriverHelper.isSup(element));
    }

    @AfterClass
    public static void Stop() {
	driver.quit();
    }
}