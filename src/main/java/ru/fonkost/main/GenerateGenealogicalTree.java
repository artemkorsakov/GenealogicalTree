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
import ru.fonkost.entities.PersonLink;
import ru.fonkost.pageObjects.PersonPage;
import ru.fonkost.utils.ExcelWorker;

/**
 * Генерация родословного дерева.
 *
 * @author Артём Корсаков
 */
public final class GenerateGenealogicalTree {
    private static WebDriver driver = DriverFactory.GetDriver();
    private static List<Person> AllPersons = new ArrayList<Person>();;
    private static List<PersonLink> AllLinks = new ArrayList<PersonLink>();;

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
	final String rurickUrl = "https://ru.wikipedia.org/wiki/Рюрик";
	final String genghisKhanUrl = "https://ru.wikipedia.org/wiki/Чингисхан";
	final String adamUrl = "https://ru.wikipedia.org/wiki/Адам";
	final String romanovUrl = "https://ru.wikipedia.org/wiki/Михаил_Фёдорович";
	final String fileName = "C:\\workspace\\GenerateGenealogicalTree.xls";

	AllLinks.add(new PersonLink("Родоначальник династии", romanovUrl));
	int i = 0;
	while (i < AllLinks.size()) {
	    DeterminePersonChildren(AllLinks.get(i));
	    i++;
	}
	UpdateChildrensLink();
	SaveResultAndQuit(fileName);
    }

    private static void DeterminePersonChildren(PersonLink currentPerson) throws Exception {
	PersonPage page = new PersonPage(driver);
	Person newPerson = page.GetPerson(currentPerson);
	int indexOfPerson = AllPersons.indexOf(newPerson);
	if (indexOfPerson == -1) {
	    AllPersons.add(newPerson);
	    indexOfPerson = AllPersons.size() - 1;
	}
	currentPerson.setName(indexOfPerson + "");

	List<PersonLink> childrensUrl = page.GetChildrensUrl();
	for (PersonLink link : childrensUrl) {
	    int indexOf = AllLinks.indexOf(link);
	    if (indexOf == -1) {
		AllLinks.add(link);
		indexOf = AllLinks.size() - 1;
	    }
	    newPerson.setChild(indexOf);
	}
    }

    private static void UpdateChildrensLink() {
	for (Person person : AllPersons) {
	    List<Integer> childrensId = person.getChildrens();
	    for (int i = 0; i < childrensId.size(); i++) {
		int temp = childrensId.get(i);
		String name = AllLinks.get(temp).getName();
		childrensId.set(i, Integer.getInteger(name));
	    }
	    person.setChildrens(childrensId);
	}
    }

    private static void SaveResultAndQuit(String fileName) throws ParseException {
	ExcelWorker excelWorker = new ExcelWorker();
	excelWorker.savePersons(fileName, AllPersons);
	excelWorker = null;
	AllLinks = null;
	AllPersons = null;
	driver.quit();
	driver = null;
    }
}