/**
 * http://fonkost.ru
 */
package ru.fonkost.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    @Test
    public void testCreatePerson() throws Exception {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(rurick.getUrl().equals("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA"));
	try {
	    new Person(null);
	    fail("Создалась персона с url равным null");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Url должен иметь непустое значение"));
	}
	try {
	    new Person("");
	    fail("Создалась персона с пустым url");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Url должен иметь непустое значение"));
	}
	try {
	    new Person("   ");
	    fail("Создалась персона с пробельным url");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Url должен иметь непустое значение"));
	}
    }

    @Test
    public void testSetUrl() throws Exception {
	Person rurick = new Person("Рюрик");
	rurick.setUrl("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	assertTrue(rurick.getUrl().equals("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA"));
	try {
	    rurick.setUrl(null);
	    fail("Для персоны удалось установить url равным null");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Url должен иметь непустое значение"));
	}
	try {
	    rurick.setUrl("");
	    fail("Для персоны удалось установить пустой url");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Url должен иметь непустое значение"));
	}
	try {
	    rurick.setUrl("   ");
	    fail("Для персоны удалось установить пробельный url");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Url должен иметь непустое значение"));
	}
    }

    @Test
    public void testSetName() throws Exception {
	Person p = new Person("Url");
	p.setName("Рюрик");
	assertTrue(p.getName().equals("Рюрик"));
	try {
	    p.setName(null);
	    fail("Создалась персона с именем равным null");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Имя должно иметь непустое значение"));
	}
	try {
	    p.setName("");
	    fail("Создалась персона с пустым именем");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Имя должно иметь непустое значение"));
	}
	try {
	    p.setName("   ");
	    fail("Создалась персона с пробельным именем");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Имя должно иметь непустое значение"));
	}
    }

    @Test
    public void testSetNameUrl() throws Exception {
	Person p = new Person("Url");
	p.setNameUrl(null);
	assertTrue(p.getNameUrl().equals("Неизвестно"));
	p.setNameUrl("");
	assertTrue(p.getNameUrl().equals("Неизвестно"));
	p.setNameUrl("   ");
	assertTrue(p.getNameUrl().equals("Неизвестно"));
    }

    @Test
    public void testCorrectPerson() throws Exception {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA");
	rurick.setName("Рюрик");
	rurick.setNameUrl("РюрикUrl");
	assertTrue(rurick.getName().equals("Рюрик"));
	assertTrue(rurick.getUrl().equals("https://ru.wikipedia.org/wiki/%D0%A0%D1%8E%D1%80%D0%B8%D0%BA"));
	assertTrue(rurick.getNameUrl().equals("РюрикUrl"));
    }

    @Test
    public void testCorrectNameUrl() throws Exception {
	Person rurick = new Person("Рюрик");
	rurick.setNameUrl("Александр 1");
	assertTrue(rurick.IsCorrectNameUrl());
	rurick.setNameUrl("1 Александр");
	assertFalse(rurick.IsCorrectNameUrl());
	rurick.setNameUrl("1");
	assertFalse(rurick.IsCorrectNameUrl());
    }

    /**
     * Проверка формирования идентификаторов и их обнуление
     */
    @Test
    public void testIdAndResetCount() throws Exception {
	Person.ResetCount();
	Person person = new Person("Рюрик");
	assertTrue(person.getId() == 1);
	Person person2 = new Person("Рюрик");
	assertTrue(person.getId() == 1);
	assertTrue(person2.getId() == 2);
	Person.ResetCount();
	Person person3 = new Person("Рюрик");
	assertTrue(person.getId() == 1);
	assertTrue(person2.getId() == 2);
	assertTrue(person3.getId() == 1);
    }

    /**
     * Проверка добавления одного ребенка к персоне
     */
    @Test
    public void testSetChild() throws Exception {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	assertTrue(rurick.getChildrens().isEmpty());

	Person igor = new Person("https://ru.wikipedia.org/wiki/Игорь Рюрикович");
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
     * Проверка добавления нескольких детей к персоне
     */
    @Test
    public void testSetChildrens() throws Exception {
	Person svyatoslav = new Person("https://ru.wikipedia.org/wiki/Святослав Игоревич");
	Person yaropolk = new Person("https://ru.wikipedia.org/wiki/Ярополк Святославич");
	Person oleg = new Person("https://ru.wikipedia.org/wiki/Олег Святославич (князь древлянский)");
	Person vladimir = new Person("https://ru.wikipedia.org/wiki/Владимир Святославич");
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

    @Test
    public void testReplaceChild() {
	Person person = new Person("Рюрик");

	// Пустой список
	person.replaceChild(1, 2);
	assertTrue(person.getChildrens().isEmpty());

	person.setChild(1);
	person.setChild(2);
	person.setChild(3);
	assertTrue(person.getChildrens().size() == 3);
	assertTrue(person.getChildrens().contains(1));
	assertTrue(person.getChildrens().contains(2));
	assertTrue(person.getChildrens().contains(3));

	// Замена на себя
	person.replaceChild(2, 2);
	assertTrue(person.getChildrens().size() == 3);
	assertTrue(person.getChildrens().contains(1));
	assertTrue(person.getChildrens().contains(2));
	assertTrue(person.getChildrens().contains(3));

	// Замена существуюшего на несуществующий номер
	person.replaceChild(2, 4);
	assertTrue(person.getChildrens().size() == 3);
	assertTrue(person.getChildrens().contains(1));
	assertTrue(person.getChildrens().contains(4));
	assertTrue(person.getChildrens().contains(3));
	assertFalse(person.getChildrens().contains(2));

	// Замена несуществующего на существующий
	person.replaceChild(5, 1);
	assertTrue(person.getChildrens().size() == 3);
	assertTrue(person.getChildrens().contains(1));
	assertTrue(person.getChildrens().contains(4));
	assertTrue(person.getChildrens().contains(3));
	assertFalse(person.getChildrens().contains(5));

	// Замена несуществующего на несуществующий
	person.replaceChild(6, 7);
	assertTrue(person.getChildrens().size() == 3);
	assertTrue(person.getChildrens().contains(1));
	assertTrue(person.getChildrens().contains(4));
	assertTrue(person.getChildrens().contains(3));
	assertFalse(person.getChildrens().contains(6));
	assertFalse(person.getChildrens().contains(7));

	// Замена на существующий номер
	person.replaceChild(4, 3);
	assertTrue(person.getChildrens().size() == 2);
	assertTrue(person.getChildrens().contains(1));
	assertTrue(person.getChildrens().contains(3));
	assertFalse(person.getChildrens().contains(4));
    }

    @Test
    public void testEquals() {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");

	// Сравнение с null
	assertFalse(rurick.equals(null));

	// Сравнение не с персоной
	assertFalse(rurick.equals("https://ru.wikipedia.org/wiki/Рюрик"));
	assertFalse(rurick.equals(false));
	assertFalse(rurick.equals(1));

	// Сравнение с персонами
	assertTrue(rurick.equals(new Person("https://ru.wikipedia.org/wiki/Рюрик")));
	assertFalse(rurick.equals(new Person("НеРюрик")));
    }

    @Test
    public void testHashCode() {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	assertTrue(rurick.hashCode() == "https://ru.wikipedia.org/wiki/Рюрик".hashCode());
	assertTrue(rurick.hashCode() == new Person("https://ru.wikipedia.org/wiki/Рюрик").hashCode());
	assertFalse(rurick.hashCode() == "НеРюрик".hashCode());
	assertFalse(rurick.hashCode() == new Person("НеРюрик").hashCode());
    }

    @Test
    public void testContains() {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	List<Person> AllPersons = new ArrayList<Person>();
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(new Person("НеРюрик"));
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(rurick);
	assertTrue(AllPersons.contains(rurick));
	AllPersons.remove(rurick);
	assertFalse(AllPersons.contains(rurick));
    }

    @Test
    public void testIndexOf() {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	List<Person> AllPersons = new ArrayList<Person>();
	AllPersons.add(rurick);
	assertTrue(AllPersons.indexOf(rurick) == 0);
	assertTrue(AllPersons.indexOf(new Person("НеРюрик")) == -1);
    }

    @Test
    public void testToString() {
	Person.ResetCount();
	Person rurick = new Person("Ссылка");
	rurick.setName("Имя");
	rurick.setNameUrl("ИмяСсылки");
	assertTrue(rurick.toString().equals("name=Имя; id=1; url=Ссылка; nameUrl=ИмяСсылки; childrens=[]"));
    }
}