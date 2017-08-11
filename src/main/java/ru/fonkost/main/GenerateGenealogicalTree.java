/**
 * http://fonkost.ru
 */
package ru.fonkost.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverFactory;
import ru.fonkost.entities.GenealogicalTree;
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
    private static List<Person> AllPersons = new ArrayList<Person>();
    private static int counter = 0;

    public static void main(String[] args) throws Exception {
	final String rurickUrl = "https://ru.wikipedia.org/wiki/Рюрик";
	final String genghisKhanUrl = "https://ru.wikipedia.org/wiki/Чингисхан";
	final String adamUrl = "https://ru.wikipedia.org/wiki/Адам";
	final String romanovUrl = "https://ru.wikipedia.org/wiki/Михаил_Фёдорович";
	final String fileName = "C:\\workspace\\GenerateGenealogicalTree.xls";

	GenealogicalTree tree = new GenealogicalTree(romanovUrl);
	while (tree.hasUnvisitingPerson()) {
	    visitingAllLinks();
	}
	saveResultAndQuit(fileName);
    }

    /*
     * Посещение всех ссылок и формирование списка персон без списка детей
     */
    private static void visitingAllLinks() throws Exception {
	fillCurrentPerson();
	Person currentPerson = AllPersons.get(counter);
	int indexOfPerson = AllPersons.indexOf(currentPerson);
	if (indexOfPerson < counter) {
	    removeCurrentPerson(indexOfPerson);
	} else {
	    setChildCurrentPerson();
	    counter++;
	}
    }

    private static void fillCurrentPerson() {
	Person currentPerson = AllPersons.get(counter);
	driver.navigate().to(currentPerson.getUrl());
	PersonPage page = new PersonPage(driver);

	String name = page.GetName();
	currentPerson.setName(name);

	String url = driver.getCurrentUrl();
	currentPerson.setUrl(url);
    }

    private static void removeCurrentPerson(int newIndexPerson) {
	int oldIdPerson = AllPersons.get(counter).getId();
	int newIdPerson = AllPersons.get(newIndexPerson).getId();
	for (int i = 0; i < counter; i++) {
	    Person person = AllPersons.get(i);
	    person.replaceChild(oldIdPerson, newIdPerson);
	}
	AllPersons.remove(counter);
    }

    private static void setChildCurrentPerson() {
	Person currentPerson = AllPersons.get(counter);
	PersonPage page = new PersonPage(driver);
	List<Person> childrens = page.GetChildrensUrl();
	for (Person person : childrens) {
	    int index = AllPersons.indexOf(person);
	    if (index == -1) {
		AllPersons.add(person);
		currentPerson.setChild(person.getId());
	    } else {
		currentPerson.setChild(AllPersons.get(index).getId());
	    }
	}
    }

    private static void saveResultAndQuit(String fileName) throws ParseException {
	ExcelWorker excelWorker = new ExcelWorker();
	excelWorker.savePersons(fileName, AllPersons);
	excelWorker = null;
	AllPersons = null;
	driver.quit();
	driver = null;
    }
}