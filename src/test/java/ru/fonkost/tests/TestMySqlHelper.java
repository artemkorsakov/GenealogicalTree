/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;

import ru.fonkost.entities.Person;
import ru.fonkost.main.GenerateGenealogicalTree;
import ru.fonkost.utils.MySqlHelper;

/**
 * Тестирование взаимодействия с БД.
 *
 * @author Артём Корсаков
 */
public class TestMySqlHelper {

    /**
     * Проверка сохранения дерева
     *
     * @throws MalformedURLException
     *             the malformed URL exception
     */
    @Test
    public void testSaveTree() throws Exception {
	Person.resetCount();
	String url = "https://ru.wikipedia.org/wiki/%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B9_II";
	List<Person> tree = GenerateGenealogicalTree.getGenealogicalTreeByUrl(url).getGenealogicalTree();
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	String tableName = "testSaveTree" + timestamp.getTime();
	MySqlHelper.saveTree(tableName, tree);

	List<Person> result = MySqlHelper.getTreeFormNewestTable();
	assertTrue(result.size() == 6);

	assertTrue(result.get(0).getName().equals("Николай II"));
	assertTrue(result.get(0).getUrl()
		.equals("https://ru.wikipedia.org/wiki/%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B9_II"));
	assertTrue(result.get(0).getNameUrl().equals("null"));
	assertTrue(result.get(0).getNumberGeneration() == 0);
	assertTrue(result.get(0).getChildren().size() == 5);
	assertTrue(result.get(0).getChildren().get(0) == 3);
	assertTrue(result.get(0).getChildren().get(1) == 4);
	assertTrue(result.get(0).getChildren().get(2) == 5);
	assertTrue(result.get(0).getChildren().get(3) == 6);
	assertTrue(result.get(0).getChildren().get(4) == 7);
	assertTrue(result.get(0).getParents().isEmpty());

	assertTrue(result.get(1).getName().equals("Ольга Николаевна (великая княжна)"));
	assertTrue(result.get(1).getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%9E%D0%BB%D1%8C%D0%B3%D0%B0_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%BD%D0%B0_(%D0%B2%D0%B5%D0%BB%D0%B8%D0%BA%D0%B0%D1%8F_%D0%BA%D0%BD%D1%8F%D0%B6%D0%BD%D0%B0)"));
	assertTrue(result.get(1).getNameUrl().equals("Ольга"));
	assertTrue(result.get(1).getNumberGeneration() == 1);
	assertTrue(result.get(1).getChildren().isEmpty());
	assertTrue(result.get(1).getParents().size() == 1);
	assertTrue(result.get(1).getParents().get(0) == 1);

	assertTrue(result.get(2).getName().equals("Татьяна Николаевна (великая княжна)"));
	assertTrue(result.get(2).getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%A2%D0%B0%D1%82%D1%8C%D1%8F%D0%BD%D0%B0_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%BD%D0%B0_(%D0%B2%D0%B5%D0%BB%D0%B8%D0%BA%D0%B0%D1%8F_%D0%BA%D0%BD%D1%8F%D0%B6%D0%BD%D0%B0)"));
	assertTrue(result.get(2).getNameUrl().equals("Татьяна"));
	assertTrue(result.get(2).getNumberGeneration() == 1);
	assertTrue(result.get(2).getChildren().isEmpty());
	assertTrue(result.get(2).getParents().size() == 1);
	assertTrue(result.get(2).getParents().get(0) == 1);

	assertTrue(result.get(3).getName().equals("Мария Николаевна (великая княжна)"));
	assertTrue(result.get(3).getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%9C%D0%B0%D1%80%D0%B8%D1%8F_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%BD%D0%B0_(%D0%B2%D0%B5%D0%BB%D0%B8%D0%BA%D0%B0%D1%8F_%D0%BA%D0%BD%D1%8F%D0%B6%D0%BD%D0%B0)"));
	assertTrue(result.get(3).getNameUrl().equals("Мария"));
	assertTrue(result.get(3).getNumberGeneration() == 1);
	assertTrue(result.get(3).getChildren().isEmpty());
	assertTrue(result.get(3).getParents().size() == 1);
	assertTrue(result.get(3).getParents().get(0) == 1);

	assertTrue(result.get(4).getName().equals("Анастасия Николаевна"));
	assertTrue(result.get(4).getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%90%D0%BD%D0%B0%D1%81%D1%82%D0%B0%D1%81%D0%B8%D1%8F_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%BD%D0%B0"));
	assertTrue(result.get(4).getNameUrl().equals("Анастасия"));
	assertTrue(result.get(4).getNumberGeneration() == 1);
	assertTrue(result.get(4).getChildren().isEmpty());
	assertTrue(result.get(4).getParents().size() == 1);
	assertTrue(result.get(4).getParents().get(0) == 1);

	assertTrue(result.get(5).getName().equals("Алексей Николаевич"));
	assertTrue(result.get(5).getUrl().equals(
		"https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B5%D0%BA%D1%81%D0%B5%D0%B9_%D0%9D%D0%B8%D0%BA%D0%BE%D0%BB%D0%B0%D0%B5%D0%B2%D0%B8%D1%87"));
	assertTrue(result.get(5).getNameUrl().equals("Алексей"));
	assertTrue(result.get(5).getNumberGeneration() == 1);
	assertTrue(result.get(5).getChildren().isEmpty());
	assertTrue(result.get(5).getParents().size() == 1);
	assertTrue(result.get(5).getParents().get(0) == 1);
    }
}