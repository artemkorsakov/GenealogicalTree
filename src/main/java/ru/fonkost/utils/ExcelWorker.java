/**
 * http://fonkost.ru
 */
package ru.fonkost.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import ru.fonkost.entities.Person;

/**
 * Работа с таблицами Excel.
 *
 * @author Артём Корсаков
 */
public class ExcelWorker {
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private int rowNum;

    /**
     * Сохранение списка персон в Excel-файле.
     *
     * @param fileName
     *            полное наименование файла
     * @param Persons
     *            список персон
     * @throws ParseException
     *             the parse exception
     */
    public void savePersons(String fileName, List<Person> Persons) throws ParseException {
	if (Persons.isEmpty()) {
	    return;
	}

	String dynastyName = "Генеалогическое древо " + Persons.get(0).getName();
	createSheet(dynastyName);
	for (Person person : Persons) {
	    savePerson(person);
	}
	saveSheet(fileName);
    }

    private void createSheet(String name) {
	workbook = new HSSFWorkbook();
	sheet = workbook.createSheet(name);
	rowNum = 0;
	saveRow("id", "name", "childrens", "url");
    }

    private void savePerson(Person person) throws ParseException {
	saveRow(String.valueOf(person.getId()), person.getName(), person.getChildrens().toString(), person.getUrl());
	System.out.println(person);
    }

    private void saveRow(String col1, String col2, String col3, String col4) {
	Row row = sheet.createRow(rowNum);
	row.createCell(0).setCellValue(col1);
	row.createCell(1).setCellValue(col2);
	row.createCell(2).setCellValue(col3);
	row.createCell(3).setCellValue(col4);
	rowNum++;
    }

    private void saveSheet(String fileName) {
	try {
	    FileOutputStream out = new FileOutputStream(new File(fileName));
	    workbook.write(out);
	    workbook.close();
	    out.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	workbook = null;
	sheet = null;
	rowNum = 0;
	System.out.println("Excel файл успешно создан!");
    }
}