/** http://fonkost.ru */
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
 * Класс сохранения родословного древа в виде Excel-таблицы. <br>
 * После реализации сохранения в БД, данный класс использоваться не будет и
 * будет оставлен в качестве Deprecated
 */
public class ExcelWorker {
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private int rowNum;

    public void savePersons(String fileName, List<Person> Persons) throws ParseException {
	if (Persons.isEmpty()) {
	    return;
	}

	String dynastyName = "Древо " + Persons.get(0).getName();
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
	saveRow("id", "name", "children", "url", "urlName", "numGen", "parents");
    }

    private void savePerson(Person person) throws ParseException {
	saveRow(String.valueOf(person.getId()), person.getName(), person.getChildren().toString(), person.getUrl(),
		person.getNameUrl(), String.valueOf(person.getNumberGeneration()), person.getParents().toString());
	System.out.println(person);
    }

    private void saveRow(String col1, String col2, String col3, String col4, String col5, String col6, String col7) {
	Row row = sheet.createRow(rowNum);
	row.createCell(0).setCellValue(col1);
	row.createCell(1).setCellValue(col2);
	row.createCell(2).setCellValue(col3);
	row.createCell(3).setCellValue(col4);
	row.createCell(4).setCellValue(col5);
	row.createCell(5).setCellValue(col6);
	row.createCell(6).setCellValue(col7);
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