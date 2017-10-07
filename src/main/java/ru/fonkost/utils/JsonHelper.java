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

    /**
     * Сохранение родословного древа в json-формате, понимаемым утилитой
     * <a href="https://philogb.github.io/jit/index.html">JavaScript InfoVis
     * Toolkit</a>
     *
     * @param tree
     *            the tree
     */
    public static void saveTree(List<Person> tree, String fileName) {
	String data = getTreeJson(tree);
	File file = new File(fileName);
	FileWriter fr = null;
	BufferedWriter br = null;
	try {
	    fr = new FileWriter(file);
	    br = new BufferedWriter(fr);
	    br.write(data);
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

    /** Вернуть Json родословного древа */
    public static String getTreeJson(List<Person> tree) {
	id = 0;
	if (tree.isEmpty()) {
	    return "";
	}
	Person person = tree.get(0);
	String result = getPersonJson(person, tree);
	return result;
    }

    /** Вернуть Json персоны */
    private static String getPersonJson(Person person, List<Person> tree) {
	StringBuilder result = new StringBuilder();

	result.append("{id:\\\"person");
	result.append(id++);
	result.append("\\\", ");

	result.append("name:\\\"");
	result.append(person.getTitleName());
	result.append(" <br><a href='");
	result.append(person.getUrl());
	result.append("'>wiki</a>\\\", ");

	result.append("data:{ ");
	result.append(getData(person));
	result.append(" }, ");

	result.append("children:[");
	String children = getChildrenJson(person.getChildren(), tree);
	result.append(children);
	result.append("]}");

	return result.toString();
    }

    /** Сформировать json детей */
    private static String getChildrenJson(List<Integer> children, List<Person> tree) {
	if (children.isEmpty()) {
	    return "";
	}

	StringBuilder result = new StringBuilder();
	int i = 0;
	for (int id : children) {
	    Person child = findPerson(id, tree);
	    String childJson = getPersonJson(child, tree);
	    result.append(childJson);
	    i++;
	    if (i < children.size()) {
		result.append(", ");
	    }
	}
	return result.toString();
    }

    /** Найти персону в списке */
    private static Person findPerson(int id, List<Person> tree) {
	for (Person person : tree) {
	    if (person.getId() == id) {
		return person;
	    }
	}
	return null;
    }

    /** Сформировать данные для всплывающего лэйбла */
    private static String getData(Person person) {
	StringBuilder result = new StringBuilder();

	result.append("\\\"id\\\": \\\"");
	result.append(person.getId());
	result.append("\\\", ");

	result.append("\\\"name\\\": \\\"");
	result.append(person.getName());
	result.append("\\\", ");

	result.append("\\\"nameUrl\\\": \\\"");
	String nameUrl = person.getNameUrl();
	if (nameUrl.isEmpty()) {
	    nameUrl = "-";
	}
	result.append(nameUrl);
	result.append("\\\", ");

	result.append("\\\"numGen\\\": \\\"");
	result.append(person.getNumberGeneration());
	result.append("\\\"");

	return result.toString();
    }
}