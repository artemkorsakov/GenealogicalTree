/** http://fonkost.ru */
package ru.fonkost.pageObjects;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.WebDriver;

import ru.fonkost.entities.Person;

/**
 * Страница исторического лица. В зависимости от переданной ссылки определяется,
 * к какому домену принадлежит ссылка и возвращается экземпляр класса страницы
 * для заданного домена.
 *
 * @author Артём Корсаков
 */
public abstract class PersonPage {
    protected WebDriver driver;

    /**
     * Создание страницы в зависимости от домена.
     */
    public static PersonPage createPersonPage(WebDriver driver, String link) throws Exception {
	if (link == null) {
	    throw new Exception("Link can not be null");
	}
	URL url = new URL(link);
	String host = url.getHost();
	if (host.contains("wikipedia")) {
	    return new WikipediaPage(driver);
	} else if (host.contains("wikidata")) {
	    return new WikidataPage(driver);
	}
	throw new Exception("The page for the link " + link + " is not defined");
    }

    /**
     * Возвращает историческое лицо, вычисленное на основе данных её страницы.
     * <br>
     * 
     * В данном методе вычисляются все данные, кроме наименования ссылки,
     * которая есть только на странице родителя и со страницы персоны её
     * вычислить не удастся. Кроме того, здесь не вычисляются дети персоны, т.к.
     * текущая персона может "стать" "дубликатом" и может быть удалена - в этом
     * случае определение детей бессмысленно. Вычисление детей происходит в
     * отдельном методе, который вызывается только если персона - не дубликат.
     * <br>
     * 
     * Отдельно стоит упомянуть, почему переопределяется url, хотя он передается
     * в качестве параметра: дело в том, что в Wikipedia одной персоне может
     * быть посвящено несколько страниц, которые редиректятся на одну. В
     * результате, если использовать исходные урлы, то возможно возникновение
     * дубликатов. Поэтому в качестве урла используется тот урл, на который
     * редиректятся все остальные.
     */
    public abstract Person getPerson(String url) throws MalformedURLException;

    /**
     * Возвращает список урлов страниц детей персоны.
     * 
     * Для урлов с "якорями" дети не вычисляются, т.к., вероятнее всего, этот
     * якорь указывает на определённый блок внутри страницы родителя и в
     * результате будут повторно вычислены дети родителя, т.е. братья и сестры
     * текущей персоны.
     */
    public abstract List<Person> getChildrenUrl() throws MalformedURLException;
}