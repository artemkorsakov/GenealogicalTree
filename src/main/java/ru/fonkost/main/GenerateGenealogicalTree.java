/** http://fonkost.ru */
package ru.fonkost.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.WebDriver;

import ru.fonkost.driverHelper.DriverHelper;
import ru.fonkost.entities.GenealogicalTree;
import ru.fonkost.entities.Person;
import ru.fonkost.pageObjects.PersonPage;
import ru.fonkost.utils.ExcelWorker;

/**
 * Генерация родословного дерева на основе данных Wikipedia. <br>
 * Статьи по генерации выложены на сайте
 * <a href="http://fonkost.ru">http://fonkost.ru</a>. <br>
 * Генерация разобрана на примере: <br>
 * <ul>
 * <li><a href="https://ru.wikipedia.org/wiki/%D0%90%D0%B4%D0%B0%D0%BC">Адама
 * </a></li>
 * <li><a href="https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA">
 * Рюрика - основателя династии Рюриковичей</a></li>
 * <li><a href=
 * "https://ru.wikipedia.org/wiki/%D0%9C%D0%B8%D1%85%D0%B0%D0%B8%D0%BB_%D0%A4%D1%91%D0%B4%D0%BE%D1%80%D0%BE%D0%B2%D0%B8%D1%87">
 * Михаила Фёдоровича - основателя династии Романовых</a></li>
 * <li><a href=
 * "https://ru.wikipedia.org/wiki/%D0%A7%D0%B8%D0%BD%D0%B3%D0%B8%D1%81%D1%85%D0%B0%D0%BD">
 * Чингисхана</a></li>
 * </ul>
 */
public final class GenerateGenealogicalTree {
    /**
     * Генерация родословного дерева на основе данных Wikipedia. <br>
     * Результаты генерации сохраняются в Excel-файле. Планируется добавление
     * сохранения в БД.
     *
     * @param args
     *            url основателя династии
     * @throws Exception
     *             исключение выдается, если задан не один параметр или если url
     *             основателя династии не является страницей Wikipedia
     */
    public static void main(String[] args) throws Exception {
	String url = getUrl(args);
	GenealogicalTree tree = getGenealogicalTreeByUrl(url);
	saveResultAndQuit(tree);
    }

    /**
     * Возвращается url основателя династии, если передан корректный параметр
     */
    private static String getUrl(String[] args) {
	if (args == null || args.length != 1) {
	    throw new IllegalArgumentException("Должен быть задан один параметр");
	}

	try {
	    URL url = new URL(args[0]);
	    if (url.getHost().contains("wikipedia.org")) {
		throw new IllegalArgumentException(
			"Алгоритм предназначен для генерации родословного древа только на основе данных Wikipedia");
	    }
	    return url.toString();
	} catch (MalformedURLException ex) {
	    throw new IllegalArgumentException("Некорректный урл " + args[0]);
	}
    }

    /**
     * Алгоритм генерации родословного древа. <br>
     * <ul>
     * <li>Создается основатель династии на основе заданного урла</li>
     * <li>Создается родословное древо на основе основателя династии</li>
     * <li>В цикле до тех пор, пока есть "непосещенные" персоны</li>
     * <li>Вычисляется персона на основе текущего урла родословного древа</li>
     * <li>Эта персона устанавливается в качестве текущей</li>
     * <li>Если текущая персона не удалена, то вычисляется и устанавливается
     * список её детей</li>
     * <li>Текущая персона обновляется</li>
     * <li>Переход к следующей итерации цикла</li>
     * </ul>
     * 
     * @param url
     *            url основателя династии
     * @return родословное древо
     */
    public static GenealogicalTree getGenealogicalTreeByUrl(String url) throws MalformedURLException {
	WebDriver driver = DriverHelper.getDriver();
	Person person = new Person(url);
	GenealogicalTree tree = new GenealogicalTree(person);
	PersonPage page = new PersonPage(driver);
	while (tree.hasUnvisitingPerson()) {
	    String currentUrl = tree.getCurrentUrl();
	    Person currentPerson = page.getPerson(currentUrl);
	    tree.setCurrentPerson(currentPerson);
	    if (!tree.isCurrentPersonDeleted()) {
		List<Person> childrens = page.getChildrenUrl();
		tree.setChildren(childrens);
	    }
	    tree.updatingCurrentPerson();
	}
	driver.quit();
	return tree;
    }

    /** Сохранение результатов генерации в Excel-файле */
    private static void saveResultAndQuit(GenealogicalTree tree) throws Exception {
	String fileName = "C:\\workspace\\GenerateGenealogicalTree.xls";
	ExcelWorker excelWorker = new ExcelWorker();
	excelWorker.savePersons(fileName, tree.getGenealogicalTree());
    }
}