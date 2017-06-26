/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
    private String fileName = "C:\\workspace\\dynasticTree.xls";
    private String dynastyName = "Рюриковичи";
    private String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";
    private WebDriver driver;

    @Before
    public void Start() {
	driver = DriverFactory.GetDriver();
    }

    /**
     * Тестирование создания Excel-файла с результатами.
     */
    @Test
    public void testExcelWorker() throws Exception {
	ExcelWorker excelWorker = new ExcelWorker();
	excelWorker.createSheet(dynastyName);

	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	Person rurick = page.GetPerson();

	List<String> childrensRurick = page.GetChildrensUrl();
	driver.navigate().to(childrensRurick.get(0));
	Person igor = page.GetPerson();
	rurick.setChildren(igor);

	List<String> childrensIgor = page.GetChildrensUrl();
	driver.navigate().to(childrensIgor.get(0));
	Person svyatoslav = page.GetPerson();
	igor.setChildren(svyatoslav);

	List<String> childrensSvyatoslav = page.GetChildrensUrl();
	driver.navigate().to(childrensSvyatoslav.get(0));
	Person yaropolk = page.GetPerson();
	svyatoslav.setChildren(yaropolk);

	driver.navigate().to(childrensSvyatoslav.get(1));
	Person oleg = page.GetPerson();
	svyatoslav.setChildren(oleg);

	driver.navigate().to(childrensSvyatoslav.get(2));
	Person vladimir = page.GetPerson();
	svyatoslav.setChildren(vladimir);

	excelWorker.savePerson(rurick);
	excelWorker.savePerson(igor);
	excelWorker.savePerson(svyatoslav);
	excelWorker.savePerson(yaropolk);
	excelWorker.savePerson(oleg);
	excelWorker.savePerson(vladimir);

	excelWorker.saveSheet(fileName);

	// Анализ результатов
	FileInputStream file = new FileInputStream(new File(fileName));
	HSSFWorkbook workbook = new HSSFWorkbook(file);
	HSSFSheet sheet = workbook.getSheetAt(0);
	assertTrue(sheet.getSheetName().equals(dynastyName));
	Row row = sheet.getRow(0);
	assertTrue(row.getCell(0).getStringCellValue().equals("id"));
	assertTrue(row.getCell(1).getStringCellValue().equals("name"));
	assertTrue(row.getCell(2).getStringCellValue().equals("childrens"));
	assertTrue(row.getCell(3).getStringCellValue().equals("numberGeneration"));
	assertTrue(row.getCell(4).getStringCellValue().equals("url"));

	assertPerson(sheet, 1, rurick);
	assertPerson(sheet, 2, igor);
	assertPerson(sheet, 3, svyatoslav);
	assertPerson(sheet, 4, yaropolk);
	assertPerson(sheet, 5, oleg);
	assertPerson(sheet, 6, vladimir);

	workbook.close();
	file.close();
    }

    public void assertPerson(HSSFSheet sheet, int rowNum, Person person) throws ParseException {
	Row row = sheet.getRow(rowNum);

	int id = (int) (row.getCell(0).getNumericCellValue());
	assertTrue(id == person.getId());

	assertTrue(row.getCell(1).getStringCellValue().equals(person.getName()));

	String childrens = row.getCell(2).getStringCellValue();
	assertTrue(childrens.equals(person.getChildrens().toString()));

	int numberGeneration = (int) (row.getCell(3).getNumericCellValue());
	assertTrue(numberGeneration == person.getNumberGeneration());

	assertTrue(row.getCell(4).getStringCellValue().equals(person.getUrl()));
    }

    @After
    public void Stop() {
	driver.quit();
	driver = null;
	Person.ResetCount();
    }
}