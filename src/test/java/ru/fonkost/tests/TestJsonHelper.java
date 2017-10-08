/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ru.fonkost.entities.Person;
import ru.fonkost.utils.JsonHelper;

public class TestJsonHelper {
    private static List<Person> data = null;
    private static String checkStr = "{id:\\\"person0\\\", name:\\\"Рюрик <br><a href='https://ru.wikipedia.org/wiki/Рюрик'>wiki</a>\\\", data:{ \\\"id\\\": \\\"1\\\", \\\"name\\\": \\\"Рюрик\\\", \\\"nameUrl\\\": \\\"-\\\", \\\"numGen\\\": \\\"0\\\" }, children:[{id:\\\"person1\\\", name:\\\"Игорь <br><a href='https://ru.wikipedia.org/wiki/Игорь Рюрикович'>wiki</a>\\\", data:{ \\\"id\\\": \\\"2\\\", \\\"name\\\": \\\"Игорь Рюрикович\\\", \\\"nameUrl\\\": \\\"Игорь\\\", \\\"numGen\\\": \\\"0\\\" }, children:[{id:\\\"person2\\\", name:\\\"Святослав <br><a href='https://ru.wikipedia.org/wiki/Святослав Игоревич'>wiki</a>\\\", data:{ \\\"id\\\": \\\"3\\\", \\\"name\\\": \\\"Святослав Игоревич\\\", \\\"nameUrl\\\": \\\"Святослав\\\", \\\"numGen\\\": \\\"0\\\" }, children:[{id:\\\"person3\\\", name:\\\"Ярополк <br><a href='https://ru.wikipedia.org/wiki/Ярополк Святославич'>wiki</a>\\\", data:{ \\\"id\\\": \\\"4\\\", \\\"name\\\": \\\"Ярополк Святославич\\\", \\\"nameUrl\\\": \\\"Ярополк\\\", \\\"numGen\\\": \\\"0\\\" }, children:[]}, {id:\\\"person4\\\", name:\\\"Олег <br><a href='https://ru.wikipedia.org/wiki/Олег Святославич (князь древлянский)'>wiki</a>\\\", data:{ \\\"id\\\": \\\"5\\\", \\\"name\\\": \\\"Олег Святославич (князь древлянский)\\\", \\\"nameUrl\\\": \\\"Олег\\\", \\\"numGen\\\": \\\"0\\\" }, children:[]}, {id:\\\"person5\\\", name:\\\"Владимир <br><a href='https://ru.wikipedia.org/wiki/Владимир Святославич'>wiki</a>\\\", data:{ \\\"id\\\": \\\"6\\\", \\\"name\\\": \\\"Владимир Святославич\\\", \\\"nameUrl\\\": \\\"Владимир\\\", \\\"numGen\\\": \\\"0\\\" }, children:[]}]}]}]}";

    @Test
    public void testSaveTree() throws IOException {
	String filename = "C:\\workspace\\temp\\result.json";
	List<Person> tree = getTestData();
	JsonHelper.saveTree(tree, filename);
	List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
	assertTrue(lines.size() == 1);
	assertTrue(lines.get(0).equals(checkStr));
    }

    private static List<Person> getTestData() throws MalformedURLException {
	if (data != null) {
	    return data;
	}

	Person.resetCount();
	List<Person> persons = new ArrayList<Person>();
	Person rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
	rurick.setName("Рюрик");
	rurick.setNameUrl("");
	Person igor = new Person("https://ru.wikipedia.org/wiki/Игорь Рюрикович");
	igor.setName("Игорь Рюрикович");
	igor.setNameUrl("Игорь");
	rurick.setChild(igor.getId());
	Person svyatoslav = new Person("https://ru.wikipedia.org/wiki/Святослав Игоревич");
	svyatoslav.setName("Святослав Игоревич");
	svyatoslav.setNameUrl("Святослав");
	igor.setChild(svyatoslav.getId());
	Person yaropolk = new Person("https://ru.wikipedia.org/wiki/Ярополк Святославич");
	yaropolk.setName("Ярополк Святославич");
	yaropolk.setNameUrl("Ярополк");
	Person oleg = new Person("https://ru.wikipedia.org/wiki/Олег Святославич (князь древлянский)");
	oleg.setName("Олег Святославич (князь древлянский)");
	oleg.setNameUrl("Олег");
	Person vladimir = new Person("https://ru.wikipedia.org/wiki/Владимир Святославич");
	vladimir.setName("Владимир Святославич");
	vladimir.setNameUrl("Владимир");
	svyatoslav.setChild(yaropolk.getId());
	svyatoslav.setChild(oleg.getId());
	svyatoslav.setChild(vladimir.getId());

	persons.add(rurick);
	persons.add(igor);
	persons.add(svyatoslav);
	persons.add(yaropolk);
	persons.add(oleg);
	persons.add(vladimir);

	data = persons;
	return data;
    }
}