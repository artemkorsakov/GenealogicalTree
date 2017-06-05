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

/**
 * Генерация родословного дерева.
 *
 * @author Артём Корсаков
 */
public final class GenerateGenealogicalTree {
    private static String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";

    /**
     * Открываем страницу основателя династии. Переходим по урлу и вычисляем его
     * основные данные. Создаем список непосещенных персон, в который добавляем
     * основателя династии. В цикле до тех пор, пока список непосещенных персон
     * не опустеет, проходим по каждой персоне: посещаем страницу этой персоны,
     * вычисляем урлы детей персоны, переходим по ним, вычисляем основные данные
     * детей и формируем список детей персоны. Затем подготавливаем следующую
     * итерацию: добавляем в список непосещенных персон всех детей текущего
     * человека, а также удаляем из списка просмотренного текущего человека. И
     * так до тех пор, пока все персоны не будут посещены.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) throws Exception {
	WebDriver driver = DriverFactory.GetDriver();
	driver.navigate().to(rurickUrl);
	PersonPage page = new PersonPage(driver);
	Person AncestorOfADynasty = page.GetPerson();

	List<Person> UnvisitedPersons = new ArrayList<Person>();
	UnvisitedPersons.add(AncestorOfADynasty);

	while (UnvisitedPersons.size() > 0) {
	    Person currentPerson = UnvisitedPersons.get(0);
	    GoToUrl(driver, currentPerson.getUrl());
	    List<String> childrensUrl = page.GetChildrensUrl();
	    for (String link : childrensUrl) {
		GoToUrl(driver, link);
		Person person = page.GetPerson();
		currentPerson.setChildren(person);
	    }
	    UnvisitedPersons.remove(currentPerson);
	    UnvisitedPersons.addAll(currentPerson.getChildrens());
	    LogResult(currentPerson);
	}

	driver.quit();
	driver = null;
    }

    private static void LogResult(Person person) {
	System.out.println(person);
    }

    private static void GoToUrl(WebDriver driver, String url) {
	if (!driver.getCurrentUrl().equals(url)) {
	    driver.navigate().to(url);
	}
    }
}