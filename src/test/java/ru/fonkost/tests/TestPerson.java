/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ru.fonkost.entities.Person;

/**
 * Тестирование класса Person
 *
 * @author Артём Корсаков
 */
public class TestPerson {
    private String rurickName = "Рюрик";
    private String rurickUrl = "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA";

    /**
     * Проверка имени и урла
     */
    @Test
    public void testNameAndUrl() throws Exception {
	Person person = new Person(rurickName, rurickUrl);
	assertTrue(person.getName().equals("Рюрик"));
	assertTrue(person.getUrl().equals(rurickUrl));
    }

    /**
     * Проверка формирования идентификаторов и их обнуление
     */
    @Test
    public void testIdAndResetCount() throws Exception {
	Person.ResetCount();
	Person person = new Person(rurickName, rurickUrl);
	assertTrue(person.getId() == 1);
	Person person2 = new Person(rurickName, rurickUrl);
	assertTrue(person.getId() == 1);
	assertTrue(person2.getId() == 2);
	Person.ResetCount();
	Person person3 = new Person(rurickName, rurickUrl);
	assertTrue(person.getId() == 1);
	assertTrue(person2.getId() == 2);
	assertTrue(person3.getId() == 1);
    }

    /**
     * Проверка добавления одного ребенка к персоне
     */
    @Test
    public void testSetChild() throws Exception {
	Person rurick = new Person(rurickName, rurickUrl);
	assertTrue(rurick.getChildrens().isEmpty());
	rurick.setChild(rurick.getId());
	assertTrue(rurick.getChildrens().isEmpty());

	Person igor = new Person("Игорь Рюрикович", "https://ru.wikipedia.org/wiki/Игорь Рюрикович");
	assertTrue(rurick.getChildrens().isEmpty());
	assertTrue(igor.getChildrens().isEmpty());
	rurick.setChild(igor.getId());
	assertTrue(rurick.getChildrens().get(0) == igor.getId());
	assertTrue(rurick.getChildrens().size() == 1);
	assertTrue(igor.getChildrens().isEmpty());
	rurick.setChild(igor.getId());
	assertTrue(rurick.getChildrens().get(0) == igor.getId());
	assertTrue(rurick.getChildrens().size() == 1);
	assertTrue(igor.getChildrens().isEmpty());
    }

    /**
     * Проверка добавления детей к персоне
     */
    @Test
    public void testSetChildrens() throws Exception {
	Person svyatoslav = new Person("Святослав Игоревич", "https://ru.wikipedia.org/wiki/Святослав Игоревич");
	Person yaropolk = new Person("Ярополк Святославич", "https://ru.wikipedia.org/wiki/Ярополк Святославич");
	Person oleg = new Person("Олег Святославич (князь древлянский)",
		"https://ru.wikipedia.org/wiki/Олег Святославич (князь древлянский)");
	Person vladimir = new Person("Владимир Святославич", "https://ru.wikipedia.org/wiki/Владимир Святославич");
	assertTrue(svyatoslav.getChildrens().isEmpty());
	assertTrue(yaropolk.getChildrens().isEmpty());
	assertTrue(oleg.getChildrens().isEmpty());
	assertTrue(vladimir.getChildrens().isEmpty());

	svyatoslav.setChild(yaropolk.getId());
	svyatoslav.setChild(oleg.getId());
	svyatoslav.setChild(vladimir.getId());

	assertTrue(svyatoslav.getChildrens().get(0) == yaropolk.getId());
	assertTrue(svyatoslav.getChildrens().get(1) == oleg.getId());
	assertTrue(svyatoslav.getChildrens().get(2) == vladimir.getId());
	assertTrue(svyatoslav.getChildrens().size() == 3);
	assertTrue(yaropolk.getChildrens().isEmpty());
	assertTrue(oleg.getChildrens().isEmpty());
	assertTrue(vladimir.getChildrens().isEmpty());
    }

    /**
     * Проверка эквивалентности
     */
    @Test
    public void testEquals() {
	Person rurick = new Person(rurickName, rurickUrl);

	// Сравнение с null
	assertFalse(rurick.equals(null));

	// Сравнение не с персоной
	assertFalse(rurick.equals(rurickUrl));
	assertFalse(rurick.equals(false));
	assertFalse(rurick.equals(1));

	// Сравнение с персонами
	assertTrue(rurick.equals(new Person(rurickName, rurickUrl)));
	assertTrue(rurick.equals(new Person("НеРюрик", rurickUrl)));
	assertFalse(rurick.equals(new Person(rurickName, "НеРюрик")));
	assertFalse(rurick.equals(new Person("НеРюрик", "НеРюрик")));
    }

    /**
     * Проверка хэш-суммы
     */
    @Test
    public void testHashCode() {
	Person rurick = new Person(rurickName, rurickUrl);
	assertTrue(rurick.hashCode() == rurickUrl.hashCode());
	assertTrue(rurick.hashCode() == new Person(rurickName, rurickUrl).hashCode());
	assertFalse(rurick.hashCode() == "НеРюрик".hashCode());
	assertFalse(rurick.hashCode() == new Person("НеРюрик", "НеРюрик").hashCode());
    }

    /**
     * Проверка поиска по массиву
     */
    @Test
    public void testContains() {
	Person rurick = new Person(rurickName, rurickUrl);
	List<Person> AllPersons = new ArrayList<Person>();
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(new Person("НеРюрик", "НеРюрик"));
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(rurick);
	assertTrue(AllPersons.contains(rurick));
	AllPersons.remove(rurick);
	assertFalse(AllPersons.contains(rurick));
    }

    /**
     * Проверка индекса в массиве
     */
    @Test
    public void testIndexOf() {
	Person rurick = new Person(rurickName, rurickUrl);
	List<Person> AllPersons = new ArrayList<Person>();
	AllPersons.add(rurick);
	assertTrue(AllPersons.indexOf(rurick) == 0);
	assertTrue(AllPersons.indexOf(new Person("НеРюрик", "НеРюрик")) == -1);
    }
}