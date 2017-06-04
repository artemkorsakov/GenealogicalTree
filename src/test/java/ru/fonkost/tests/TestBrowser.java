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
import ru.fonkost.entities.Person;
import ru.fonkost.pageObjects.PersonPage;

/**
 * Тесты, проверяющие, что драйвер корректно запустился и отработал, а также
 * определение Имени и Страницы для представителя династии.
 *
 * @author Артём Корсаков
 */
public class TestBrowser {
    private String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";
    private WebDriver driver;

    /**
     * Инициализация драйвера перед запуском каждого теста.
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
	driver.navigate().to(rurickUrl);
	assertTrue(driver.getTitle().equals("Рюрик — Википедия"));
    }

    /**
     * Проверка формирования класса Person на основе страницы Рюрика.
     */
    @Test
    public void testPersonRurick() throws Exception {
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	Person person = page.GetPerson();
	assertTrue(person.getUrl().equals(rurickUrl));
	assertTrue(person.getName().equals("Рюрик"));
	assertTrue(person.toString().equals("Рюрик (https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA)"));
    }

    /**
     * Остановка драйвера после запуска каждого теста.
     */
    @After
    public void Stop() {
	driver.quit();
	driver = null;
    }
}