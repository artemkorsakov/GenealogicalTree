/** http://fonkost.ru */
package ru.fonkost.main;

import java.util.List;

import ru.fonkost.entities.Person;
import ru.fonkost.utils.JsonHelper;
import ru.fonkost.utils.MySqlHelper;

/**
 * Генерация файла в Json-формате для отображения в виде дерева. <br>
 * Примеры:
 * <a href="http://fonkost.ru/genealogicaltree_tree/romanov">Романовы</a>,
 * <a href="http://fonkost.ru/genealogicaltree_tree/rurick">Рюриковичи</a>,
 * <a href="http://fonkost.ru/genealogicaltree_tree/adam">Адам</a>,
 * <a href="http://fonkost.ru/genealogicaltree_tree/genghiskhan">Чингисхан</a>
 *
 * @author Артём Корсаков
 */
public class GenerateJsonFromBD {

    /**
     * Метод выгружает из БД родословное древо и сохраняет его в Json. Создан
     * для родословных деревьев, сгенерированных до выгрузки в Json.
     */
    public static void main(String[] args) throws Exception {
	String tableName = "rurick20170910";
	List<Person> tree = MySqlHelper.getTreeFormTable(tableName);
	System.out.println("size = " + tree.size());
	String filename = "C:\\workspace\\temp\\" + tableName + ".json";
	JsonHelper.saveTree(tree, filename);
	System.out.println("Complite");
    }
}