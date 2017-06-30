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
     * Сохранение списка персон в Excel-файле
     */
    public void savePersons(String fileName, List<Person> Persons) throws ParseException {
	if (Persons.isEmpty()) {
	    return;
	}

	String dynastyName = Persons.get(0).getName();
	createSheet("Генеалогическое древо " + dynastyName);
	for (Person person : Persons) {
	    savePerson(person);
	}
	saveSheet(fileName);
    }

    private void createSheet(String name) {
	workbook = new HSSFWorkbook();
	sheet = workbook.createSheet(name);
	rowNum = 0;
	Row row = sheet.createRow(rowNum);
	row.createCell(0).setCellValue("id");
	row.createCell(1).setCellValue("name");
	row.createCell(2).setCellValue("childrens");
	row.createCell(3).setCellValue("url");
	rowNum++;
    }

    private void savePerson(Person person) throws ParseException {
	Row row = sheet.createRow(rowNum);
	row.createCell(0).setCellValue(person.getId());
	row.createCell(1).setCellValue(person.getName());
	row.createCell(2).setCellValue(person.getChildrens().toString());
	row.createCell(3).setCellValue(person.getUrl());
	rowNum++;
	System.out.println(person);
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