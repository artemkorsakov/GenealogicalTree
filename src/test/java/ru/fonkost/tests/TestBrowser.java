/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverFactory;

/**
 * Тесты, проверяющие, что драйвер корректно запустился и отработал, а также
 * определение Имени и Url-страницы для представителя династии.
 *
 * @author Артём Корсаков
 */
public class TestBrowser {
    private WebDriver driver;

    @Before
    public void Start() {
	driver = DriverFactory.GetDriver();
    }

    /**
     * Проверяет, что корректно запустился драйвер и что при переходе по урлу
     * https://ru.wikipedia.org/wiki/Рюрик открывается страница с заголовком
     * "Рюрик — Википедия"
     */
    @Test
    public void testStartBrowserAndNavigate() {
	driver.navigate().to("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    @After
    public void Stop() {
	driver.quit();
	driver = null;
    }
}