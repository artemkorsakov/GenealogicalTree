/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ru.fonkost.entities.Person;

/** Тестирование класса Person */
public class TestPerson {
    @Test
    public void testCreatePerson() throws Exception {
	Person rurick = new Person("https://ru.wikipedia.org");
	assertTrue(rurick.getUrl().equals("https://ru.wikipedia.org"));
	try {
	    new Person(null);
	    fail("Создалась персона с нулевым url");
	} catch (MalformedURLException ex) {
	}
	try {
	    new Person("");
	    fail("Создалась персона с url равным пустой строке");
	} catch (MalformedURLException ex) {
	}
	try {
	    new Person("ru.wikipedia.org");
	    fail("Создалась персона с некорректным url");
	} catch (MalformedURLException ex) {
	}
    }

    @Test
    public void testSetUrl() throws Exception {
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setUrl("https://ru.wikipedia.org/wiki/Рюрик");
	assertTrue(rurick.getUrl().equals("https://ru.wikipedia.org/wiki/Рюрик"));
	try {
	    rurick.setUrl(null);
	    fail("Для персоны удалось установить url равным null");
	} catch (MalformedURLException ex) {
	}
	try {
	    rurick.setUrl("");
	    fail("Создалась персона с url равным пустой строке");
	} catch (MalformedURLException ex) {
	}
	try {
	    rurick.setUrl("ru.wikipedia.org");
	    fail("Создалась персона с некорректным url");
	} catch (MalformedURLException ex) {
	}
    }

    @Test
    public void testSetName() throws Exception {
	Person p = new Person("https://ru.wikipedia.org");
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
	Person p = new Person("https://ru.wikipedia.org");
	p.setNameUrl(null);
	assertTrue(p.getNameUrl().equals(""));
	p.setNameUrl("");
	assertTrue(p.getNameUrl().equals(""));
	p.setNameUrl("   ");
	assertTrue(p.getNameUrl().equals(""));
    }

    @Test
    public void testPersonthisCorrectNameAndNameUrl() throws Exception {
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setName("Рюрик");
	rurick.setNameUrl("РюрикUrl");
	assertTrue(rurick.getName().equals("Рюрик"));
	assertTrue(rurick.getUrl().equals("https://ru.wikipedia.org"));
	assertTrue(rurick.getNameUrl().equals("РюрикUrl"));
    }

    @Test
    public void testIsCorrectNameUrl() throws Exception {
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setNameUrl("Александр 1");
	assertTrue(rurick.isCorrectNameUrl());
	rurick.setNameUrl("1 Александр");
	assertFalse(rurick.isCorrectNameUrl());
	rurick.setNameUrl("1");
	assertFalse(rurick.isCorrectNameUrl());
    }

    @Test
    public void testIdAndResetCount() throws Exception {
	Person.resetCount();
	Person person = new Person("https://ru.wikipedia.org");
	assertTrue(person.getId() == 1);
	Person person2 = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	assertTrue(person.getId() == 1);
	assertTrue(person2.getId() == 2);
	Person.resetCount();
	Person person3 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	assertTrue(person.getId() == 1);
	assertTrue(person2.getId() == 2);
	assertTrue(person3.getId() == 1);
    }

    @Test
    public void testSetChild() throws Exception {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	assertTrue(rurick.getChildren().isEmpty());

	Person igor = new Person("https://ru.wikipedia.org/wiki/Игорь Рюрикович");
	assertTrue(rurick.getChildren().isEmpty());
	assertTrue(igor.getChildren().isEmpty());
	rurick.setChild(igor.getId());
	assertTrue(rurick.getChildren().get(0) == igor.getId());
	assertTrue(rurick.getChildren().size() == 1);
	assertTrue(igor.getChildren().isEmpty());
	rurick.setChild(igor.getId());
	assertTrue(rurick.getChildren().get(0) == igor.getId());
	assertTrue(rurick.getChildren().size() == 1);
	assertTrue(igor.getChildren().isEmpty());
    }

    @Test
    public void testSetChildren() throws Exception {
	Person svyatoslav = new Person("https://ru.wikipedia.org/wiki/Святослав Игоревич");
	Person yaropolk = new Person("https://ru.wikipedia.org/wiki/Ярополк Святославич");
	Person oleg = new Person("https://ru.wikipedia.org/wiki/Олег Святославич (князь древлянский)");
	Person vladimir = new Person("https://ru.wikipedia.org/wiki/Владимир Святославич");
	assertTrue(svyatoslav.getChildren().isEmpty());
	assertTrue(yaropolk.getChildren().isEmpty());
	assertTrue(oleg.getChildren().isEmpty());
	assertTrue(vladimir.getChildren().isEmpty());

	svyatoslav.setChild(yaropolk.getId());
	svyatoslav.setChild(oleg.getId());
	svyatoslav.setChild(vladimir.getId());

	assertTrue(svyatoslav.getChildren().get(0) == yaropolk.getId());
	assertTrue(svyatoslav.getChildren().get(1) == oleg.getId());
	assertTrue(svyatoslav.getChildren().get(2) == vladimir.getId());
	assertTrue(svyatoslav.getChildren().size() == 3);
	assertTrue(yaropolk.getChildren().isEmpty());
	assertTrue(oleg.getChildren().isEmpty());
	assertTrue(vladimir.getChildren().isEmpty());
    }

    @Test
    public void testReplaceChild() throws MalformedURLException {
	Person person = new Person("https://ru.wikipedia.org/wiki/Рюрик");

	// Пустой список
	person.replaceChild(1, 2);
	assertTrue(person.getChildren().isEmpty());

	person.setChild(1);
	person.setChild(2);
	person.setChild(3);
	assertTrue(person.getChildren().size() == 3);
	assertTrue(person.getChildren().contains(1));
	assertTrue(person.getChildren().contains(2));
	assertTrue(person.getChildren().contains(3));

	// Замена на себя
	person.replaceChild(2, 2);
	assertTrue(person.getChildren().size() == 3);
	assertTrue(person.getChildren().contains(1));
	assertTrue(person.getChildren().contains(2));
	assertTrue(person.getChildren().contains(3));

	// Замена существуюшего на несуществующий номер
	person.replaceChild(2, 4);
	assertTrue(person.getChildren().size() == 3);
	assertTrue(person.getChildren().contains(1));
	assertTrue(person.getChildren().contains(4));
	assertTrue(person.getChildren().contains(3));
	assertFalse(person.getChildren().contains(2));

	// Замена несуществующего на существующий
	person.replaceChild(5, 1);
	assertTrue(person.getChildren().size() == 3);
	assertTrue(person.getChildren().contains(1));
	assertTrue(person.getChildren().contains(4));
	assertTrue(person.getChildren().contains(3));
	assertFalse(person.getChildren().contains(5));

	// Замена несуществующего на несуществующий
	person.replaceChild(6, 7);
	assertTrue(person.getChildren().size() == 3);
	assertTrue(person.getChildren().contains(1));
	assertTrue(person.getChildren().contains(4));
	assertTrue(person.getChildren().contains(3));
	assertFalse(person.getChildren().contains(6));
	assertFalse(person.getChildren().contains(7));

	// Замена на существующий номер
	person.replaceChild(4, 3);
	assertTrue(person.getChildren().size() == 2);
	assertTrue(person.getChildren().contains(1));
	assertTrue(person.getChildren().contains(3));
	assertFalse(person.getChildren().contains(4));
    }

    @Test
    public void testEquals() throws MalformedURLException {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");

	// Сравнение с null
	assertFalse(rurick.equals(null));

	// Сравнение не с персоной
	assertFalse(rurick.equals("https://ru.wikipedia.org/wiki/Рюрик"));
	assertFalse(rurick.equals(false));
	assertFalse(rurick.equals(1));

	// Сравнение с персонами
	assertTrue(rurick.equals(new Person("https://ru.wikipedia.org/wiki/Рюрик")));
	assertFalse(rurick.equals(new Person("https://ru.wikipedia.org/wiki/НеРюрик")));
    }

    @Test
    public void testHashCode() throws MalformedURLException {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	assertTrue(rurick.hashCode() == "https://ru.wikipedia.org/wiki/Рюрик".hashCode());
	assertTrue(rurick.hashCode() == new Person("https://ru.wikipedia.org/wiki/Рюрик").hashCode());
	assertFalse(rurick.hashCode() == new Person("https://ru.wikipedia.org/wiki/НеРюрик").hashCode());
    }

    @Test
    public void testContains() throws MalformedURLException {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	List<Person> AllPersons = new ArrayList<Person>();
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(new Person("https://ru.wikipedia.org/wiki/НеРюрик"));
	assertFalse(AllPersons.contains(rurick));
	AllPersons.add(rurick);
	assertTrue(AllPersons.contains(rurick));
	AllPersons.remove(rurick);
	assertFalse(AllPersons.contains(rurick));
    }

    @Test
    public void testIndexOf() throws MalformedURLException {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	List<Person> AllPersons = new ArrayList<Person>();
	AllPersons.add(rurick);
	assertTrue(AllPersons.indexOf(rurick) == 0);
	assertTrue(AllPersons.indexOf(new Person("https://ru.wikipedia.org/wiki/НеРюрик")) == -1);
    }

    @Test
    public void testToString() throws MalformedURLException {
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	rurick.setName("Имя");
	rurick.setNameUrl("ИмяСсылки");
	assertTrue(rurick.toString().equals("name=Имя; id=" + rurick.getId()
		+ "; url=https://ru.wikipedia.org/wiki/Рюрик; nameUrl=ИмяСсылки; children=[]"));
    }

    @Test
    public void testCopyMainData() throws MalformedURLException {
	Person.resetCount();
	Person first = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	first.setName("Name_first");
	first.setNameUrl("NameUrl_first");
	first.setChild(3);
	Person second = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	second.setName("Name_second");
	second.setNameUrl("NameUrl_second");
	second.setChild(4);

	first.copyMainData(second);

	assertTrue(first.getName().equals(second.getName()));
	assertTrue(second.getName().equals("Name_second"));
	assertTrue(first.getUrl().equals(second.getUrl()));
	assertTrue(second.getUrl().equals("https://ru.wikipedia.org/wiki/Рюрик2"));
	assertTrue(first.getId() == 1);
	assertTrue(second.getId() == 2);
	assertTrue(first.getNameUrl().equals("NameUrl_first"));
	assertTrue(second.getNameUrl().equals("NameUrl_second"));
	assertTrue(first.getChildren().get(0) == 3);
	assertTrue(first.getChildren().size() == 1);
	assertTrue(second.getChildren().get(0) == 4);
	assertTrue(second.getChildren().size() == 1);
    }
}