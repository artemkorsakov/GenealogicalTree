/** http://fonkost.ru */
package ru.fonkost.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.fonkost.entities.JsonPerson;
import ru.fonkost.entities.Person;

/**
 * Класс генерации результатов в json-формате.
 * 
 * <br>
 * <strong style="color: red;">Класс является небезопасным и может работать
 * некорректно, если его использовать вне
 * {@link ru.fonkost.main.GenerateGenealogicalTree#main(String[]) алгоритма
 * генерации родословного древа}. <br>
 * Чтобы подвесить данный класс достаточно передать одну персону, которая сама у
 * себя является ребенком - тогда цикл будет выполняться бесконечно. <br>
 * Более правильным решением был бы перенос данного класса в качестве
 * внутреннего приватного класса для
 * {@link ru.fonkost.main.GenerateGenealogicalTree GenerateGenealogicalTree}.
 * Пока оставлено так для удобства тестирования алгоритма. </strong>
 */
public class JsonHelper {
    private static List<JsonPerson> treeJson = null;

    /**
     * Сохранение родословного древа в json-формате, понимаемым утилитой
     * <a href="https://philogb.github.io/jit/index.html">JavaScript InfoVis
     * Toolkit</a>
     */
    public static void saveTree(String tableName) throws Exception {
	calculateJsonTree(tableName);
	calculateDescendants();
	sortTree();
	String fileName = "C:\\workspace\\temp\\" + tableName + ".json";
	File file = new File(fileName);
	FileWriter fr = new FileWriter(file);
	BufferedWriter br = new BufferedWriter(fr);
	try {
	    long start = System.currentTimeMillis();
	    String result = getTreeJson();
	    long finish = System.currentTimeMillis();
	    double time = (finish - start) / 1000;
	    System.out.println("Прошло времени в секундах: " + time);
	    br.write(result);
	    br.flush();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		br.close();
		fr.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    /** Сформировать дерево из JsonPerson */
    private static void calculateJsonTree(String tableName) throws Exception {
	List<Person> tree = MySqlHelper.getTreeFormTable(tableName);
	treeJson = new ArrayList<JsonPerson>();
	for (Person person : tree) {
	    treeJson.add(new JsonPerson(person));
	}
	tree = null;
    }

    /** Расчитать количество потомков для всего родословного древа */
    private static void calculateDescendants() throws Exception {
	for (int i = treeJson.size() - 1; i >= 0; i--) {
	    JsonPerson jp = treeJson.get(i);
	    int count = getDescendants(jp);
	    jp.setDescendants(count);
	}
    }

    /** Вернуть количество потомков для всего родословного древа */
    private static int getDescendants(JsonPerson person) throws Exception {
	int result = person.getDescendants();
	if (result != -1) {
	    return result;
	}
	List<Integer> children = person.getChildren();
	result = 0;
	for (int idChild : children) {
	    JsonPerson jp = findJsonPerson(idChild);
	    result += getDescendants(jp) + 1;
	}
	return result;
    }

    /** Отсортировать список по количеству потомков */
    private static void sortTree() {
	Collections.sort(treeJson, new Comparator<JsonPerson>() {
	    @Override
	    public int compare(JsonPerson o1, JsonPerson o2) {
		return o1.getDescendants() - o2.getDescendants();
	    }
	});
    }

    /** Сохранить родословное древо */
    private static String getTreeJson() throws Exception {
	for (int i = 0; i < treeJson.size(); i++) {
	    JsonPerson person = treeJson.get(i);
	    String format = person.getFormat();
	    List<Integer> children = person.getChildren();
	    for (int idChild : children) {
		JsonPerson child = findJsonPerson(idChild);
		format = format.replace("indefinedChild" + idChild + " ", child.getFormat());
	    }
	    person.setFormat(format);

	    System.out.println(format);
	}
	return treeJson.get(treeJson.size() - 1).getFormat();
    }

    /** Найти персону в списке */
    private static JsonPerson findJsonPerson(int id) throws Exception {
	for (JsonPerson person : treeJson) {
	    if (person.isPersonId(id)) {
		return person;
	    }
	}
	return null;
    }
}