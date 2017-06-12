/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverFactory;
import ru.fonkost.entities.Person;
import ru.fonkost.pageObjects.PersonPage;
import ru.fonkost.utils.ExcelWorker;

/**
 * Тестирование создания файла-Excel.
 *
 * @author Артём Корсаков
 */
public class TestExcelWorker {
    private String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";
    private WebDriver driver;
    private ExcelWorker excelWorker;

    @Before
    public void Start() {
	excelWorker = new ExcelWorker();
	excelWorker.createSheet("Рюриковичи");
	driver = DriverFactory.GetDriver();
    }

    /**
     * Тестирование создания Excel-файла с результатами.
     */
    @Test
    public void testExcelWorker() throws Exception {
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	Person rurick = page.GetPerson();
	excelWorker.savePerson(rurick);

	List<String> childrensRurick = page.GetChildrensUrl();
	driver.navigate().to(childrensRurick.get(0));
	Person igor = page.GetPerson();
	rurick.setChildren(igor);
	excelWorker.savePerson(igor);

	List<String> childrensIgor = page.GetChildrensUrl();
	driver.navigate().to(childrensIgor.get(0));
	Person svyatoslav = page.GetPerson();
	igor.setChildren(svyatoslav);
	excelWorker.savePerson(svyatoslav);

	List<String> childrensSvyatoslav = page.GetChildrensUrl();
	for (String link : childrensSvyatoslav) {
	    driver.navigate().to(link);
	    Person person = page.GetPerson();
	    svyatoslav.setChildren(person);
	    excelWorker.savePerson(person);
	}
    }

    @After
    public void Stop() {
	excelWorker.saveSheet();
	driver.quit();
	driver = null;
	Person.ResetCount();
    }
}