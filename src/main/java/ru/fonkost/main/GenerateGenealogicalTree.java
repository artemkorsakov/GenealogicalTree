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
    private static List<Person> AllPersons = new ArrayList<Person>();
    private static List<PersonLink> AllLinks = new ArrayList<PersonLink>();
    private static List<Integer> RelationsAllLinks = new ArrayList<Integer>();
    private static List<String> RelationsAllPersons = new ArrayList<String>();

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

	AllLinks.add(new PersonLink(null, romanovUrl));
	RelationsAllLinks.add(-1);
	int i = 0;
	while (i < AllLinks.size()) {
	    VisitingAllLinks(i);
	    i++;
	}
	UpdateChildrens();
	SaveResultAndQuit(fileName);
    }

    /*
     * Посещение всех ссылок и формирование списка персон без списка детей
     */
    private static void VisitingAllLinks(int i) throws Exception {
	PersonLink currentLink = AllLinks.get(i);
	PersonPage page = new PersonPage(driver);
	Person newPerson = page.GetPerson(currentLink);
	int indexOfPerson = AllPersons.indexOf(newPerson);
	if (indexOfPerson == -1) {
	    AllPersons.add(newPerson);
	    RelationsAllPersons.add("");
	    indexOfPerson = AllPersons.size() - 1;
	}
	RelationsAllLinks.set(i, indexOfPerson);

	List<PersonLink> childrensUrl = page.GetChildrensUrl();
	RelationsAllPersons.set(indexOfPerson, "");
	for (PersonLink link : childrensUrl) {
	    int indexOfLink = AllLinks.indexOf(link);
	    if (indexOfLink == -1) {
		AllLinks.add(link);
		RelationsAllLinks.add(-1);
		indexOfLink = AllLinks.size() - 1;
	    }
	    String linkIds = RelationsAllPersons.get(indexOfPerson);
	    RelationsAllPersons.set(indexOfPerson, linkIds + " " + indexOfLink);
	}
    }

    /*
     * Заполнение списка детей для всех персон
     */
    private static void UpdateChildrens() {
	for (int i = 0; i < RelationsAllPersons.size(); i++) {
	    String[] arr = RelationsAllPersons.get(i).split(" ");
	    for (String npalId : arr) {
		try {
		    int alId = Integer.parseInt(npalId);
		    Integer apId = RelationsAllLinks.get(alId);
		    AllPersons.get(i).setChild(AllPersons.get(apId).getId());
		} catch (NumberFormatException ex) {
		}
	    }
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