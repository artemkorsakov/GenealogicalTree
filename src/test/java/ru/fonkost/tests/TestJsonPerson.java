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
	jp.setDescendants(2);
	assertTrue(jp.getDescendants() == 2);
    }

    @Test
    public void testFormat() throws Exception {
	List<Person> tree = MySqlHelper.getTreeFormTable("testsavetreeJson");
	Person person = tree.get(5);
	JsonPerson jp = new JsonPerson(person);
	System.out.println(jp.getFormat());
	assertTrue(jp.getFormat().equals(
		"{id:\\\"person0\\\", name:\\\"Алексей <br><a href='https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B5%D0%BA%D1%81%D0%B5%D0%B9_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%B8%D1%87'>wiki</a>\\\", data:{ \\\"id\\\": \\\"7\\\", \\\"name\\\": \\\"Алексей Николаевич\\\", \\\"nameUrl\\\": \\\"Алексей\\\", \\\"numGen\\\": \\\"1\\\" }, children:[]}"));
	jp.setFormat("test");
	assertTrue(jp.getFormat().equals("test"));
    }
}