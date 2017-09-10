/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ru.fonkost.entities.Person;
import ru.fonkost.main.GenerateGenealogicalTree;
import ru.fonkost.utils.MySqlHelper;

/** Тестирование генерации родословного древа */
public class TestGenerateGenealogicalTree {
    @Test
    public void testIncorrectArguments() throws Exception {
	new GenerateGenealogicalTree();
	try {
	    GenerateGenealogicalTree.main(null);
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Должен быть задан один параметр"));
	}

	try {
	    GenerateGenealogicalTree.main(new String[] { "first", "second" });
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Должен быть задан один параметр"));
	}

	try {
	    GenerateGenealogicalTree.main(new String[] { "first" });
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Некорректный урл first"));
	}

	try {
	    GenerateGenealogicalTree.main(new String[] { "http://fonkost.ru/" });
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals(
		    "Алгоритм предназначен для генерации родословного древа только на основе данных Wikipedia"));
	}
    }

    @Test
    public void testGenerateWhenThereAreDuplicates() throws Exception {
	Person.resetCount();
	String url = "https://ru.wikipedia.org/wiki/%D0%A4%D1%80%D0%B8%D0%B7%D0%BE_%D0%9E%D1%80%D0%B0%D0%BD%D1%81%D0%BA%D0%BE-%D0%9D%D0%B0%D1%81%D1%81%D0%B0%D1%83%D1%81%D0%BA%D0%B8%D0%B9";
	GenerateGenealogicalTree.main(new String[] { url });

	List<Person> result = MySqlHelper.getTreeFormNewestTable();
	assertTrue(result.size() == 2);

	assertTrue(result.get(0).getName().equals("Фризо Оранско-Нассауский"));
	assertTrue(result.get(0).getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%A4%D1%80%D0%B8%D0%B7%D0%BE_%D0%9E%D1%80%D0%B0%D0%BD%D1%81%D0%BA%D0%BE-%D0%9D%D0%B0%D1%81%D1%81%D0%B0%D1%83%D1%81%D0%BA%D0%B8%D0%B9"));
	assertTrue(result.get(0).getNameUrl().equals("null"));
	assertTrue(result.get(0).getNumberGeneration() == 0);
	assertTrue(result.get(0).getChildren().size() == 1);
	assertTrue(result.get(0).getChildren().get(0) == 3);
	assertTrue(result.get(0).getParents().isEmpty());

	assertTrue(result.get(1).getName().equals("Брак и дети"));
	assertTrue(result.get(1).getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%A4%D1%80%D0%B8%D0%B7%D0%BE_%D0%9E%D1%80%D0%B0%D0%BD%D1%81%D0%BA%D0%BE-%D0%9D%D0%B0%D1%81%D1%81%D0%B0%D1%83%D1%81%D0%BA%D0%B8%D0%B9#.D0.91.D1.80.D0.B0.D0.BA_.D0.B8_.D0.B4.D0.B5.D1.82.D0.B8"));
	assertTrue(result.get(1).getNameUrl().equals("Луана Оранско-Нассауская"));
	assertTrue(result.get(1).getNumberGeneration() == 1);
	assertTrue(result.get(1).getChildren().isEmpty());
	assertTrue(result.get(1).getParents().size() == 1);
	assertTrue(result.get(1).getParents().get(0) == 1);
    }
}