/**
 * http://fonkost.ru
 */
package ru.fonkost.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverFactory;
import ru.fonkost.entities.Person;
import ru.fonkost.pageObjects.PersonPage;
import ru.fonkost.utils.ExcelWorker;

/**
 * Генерация родословного дерева.
 *
 * @author Артём Корсаков
 */
public final class GenerateGenealogicalTree {
    private static WebDriver driver = DriverFactory.GetDriver();
    private static List<Person> AllPersons;

    /**
     * Вычисляем основателя династии и добавляем его в генеалогическое древо. В
     * цикле до тех пор, пока не достигнем конца списка древа, проходим по
     * каждой персоне: посещаем страницу, вычисляем урлы детей, переходим по
     * ним, вычисляем основные данные и формируем список детей персоны. Затем
     * подготавливаем следующую итерацию: добавляем в древо всех детей текущего
     * человека, если их ещё нет в списке, а также увеличиваем итератор.
     *
     * @param args
     *            the arguments
     * @throws Exception
     *             the exception
     */
    public static void main(String[] args) throws Exception {
	final String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";
	final String romanovUrl = "https://ru.wikipedia.org/wiki/Михаил_Фёдорович";
	final String fileName = "C:\\workspace\\GenerateGenealogicalTree.xls";

	DetermineAncestorOfADynasty(romanovUrl);
	int i = 0;
	while (i < AllPersons.size()) {
	    DeterminePersonChildren(AllPersons.get(i));
	    i++;
	}
	SaveResultAndQuit(fileName);
    }

    /*
     * Определение родоначальника династии
     */
    private static void DetermineAncestorOfADynasty(String url) throws IllegalArgumentException {
	driver.navigate().to(url);
	PersonPage page = new PersonPage(driver);
	Person AncestorOfADynasty = page.GetPerson();
	AllPersons = new ArrayList<Person>();
	AllPersons.add(AncestorOfADynasty);
    }

    /*
     * Определение детей персоны
     */
    private static void DeterminePersonChildren(Person currentPerson) throws Exception {
	PersonPage page = new PersonPage(driver);
	page.GoToUrl(currentPerson.getUrl());
	List<String> childrensUrl = page.GetChildrensUrl();
	for (String link : childrensUrl) {
	    page.GoToUrl(link);
	    Person newPerson = page.GetPerson();
	    int indexOf = AllPersons.indexOf(newPerson);
	    if (indexOf == -1) {
		currentPerson.setChild(newPerson.getId());
		AllPersons.add(newPerson);
		System.out.println(newPerson);
	    } else {
		currentPerson.setChild(AllPersons.get(indexOf).getId());
	    }
	}
    }

    private static void SaveResultAndQuit(String fileName) throws ParseException {
	ExcelWorker excelWorker = new ExcelWorker();
	excelWorker.savePersons(fileName, AllPersons);
	excelWorker = null;
	AllPersons = null;
	driver.quit();
	driver = null;
    }
}