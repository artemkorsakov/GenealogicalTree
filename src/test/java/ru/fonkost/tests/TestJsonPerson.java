/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import ru.fonkost.entities.JsonPerson;
import ru.fonkost.entities.Person;
import ru.fonkost.utils.MySqlHelper;

public class TestJsonPerson {

    @Test
    public void testCreate() throws MalformedURLException {
	Person rurick = new Person("https://ru.wikipedia.org");
	JsonPerson jp = new JsonPerson(rurick);
	assertTrue(jp.getChildren().isEmpty());
    }

    @Test
    public void testIsPersonId() throws MalformedURLException {
	Person.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	JsonPerson jp = new JsonPerson(rurick);
	assertTrue(jp.isPersonId(1));
	assertFalse(jp.isPersonId(2));
    }

    @Test
    public void testGetChildren() throws Exception {
	List<Person> tree = MySqlHelper.getTreeFormTable("testsavetreeJson");
	Person person = tree.get(0);
	assertTrue(person.getChildren().size() == 5);
    }

    @Test
    public void testDescendants() throws MalformedURLException {
	Person rurick = new Person("https://ru.wikipedia.org");
	JsonPerson jp = new JsonPerson(rurick);
	assertTrue(jp.getDescendants() == -1);
	jp.setDescendants(5);
	assertTrue(jp.getDescendants() == 5);
	long max = Integer.MAX_VALUE;
	jp.setDescendants(max);
	assertTrue(jp.getDescendants() == max);
	max++;
	jp.setDescendants(max);
	assertTrue(jp.getDescendants() == max);
    }

    @Test
    public void testGetPersonJson() throws MalformedURLException {
	Person.resetCount();
	JsonPerson.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setName("Рюрик");
	rurick.setNameUrl("Рюрик");
	JsonPerson jp = new JsonPerson(rurick);
	String json = "{id:\\\"person0\\\", name:\\\"Рюрик <br><a href='https://ru.wikipedia.org'>wiki</a>\\\", data:{ \\\"id\\\": \\\"1\\\", \\\"name\\\": \\\"Рюрик\\\", \\\"nameUrl\\\": \\\"Рюрик\\\", \\\"numGen\\\": \\\"0\\\" }, children:[";
	assertTrue(jp.getPersonJson().equals(json));
    }

    @Test
    public void testGetPersonJsonWithNullNameUrl() throws MalformedURLException {
	Person.resetCount();
	JsonPerson.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setName("Рюрик");
	JsonPerson jp = new JsonPerson(rurick);
	String json = "{id:\\\"person0\\\", name:\\\"Рюрик <br><a href='https://ru.wikipedia.org'>wiki</a>\\\", data:{ \\\"id\\\": \\\"1\\\", \\\"name\\\": \\\"Рюрик\\\", \\\"nameUrl\\\": \\\"-\\\", \\\"numGen\\\": \\\"0\\\" }, children:[";
	assertTrue(jp.getPersonJson().equals(json));
    }

    @Test
    public void testGetPersonJsonWithEmptyNameUrl() throws MalformedURLException {
	Person.resetCount();
	JsonPerson.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setName("Рюрик");
	rurick.setNameUrl("");
	JsonPerson jp = new JsonPerson(rurick);
	String json = "{id:\\\"person0\\\", name:\\\"Рюрик <br><a href='https://ru.wikipedia.org'>wiki</a>\\\", data:{ \\\"id\\\": \\\"1\\\", \\\"name\\\": \\\"Рюрик\\\", \\\"nameUrl\\\": \\\"-\\\", \\\"numGen\\\": \\\"0\\\" }, children:[";
	assertTrue(jp.getPersonJson().equals(json));
    }

    @Test
    public void testGetPersonJsonWithNameContainsApostrophe() throws MalformedURLException {
	Person.resetCount();
	JsonPerson.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setName("Gianfrancesco \"Cagnino\" \'Gonzaga\'");
	rurick.setNameUrl("Gianfrancesco \"Cagnino\" \'Gonzaga\'");
	JsonPerson jp = new JsonPerson(rurick);
	System.out.println(jp.getPersonJson());
	String json = "{id:\\\"person0\\\", name:\\\"Gianfrancesco Cagnino Gonzaga <br><a href='https://ru.wikipedia.org'>wiki</a>\\\", data:{ \\\"id\\\": \\\"1\\\", \\\"name\\\": \\\"Gianfrancesco Cagnino Gonzaga\\\", \\\"nameUrl\\\": \\\"Gianfrancesco Cagnino Gonzaga\\\", \\\"numGen\\\": \\\"0\\\" }, children:[";
	assertTrue(jp.getPersonJson().equals(json));
    }

    @Test
    public void testGetDublicateJson() throws Exception {
	Person.resetCount();
	JsonPerson.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setName("Рюрик");
	rurick.setNameUrl("Рюрик");
	JsonPerson jp = new JsonPerson(rurick);
	String json = "{id:\\\"person0_1\\\", name:\\\"Дубликат (Рюрик) <a href='http://fonkost.ru/genealogicaltree/rurick/1'>link</a>\\\", data:{ \\\"name\\\": \\\"Эта персона уже встречается в родословном древе и она детализирована у своего первого родителя\\\" }, children:[]}";
	assertTrue(jp.getDublicateJson("rurick", 1).equals(json));
    }

    @Test
    public void testToString() throws MalformedURLException {
	Person.resetCount();
	JsonPerson.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	JsonPerson jp = new JsonPerson(rurick);
	assertTrue(jp.toString().equals("personId=1"));
    }

    @Test
    public void testGetId() throws MalformedURLException {
	Person.resetCount();
	JsonPerson.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	JsonPerson jp = new JsonPerson(rurick);
	assertTrue(jp.getPersonId() == 1);
    }

    @Test
    public void testIsFirstParent() throws MalformedURLException {
	Person.resetCount();
	JsonPerson.resetCount();
	Person rurick = new Person("https://ru.wikipedia.org");
	rurick.setParent(1);
	JsonPerson jp = new JsonPerson(rurick);
	assertFalse(jp.isFirstParent(0));
	assertTrue(jp.isFirstParent(1));
	assertFalse(jp.isFirstParent(2));
    }
}