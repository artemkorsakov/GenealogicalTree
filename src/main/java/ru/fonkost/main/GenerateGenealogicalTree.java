/**
 * http://fonkost.ru
 */
package ru.fonkost.main;

import java.net.MalformedURLException;
import java.net.URL;
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
 * Примеры урлов:
 * 
 * rurickUrl = "https://ru.wikipedia.org/wiki/Рюрик";
 * 
 * genghisKhanUrl = "https://ru.wikipedia.org/wiki/Чингисхан";
 * 
 * adamUrl = "https://ru.wikipedia.org/wiki/Адам";
 * 
 * romanovUrl = "https://ru.wikipedia.org/wiki/Михаил_Фёдорович";
 * 
 * @author Артём Корсаков
 */
public final class GenerateGenealogicalTree {
    public static void main(String[] args) throws Exception {
	String url = getUrl(args);
	GenealogicalTree tree = getGenealogicalTreeByUrl(url);
	saveResultAndQuit(tree);
    }

    private static String getUrl(String[] args) {
	if (args == null || args.length != 1) {
	    throw new IllegalArgumentException("Должен быть задан один параметр");
	}

	try {
	    URL url = new URL(args[0]);
	    return url.toString();
	} catch (MalformedURLException ex) {
	    throw new IllegalArgumentException("Некорректный урл " + args[0]);
	}
    }

    private static GenealogicalTree getGenealogicalTreeByUrl(String url) throws MalformedURLException {
	WebDriver driver = DriverFactory.GetDriver();
	Person person = new Person(url);
	GenealogicalTree tree = new GenealogicalTree(person);
	PersonPage page = new PersonPage(driver);
	while (tree.hasUnvisitingPerson()) {
	    String currentUrl = tree.getUnvisitingUrl();
	    Person currentPerson = page.getPerson(currentUrl);
	    tree.setCurrentPerson(currentPerson);
	    if (!tree.isCurrentPersonDeleted()) {
		List<Person> childrens = page.GetChildrensUrl();
		tree.setChildrens(childrens);
	    }
	    tree.calculateNextUnvisitingPerson();
	}
	driver.quit();
	return tree;
    }

    private static void saveResultAndQuit(GenealogicalTree tree) throws Exception {
	String fileName = "C:\\workspace\\GenerateGenealogicalTree.xls";
	ExcelWorker excelWorker = new ExcelWorker();
	excelWorker.savePersons(fileName, tree.getAllPersons());
    }
}