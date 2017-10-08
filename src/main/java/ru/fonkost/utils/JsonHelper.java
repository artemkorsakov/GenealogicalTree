/** http://fonkost.ru */
package ru.fonkost.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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
    private static List<Person> tree = new ArrayList<Person>();
    private static String filePath;

    /**
     * Сохранение родословного древа в json-формате, понимаемым утилитой
     * <a href="https://philogb.github.io/jit/index.html">JavaScript InfoVis
     * Toolkit</a>
     */
    public static void saveTree(List<Person> list, String fileName) throws IOException {
	filePath = fileName;
	tree = list;
	saveTreeJson();
    }

    /** Сохранение строки в файл */
    private static void write(String str) throws IOException {
	try {
	    File myFile = new File(filePath);
	    if (!myFile.exists()) {
		myFile.createNewFile();
	    }
	    Files.write(Paths.get(filePath), str.getBytes(), StandardOpenOption.APPEND);
	} catch (IOException e) {
	    System.out.println(e);
	}
    }

    /** Сохранить родословное древо */
    private static void saveTreeJson() throws IOException {
	id = 0;
	if (tree.isEmpty()) {
	    return;
	}
	Person person = tree.get(0);
	savePersonJson(person);
    }

    /** Вернуть Json персоны */
    private static void savePersonJson(Person person) throws IOException {
	write("{id:\\\"person" + id++ + "\\\", ");
	write("name:\\\"" + person.getTitleName() + " <br><a href='" + person.getUrl() + "'>wiki</a>\\\", ");
	write("data:{ ");
	saveData(person);
	write(" }, ");
	write("children:[");
	saveChildrenJson(person.getChildren());
	write("]}");
    }

    /** Сформировать json детей */
    private static void saveChildrenJson(List<Integer> children) throws IOException {
	if (children.isEmpty()) {
	    return;
	}

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

    /** Сформировать данные для всплывающего лэйбла */
    private static void saveData(Person person) throws IOException {
	write("\\\"id\\\": \\\"" + person.getId() + "\\\", ");
	write("\\\"name\\\": \\\"" + person.getName() + "\\\", ");
	String nameUrl = person.getNameUrl();
	if (nameUrl == null || nameUrl.isEmpty()) {
	    nameUrl = "-";
	}
	write("\\\"nameUrl\\\": \\\"" + nameUrl + "\\\", ");
	write("\\\"numGen\\\": \\\"" + person.getNumberGeneration() + "\\\"");
    }

    /** Найти персону в списке */
    private static Person findPerson(int id) {
	for (Person person : tree) {
	    if (person.getId() == id) {
		return person;
	    }
	}
	return null;
    }
}