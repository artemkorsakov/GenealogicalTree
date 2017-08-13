/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import ru.fonkost.entities.Person;
import ru.fonkost.utils.ExcelWorker;

/**
 * Тестирование создания файла-Excel.
 *
 * @author Артём Корсаков
 */
public class TestExcelWorker {
    
    /**
     * Тестирование создания Excel-файла со списком персон.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testExcelWorker() throws Exception {
	String fileName = "C:\\workspace\\dynasticTree.xls";

	List<Person> persons = new ArrayList<Person>();
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	rurick.setName("Рюрик");
	rurick.setNameUrl("Рюрик");
	Person igor = new Person("https://ru.wikipedia.org/wiki/Игорь Рюрикович");
	igor.setName("Игорь Рюрикович");
	igor.setNameUrl("Игорь");
	rurick.setChild(igor.getId());
	Person svyatoslav = new Person("https://ru.wikipedia.org/wiki/Святослав Игоревич");
	svyatoslav.setName("Святослав Игоревич");
	svyatoslav.setNameUrl("Святослав");
	igor.setChild(svyatoslav.getId());
	Person yaropolk = new Person("https://ru.wikipedia.org/wiki/Ярополк Святославич");
	yaropolk.setName("Ярополк Святославич");
	yaropolk.setNameUrl("Ярополк");
	Person oleg = new Person("https://ru.wikipedia.org/wiki/Олег Святославич (князь древлянский)");
	oleg.setName("Олег Святославич (князь древлянский)");
	oleg.setNameUrl("Олег");
	Person vladimir = new Person("https://ru.wikipedia.org/wiki/Владимир Святославич");
	vladimir.setName("Владимир Святославич");
	vladimir.setNameUrl("Владимир");
	svyatoslav.setChild(yaropolk.getId());
	svyatoslav.setChild(oleg.getId());
	svyatoslav.setChild(vladimir.getId());

	persons.add(rurick);
	persons.add(igor);
	persons.add(svyatoslav);
	persons.add(yaropolk);
	persons.add(oleg);
	persons.add(vladimir);

	ExcelWorker excelWorker = new ExcelWorker();
	excelWorker.savePersons(fileName, persons);

	// Анализ результатов
	FileInputStream file = new FileInputStream(new File(fileName));
	HSSFWorkbook workbook = new HSSFWorkbook(file);
	HSSFSheet sheet = workbook.getSheetAt(0);
	assertTrue(sheet.getSheetName().equals("Генеалогическое древо Рюрик"));
	Row row = sheet.getRow(0);
	assertTrue(row.getCell(0).getStringCellValue().equals("id"));
	assertTrue(row.getCell(1).getStringCellValue().equals("name"));
	assertTrue(row.getCell(2).getStringCellValue().equals("childrens"));
	assertTrue(row.getCell(3).getStringCellValue().equals("url"));
	assertTrue(row.getCell(4).getStringCellValue().equals("urlName"));

	assertPerson(sheet, 1, rurick);
	assertPerson(sheet, 2, igor);
	assertPerson(sheet, 3, svyatoslav);
	assertPerson(sheet, 4, yaropolk);
	assertPerson(sheet, 5, oleg);
	assertPerson(sheet, 6, vladimir);

	workbook.close();
	file.close();
    }

    private void assertPerson(HSSFSheet sheet, int rowNum, Person person) throws ParseException {
	Row row = sheet.getRow(rowNum);

	int id = Integer.parseInt(row.getCell(0).getStringCellValue());
	assertTrue(id == person.getId());

	assertTrue(row.getCell(1).getStringCellValue().equals(person.getName()));

	String childrens = row.getCell(2).getStringCellValue();
	assertTrue(childrens.equals(person.getChildren().toString()));

	assertTrue(row.getCell(3).getStringCellValue().equals(person.getUrl()));

	assertTrue(row.getCell(4).getStringCellValue().equals(person.getNameUrl()));
    }
}