/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ru.fonkost.entities.PersonLink;

/**
 * Тестирование класса PersonLink
 *
 * @author Артём Корсаков
 */
public class TestPersonLink {
    private String Text = "Александр";
    private String Url = "https://ru.wikipedia.org/wiki/%D0%92%D0%BB%D0%B0%D0%B4%D0%B8%D0%BC%D0%B8%D1%80_%D0%90%D0%BB%D0%B5%D0%BA%D1%81%D0%B0%D0%BD%D0%B4%D1%80%D0%BE%D0%B2%D0%B8%D1%87#.D0.A1.D0.B5.D0.BC.D1.8C.D1.8F";
    private String rurickName = "Рюрик";
    private String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";

    /**
     * Проверка имени и урла
     */
    @Test
    public void testNameAndUrl() throws Exception {
	PersonLink personLink = new PersonLink(Text, Url);
	assertTrue(personLink.getName().equals(Text));
	assertTrue(personLink.getUrl().equals(Url));
    }

    /**
     * Проверка текста ссылки на корректность
     * 
     * @throws Exception
     */
    @Test
    public void testIsCorrectName() throws Exception {
	assertTrue(new PersonLink("Александр 1", Url).IsCorrectName());
	assertFalse(new PersonLink("1 Александр", Url).IsCorrectName());
    }

    /**
     * Проверка эквивалентности
     */
    @Test
    public void testEquals() {
	PersonLink rurick = new PersonLink(rurickName, rurickUrl);

	// Сравнение с null
	assertFalse(rurick.equals(null));

	// Сравнение не с персоной
	assertFalse(rurick.equals(rurickUrl));
	assertFalse(rurick.equals(false));
	assertFalse(rurick.equals(1));

	// Сравнение с персонами
	assertTrue(rurick.equals(new PersonLink(rurickName, rurickUrl)));
	assertTrue(rurick.equals(new PersonLink("НеРюрик", rurickUrl)));
	assertFalse(rurick.equals(new PersonLink(rurickName, "НеРюрик")));
	assertFalse(rurick.equals(new PersonLink("НеРюрик", "НеРюрик")));
    }

    /**
     * Проверка хэш-суммы
     */
    @Test
    public void testHashCode() {
	PersonLink rurick = new PersonLink(rurickName, rurickUrl);
	assertTrue(rurick.hashCode() == rurickUrl.hashCode());
	assertTrue(rurick.hashCode() == new PersonLink(rurickName, rurickUrl).hashCode());
	assertFalse(rurick.hashCode() == "НеРюрик".hashCode());
	assertFalse(rurick.hashCode() == new PersonLink("НеРюрик", "НеРюрик").hashCode());
    }

    /**
     * Проверка поиска по массиву
     */
    @Test
    public void testContains() {
	PersonLink rurick = new PersonLink(rurickName, rurickUrl);
	List<PersonLink> list = new ArrayList<PersonLink>();
	assertFalse(list.contains(rurick));
	list.add(new PersonLink("НеРюрик", "НеРюрик"));
	assertFalse(list.contains(rurick));
	list.add(rurick);
	assertTrue(list.contains(rurick));
	list.remove(rurick);
	assertFalse(list.contains(rurick));
    }

    /**
     * Проверка индекса в массиве
     */
    @Test
    public void testIndexOf() {
	PersonLink rurick = new PersonLink(rurickName, rurickUrl);
	List<PersonLink> list = new ArrayList<PersonLink>();
	list.add(rurick);
	assertTrue(list.indexOf(rurick) == 0);
	assertTrue(list.indexOf(new PersonLink("НеРюрик", "НеРюрик")) == -1);
    }
}