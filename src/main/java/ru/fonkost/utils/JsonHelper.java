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

import com.mysql.cj.jdbc.exceptions.PacketTooBigException;

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
    public static void saveTree(String tableName, String ancestor) throws Exception {
	calculateJsonTree(tableName);
	System.out.println("Complite calculateJsonTree");
	calculateDescendants();
	System.out.println("Complite calculateDescendants");
	sortTree();
	System.out.println("Complite sortTree");
	String fileName = "C:\\workspace\\temp\\" + tableName + ".json";
	File file = new File(fileName);
	FileWriter fr = new FileWriter(file);
	BufferedWriter br = new BufferedWriter(fr);
	try {
	    long start = System.currentTimeMillis();
	    String result = getTreeJson(tableName, ancestor);
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
	    loopbackProtection(person);
	    treeJson.add(new JsonPerson(person));
	}
	tree = null;
    }

    /**
     * Среди потомков Рюрика возможно зацикливание, когда в информации о детях
     * персоны есть ссылки на матерей детей, а на страницах матерей ответная
     * ссылка на персону. В этом случае у персоны среди детей может быть
     * идентификатор собственного родителя, что послучит причиной зацикливания.
     * <br>
     * Примером может послужить <a href=
     * "https://ru.wikipedia.org/wiki/%D0%9A%D0%B0%D1%80%D0%BB_IX_(%D0%BA%D0%BE%D1%80%D0%BE%D0%BB%D1%8C_%D0%A4%D1%80%D0%B0%D0%BD%D1%86%D0%B8%D0%B8)">
     * Карл IX</a> и его любовница <a href=
     * "https://ru.wikipedia.org/wiki/%D0%A2%D1%83%D1%88%D0%B5,_%D0%9C%D0%B0%D1%80%D0%B8">
     * Мария Туше</a><br>
     * Избавляемся от дубликатов, путем удаления из списка детей тех, кто есть в
     * родителях.
     */
    private static void loopbackProtection(Person person) {
	for (int id : person.getParents()) {
	    if (person.getChildren().contains(id)) {
		System.out.println(person.getId());
		person.removeChild(id);
	    }
	}
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
    private static String getTreeJson(String tableName, String ancestor) throws Exception {
	boolean isBigTree = treeJson.size() > 500;
	for (int i = 0; i < treeJson.size(); i++) {
	    JsonPerson jperson = treeJson.get(i);
	    try {
		StringBuilder sb = new StringBuilder();
		String json = jperson.getPersonJson();
		sb.append(json);
		List<Integer> children = jperson.getChildren();
		for (int j = 0; j < children.size(); j++) {
		    int idChild = children.get(j);
		    JsonPerson jChild = findJsonPerson(idChild);
		    String formatChild = !isBigTree || jChild.isFirstParent(jperson.getId())
			    ? MySqlHelper.getFormat(tableName, idChild)
			    : jChild.getDublicateJson(ancestor, jperson.getId());
		    sb.append(formatChild);
		    if (j + 1 < children.size()) {
			sb.append(", ");
		    }
		}
		sb.append("]}");
		String result = sb.toString();
		MySqlHelper.updateFormat(tableName, jperson.getId(), result);
		if (i % 100 == 0 || i + 1 == treeJson.size()) {
		    System.out.println("Успешно: " + jperson.getId() + " - это " + i + " из " + treeJson.size());
		}
	    } catch (PacketTooBigException ex) {
		System.out.println(jperson.getDescendants());
		throw ex;
	    }
	}
	String result = MySqlHelper.getFormat(tableName, 1);
	return result;
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