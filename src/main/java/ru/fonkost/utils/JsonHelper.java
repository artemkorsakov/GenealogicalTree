/** http://fonkost.ru */
package ru.fonkost.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
    private static int id = 0;
    private static String fileName;
    private static File file = null;
    private static FileWriter fr = null;
    private static BufferedWriter br = null;
    private static List<Person> tree = null;

    /**
     * Сохранение родословного древа в json-формате, понимаемым утилитой
     * <a href="https://philogb.github.io/jit/index.html">JavaScript InfoVis
     * Toolkit</a>
     */
    public static void saveTree(String tableName) throws Exception {
	tree = MySqlHelper.getTreeFormTable(tableName);
	fileName = "C:\\workspace\\temp\\" + tableName + ".json";
	file = new File(fileName);
	try {
	    fr = new FileWriter(file);
	    br = new BufferedWriter(fr);
	    saveTreeJson();
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

    /** Сохранить родословное древо */
    private static void saveTreeJson() throws Exception {
	id = 0;
	Person person = findPerson(1);
	if (person == null) {
	    return;
	}
	savePersonJson(person);
    }

    /** Найти персону в списке */
    private static Person findPerson(int id) throws Exception {
	for (Person person : tree) {
	    if (person.getId() == id) {
		return person;
	    }
	}
	return null;
    }

    /** Вернуть Json персоны */
    private static void savePersonJson(Person person) throws Exception {
	write(getId());
	write(getName(person));
	write(getData(person));

	write("children:[");
	List<Integer> children = person.getChildren();
	if (!children.isEmpty()) {
	    int i = 0;
	    for (int id : children) {
		Person child = findPerson(id);
		savePersonJson(child);
		i++;
		if (i < children.size()) {
		    write(", ");
		}
	    }
	}
	write("]}");
    }

    /** Сохранение строки в файл */
    private static void write(String str) throws IOException {
	br.write(str);
	br.flush();
    }

    /** Вернуть данные для идентификатора элемента */
    private static String getId() {
	StringBuilder sb = new StringBuilder();
	sb.append("{id:\\\"person");
	sb.append(id++);
	sb.append("\\\", ");
	return sb.toString();
    }

    /** Вернуть данные для имени персоны со ссылкой на wiki */
    private static String getName(Person person) {
	StringBuilder sb = new StringBuilder();
	sb.append("name:\\\"");
	sb.append(person.getTitleName());
	sb.append(" <br><a href='");
	sb.append(person.getUrl());
	sb.append("'>wiki</a>\\\", ");
	return sb.toString();
    }

    /** Вернуть данные для всплывающего лэйбла */
    private static String getData(Person person) {
	StringBuilder sb = new StringBuilder();
	sb.append("data:{ ");
	sb.append("\\\"id\\\": \\\"");
	sb.append(person.getId());
	sb.append("\\\", ");
	sb.append("\\\"name\\\": \\\"");
	sb.append(person.getName());
	sb.append("\\\", ");
	sb.append("\\\"nameUrl\\\": \\\"");
	String nameUrl = person.getNameUrl();
	if (nameUrl == null || nameUrl.isEmpty()) {
	    nameUrl = "-";
	}
	sb.append(nameUrl);
	sb.append("\\\", ");
	sb.append("\\\"numGen\\\": \\\"");
	sb.append(person.getNumberGeneration());
	sb.append("\\\"");
	sb.append(" }, ");
	return sb.toString();
    }
}