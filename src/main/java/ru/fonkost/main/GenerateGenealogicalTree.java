/**
 * http://fonkost.ru
 */
package ru.fonkost.main;

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
    private static ExcelWorker excelWorker = new ExcelWorker();
    private static List<Person> AllPersons = new ArrayList<Person>();

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
	final String fileName = "C:\\workspace\\GenerateGenealogicalTree.xls";

	DetermineAncestorOfADynasty(rurickUrl);

	int i = 0;
	while (i < AllPersons.size()) {
	    DeterminePersonChildren(i);
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
	excelWorker.createSheet("Генеалогическое древо " + AncestorOfADynasty.getName());
	AllPersons.add(AncestorOfADynasty);
    }

    /*
     * Определение детей персоны
     */
    private static void DeterminePersonChildren(int i) throws Exception {
	Person currentPerson = AllPersons.get(i);
	GoToUrl(currentPerson.getUrl());
	PersonPage page = new PersonPage(driver);
	List<String> childrensUrl = page.GetChildrensUrl();
	for (String link : childrensUrl) {
	    GoToUrl(link);
	    Person newPerson = page.GetPerson();
	    int indexOf = AllPersons.indexOf(newPerson);
	    if (indexOf == -1) {
		currentPerson.setChildren(newPerson.getId());
		AllPersons.add(newPerson);
	    } else {
		currentPerson.setChildren(AllPersons.get(indexOf).getId());
	    }
	}
	excelWorker.savePerson(currentPerson);
    }

    /*
     * Переход по заданному урлу только если текущий урл отличается от заданого.
     */
    private static void GoToUrl(String url) {
	if (!driver.getCurrentUrl().equals(url)) {
	    driver.navigate().to(url);
	}
    }

    /*
     * Сохранение результатов и обнуление ссылок.
     */
    private static void SaveResultAndQuit(String fileName) {
	excelWorker.saveSheet(fileName);
	excelWorker = null;
	AllPersons = null;
	Person.ResetCount();
	driver.quit();
	driver = null;
    }
}