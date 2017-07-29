/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.Browser;
import ru.fonkost.driverHelper.DriverFactory;

/**
 * Тесты покрытия классов Browser и DriverFactory
 *
 * @author Артём Корсаков
 */
public class TestDriverFactoryAndBrowser {
    private WebDriver driver;

    @Test
    public void testCreateDriverFactory() {
	new DriverFactory();
    }

    @Test
    public void testNonZeroTimeoutValue() {
	assertTrue(DriverFactory.timeout > 0);
    }

    @Test
    public void testBrowserValuesContainsDefault() {
	assertTrue(Arrays.toString(Browser.values()).contains("Default"));
	assertTrue(Browser.valueOf("Default").equals(Browser.Default));
    }

    @Test
    public void testGetDriverWithoutParameter() {
	driver = DriverFactory.GetDriver();
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    @Test
    public void testGetDriverNullParameter() {
	driver = DriverFactory.GetDriver(null);
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    @Test
    public void testStartDefaultBrowser() {
	driver = DriverFactory.GetDriver(Browser.Default);
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    @Test
    public void testStartFirefox() {
	driver = DriverFactory.GetDriver(Browser.Firefox);
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    @Test
    public void testStartPhantom() {
	driver = DriverFactory.GetDriver(Browser.Phantom);
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    @Test
    public void testStartChrome() {
	driver = DriverFactory.GetDriver(Browser.Chrome);
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    @After
    public void Stop() {
	if (driver != null) {
	    driver.quit();
	    driver = null;
	}
    }
}