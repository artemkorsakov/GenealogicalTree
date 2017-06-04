/**
 * Артём Корсаков 
 * site: http://fonkost.ru
 * email: artemkorsakov@mail.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverFactory;

/**
 * Тесты, проверяющие, что драйвер корректно запустился и отработал.
 *
 * @author Артём Корсаков
 */
public class TestBrowser {

    /** The driver. */
    private WebDriver driver;

    /**
     * Инициализация драйвера перед запуском каждого теста
     */
    @Before
    public void Start() {
	driver = DriverFactory.GetDriver(30);
    }

    /**
     * Проверка корректного открытия страницы
     * https://ru.wikipedia.org/wiki/Рюрик
     */
    @Test
    public void testStartBrowserAndNavigate() {
	driver.navigate().to("https://ru.wikipedia.org/wiki/Рюрик");
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    /**
     * Остановка драйвера после запуска каждого теста
     */
    @After
    public void Stop() {
	driver.quit();
	driver = null;
    }
}