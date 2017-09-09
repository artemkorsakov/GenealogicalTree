/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import ru.fonkost.entities.Person;
import ru.fonkost.main.GenerateGenealogicalTree;

/** Тестирование генерации родословного древа */
public class TestGenerateGenealogicalTree {
    @Test
    public void testIncorrectArguments() throws Exception {
	new GenerateGenealogicalTree();
	try {
	    GenerateGenealogicalTree.main(null);
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Должен быть задан один параметр"));
	}

	try {
	    GenerateGenealogicalTree.main(new String[] { "first", "second" });
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Должен быть задан один параметр"));
	}

	try {
	    GenerateGenealogicalTree.main(new String[] { "first" });
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Некорректный урл first"));
	}

	try {
	    GenerateGenealogicalTree.main(new String[] { "http://fonkost.ru/" });
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals(
		    "Алгоритм предназначен для генерации родословного древа только на основе данных Wikipedia"));
	}
    }

    @Test
    public void testGenerateGenealogicalTree() throws Exception {
	Person.resetCount();
	String url = "https://ru.wikipedia.org/wiki/%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B9_II";
	GenerateGenealogicalTree.main(new String[] { url });

	String fileName = "C:\\workspace\\GenerateGenealogicalTree.xls";

	// Анализ результатов
	FileInputStream file = new FileInputStream(new File(fileName));
	HSSFWorkbook workbook = new HSSFWorkbook(file);
	HSSFSheet sheet = workbook.getSheetAt(0);
	assertTrue(sheet.getSheetName().equals("Древо Николай II"));

	Person.resetCount();
	Person nikolay = new Person("https://ru.wikipedia.org/wiki/%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B9_II");
	nikolay.setName("Николай II");
	nikolay.setNameUrl("");
	nikolay.setChild(3);
	nikolay.setChild(4);
	nikolay.setChild(5);
	nikolay.setChild(6);
	nikolay.setChild(7);
	Person olga = new Person(
		"https://ru.wikipedia.org/wiki/%D0%9E%D0%BB%D1%8C%D0%B3%D0%B0_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%BD%D0%B0_(%D0%B2%D0%B5%D0%BB%D0%B8%D0%BA%D0%B0%D1%8F_%D0%BA%D0%BD%D1%8F%D0%B6%D0%BD%D0%B0)");
	olga.setName("Ольга Николаевна (великая княжна)");
	olga.setNameUrl("Ольга");
	olga.setNumberGeneration(1);
	olga.setParent(nikolay.getId());
	Person tatyana = new Person(
		"https://ru.wikipedia.org/wiki/%D0%A2%D0%B0%D1%82%D1%8C%D1%8F%D0%BD%D0%B0_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%BD%D0%B0_(%D0%B2%D0%B5%D0%BB%D0%B8%D0%BA%D0%B0%D1%8F_%D0%BA%D0%BD%D1%8F%D0%B6%D0%BD%D0%B0)");
	tatyana.setName("Татьяна Николаевна (великая княжна)");
	tatyana.setNameUrl("Татьяна");
	tatyana.setNumberGeneration(1);
	tatyana.setParent(nikolay.getId());
	Person maria = new Person(
		"https://ru.wikipedia.org/wiki/%D0%9C%D0%B0%D1%80%D0%B8%D1%8F_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%BD%D0%B0_(%D0%B2%D0%B5%D0%BB%D0%B8%D0%BA%D0%B0%D1%8F_%D0%BA%D0%BD%D1%8F%D0%B6%D0%BD%D0%B0)");
	maria.setName("Мария Николаевна (великая княжна)");
	maria.setNameUrl("Мария");
	maria.setNumberGeneration(1);
	maria.setParent(nikolay.getId());
	Person anastasia = new Person(
		"https://ru.wikipedia.org/wiki/%D0%90%D0%BD%D0%B0%D1%81%D1%82%D0%B0%D1%81%D0%B8%D1%8F_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%BD%D0%B0");
	anastasia.setName("Анастасия Николаевна");
	anastasia.setNameUrl("Анастасия");
	anastasia.setNumberGeneration(1);
	anastasia.setParent(nikolay.getId());
	Person alexey = new Person(
		"https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B5%D0%BA%D1%81%D0%B5%D0%B9_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%B8%D1%87");
	alexey.setName("Алексей Николаевич");
	alexey.setNameUrl("Алексей");
	alexey.setNumberGeneration(1);
	alexey.setParent(nikolay.getId());

	assertPerson(sheet, 1, nikolay);
	assertPerson(sheet, 2, olga);
	assertPerson(sheet, 3, tatyana);
	assertPerson(sheet, 4, maria);
	assertPerson(sheet, 5, anastasia);
	assertPerson(sheet, 6, alexey);

	workbook.close();
	file.close();
    }

    @Test
    public void testGenerateWhenThereAreDuplicates() throws Exception {
	Person.resetCount();
	String url = "https://ru.wikipedia.org/wiki/%D0%A4%D1%80%D0%B8%D0%B7%D0%BE_%D0%9E%D1%80%D0%B0%D0%BD%D1%81%D0%BA%D0%BE-%D0%9D%D0%B0%D1%81%D1%81%D0%B0%D1%83%D1%81%D0%BA%D0%B8%D0%B9";
	GenerateGenealogicalTree.main(new String[] { url });

	String fileName = "C:\\workspace\\GenerateGenealogicalTree.xls";

	// Анализ результатов
	FileInputStream file = new FileInputStream(new File(fileName));
	HSSFWorkbook workbook = new HSSFWorkbook(file);
	HSSFSheet sheet = workbook.getSheetAt(0);
	// Николай I, потому что больше не влезает
	assertTrue(sheet.getSheetName().equals("Древо Фризо Оранско-Нассауский"));

	Person.resetCount();
	Person frizo = new Person(
		"https://ru.wikipedia.org/wiki/%D0%A4%D1%80%D0%B8%D0%B7%D0%BE_%D0%9E%D1%80%D0%B0%D0%BD%D1%81%D0%BA%D0%BE-%D0%9D%D0%B0%D1%81%D1%81%D0%B0%D1%83%D1%81%D0%BA%D0%B8%D0%B9");
	frizo.setName("Фризо Оранско-Нассауский");
	frizo.setNameUrl("");
	frizo.setChild(3);
	Person luana = new Person(
		"https://ru.wikipedia.org/wiki/%D0%A4%D1%80%D0%B8%D0%B7%D0%BE_%D0%9E%D1%80%D0%B0%D0%BD%D1%81%D0%BA%D0%BE-%D0%9D%D0%B0%D1%81%D1%81%D0%B0%D1%83%D1%81%D0%BA%D0%B8%D0%B9#.D0.91.D1.80.D0.B0.D0.BA_.D0.B8_.D0.B4.D0.B5.D1.82.D0.B8");
	luana.setName("Брак и дети");
	luana.setNameUrl("Луана Оранско-Нассауская");
	luana.setNumberGeneration(1);
	luana.setParent(frizo.getId());

	System.out.println(luana.getParents().toString());

	assertPerson(sheet, 1, frizo);
	assertPerson(sheet, 2, luana);

	workbook.close();
	file.close();
    }

    private void assertPerson(HSSFSheet sheet, int rowNum, Person person) throws ParseException {
	Row row = sheet.getRow(rowNum);

	assertTrue(row.getCell(1).getStringCellValue().equals(person.getName()));

	String children = row.getCell(2).getStringCellValue();
	assertTrue(children.equals(person.getChildren().toString()));

	assertTrue(row.getCell(3).getStringCellValue().equals(person.getUrl()));

	assertTrue(row.getCell(4).getStringCellValue().equals(person.getNameUrl()));

	assertTrue(row.getCell(5).getStringCellValue().equals(String.valueOf(person.getNumberGeneration())));

	assertTrue(row.getCell(6).getStringCellValue().equals(person.getParents().toString()));
    }
}