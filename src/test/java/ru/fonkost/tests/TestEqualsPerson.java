/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ru.fonkost.entities.Person;

/**
 * Тестирование эквивалентности персон.
 *
 * @author Артём Корсаков
 */
public class TestEqualsPerson {
    private String rurickName = "Рюрик";
    private String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";
    private Person rurick = new Person(rurickName, rurickUrl);

    /**
     * Обнуляем счетчик персон перед запуском теста.
     */
    @Before
    public void setUp() {
	Person.ResetCount();
    }

    /**
     * Проверка эквивалентности
     */
    @Test
    public void testEquals() {
	// Сравнение с null
	assertFalse(rurick.equals(null));

	// Сравнение не с персоной
	assertFalse(rurick.equals(rurickUrl));
	assertFalse(rurick.equals(false));
	assertFalse(rurick.equals(1));

	// Сравнение с персонами
	assertTrue(rurick.equals(new Person(rurickName, rurickUrl)));
	assertTrue(rurick.equals(new Person(rurickName + "a", rurickUrl)));
	assertFalse(rurick.equals(new Person(rurickName, rurickUrl + "a")));
	assertFalse(rurick.equals(new Person(rurickName + "a", rurickUrl + "a")));
    }

    /**
     * Проверка хэш-суммы
     */
    @Test
    public void testHashCode() {
	assertTrue(rurick.hashCode() == rurickUrl.hashCode());
	assertTrue(rurick.hashCode() == new Person(rurickName, rurickUrl).hashCode());
	assertFalse(rurick.hashCode() == (rurickUrl + "a").hashCode());
	assertFalse(rurick.hashCode() == new Person(rurickName, rurickUrl + "a").hashCode());
    }

    /**
     * Проверка поиска по массиву
     */
    @Test
    public void testContains() {
	List<Person> AllPersons = new ArrayList<Person>();
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(new Person(rurickName + "a", rurickUrl + "a"));
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(rurick);
	assertTrue(AllPersons.contains(rurick));
	assertTrue(AllPersons.contains(new Person(rurickName + "a", rurickUrl)));
	assertFalse(AllPersons.contains(rurickUrl));
	AllPersons.remove(rurick);
	assertFalse(AllPersons.contains(rurick));
    }

    /**
     * Проверка индекса в массиве
     */
    @Test
    public void testIndexOf() {
	List<Person> AllPersons = new ArrayList<Person>();
	AllPersons.add(rurick);
	assertTrue(AllPersons.indexOf(rurick) == 0);
	assertTrue(AllPersons.indexOf(new Person(rurickName + "a", rurickUrl)) == 0);
	assertTrue(AllPersons.indexOf(new Person("НеРюрик", "НеРюрик")) == -1);
    }
}