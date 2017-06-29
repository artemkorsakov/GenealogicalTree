/**
 * http://fonkost.ru
 */
package ru.fonkost.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

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
    private static HSSFWorkbook workbook = null;
    private static HSSFSheet sheet = null;
    private static int rowNum = 0;

    /**
     * Создание листа Excel.
     */
    public void createSheet(String name) {
	workbook = new HSSFWorkbook();
	sheet = workbook.createSheet(name);
	Row row = sheet.createRow(rowNum);
	row.createCell(0).setCellValue("id");
	row.createCell(1).setCellValue("name");
	row.createCell(2).setCellValue("childrens");
	row.createCell(3).setCellValue("url");
	rowNum++;
    }

    /**
     * Сохранение персоны.
     *
     * @param person
     *            the person
     * @throws ParseException
     *             the parse exception
     */
    public void savePerson(Person person) throws ParseException {
	Row row = sheet.createRow(rowNum);
	row.createCell(0).setCellValue(person.getId());
	row.createCell(1).setCellValue(person.getName());
	row.createCell(2).setCellValue(person.getChildrens().toString());
	row.createCell(3).setCellValue(person.getUrl());
	rowNum++;

	System.out.println(person);
    }

    /**
     * Сохраняем созданный в памяти Excel документ в файл
     */
    public void saveSheet(String fileName) {
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