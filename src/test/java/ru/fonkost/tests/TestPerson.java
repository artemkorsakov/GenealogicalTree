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
import ru.fonkost.entities.PersonLink;

/**
 * Тестирование класса Person
 *
 * @author Артём Корсаков
 */
public class TestPerson {
    private String rurickName = "Рюрик";
    private PersonLink rurickLink = new PersonLink("Рюрик",
	    "https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
    private PersonLink notRurickLink = new PersonLink("Рюрик", "НеРюрик");

    /**
     * Проверка имени и урла
     */
    @Test
    public void testNameAndUrl() throws Exception {
	Person person = new Person(rurickName, rurickLink);
	assertTrue(person.getName().equals(rurickName));
	assertTrue(person.getUrlName().equals("Рюрик"));
	assertTrue(person.getUrl().equals("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA"));
    }

    /**
     * Проверка формирования идентификаторов и их обнуление
     */
    @Test
    public void testIdAndResetCount() throws Exception {
	Person.ResetCount();
	Person person = new Person(rurickName, rurickLink);
	assertTrue(person.getId() == 1);
	Person person2 = new Person(rurickName, rurickLink);
	assertTrue(person.getId() == 1);
	assertTrue(person2.getId() == 2);
	Person.ResetCount();
	Person person3 = new Person(rurickName, rurickLink);
	assertTrue(person.getId() == 1);
	assertTrue(person2.getId() == 2);
	assertTrue(person3.getId() == 1);
    }

    /**
     * Проверка добавления одного ребенка к персоне
     */
    @Test
    public void testSetChild() throws Exception {
	Person rurick = new Person(rurickName, rurickLink);
	assertTrue(rurick.getChildrens().isEmpty());
	rurick.setChild(rurick.getId());
	assertTrue(rurick.getChildrens().isEmpty());

	PersonLink igorLink = new PersonLink("Игорь", "https://ru.wikipedia.org/wiki/Игорь Рюрикович");
	Person igor = new Person("Игорь Рюрикович", igorLink);
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
	Person svyatoslav = new Person("Святослав Игоревич",
		new PersonLink("Святослав", "https://ru.wikipedia.org/wiki/Святослав Игоревич"));
	Person yaropolk = new Person("Ярополк Святославич",
		new PersonLink("Ярополк", "https://ru.wikipedia.org/wiki/Ярополк Святославич"));
	Person oleg = new Person("Олег Святославич (князь древлянский)",
		new PersonLink("Олег", "https://ru.wikipedia.org/wiki/Олег Святославич (князь древлянский)"));
	Person vladimir = new Person("Владимир Святославич",
		new PersonLink("Владимир", "https://ru.wikipedia.org/wiki/Владимир Святославич"));
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
	Person rurick = new Person(rurickName, rurickLink);

	// Сравнение с null
	assertFalse(rurick.equals(null));

	// Сравнение не с персоной
	assertFalse(rurick.equals(rurickLink));
	assertFalse(rurick.equals(false));
	assertFalse(rurick.equals(1));

	// Сравнение с персонами
	assertTrue(rurick.equals(new Person(rurickName, rurickLink)));
	assertTrue(rurick.equals(new Person("НеРюрик", rurickLink)));
	assertFalse(rurick.equals(new Person(rurickName, notRurickLink)));
	assertFalse(rurick.equals(new Person("НеРюрик", notRurickLink)));
    }

    /**
     * Проверка хэш-суммы
     */
    @Test
    public void testHashCode() {
	Person rurick = new Person(rurickName, rurickLink);
	assertTrue(rurick.hashCode() == rurickLink.hashCode());
	assertTrue(rurick.hashCode() == new Person(rurickName, rurickLink).hashCode());
	assertFalse(rurick.hashCode() == "НеРюрик".hashCode());
	assertFalse(rurick.hashCode() == new Person("НеРюрик", notRurickLink).hashCode());
    }

    /**
     * Проверка поиска по массиву
     */
    @Test
    public void testContains() {
	Person rurick = new Person(rurickName, rurickLink);
	List<Person> AllPersons = new ArrayList<Person>();
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(new Person("НеРюрик", notRurickLink));
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
	Person rurick = new Person(rurickName, rurickLink);
	List<Person> AllPersons = new ArrayList<Person>();
	AllPersons.add(rurick);
	assertTrue(AllPersons.indexOf(rurick) == 0);
	assertTrue(AllPersons.indexOf(new Person("НеРюрик", notRurickLink)) == -1);
    }
}